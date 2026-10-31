# Changelog

All notable changes to Tomoko are documented here.

## [beta-1-0-0] - 2026-10-31

### Branding and Identity
- Rebranded the NewPipe-based application as **Tomoko**.
- Started migrating application package references and source organization to the Tomoko identity.
- Updated project metadata and documentation to use the Tomoko name.
- Replaced the launcher icon with the new Tomoko artwork.

### Interface and Design
- Introduced a Pixel-inspired visual theme.
- Added light and dark color palettes.
- Added reusable dimensions for rounded corners, spacing, icon sizes, and elevation.
- Introduced typography and component styles inspired by Pixel and Material Design.
- Applied the new Tomoko theme through the Android application manifest.
- Refined system bar appearance and theme resources.

### Build and Release
- Corrected Gradle plugin configuration to enable the Kotlin build.
- Updated the GitHub Actions release workflow to use JDK 21.
- Added support for Last.fm credentials and Listen Together configuration through GitHub Actions secrets.
- Added release signing configuration using a keystore supplied through repository secrets.
- Added APK signature verification before artifact upload.

### Compatibility and Maintenance
- Updated extractor imports to use the upstream `org.schabi.newpipe.extractor` namespace.
- Cleaned up the README and refreshed contribution guidelines.
- Refined Android resources and build configuration during compilation troubleshooting.

---

**Build note:** The release workflow requires signing secrets to be configured in the GitHub repository. APK signature verification runs before the release artifact is uploaded.