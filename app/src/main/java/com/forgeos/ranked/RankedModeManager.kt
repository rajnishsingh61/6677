package com.forgeos.ranked

import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Settings
import com.forgeos.data.GameSession
import com.forgeos.data.SessionMode
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Core controller for Ranked / Game Session.
 * Handles:
 * - Starting / stopping session
 * - DND / notification suppression
 * - Allowed apps whitelist
 * - Session timer state
 */
class RankedModeManager(private val context: Context) {

    private val _sessionState = MutableStateFlow<SessionState>(SessionState.Idle)
    val sessionState: StateFlow<SessionState> = _sessionState.asStateFlow()

    // Default allowed packages during Ranked Mode
    private val defaultAllowedPackages = setOf(
        "com.dts.freefireth",          // Free Fire
        "com.dts.freefiremax",         // Free Fire Max
        "com.pubg.imobile",            // BGMI
        "com.tencent.ig",              // PUBG Global (fallback)
        "com.discord",                 // Discord
        "com.spotify.music",           // Spotify
        "com.google.android.youtube",  // YouTube (optional)
        "com.android.chrome",          // Browser
        "com.forgeos"                  // Ourselves
    )

    private var currentAllowed = defaultAllowedPackages.toMutableSet()
    private var sessionStartTime: Long = 0L
    private var currentMode: SessionMode = SessionMode.RANKED

    fun startSession(mode: SessionMode = SessionMode.RANKED, primaryGame: String = "BGMI") {
        if (_sessionState.value is SessionState.Active) return

        currentMode = mode
        sessionStartTime = System.currentTimeMillis()

        // Enable DND if possible
        enableDoNotDisturb()

        _sessionState.value = SessionState.Active(
            startTime = sessionStartTime,
            mode = mode,
            primaryGame = primaryGame,
            allowedPackages = currentAllowed.toSet()
        )
    }

    fun stopSession(): GameSession? {
        val state = _sessionState.value
        if (state !is SessionState.Active) return null

        val endTime = System.currentTimeMillis()
        val durationMin = ((endTime - state.startTime) / 60000).toInt()

        // Calculate XP (simple formula for now)
        val baseXp = when (state.mode) {
            SessionMode.RANKED -> durationMin * 8
            SessionMode.CASUAL -> durationMin * 3
            SessionMode.PRACTICE -> 0
        }
        val bonus = if (durationMin >= 60) 100 else 0
        val xpEarned = baseXp + bonus

        disableDoNotDisturb()

        val session = GameSession(
            startTime = state.startTime,
            endTime = endTime,
            durationMinutes = durationMin,
            xpEarned = xpEarned,
            mode = state.mode,
            primaryGame = state.primaryGame,
            completed = true
        )

        _sessionState.value = SessionState.Idle
        return session
    }

    fun isPackageAllowed(packageName: String): Boolean {
        val state = _sessionState.value
        return if (state is SessionState.Active) {
            state.allowedPackages.contains(packageName)
        } else true
    }

    fun addAllowedPackage(pkg: String) {
        currentAllowed.add(pkg)
    }

    fun removeAllowedPackage(pkg: String) {
        currentAllowed.remove(pkg)
    }

    private fun enableDoNotDisturb() {
        try {
            val nm = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (nm.isNotificationPolicyAccessGranted) {
                    nm.setInterruptionFilter(NotificationManager.INTERRUPTION_FILTER_NONE)
                }
            }
        } catch (e: Exception) {
            // Permission not granted – user must grant Notification Policy access
        }
    }

    private fun disableDoNotDisturb() {
        try {
            val nm = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                nm.setInterruptionFilter(NotificationManager.INTERRUPTION_FILTER_ALL)
            }
        } catch (e: Exception) {
            // ignore
        }
    }

    fun openNotificationPolicySettings() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val intent = Intent(Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
        }
    }

    sealed class SessionState {
        object Idle : SessionState()
        data class Active(
            val startTime: Long,
            val mode: SessionMode,
            val primaryGame: String,
            val allowedPackages: Set<String>
        ) : SessionState()
    }
}
