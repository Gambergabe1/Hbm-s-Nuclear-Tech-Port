param(
    [string]$ProjectRoot = (Resolve-Path (Join-Path $PSScriptRoot "..")).Path
)

$ErrorActionPreference = "Stop"

$reportsRoot = Join-Path $ProjectRoot "migration\1.21.1\reports"
$legacyJavaRoot = Join-Path $ProjectRoot "migration\1.21.1\archive\legacy-source-tree\src\main\java"
New-Item -ItemType Directory -Path $reportsRoot -Force | Out-Null

$paths = @{
    LegacyItems       = Join-Path $legacyJavaRoot "com\hbm\items\ModItems.java"
    LegacyBlocks      = Join-Path $legacyJavaRoot "com\hbm\blocks\ModBlocks.java"
    LegacyMain        = Join-Path $legacyJavaRoot "com\hbm\main\MainRegistry.java"
    NeoItems          = Join-Path $ProjectRoot "src\neo\java\com\hbm\registry\HbmItems.java"
    NeoBlocks         = Join-Path $ProjectRoot "src\neo\java\com\hbm\registry\HbmBlocks.java"
    NeoEntityTypes    = Join-Path $ProjectRoot "src\neo\java\com\hbm\registry\HbmEntityTypes.java"
    NeoBlockEntities  = Join-Path $ProjectRoot "src\neo\java\com\hbm\registry\HbmBlockEntityTypes.java"
    NeoMenuTypes      = Join-Path $ProjectRoot "src\neo\java\com\hbm\registry\HbmMenuTypes.java"
    Audit             = Join-Path $reportsRoot "codebase-audit.json"
}

function Get-RawContent {
    param([string]$Path)
    Get-Content -Path $Path -Raw
}

function Get-SortedUnique {
    param([System.Collections.IEnumerable]$Values)
    @(@($Values) | Where-Object { $_ } | Sort-Object -Unique)
}

function Get-DeclarationIds {
    param(
        [string]$Path,
        [string]$DeclarationStartPattern
    )

    $definitions = New-Object System.Collections.Generic.List[string]
    $currentDefinition = ""
    $capturing = $false

    foreach ($line in Get-Content -Path $Path) {
        if (-not $capturing) {
            if ($line -match $DeclarationStartPattern) {
                $currentDefinition = $line
                $capturing = $true

                if ($line -match ';') {
                    $definitions.Add($currentDefinition)
                    $currentDefinition = ""
                    $capturing = $false
                }
            }

            continue
        }

        $currentDefinition += "`n$line"

        if ($line -match ';') {
            $definitions.Add($currentDefinition)
            $currentDefinition = ""
            $capturing = $false
        }
    }

    $ids = New-Object System.Collections.Generic.List[string]

    foreach ($definition in $definitions) {
        $quoted = @([regex]::Matches($definition, '"([^"\r\n]+)"') | ForEach-Object { $_.Groups[1].Value })
        if ($quoted.Count -eq 0) {
            continue
        }

        $id = $quoted[$quoted.Count - 1]
        if ($id -match '[:/\\]' -or $id -match '^\s*$') {
            continue
        }

        $ids.Add($id)
    }

    Get-SortedUnique -Values $ids
}

function Get-CapturedIds {
    param(
        [string]$Path,
        [string]$Pattern
    )

    $content = Get-RawContent -Path $Path
    $options = [System.Text.RegularExpressions.RegexOptions]::Multiline -bor [System.Text.RegularExpressions.RegexOptions]::Singleline
    Get-SortedUnique -Values @([regex]::Matches($content, $Pattern, $options) | ForEach-Object { $_.Groups[1].Value })
}

function Get-CoveragePercent {
    param(
        [int]$Covered,
        [int]$Total
    )

    if ($Total -le 0) {
        return "0.0%"
    }

    return "{0:P1}" -f ($Covered / $Total)
}

function Get-SampleList {
    param(
        [string[]]$Values,
        [int]$Count = 25
    )

    @($Values | Select-Object -First $Count)
}

$audit = Get-Content -Path $paths.Audit -Raw | ConvertFrom-Json

$legacyItemIds = Get-DeclarationIds -Path $paths.LegacyItems -DeclarationStartPattern '^\s*public\s+static\s+final\s+Item\s+\w+\s*='
$legacyBlockIds = Get-DeclarationIds -Path $paths.LegacyBlocks -DeclarationStartPattern '^\s*public\s+static\s+final\s+Block\s+\w+\s*='
$neoItemIds = Get-DeclarationIds -Path $paths.NeoItems -DeclarationStartPattern '^\s*public\s+static\s+final\s+DeferredItem<Item>\s+\w+\s*='
$neoBlockItemIds = Get-DeclarationIds -Path $paths.NeoItems -DeclarationStartPattern '^\s*public\s+static\s+final\s+DeferredItem<BlockItem>\s+\w+\s*='
$neoBlockIds = Get-DeclarationIds -Path $paths.NeoBlocks -DeclarationStartPattern '^\s*public\s+static\s+final\s+DeferredBlock<[^>]+>\s+\w+\s*='

$legacyEntityIds = Get-CapturedIds -Path $paths.LegacyMain -Pattern 'registerModEntity\(\s*new\s+ResourceLocation\([^,]+,\s*"([^"]+)"'
$neoEntityIds = Get-CapturedIds -Path $paths.NeoEntityTypes -Pattern 'ENTITY_TYPES\.register\(\s*"([^"]+)"'
$legacyBlockEntityIds = Get-CapturedIds -Path $paths.LegacyMain -Pattern 'registerTileEntity\([^;]+?new\s+ResourceLocation\([^,]+,\s*"([^"]+)"\)'
$neoBlockEntityIds = Get-CapturedIds -Path $paths.NeoBlockEntities -Pattern 'BLOCK_ENTITY_TYPES\.register\(\s*"([^"]+)"'
$neoMenuIds = Get-CapturedIds -Path $paths.NeoMenuTypes -Pattern 'MENU_TYPES\.register\(\s*"([^"]+)"'

$coveredLegacyItemIds = foreach ($id in $legacyItemIds) {
    if ($neoItemIds -contains $id) {
        $id
        continue
    }

    if ($id -eq "circuit" -and ($neoItemIds | Where-Object { $_ -like "circuit_*" }).Count -gt 0) {
        $id
    }
}
$coveredLegacyItemIds = Get-SortedUnique -Values $coveredLegacyItemIds

$missingLegacyItemIds = foreach ($id in $legacyItemIds) {
    if ($coveredLegacyItemIds -contains $id) {
        continue
    }
    $id
}
$missingLegacyItemIds = Get-SortedUnique -Values $missingLegacyItemIds

$coveredLegacyBlockIds = foreach ($id in $legacyBlockIds) {
    if ($neoBlockIds -contains $id) {
        $id
    }
}
$coveredLegacyBlockIds = Get-SortedUnique -Values $coveredLegacyBlockIds
$missingLegacyBlockIds = Get-SortedUnique -Values ($legacyBlockIds | Where-Object { $coveredLegacyBlockIds -notcontains $_ })

$coveredLegacyEntityIds = Get-SortedUnique -Values ($legacyEntityIds | Where-Object { $neoEntityIds -contains $_ })
$missingLegacyEntityIds = Get-SortedUnique -Values ($legacyEntityIds | Where-Object { $neoEntityIds -notcontains $_ })

$coveredLegacyBlockEntityIds = Get-SortedUnique -Values ($legacyBlockEntityIds | Where-Object { $neoBlockEntityIds -contains $_ })
$missingLegacyBlockEntityIds = Get-SortedUnique -Values ($legacyBlockEntityIds | Where-Object { $neoBlockEntityIds -notcontains $_ })

$neoJavaFiles = (Get-ChildItem -Path (Join-Path $ProjectRoot "src\neo\java") -Recurse -Filter *.java | Measure-Object).Count
$legacyJavaFiles = (Get-ChildItem -Path $legacyJavaRoot -Recurse -Filter *.java | Measure-Object).Count

$report = [ordered]@{
    generatedAtUtc = (Get-Date).ToUniversalTime().ToString("o")
    projectRoot = $ProjectRoot
    sourceSets = [ordered]@{
        neoJavaFiles = $neoJavaFiles
        legacyJavaArchivePath = $legacyJavaRoot
        legacyJavaFilesArchivedReference = $legacyJavaFiles
    }
    coverage = [ordered]@{
        items = [ordered]@{
            legacyDeclared = $legacyItemIds.Count
            neoRegistered = $neoItemIds.Count
            legacyCovered = $coveredLegacyItemIds.Count
            legacyMissing = $missingLegacyItemIds.Count
            coverage = (Get-CoveragePercent -Covered $coveredLegacyItemIds.Count -Total $legacyItemIds.Count)
            notes = @(
                "Counts are declaration-level and do not expand every legacy ItemAutogen family."
                "Legacy metadata circuit coverage is normalized against explicit NeoForge circuit_* items."
            )
            sampleMissing = (Get-SampleList -Values $missingLegacyItemIds -Count 30)
        }
        blocks = [ordered]@{
            legacyDeclared = $legacyBlockIds.Count
            neoRegistered = $neoBlockIds.Count
            legacyCovered = $coveredLegacyBlockIds.Count
            legacyMissing = $missingLegacyBlockIds.Count
            coverage = (Get-CoveragePercent -Covered $coveredLegacyBlockIds.Count -Total $legacyBlockIds.Count)
            sampleMissing = (Get-SampleList -Values $missingLegacyBlockIds -Count 30)
        }
        entities = [ordered]@{
            legacyDeclared = $legacyEntityIds.Count
            neoRegistered = $neoEntityIds.Count
            legacyCovered = $coveredLegacyEntityIds.Count
            legacyMissing = $missingLegacyEntityIds.Count
            coverage = (Get-CoveragePercent -Covered $coveredLegacyEntityIds.Count -Total $legacyEntityIds.Count)
            sampleMissing = (Get-SampleList -Values $missingLegacyEntityIds -Count 20)
        }
        blockEntities = [ordered]@{
            legacyDeclared = $legacyBlockEntityIds.Count
            neoRegistered = $neoBlockEntityIds.Count
            legacyCovered = $coveredLegacyBlockEntityIds.Count
            legacyMissing = $missingLegacyBlockEntityIds.Count
            coverage = (Get-CoveragePercent -Covered $coveredLegacyBlockEntityIds.Count -Total $legacyBlockEntityIds.Count)
            sampleMissing = (Get-SampleList -Values $missingLegacyBlockEntityIds -Count 20)
        }
        menus = [ordered]@{
            neoRegistered = $neoMenuIds.Count
            legacyGuiContainerUsesFromAudit = $audit.guiContainerUses
        }
    }
    auditSnapshot = [ordered]@{
        javaFiles = $audit.javaFiles
        resourceFiles = $audit.resourceFiles
        sideOnlyUses = $audit.sideOnlyUses
        guiContainerUses = $audit.guiContainerUses
        tileEntityRegistrations = $audit.tileEntityRegistrations
        oreDictionaryUses = $audit.oreDictionaryUses
        accessTransformerPresent = $audit.accessTransformerPresent
        coremodPluginPresent = $audit.coremodPluginPresent
    }
    neoRegistrySnapshot = [ordered]@{
        itemRegistrations = $neoItemIds.Count
        blockItemRegistrations = $neoBlockItemIds.Count
        blockRegistrations = $neoBlockIds.Count
        entityRegistrations = $neoEntityIds.Count
        blockEntityRegistrations = $neoBlockEntityIds.Count
        menuRegistrations = $neoMenuIds.Count
    }
}

$jsonPath = Join-Path $reportsRoot "neo-port-gap.json"
$markdownPath = Join-Path $reportsRoot "neo-port-gap.md"

$report | ConvertTo-Json -Depth 10 | Set-Content -Path $jsonPath -Encoding UTF8

$missingItemsMarkdown = ($report.coverage.items.sampleMissing | ForEach-Object { "- $_" }) -join "`r`n"
$missingBlocksMarkdown = ($report.coverage.blocks.sampleMissing | ForEach-Object { "- $_" }) -join "`r`n"
$missingEntitiesMarkdown = ($report.coverage.entities.sampleMissing | ForEach-Object { "- $_" }) -join "`r`n"
$missingBlockEntitiesMarkdown = ($report.coverage.blockEntities.sampleMissing | ForEach-Object { "- $_" }) -join "`r`n"

$markdown = @"
# NeoForge Port Gap Report

Generated: $($report.generatedAtUtc)

## Source Sets

- NeoForge Java files under `src/neo/java`: $neoJavaFiles
- Archived legacy Java reference files under `migration/1.21.1/archive/legacy-source-tree/src/main/java`: $legacyJavaFiles

## Coverage Summary

| Area | Legacy | Neo | Covered | Missing | Coverage |
| --- | ---: | ---: | ---: | ---: | ---: |
| Items (declared) | $($report.coverage.items.legacyDeclared) | $($report.coverage.items.neoRegistered) | $($report.coverage.items.legacyCovered) | $($report.coverage.items.legacyMissing) | $($report.coverage.items.coverage) |
| Blocks | $($report.coverage.blocks.legacyDeclared) | $($report.coverage.blocks.neoRegistered) | $($report.coverage.blocks.legacyCovered) | $($report.coverage.blocks.legacyMissing) | $($report.coverage.blocks.coverage) |
| Entities | $($report.coverage.entities.legacyDeclared) | $($report.coverage.entities.neoRegistered) | $($report.coverage.entities.legacyCovered) | $($report.coverage.entities.legacyMissing) | $($report.coverage.entities.coverage) |
| Block Entities | $($report.coverage.blockEntities.legacyDeclared) | $($report.coverage.blockEntities.neoRegistered) | $($report.coverage.blockEntities.legacyCovered) | $($report.coverage.blockEntities.legacyMissing) | $($report.coverage.blockEntities.coverage) |

## Immediate Blockers

- NeoForge menu registrations: $($report.coverage.menus.neoRegistered)
- Legacy `GuiContainer` uses from audit: $($report.coverage.menus.legacyGuiContainerUsesFromAudit)
- Legacy `@SideOnly` uses from audit: $($report.auditSnapshot.sideOnlyUses)
- Legacy tile entity registrations from audit: $($report.auditSnapshot.tileEntityRegistrations)
- Legacy OreDictionary uses from audit: $($report.auditSnapshot.oreDictionaryUses)
- Access transformer present: $($report.auditSnapshot.accessTransformerPresent)
- Coremod plugin present: $($report.auditSnapshot.coremodPluginPresent)

## Sample Missing Item IDs

$missingItemsMarkdown

## Sample Missing Block IDs

$missingBlocksMarkdown

## Sample Missing Entity IDs

$missingEntitiesMarkdown

## Sample Missing Block Entity IDs

$missingBlockEntitiesMarkdown

## Notes

- This report measures declared registry coverage, not gameplay parity or rendering/network correctness.
- Legacy `ItemAutogen` families are not fully expanded here; they still represent major unported surface area.
- Legacy metadata circuits are treated as covered when explicit NeoForge `circuit_*` items exist.
"@

$markdown | Set-Content -Path $markdownPath -Encoding UTF8

Write-Output "Wrote $jsonPath"
Write-Output "Wrote $markdownPath"
