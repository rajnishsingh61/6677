package com.forgeos.performance

import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Settings
import android.widget.Toast

/**
 * Simple Performance Mode helper.
 * Real performance boosts (CPU/GPU governors, thermal, etc.) need root or special OEM APIs.
 * This version focuses on user-controllable settings: DND, screen timeout, battery optimization hints.
 */
class PerformanceModeManager(private val context: Context) {

    private var isEnabled = false

    fun enable() {
        isEnabled = true
        // In a full implementation:
        // - Request ignore battery optimizations
        // - Set short screen timeout or keep screen on during session
        // - Optionally launch game booster apps if present
        Toast.makeText(context, "Performance Mode ON", Toast.LENGTH_SHORT).show()
    }

    fun disable() {
        isEnabled = false
        Toast.makeText(context, "Performance Mode OFF", Toast.LENGTH_SHORT).show()
    }

    fun isEnabled(): Boolean = isEnabled

    fun openBatteryOptimizationSettings() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            try {
                val intent = Intent(Settings.ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                context.startActivity(intent)
            } catch (e: Exception) {
                // fallback
            }
        }
    }
}
