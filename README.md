# ✨ Hoshiya (星夜) - Lofi Anime Focus & Pomodoro Timer

<div align="center">
  <img src="app/src/main/res/drawable/hoshiya_logo.png" width="128" height="128" alt="Hoshiya Logo" />
  <br/>
  <h3>星夜 • Distraction-free Lofi Anime Pomodoro for Android</h3>
  <p>Built with <b>Kotlin</b> & <b>Jetpack Compose</b></p>
</div>

---

## 📖 Overview / Tentang Hoshiya

**Hoshiya (星夜)** adalah aplikasi Pomodoro & focus timer untuk Android yang menggabungkan filosofi desain *distraction-free* dan minimalis seperti **Goodtime**, dengan estetika **Lofi Anime & Celestial Night Sky** yang menenangkan.

Dibuat khusus untuk para wibu, kreator, programmer, dan pelajar yang ingin fokus belajar tanpa distraksi, ditemani suara ambient lofi dan pesan motivasi anime yang wholesome.

---

## 🌟 Fitur Utama (Key Features)

- ⏳ **Minimalist Circular Celestial Timer:** Progress bar melingkar dengan glowing star node dan animasi mulus yang bebas distraksi.
- 🖐️ **Fluid Gesture Controls:**
  - Tap di lingkaran timer untuk **Start / Pause**.
  - Geser (Swipe) kiri / kanan untuk berganti mode (**Focus**, **Short Break**, **Long Break**).
  - Tombol instan **+5 min** untuk menambah waktu fokus tanpa repot.
- 🌌 **Shimmering Starfield Canvas:** Latar belakang partikel bintang berkelip dan nebula kosmik yang menenangkan mata.
- 🎧 **Procedural Lofi Ambient Soundscapes:** Built-in generator audio relaksasi tanpa butuh download file berat:
  - 🌧️ *Lofi Rain on Tatami (雨音)*
  - ✨ *Starry Night Cosmic Drone (星空)*
  - 🍵 *Cozy Japanese Room (和室)*
  - ☕ *Midnight Cafe Ambiance (夜カフェ)*
- 🔔 **Crystal Bell Chime & Gentle Haptics:** Suara lonceng kristal lembut dan getaran haptik saat sesi fokus selesai.
- 🌸 **Anime Companion Quotes:** Kutipan inspiratif berbahasa Jepang + artinya (*"Ganbatte, Senpai!"*, *"Otsukaresama desu!"*).
- 📊 **Productivity Records & Streaks:**
  - Pelacak streak harian (*Starlight Streaks*).
  - Total jam fokus dan pembagian kategori (*Coding, Study, Art, Reading, Writing*).
  - Peringkat fokus anime (*Starlight Novice* hingga *Cosmic SSS-Rank Master*).
- ⚙️ **Fully Customizable:** Kustomisasi durasi fokus, istirahat, siklus pomodoro, auto-start, dan keep-screen-on.
- 🔋 **Distraction-Free Foreground Service:** Notifikasi timer presisi dan hemat baterai.

---

## 🛠️ Tech Stack

- **Language:** Kotlin 2.0+
- **UI Toolkit:** Jetpack Compose (Material 3 + Custom Celestial Theme)
- **Architecture:** Modern Android Architecture (MVVM, StateFlow, Coroutines)
- **Persistence:** Jetpack DataStore Preferences
- **Audio Engine:** Procedural PCM AudioTrack Synthesis
- **Build System:** Gradle Kotlin DSL (`build.gradle.kts`)

---

## 🚀 Cara Build & Menjalankan (Build with Gradle CLI)

Kamu bisa langsung kompilasi dan buat file APK secara mandiri via Terminal / PowerShell (tanpa butuh Android Studio):

### 1. Build APK Debug
```powershell
.\gradlew assembleDebug
```
Hasil file APK akan berada di:
```
app/build/outputs/apk/debug/app-debug.apk
```

### 2. Install langsung ke HP / Emulator (jika terhubung via USB / ADB)
```powershell
.\gradlew installDebug
```

---

## 📜 Lisensi
Lisensi open-source untuk komunitas. Selamat berkonsentrasi di bawah bintang-bintang! ✨
