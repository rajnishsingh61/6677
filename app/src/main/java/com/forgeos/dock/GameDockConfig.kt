package com.forgeos.dock

/**
 * Configuration for the floating Game Dock.
 * Free Fire + BGMI focused by default.
 */
data class DockApp(
    val packageName: String,
    val label: String,
    val isPrimary: Boolean = false      // Highlight Free Fire / BGMI
)

object GameDockConfig {

    val defaultDockApps = listOf(
        DockApp("com.dts.freefireth", "Free Fire", isPrimary = true),
        DockApp("com.dts.freefiremax", "FF Max", isPrimary = true),
        DockApp("com.pubg.imobile", "BGMI", isPrimary = true),
        DockApp("com.discord", "Discord"),
        DockApp("com.spotify.music", "Spotify"),
        DockApp("com.google.android.youtube", "YouTube"),
    )

    // Position & behavior
    const val DOCK_HEIGHT_DP = 72
    const val DOCK_CORNER_RADIUS = 20f
    const val DOCK_ALPHA = 0.92f
    const val ANIMATION_DURATION_MS = 220L
}
