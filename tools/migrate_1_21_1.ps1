param(
    [string]$ProjectRoot = (Resolve-Path (Join-Path $PSScriptRoot "..")).Path,
    [string]$OutputRoot = (Join-Path (Resolve-Path (Join-Path $PSScriptRoot "..")).Path "migration/1.21.1")
)

Set-StrictMode -Version Latest
$ErrorActionPreference = "Stop"

function Ensure-Directory {
    param([Parameter(Mandatory = $true)][string]$Path)

    if (-not (Test-Path -LiteralPath $Path)) {
        New-Item -ItemType Directory -Path $Path -Force | Out-Null
    }
}

function Read-KeyValueFile {
    param([Parameter(Mandatory = $true)][string]$Path)

    $values = [ordered]@{}

    foreach ($rawLine in Get-Content -LiteralPath $Path) {
        $line = $rawLine.Trim()
        if ([string]::IsNullOrWhiteSpace($line) -or $line.StartsWith("#")) {
            continue
        }

        $separatorIndex = $line.IndexOf("=")
        if ($separatorIndex -lt 0) {
            continue
        }

        $key = $line.Substring(0, $separatorIndex).Trim()
        $value = $line.Substring($separatorIndex + 1).Trim()
        $values[$key] = $value
    }

    return $values
}

function Convert-LangFile {
    param(
        [Parameter(Mandatory = $true)][string]$SourcePath,
        [Parameter(Mandatory = $true)][string]$TargetPath,
        [Parameter(Mandatory = $true)][string]$Namespace
    )

    $entries = [ordered]@{}
    $duplicateKeys = New-Object System.Collections.Generic.List[string]
    $skippedLines = 0
    $modernItemKeyAliases = 0
    $modernBlockKeyAliases = 0
    $modernEntityKeyAliases = 0

    foreach ($rawLine in Get-Content -LiteralPath $SourcePath) {
        if ([string]::IsNullOrWhiteSpace($rawLine)) {
            continue
        }

        $trimmed = $rawLine.Trim()
        if ($trimmed.StartsWith("#")) {
            continue
        }

        $separatorIndex = $rawLine.IndexOf("=")
        if ($separatorIndex -lt 0) {
            $skippedLines++
            continue
        }

        $key = $rawLine.Substring(0, $separatorIndex).Trim()
        $value = $rawLine.Substring($separatorIndex + 1)

        if ($entries.Contains($key)) {
            $duplicateKeys.Add($key)
        }

        $entries[$key] = $value
    }

    foreach ($existingKey in @($entries.Keys)) {
        $value = $entries[$existingKey]
        $aliasKey = $null

        if ($existingKey -match '^item\.(.+)\.name$') {
            $aliasKey = "item.$Namespace.$($Matches[1])"
            $modernItemKeyAliases++
        } elseif ($existingKey -match '^tile\.(.+)\.name$') {
            $aliasKey = "block.$Namespace.$($Matches[1])"
            $modernBlockKeyAliases++
        } elseif ($existingKey -match '^entity\.(.+)\.name$') {
            $aliasKey = "entity.$Namespace.$($Matches[1])"
            $modernEntityKeyAliases++
        }

        if ($null -ne $aliasKey -and -not $entries.Contains($aliasKey)) {
            $entries[$aliasKey] = $value
        }
    }

    Ensure-Directory -Path (Split-Path -Parent $TargetPath)
    $json = $entries | ConvertTo-Json -Depth 8
    Set-Content -LiteralPath $TargetPath -Value $json -Encoding utf8

    return [pscustomobject]@{
        source = $SourcePath
        target = $TargetPath
        entries = $entries.Count
        duplicateKeys = @($duplicateKeys)
        skippedLines = $skippedLines
        modernItemKeyAliases = $modernItemKeyAliases
        modernBlockKeyAliases = $modernBlockKeyAliases
        modernEntityKeyAliases = $modernEntityKeyAliases
    }
}

function Copy-Tree {
    param(
        [Parameter(Mandatory = $true)][string]$SourceRoot,
        [Parameter(Mandatory = $true)][string]$TargetRoot,
        [Parameter(Mandatory = $true)][string]$Filter
    )

    $copied = @()
    $normalizedSourceRoot = [System.IO.Path]::GetFullPath($SourceRoot)
    if (-not $normalizedSourceRoot.EndsWith([System.IO.Path]::DirectorySeparatorChar.ToString())) {
        $normalizedSourceRoot += [System.IO.Path]::DirectorySeparatorChar
    }

    if (-not (Test-Path -LiteralPath $SourceRoot)) {
        return $copied
    }

    foreach ($file in Get-ChildItem -LiteralPath $SourceRoot -Recurse -File -Filter $Filter) {
        $normalizedFilePath = [System.IO.Path]::GetFullPath($file.FullName)
        $relativePath = $normalizedFilePath.Substring($normalizedSourceRoot.Length)
        $targetPath = Join-Path $TargetRoot $relativePath
        Ensure-Directory -Path (Split-Path -Parent $targetPath)
        Copy-Item -LiteralPath $file.FullName -Destination $targetPath -Force
        $copied += [pscustomobject]@{
            source = $file.FullName
            target = $targetPath
        }
    }

    return $copied
}

function Count-Matches {
    param(
        [Parameter(Mandatory = $true)][string]$Root,
        [Parameter(Mandatory = $true)][string]$Pattern
    )

    return @(
        Get-ChildItem -LiteralPath $Root -Recurse -File -Filter *.java |
            Select-String -Pattern $Pattern
    ).Count
}

$resourcesRoot = Join-Path $ProjectRoot "src/main/resources"
$assetsRoot = Join-Path $resourcesRoot "assets"
$javaRoot = Join-Path $ProjectRoot "migration\1.21.1\archive\legacy-source-tree\src\main\java"

if (-not (Test-Path -LiteralPath $resourcesRoot)) {
    throw "Could not find resources directory at '$resourcesRoot'."
}

if (-not (Test-Path -LiteralPath $javaRoot)) {
    throw "Could not find archived legacy Java sources at '$javaRoot'."
}

Ensure-Directory -Path $OutputRoot

$modernResourcesRoot = Join-Path $OutputRoot "src/main/resources"
$reportRoot = Join-Path $OutputRoot "reports"
Ensure-Directory -Path $modernResourcesRoot
Ensure-Directory -Path $reportRoot

$gradlePropertiesPath = Join-Path $ProjectRoot "gradle.properties"
$projectProperties = if (Test-Path -LiteralPath $gradlePropertiesPath) {
    Read-KeyValueFile -Path $gradlePropertiesPath
} else {
    [ordered]@{}
}

$langReports = New-Object System.Collections.Generic.List[object]
$advancementReports = New-Object System.Collections.Generic.List[object]

if (Test-Path -LiteralPath $assetsRoot) {
    foreach ($namespaceDir in Get-ChildItem -LiteralPath $assetsRoot -Directory) {
        $namespace = $namespaceDir.Name

        $langDir = Join-Path $namespaceDir.FullName "lang"
        if (Test-Path -LiteralPath $langDir) {
            foreach ($langFile in Get-ChildItem -LiteralPath $langDir -File -Filter *.lang) {
                $targetDir = Join-Path $modernResourcesRoot "assets/$namespace/lang"
                $targetFile = Join-Path $targetDir ([System.IO.Path]::GetFileNameWithoutExtension($langFile.Name) + ".json")
                $langReports.Add((Convert-LangFile -SourcePath $langFile.FullName -TargetPath $targetFile -Namespace $namespace))
            }
        }

        $advancementDir = Join-Path $namespaceDir.FullName "advancements"
        if (Test-Path -LiteralPath $advancementDir) {
            $targetAdvancementDir = Join-Path $modernResourcesRoot "data/$namespace/advancement"
            foreach ($copied in Copy-Tree -SourceRoot $advancementDir -TargetRoot $targetAdvancementDir -Filter *.json) {
                $advancementReports.Add($copied)
            }
        }
    }
}

$legacyAtPath = Join-Path $resourcesRoot "hbm_at.cfg"
if (Test-Path -LiteralPath $legacyAtPath) {
    $atTargetDir = Join-Path $modernResourcesRoot "META-INF"
    Ensure-Directory -Path $atTargetDir
    Copy-Item -LiteralPath $legacyAtPath -Destination (Join-Path $atTargetDir "accesstransformer.cfg") -Force
}

$templateRoot = Join-Path $OutputRoot "templates"
Ensure-Directory -Path $templateRoot

$modId = if ($projectProperties.Contains("modId")) { $projectProperties["modId"] } else { "hbm" }
$modName = if ($projectProperties.Contains("modName")) { $projectProperties["modName"] } else { "HBM Nuclear Tech" }
$modVersion = if ($projectProperties.Contains("modVersion")) { $projectProperties["modVersion"] } else { "0.0.0" }
$modGroup = if ($projectProperties.Contains("modGroup")) { $projectProperties["modGroup"] } else { "com.example" }
$modAuthors = "HBMTheBobcat, Drillgon200, TheOriginalGolem, Alcatergit"

$gradleTemplate = @'
# Generated by tools/migrate_1_21_1.ps1
minecraft_version=1.21.1
minecraft_version_range=[1.21.1,1.22)

# Fill these in with the NeoForge version you actually choose.
neo_version=
neo_version_range=
loader_version_range=[1,)

mod_id=__MOD_ID__
mod_name=__MOD_NAME__
mod_license=See LICENSE and LICENSE.LESSER in repository root
mod_version=__MOD_VERSION__-1.21.1
mod_group_id=__MOD_GROUP__
mod_authors=__MOD_AUTHORS__
mod_description=Generated 1.21.1 port scaffold from the legacy 1.12.2 source snapshot. Replace this with a real description before publishing.
'@
$gradleTemplate = $gradleTemplate.
    Replace("__MOD_ID__", $modId).
    Replace("__MOD_NAME__", $modName).
    Replace("__MOD_VERSION__", $modVersion).
    Replace("__MOD_GROUP__", $modGroup).
    Replace("__MOD_AUTHORS__", $modAuthors)
Set-Content -LiteralPath (Join-Path $templateRoot "gradle.properties.template") -Value $gradleTemplate -Encoding utf8

$modsTomlTemplate = @'
# Generated by tools/migrate_1_21_1.ps1
modLoader="javafml"
loaderVersion="[1,)"
license="See LICENSE and LICENSE.LESSER in repository root"
issueTrackerURL="https://github.com/Alcatergit/Hbm-s-Nuclear-Tech-GIT/issues"
showAsResourcePack=true
showAsDataPack=true

[[mods]]
modId="__MOD_ID__"
version="${file.jarVersion}"
displayName="__MOD_NAME__"
authors="__MOD_AUTHORS__"
displayURL="https://modrinth.com/mod/ntm-extended"
description='''
Generated 1.21.1 port scaffold from the legacy 1.12.2 source snapshot.
The original codebase still needs a full NeoForge rewrite for registries,
block entities, menus, networking, configs, recipes, tags, worldgen, and rendering.
'''

[[dependencies.__MOD_ID__]]
modId="neoforge"
type="required"
versionRange="[REPLACE_WITH_NEOFORGE_RANGE]"
ordering="NONE"
side="BOTH"

[[dependencies.__MOD_ID__]]
modId="minecraft"
type="required"
versionRange="[1.21.1]"
ordering="NONE"
side="BOTH"

[[features.__MOD_ID__]]
javaVersion="[21,)"
'@
$modsTomlTemplate = $modsTomlTemplate.
    Replace("__MOD_ID__", $modId).
    Replace("__MOD_NAME__", $modName).
    Replace("__MOD_AUTHORS__", $modAuthors)
Set-Content -LiteralPath (Join-Path $templateRoot "neoforge.mods.toml.template") -Value $modsTomlTemplate -Encoding utf8

$audit = [ordered]@{
    generatedAtUtc = (Get-Date).ToUniversalTime().ToString("o")
    projectRoot = $ProjectRoot
    outputRoot = $OutputRoot
    javaFiles = (Get-ChildItem -LiteralPath $javaRoot -Recurse -File -Filter *.java).Count
    resourceFiles = (Get-ChildItem -LiteralPath $resourcesRoot -Recurse -File).Count
    blockDefinitions = @(
        Select-String -Path (Join-Path $javaRoot "com/hbm/blocks/ModBlocks.java") -Pattern "public static final Block "
    ).Count
    itemDefinitions = @(
        Select-String -Path (Join-Path $javaRoot "com/hbm/items/ModItems.java") -Pattern "public static final Item "
    ).Count
    entityRegistrations = Count-Matches -Root $javaRoot -Pattern "registerModEntity\("
    tileEntityRegistrations = Count-Matches -Root $javaRoot -Pattern "registerTileEntity\("
    blockContainerUses = Count-Matches -Root $javaRoot -Pattern "extends BlockContainer"
    tileEntityBaseUses = Count-Matches -Root $javaRoot -Pattern "extends TileEntity"
    oreDictionaryUses = Count-Matches -Root $javaRoot -Pattern "OreDictionary"
    guiContainerUses = Count-Matches -Root $javaRoot -Pattern "GuiContainer"
    legacyConfigUses = Count-Matches -Root $javaRoot -Pattern "net\.minecraftforge\.common\.config\.Configuration"
    sideOnlyUses = Count-Matches -Root $javaRoot -Pattern "SideOnly"
    optionalApiUses = Count-Matches -Root $javaRoot -Pattern "@Optional"
    targetPointUses = Count-Matches -Root $javaRoot -Pattern "TargetPoint"
    capabilityInjectUses = Count-Matches -Root $javaRoot -Pattern "CapabilityInject"
    accessTransformerPresent = (Test-Path -LiteralPath $legacyAtPath)
    coremodPluginPresent = @(
        Get-ChildItem -LiteralPath $javaRoot -Recurse -File -Filter *.java |
            Select-String -Pattern "IFMLLoadingPlugin"
    ).Count -gt 0
    langFilesConverted = $langReports.Count
    langEntriesConverted = ($langReports | Measure-Object -Property entries -Sum).Sum
    modernItemKeyAliases = ($langReports | Measure-Object -Property modernItemKeyAliases -Sum).Sum
    modernBlockKeyAliases = ($langReports | Measure-Object -Property modernBlockKeyAliases -Sum).Sum
    modernEntityKeyAliases = ($langReports | Measure-Object -Property modernEntityKeyAliases -Sum).Sum
    advancementsCopied = $advancementReports.Count
}

$auditJson = $audit | ConvertTo-Json -Depth 8
Set-Content -LiteralPath (Join-Path $reportRoot "codebase-audit.json") -Value $auditJson -Encoding utf8

$langReportJson = $langReports | ConvertTo-Json -Depth 8
Set-Content -LiteralPath (Join-Path $reportRoot "lang-conversion.json") -Value $langReportJson -Encoding utf8

$advancementReportJson = $advancementReports | ConvertTo-Json -Depth 8
Set-Content -LiteralPath (Join-Path $reportRoot "advancement-copy.json") -Value $advancementReportJson -Encoding utf8

$summaryLines = @(
    "Generated 1.21.1 migration outputs.",
    "Java files: $($audit.javaFiles)",
    "Resource files: $($audit.resourceFiles)",
    "Blocks defined: $($audit.blockDefinitions)",
    "Items defined: $($audit.itemDefinitions)",
    "Entity registrations: $($audit.entityRegistrations)",
    "Tile entity registrations: $($audit.tileEntityRegistrations)",
    "Lang files converted: $($audit.langFilesConverted)",
    "Lang entries converted: $($audit.langEntriesConverted)",
    "Modern item translation aliases: $($audit.modernItemKeyAliases)",
    "Modern block translation aliases: $($audit.modernBlockKeyAliases)",
    "Modern entity translation aliases: $($audit.modernEntityKeyAliases)",
    "Advancements copied: $($audit.advancementsCopied)"
)

Set-Content -LiteralPath (Join-Path $reportRoot "summary.txt") -Value $summaryLines -Encoding ascii

Write-Host "Migration outputs written to $OutputRoot"
