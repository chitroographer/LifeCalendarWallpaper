# 🟠 Life Calendar Wallpaper

> A minimalist Android wallpaper app that visualizes your year and life as a grid of dots — automatically updated on your lockscreen every night at midnight.

---

## ✨ Features

- **Year Calendar** — Every day of the year visualized as a dot. White = past, Orange = today, Grey = future
- **Life Calendar** — Your entire life mapped in weeks based on your date of birth and expected lifespan
- **Auto Refresh** — Wallpaper regenerates automatically at midnight every night using WorkManager
- **Live Stats** — Shows days elapsed, % of year complete, and days remaining
- **DOB Form** — Enter your date of birth and the app calculates your life in weeks (default lifespan set to 72 years — avg Indian lifespan)
- **Wallpaper Picker** — Saves generated image to gallery and opens Android's native wallpaper picker to set on home screen + lockscreen

---

## 📱 Screenshots

> Add your screenshots here after taking them from your device

| Home Screen | Year Calendar | Life Calendar |
|---|---|---|
| ![Home](screenshots/home.png) | ![Year](screenshots/year.png) | ![Life](screenshots/life.png) |

---

## 🛠️ Tech Stack

| Technology | Purpose |
|---|---|
| Kotlin | Primary language |
| Canvas API | Drawing dot grid bitmaps |
| WorkManager | Scheduled midnight wallpaper refresh |
| SharedPreferences | Persisting user DOB and lifespan |
| MediaStore | Saving generated wallpaper to gallery |
| WallpaperManager | Opening native wallpaper picker |

---

## 📐 How It Works

### Year Calendar
1. App calculates `dayOfYear` for today's date
2. Draws a 7-column dot grid (1 row = 1 week) for all 365/366 days
3. Colors each dot — white for past, orange for today, dark grey for future
4. Adds stats text at the bottom showing days left and % complete
5. Saves bitmap to gallery and opens wallpaper picker

### Life Calendar
1. User enters their date of birth and expected lifespan (default 72 years)
2. App calculates total weeks lived vs total weeks in lifespan
3. Draws a 52-column dot grid (1 row = 1 year, 1 dot = 1 week)
4. Colors dots — white for lived weeks, orange for current week, dark grey for future
5. Saves to SharedPreferences so midnight refresh works automatically

### Midnight Refresh
1. On first launch, WorkManager schedules a daily task
2. Task fires at midnight, regenerates the wallpaper bitmap
3. Saves new image to gallery silently in the background

---

## 🚀 Getting Started

### Prerequisites
- Android Studio Hedgehog or newer
- Android SDK API 26+
- Kotlin 2.1.0+

### Installation

1. Clone the repository
```bash
git clone https://github.com/yourusername/LifeCalendarWallpaper.git
```

2. Open the project in Android Studio

3. Let Gradle sync complete

4. Run on your device or emulator

```bash
# Or build APK directly
./gradlew assembleRelease
```

---

## 📦 Project Structure

```
app/
├── src/main/
│   ├── java/com/example/lifecalendarwallpaper/
│   │   ├── MainActivity.kt              # Home screen with stats + previews
│   │   ├── YearCalendarActivity.kt      # Year calendar detail + set wallpaper
│   │   ├── LifeCalendarActivity.kt      # DOB form + life calendar + set wallpaper
│   │   ├── WallpaperGenerator.kt        # Draws year calendar bitmap
│   │   ├── LifeCalendarGenerator.kt     # Draws life calendar bitmap
│   │   ├── WallpaperSetter.kt           # Saves to gallery + opens picker
│   │   └── WallpaperWorker.kt           # WorkManager midnight refresh task
│   └── res/
│       ├── layout/
│       │   ├── activity_main.xml
│       │   ├── activity_year_calendar.xml
│       │   └── activity_life_calendar.xml
│       └── drawable/
│           ├── card_background.xml
│           ├── badge_background.xml
│           ├── input_background.xml
│           └── progress_drawable.xml
```

---

## ⚙️ Permissions

```xml
<uses-permission android:name="android.permission.SET_WALLPAPER" />
<uses-permission android:name="android.permission.SET_WALLPAPER_HINTS" />
<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"
    android:maxSdkVersion="28"/>
```

---

## 📋 Known Limitations

- **Android 13+ lockscreen** — Android 13 and above no longer allows third party apps to set the lockscreen wallpaper programmatically. The app uses Android's native wallpaper picker instead, where users can choose home screen, lockscreen, or both
- **Emulator** — Wallpaper setting may not work on emulators due to system restrictions. Test on a real device for best results

---

## 🗺️ Roadmap

- [ ] Custom color themes
- [ ] Milestone markers on specific dates
- [ ] Goal tracker visible on wallpaper
- [ ] Widget support for home screen
- [ ] Custom font selection
- [ ] Share wallpaper as image

---

## 🤝 Contributing

Pull requests are welcome! For major changes, please open an issue first to discuss what you would like to change.

1. Fork the project
2. Create your feature branch `git checkout -b feature/AmazingFeature`
3. Commit your changes `git commit -m 'Add some AmazingFeature'`
4. Push to the branch `git push origin feature/AmazingFeature`
5. Open a Pull Request

---

## 📄 License

Distributed under the MIT License. See `LICENSE` for more information.

---

## 👤 Author

**Chitroographer**

- GitHub: [@chitroographer](https://github.com/chitroographer)
- LinkedIn: [Your LinkedIn](https://linkedin.com/in/yourprofile)

---

## 🙏 Acknowledgements

- Inspired by [The Life Calendar](https://thelifecalendar.com) concept
- Built with guidance from [Anthropic Claude](https://claude.ai)
- Dot grid design inspired by Tim Urban's [Wait But Why](https://waitbutwhy.com/2014/05/life-weeks.html)

---

> *"Every dot is a day. Every row is a week. The orange one is today — and it's moving."*
