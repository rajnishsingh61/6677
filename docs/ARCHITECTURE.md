# ForgeOS Architecture

## High Level

ForgeOS is designed as a **launcher + overlay system**.

Recommended base: **Lawnchair** (or AOSP Launcher3) because:
- Already battle-tested as a full home replacement
- Good support for shortcuts, widgets, deep customization
- Same base used by PW LearnOS

We add our own modules on top instead of rewriting the entire launcher from scratch.

## Module Responsibilities

### ranked/
- RankedModeManager → start/stop session, DND, whitelist
- RankedModeService → Foreground service while session is active
- AppInterceptor (future) → block non-allowed apps when possible

### dock/
- Floating Game Dock (Compose or custom View)
- Quick launch for Free Fire / BGMI / Discord / Spotify
- Can be shown/hidden with gesture

### xp/
- XpManager → award XP, calculate rank
- Badge unlocking logic
- RankProgress helper

### performance/
- PerformanceModeManager → toggle high performance settings
- Optional FPS overlay (requires special permissions / accessibility)

### stats/
- Room database for sessions + profile
- Streak calculation
- Weekly summary

### data/
- Entities: PlayerProfile, GameSession, Badge
- DAOs + Repository

### ui/theme/
- Neon / AMOLED color tokens
- Typography
- Rank color mapping

## Data Flow (Ranked Session)

1. User taps "Start Ranked"
2. RankedModeManager.startSession()
3. DND enabled (if permission granted)
4. Foreground service started
5. Game Dock becomes primary UI
6. On stop → XP calculated → profile updated → badges checked → stats saved

## Permissions Required

- Notification Policy Access (for DND)
- QUERY_ALL_PACKAGES (or package visibility)
- FOREGROUND_SERVICE
- SYSTEM_ALERT_WINDOW (for floating dock)
- PACKAGE_USAGE_STATS (optional, for better stats)

## Next Implementation Steps

1. Integrate RankedModeManager into a real Service
2. Build floating Dock with Compose
3. Room database setup
4. Simple home screen replacement or overlay on top of existing launcher for MVP
5. Theme engine
