# Music Player

An Android music player app that fetches and plays audio files from the device's local storage.

## Features

- Lists all music files from device storage using MediaStore
- Paginated song list loaded on a background coroutine
- Play, pause, next, and previous controls
- Supports `.mp3` and `.wav` formats

## Tech Stack

- **Language:** Kotlin
- **Min SDK:** 21
- **Target SDK:** 34
- **Build:** Gradle 8.6, AGP 8.3.1

## Project Structure

| File | Purpose |
|---|---|
| `MainActivity.kt` | Lists songs fetched via MediaStore ContentProvider |
| `PlayerActivity.kt` | Handles playback using Android MediaPlayer |

## Getting Started

1. Open the project in Android Studio
2. Sync Gradle
3. Run on a device or emulator with audio files in external storage

## Permissions

- `READ_EXTERNAL_STORAGE` — to access audio files
- `WRITE_EXTERNAL_STORAGE` — declared but unused currently
