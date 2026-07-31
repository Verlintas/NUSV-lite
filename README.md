# NUSV LITE

Official Android client for NUSV, putting the entire NUSV ecosystem in your pocket.

## Features

- Browse the latest content across NUSV sites, organized by category for quick access
- Built-in tools & games: classic games, calculators & converters, dev tools, generative creative tools and more
- Daily check-in & points system; points unlock color themes in the theme shop
- **Orca premium theme**: exclusive theme unlocked through long-term check-ins
  - Pure black background + pure white UI, transparent cards with white borders
  - Double points (×2) from all games and check-ins
  - Unlock exclusive hidden tools visible only to Orca users
- 中文 / English language switching
- Dark / light themes
- Layout switching (list / two-column grid / three-column grid)
- Search result highlighting, haptic feedback, remote sync
- Earn points from games & per-game high-score tracking

## Tech Stack

- Kotlin + Jetpack Compose
- Material 3 design language
- Room local database
- Inspired by Arc / Linear / Vercel

## Build

```bash
# Requires JDK 17
./gradlew assembleDebug
# Output: app/build/outputs/apk/debug/app-debug.apk
```

Release APKs live in the `release/` directory.

## Documentation

- [CHANGELOG](CHANGELOG.md) (complete version history from v1.1.0)
- Mirrored in the in-app "Docs" page
