package com.forgeos.ranked

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.forgeos.ForgeConstants
import com.forgeos.data.SessionMode

/**
 * Foreground service that keeps Ranked Mode alive.
 * Shows a persistent notification while a session is running.
 */
class RankedModeService : Service() {

    companion object {
        const val ACTION_START = "com.forgeos.action.START_RANKED"
        const val ACTION_STOP = "com.forgeos.action.STOP_RANKED"
        const val EXTRA_MODE = "mode"
        const val EXTRA_GAME = "game"

        fun start(context: Context, mode: SessionMode = SessionMode.RANKED, game: String = "BGMI") {
            val intent = Intent(context, RankedModeService::class.java).apply {
                action = ACTION_START
                putExtra(EXTRA_MODE, mode.name)
                putExtra(EXTRA_GAME, game)
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(intent)
            } else {
                context.startService(intent)
            }
        }

        fun stop(context: Context) {
            val intent = Intent(context, RankedModeService::class.java).apply {
                action = ACTION_STOP
            }
            context.startService(intent)
        }
    }

    private lateinit var rankedManager: RankedModeManager

    override fun onCreate() {
        super.onCreate()
        rankedManager = RankedModeManager(this)
        createNotificationChannel()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            ACTION_START -> {
                val modeName = intent.getStringExtra(EXTRA_MODE) ?: SessionMode.RANKED.name
                val game = intent.getStringExtra(EXTRA_GAME) ?: "BGMI"
                val mode = SessionMode.valueOf(modeName)

                rankedManager.startSession(mode, game)
                startForeground(ForgeConstants.RANKED_MODE_NOTIFICATION_ID, buildNotification(mode, game))
            }
            ACTION_STOP -> {
                rankedManager.stopSession()
                stopForeground(STOP_FOREGROUND_REMOVE)
                stopSelf()
            }
        }
        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? = null

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                ForgeConstants.RANKED_MODE_CHANNEL_ID,
                "Ranked Mode",
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = "Shows while a Ranked / Game session is active"
                setShowBadge(false)
            }
            val nm = getSystemService(NotificationManager::class.java)
            nm.createNotificationChannel(channel)
        }
    }

    private fun buildNotification(mode: SessionMode, game: String): Notification {
        val stopIntent = Intent(this, RankedModeService::class.java).apply {
            action = ACTION_STOP
        }
        val stopPending = PendingIntent.getService(
            this, 0, stopIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        return NotificationCompat.Builder(this, ForgeConstants.RANKED_MODE_CHANNEL_ID)
            .setContentTitle("ForgeOS • ${mode.name}")
            .setContentText("$game session running • Tap to stop")
            .setSmallIcon(android.R.drawable.ic_media_play)
            .setOngoing(true)
            .setOnlyAlertOnce(true)
            .addAction(android.R.drawable.ic_delete, "Stop Session", stopPending)
            .build()
    }
}
