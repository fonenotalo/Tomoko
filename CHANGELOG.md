# Tomoko Changelog

## [beta-1-0-0] - 2026-10-31

### Added
- Rebranded the NewPipe-based application as **Tomoko**, including package, metadata, and documentation updates.
- Introduced a Pixel-inspired visual theme with light and dark color palettes, reusable dimensions, typography, and component styles.
- Added the Tomoko adaptive launcher icon using artwork sampled from `icon.jpg`.
- Replaced remaining NewPipe notification and splash icons with Tomoko branding (`ic_play_arrow`, `ic_file_download`).
- Transformed Tomoko into a music-first player: the main screen defaults to a **Trending music** tab, with an opt-in **Video Mode** restoring the traditional video experience.
- Added `MusicTab` resolving the service `trending_music` kiosk (falling back to the default kiosk).
- Added the `video_mode` preference key and a **Video Mode** switch in main settings.
- Added Last.fm credentials and Listen Together server configuration to the release workflow.
- Added release signing configuration using a keystore supplied through repository secrets.
- Added APK signature verification before artifact upload.

### Changed
- Applied the Tomoko Pixel theme through the Android manifest and made it the definitive theme for dialogs and activities.
- Simplified `ThemeHelper` to always return the Pixel theme instead of selecting light/dark/black variants.
- Updated `RouterActivity` and the video detail player crasher to use the Pixel theme wrapper.
- Added a night variant of `styles_pixel.xml`.
- Migrated extractor imports to the upstream `org.schabi.newpipe.extractor` namespace.
- Cleaned up README and contribution guidelines.

### Fixed
- Corrected the Tomoko launcher icon: adaptive icon background color, `roundIcon`, and regenerated mipmap assets.
- Corrected the available kiosks lookup to use `Set` instead of `List`.
- Removed `apply false` from Gradle plugins to enable the Kotlin build.

### Removed
- Removed legacy NewPipe triangle/update icon drawables (`ic_newpipe_triangle_white`, `ic_newpipe_update`) and their density variants.
- Removed legacy light/dark/black theme selection branches superseded by the Pixel theme.

### Build & Infrastructure
- Updated the GitHub Actions release workflow to use JDK 21.
- Updated the release workflow to build and upload a signed APK (`tomoko-release-signed`).
- Added keystore preparation and APK signature verification steps to the release workflow.

### Notes
- This is a **beta** release; Video Mode is disabled by default and can be enabled in settings.
- The release workflow requires signing secrets to be configured in the GitHub repository.
- APK signature verification runs before the release artifact is uploaded.
- Published as a beta and not marked as latest.