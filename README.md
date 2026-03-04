# 📺 SmartTV Launcher

A modern, beautiful Android TV Launcher UI built with Kotlin.

## Features

- 🕐 **Real-time clock & date** — Live clock display in top bar
- ☀️ **Weather widget** — Placeholder for weather integration
- ⭐ **Featured apps row** — Horizontal scrolling featured apps
- 📱 **Full app grid** — 6-column grid of all installed TV apps
- 🗂️ **Category tabs** — Filter by All, Streaming, Games, Music
- ⚙️ **Settings screen** — TV-optimized settings menu
- 🎮 **D-pad navigation** — Full remote control support
- ✨ **Focus animations** — Scale-up on focus with glow effects
- 🌙 **Dark theme** — Deep navy/dark UI optimized for TV viewing

## Project Structure

```
SmartTVLauncher/
├── app/
│   ├── src/main/
│   │   ├── java/com/smarttv/launcher/
│   │   │   ├── LauncherActivity.kt       # Main launcher
│   │   │   ├── SettingsActivity.kt       # Settings screen
│   │   │   ├── adapters/
│   │   │   │   ├── AppAdapter.kt         # Grid adapter
│   │   │   │   └── FeaturedAdapter.kt    # Featured row adapter
│   │   │   ├── models/
│   │   │   │   └── AppItem.kt            # App data model
│   │   │   └── utils/
│   │   │       ├── AppUtils.kt           # App list loader
│   │   │       └── TimeUtils.kt          # Date/time formatter
│   │   ├── res/
│   │   │   ├── layout/
│   │   │   │   ├── activity_launcher.xml
│   │   │   │   ├── activity_settings.xml
│   │   │   │   ├── item_app.xml
│   │   │   │   └── item_featured_app.xml
│   │   │   ├── drawable/                 # Vector icons & backgrounds
│   │   │   └── values/                   # Colors, strings, themes
│   │   └── AndroidManifest.xml
│   └── build.gradle
└── build.gradle
```

## Setup Instructions

### Requirements
- Android Studio Hedgehog or newer
- Android SDK 21+ (Android 5.0 Lollipop)
- Kotlin 1.9.0+
- Target: Android TV device or emulator

### Steps

1. **Open in Android Studio**
   ```
   File → Open → Select SmartTVLauncher folder
   ```

2. **Sync Gradle**
   - Click "Sync Now" when prompted

3. **Run on TV**
   - Use Android TV emulator or physical TV device
   - Target API 21+ with TV system image

4. **Set as Default Launcher** (on device)
   - Go to Settings → Device Preferences → Home Screen
   - Select "SmartTV Launcher"

## Customization

### Adding Weather API
In `LauncherActivity.kt`, find the `weatherWidget` section and integrate your preferred weather API (e.g., OpenWeatherMap):

```kotlin
// Replace placeholder in LauncherActivity.kt
private fun fetchWeather() {
    // Add your API call here
    // Update tvTemperature and tvWeatherDesc
}
```

### Changing Wallpaper/Background
Edit `res/drawable/bg_launcher.xml` to use an image:
```xml
<bitmap xmlns:android="..."
    android:src="@drawable/your_wallpaper"
    android:tileMode="disabled" />
```

### Adding More App Categories
Edit `AppUtils.kt` → `getCategory()` function to add your own category rules.

### Changing App Grid Columns
In `LauncherActivity.kt`:
```kotlin
GridLayoutManager(this@LauncherActivity, 6) // Change 6 to desired columns
```

## Key TV Development Notes

- **D-pad navigation**: All items are `android:focusable="true"` with proper `selector` drawables for focus states
- **No touchscreen required**: Manifest declares `touchscreen` as `required="false"`
- **Leanback category**: Launcher intent uses `CATEGORY_LEANBACK_LAUNCHER`
- **Back button blocked**: `onKeyDown` intercepts `KEYCODE_BACK` and `KEYCODE_HOME` to keep user in launcher

## License

MIT License — Free to use and modify.
