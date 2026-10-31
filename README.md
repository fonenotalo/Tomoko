# 🖤 Tomoko

<p align="center"><img src="assets/icon.jpg" width="150" alt="Tomoko icon"></p>

<h2 align="center"><b>Tomoko</b></h2>
<h4 align="center">A free, lightweight streaming front-end for Android, desktop, and iOS.</h4>

<p align="center">
<a href="https://github.com/fonenotalo/Tomoko/releases"><img src="https://img.shields.io/github/release/fonenotalo/Tomoko.svg?style=for-the-badge&logo=github&color=5865F2"></a>
<a href="https://github.com/fonenotalo/Tomoko/actions/workflows/build-release-apk.yml"><img src="https://img.shields.io/github/actions/workflow-run/fonenotalo/Tomoko/build-release-apk.yml?branch=main&event=push&style=for-the-badge"></a>
<a href="https://www.gnu.org/licenses/gpl-3.0" alt="License: GPLv3"><img src="https://img.shields.io/badge/License-GPL%20v3-blue?style=for-the-badge&color=2ECC71"></a>
<a href="https://github.com/fonenotalo/Tomoko/stargazers"><img src="https://img.shields.io/github/stars/fonenotalo/Tomoko?style=for-the-badge&logo=github&color=F1E05A"></a>
</p>

<p align="center">
<a href="https://github.com/fonenotalo/Tomoko/discussions"><img src="https://img.shields.io/badge/Chat-Discussions-blue?style=for-the-badge&color=3498DB"></a>
<a href="https://github.com/fonenotalo/Tomoko/issues"><img src="https://img.shields.io/badge/Issues-Open-green?style=for-the-badge&color=27AE60"></a>
<a href="https://github.com/fonenotalo/Tomoko/blob/main/app/build.gradle.kts"><img src="https://img.shields.io/badge/Platform-Android%2C%20Desktop%2C%20iOS-brightgreen?style=for-the-badge&logo=android&logoColor=white&logoSize=12"></a>
</p>

---

## 📱 Description

Tomoko is a free, open-source front-end for popular video and audio streaming services. It provides a clean, private, and ad-free experience without requiring any accounts.

Built on Kotlin Multiplatform, Tomoko runs on Android, desktop, and iOS with a shared codebase.

### ✨ Features

- 🎥 Watch videos at resolutions up to 4K
- 🎧 Background audio — listen while using other apps
- 🪟 Picture-in-Picture floating player mode
- 📡 Live streams support
- 📝 Subtitles / Closed captions
- 🔍 Search videos, audios, channels, playlists
- 📺 Subscribe to channels without logging in
- 🔔 Notifications for new videos
- 📥 Download videos/audios/subtitles
- 🎵 Multiple services: YouTube, PeerTube, Bandcamp, SoundCloud
- 🌓 Dark mode with Material You support
- 🎨 Pixel-style Material Design 3 interface

## 📦 Installation

Install Tomoko via:

1. **GitHub Releases** — Download the latest APK
2. **Build from source** — Clone and build with Gradle

## 📜 License

Tomoko is free software: you can redistribute and/or modify it under the terms of the **GNU General Public License v3** as published by the Free Software Foundation.

---

### ⚙️ Build from Source

```bash
# Instalar Java 21, Kotlin, Android SDK
echo "LASTFM_API_KEY=your_api_key" > local.properties
echo "LASTFM_SECRET=your_secret" >> local.properties

echo "LISTEN_TOGETHER_SERVER=https://api.tomoko.app" >> local.properties

./gradlew assembleContinuous --stacktrace
```

This produces an APK at `app/build/outputs/apk/continuous/app-continuous.apk`.

*Last updated: 2026-10-31*
