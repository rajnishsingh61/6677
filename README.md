<<<<<<< HEAD
# ForgeOS

**Phone ko gaming device bana do.**

Free Fire + BGMI focused gaming launcher foundation.

## What this is

A modular foundation for a gaming launcher (not a complete home-screen replacement yet).

Includes:
- Rank system (Iron → Radiant)
- Ranked Mode manager + Foreground Service
- XP & Badge system
- Game Dock configuration (Free Fire / BGMI primary)
- Room database (Profile, Sessions, Badges)
- Repository layer
- Performance Mode helper
- Neon gaming color tokens
- AndroidManifest with required permissions

## Build APK with GitHub Actions (recommended)

1. Create a **new empty repository** on GitHub  
   (do **not** initialize with README / .gitignore / license).

2. Push this project:

```bash
git init
git add .
git commit -m "ForgeOS foundation + CI"
git branch -M main
git remote add origin https://github.com/YOUR_USERNAME/ForgeOS.git
git push -u origin main
```

3. Open the **Actions** tab of your repo.  
   The workflow **Build APK** will start automatically.  
   When it finishes, download the artifact named **ForgeOS-debug** (the APK is inside).

You can also run it manually: Actions → Build APK → Run workflow.

## Local build (optional)

```bash
chmod +x gradlew
./gradlew assembleDebug
# APK location: app/build/outputs/apk/debug/app-debug.apk
```

## Project Structure

```
com.forgeos
├── data/           # Models, Room DB, DAOs, Repository
├── ranked/         # RankedModeManager + RankedModeService
├── xp/             # XpManager + rank progression
├── dock/           # GameDockConfig
├── performance/    # PerformanceModeManager
├── stats/          # (ready for expansion)
└── ui/theme/       # ForgeColors
```

## Important Reality Check

To turn this into a real launcher that replaces the home screen you still need:

1. A base launcher (recommended: Lawnchair or AOSP Launcher3)
2. Integration of these modules into that base
3. Proper launcher Activity with HOME + DEFAULT categories
4. Full UI (Compose screens for Ranked Mode, Dock, Profile, Stats)
5. Signing & testing on real devices

This source gives you the **core gaming logic** ready to plug in.

## Rank Ladder

Iron → Bronze → Silver → Gold → Platinum → Diamond → Master → Grandmaster → Radiant

## License

Use freely for personal / educational projects.
=======
# Launcher
>>>>>>> 067026b4ff6959465a65ad9d6655e6d1c29f7912
