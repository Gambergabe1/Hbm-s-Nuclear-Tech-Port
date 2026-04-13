These files were removed from the live source and resource trees during the NeoForge 1.21.1 port.

Archived here:

- The Forge 1.12 coremod package from `migration/1.21.1/archive/legacy-source-tree/src/main/java/com/hbm/core`
- The legacy access transformer input from `src/main/resources/hbm_at.cfg`
- The legacy manifest bootstrap from `src/main/resources/META-INF/MANIFEST.mf`
- An old generated access-transformer copy from `migration/1.21.1/src/main/resources/META-INF/accesstransformer.cfg`

Reason:

- NeoForge 1.21.1 does not support the old `IFMLLoadingPlugin` coremod path.
- The port no longer has a justified supported use for an access transformer.

Rule going forward:

- If behavior from these files is still needed, rewrite it through supported NeoForge mechanisms.
- Do not restore these files to active `src/main` or `src/neo` source sets unchanged.
