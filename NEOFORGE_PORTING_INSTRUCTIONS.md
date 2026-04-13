# Porting HBM to NeoForge 21.1.222

## Required Changes

### 1. Update build.gradle

Replace your current build.gradle with this NeoForge-compatible version:

```gradle
plugins {
    id 'java-library'
    id 'maven-publish'
    id 'net.neoforged.gradle.userdev' version '7.0.142'
}

java.toolchain.languageVersion = JavaLanguageVersion.of(21)

archivesBaseName = 'hbm'
version = '1.0.0'
group = 'com.hbm'

repositories {
    mavenLocal()
}

dependencies {
    implementation "net.neoforged:neoforge:21.1.222"
}

// Configure Gradle to output to the build directory
tasks.withType(JavaCompile) {
    options.encoding = 'UTF-8'
}

processResources {
    inputs.property 'version', version
    filteringCharset 'UTF-8'
    
    filesMatching('META-INF/neoforge.mods.toml') {
        expand 'version': version
    }
}

jar {
    manifest {
        attributes([
            'Specification-Title': 'hbm',
            'Specification-Vendor': 'hbm',
            'Specification-Version': '1',
            'Implementation-Title': 'hbm',
            'Implementation-Version': version,
            'Implementation-Vendor': 'hbm',
        ])
    }
}

// Merge the resources and classes into the jar
jar.finalizedBy('reobfJar')
```

### 2. Update gradle.properties

Make sure your gradle.properties contains:

```properties
# Gradle properties
org.gradle.jvmargs=-Xmx4G
org.gradle.daemon=false
```

### 3. Update mods.toml

Rename your `mods.toml` to `neoforge.mods.toml` and update it:

```toml
modLoader="javafml"
loaderVersion="[1,)"
license="All rights reserved"
issueTrackerURL="https://github.com/yourusername/hbm/issues"

[[mods]]
modId="hbm"
version="${file.jarVersion}"
displayName="HBM's Nuclear Tech Mod"
logoFile="logo.png"
credits="HBM and contributors"
authors="HBM"
description='''
HBM's Nuclear Tech Mod - A mod about nuclear technology, machinery, and weapons.
'''

[[dependencies.hbm]]
modId="neoforge"
type="required"
versionRange="[21.1.222,)"
ordering="NONE"
side="BOTH"

[[dependencies.hbm]]
modId="minecraft"
type="required"
versionRange="[1.21.1]"
ordering="NONE"
side="BOTH"
```

### 4. Update Main Class

Ensure your main class uses the NeoForge annotations:

```java
package com.hbm.main;

import net.neoforged.fml.common.Mod;
import net.neoforged.bus.api.IEventBus;

@Mod("hbm")
public class HbmMod {
    public static final String MODID = "hbm";

    public HbmMod(IEventBus modEventBus) {
        // Registration happens here
    }
}
```

### 5. Recipe System Implementation

The recipe serializers and types we created are already NeoForge compatible.

### 6. Data Generation (Optional)

If you want to generate recipe JSON files programmatically, create a data generator:

```java
// In your main class or a separate datagen class
public void gatherData(GatherDataEvent event) {
    DataGenerator gen = event.getGenerator();
    PackOutput packOutput = gen.getPackOutput();
    
    gen.addProvider(event.includeServer(), new HbmRecipeProvider(packOutput));
}
```

## File Structure for Recipes

Place your JSON recipes in:
`src/main/resources/data/hbm/recipes/pressing/`
`src/main/resources/data/hbm/recipes/shredding/`
`src/main/resources/data/hbm/recipes/centrifuging/`
`src/main/resources/data/hbm/recipes/rbmk/`
`src/main/resources/data/hbm/recipes/chem_plant/`

Each recipe file should follow the format shown in the example JSON files created.