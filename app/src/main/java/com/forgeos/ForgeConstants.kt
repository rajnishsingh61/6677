package com.forgeos

object ForgeConstants {

    const val APP_NAME = "ForgeOS"
    const val PREF_NAME = "forgeos_prefs"

    // Session
    const val MIN_SESSION_MINUTES_FOR_XP = 5
    const val LONG_SESSION_BONUS_MINUTES = 60
    const val LONG_SESSION_BONUS_XP = 100

    // Ranked Mode
    const val RANKED_MODE_CHANNEL_ID = "ranked_mode_channel"
    const val RANKED_MODE_NOTIFICATION_ID = 1001

    // Feature flags (can be remote-configured later)
    object Features {
        const val RANKED_MODE = true
        const val GAME_DOCK = true
        const val XP_SYSTEM = true
        const val PERFORMANCE_MODE = true
        const val BADGES = true
        const val SESSION_STATS = true
    }

    // Primary games
    object Games {
        const val FREE_FIRE = "com.dts.freefireth"
        const val FREE_FIRE_MAX = "com.dts.freefiremax"
        const val BGMI = "com.pubg.imobile"
    }
}
