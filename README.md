# KARAN BUDS — Ultimate Audio Experience

**Copyright © Karan**

An Android companion application for wireless earbuds built with **Kotlin**, **Jetpack Compose**, and **Material Design 3**, featuring a ColorOS 17-inspired UI, animated connection popups, 10-band equalizer, ANC/Transparency controls, touch gesture customization, and an earbud finder.

## Requirements & Permissions

- Android 7.0+ (minSdk 24), targetSdk 36
- Permissions declared in `AndroidManifest.xml`:
  - `BLUETOOTH`, `BLUETOOTH_ADMIN`
  - `BLUETOOTH_SCAN`, `BLUETOOTH_CONNECT` (Android 12+)
  - `ACCESS_FINE_LOCATION`, `ACCESS_COARSE_LOCATION`
  - `VIBRATE`, `INTERNET`, `POST_NOTIFICATIONS`

## Building the APK

1. Open the project in Android Studio (or use Gradle CLI with `ANDROID_SDK_ROOT` configured).
2. Run:
   ```bash
   gradle assembleDebug
   ```
3. The debug APK will be generated under `app/build/outputs/apk/debug/`.

## Pushing to GitHub

- In **Google AI Studio**: Use the **Push to GitHub** option in the settings/export menu, authorize your GitHub account, and choose a repository name without spaces (for example: `karan-buds`).
- If uploading manually via **Git CLI**:
  ```bash
  git init
  git add .
  git commit -m "Initial commit: Karan Buds Android app"
  git branch -M main
  git remote add origin https://github.com/<your-username>/karan-buds.git
  git push -u origin main
  ```
