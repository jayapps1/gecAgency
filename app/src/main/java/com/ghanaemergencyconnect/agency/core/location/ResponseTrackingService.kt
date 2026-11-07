package com.ghanaemergencyconnect.agency.core.location

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.ServiceInfo
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.ghanaemergencyconnect.agency.MainActivity
import com.ghanaemergencyconnect.agency.R
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private const val CHANNEL_ID = "gec_response_tracking_channel"
private const val NOTIFICATION_ID = 3001

/**
 * Uses the designated Response Lead's device as the temporary live location
 * source for the assigned appliance until dedicated vehicle GPS is available.
 */
@AndroidEntryPoint
class ResponseTrackingService : Service() {

    private val serviceScope = CoroutineScope(Dispatchers.IO + Job())
    private var trackingJob: Job? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val action = intent?.action
        if (action == ACTION_STOP_TRACKING) {
            stopSelf()
            return START_NOT_STICKY
        }

        createNotificationChannel()

        val notificationIntent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            notificationIntent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT,
        )

        val stopIntent = Intent(this, ResponseTrackingService::class.java).apply {
            this.action = ACTION_STOP_TRACKING
        }
        val stopPendingIntent = PendingIntent.getService(
            this,
            1,
            stopIntent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT,
        )

        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Ghana Emergency Connect")
            .setContentText("Response tracking is active.")
            .setSmallIcon(R.drawable.gec_icon_transparent)
            .setContentIntent(pendingIntent)
            .setOngoing(true)
            .addAction(R.drawable.gec_icon_transparent, "Stop Tracking", stopPendingIntent)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            startForeground(
                NOTIFICATION_ID,
                notification,
                ServiceInfo.FOREGROUND_SERVICE_TYPE_LOCATION,
            )
        } else {
            startForeground(NOTIFICATION_ID, notification)
        }

        startTrackingLoop()

        return START_STICKY
    }

    private fun startTrackingLoop() {
        trackingJob?.cancel()
        trackingJob = serviceScope.launch {
            while (true) {
                delay(10000L) // 10-second interval
            }
        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Response Crew Live Tracking",
                NotificationManager.IMPORTANCE_DEFAULT,
            ).apply {
                description = "Persistent indicator active during live response tender tracking"
            }
            val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)
        }
    }

    override fun onDestroy() {
        trackingJob?.cancel()
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? = null

    companion object {
        const val ACTION_START_TRACKING = "com.ghanaemergencyconnect.agency.action.START_RESPONSE_TRACKING"
        const val ACTION_STOP_TRACKING = "com.ghanaemergencyconnect.agency.action.STOP_RESPONSE_TRACKING"
        const val EXTRA_SESSION_ID = "extra_session_id"

        fun startService(context: Context, sessionId: String) {
            val intent = Intent(context, ResponseTrackingService::class.java).apply {
                action = ACTION_START_TRACKING
                putExtra(EXTRA_SESSION_ID, sessionId)
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(intent)
            } else {
                context.startService(intent)
            }
        }

        fun stopService(context: Context) {
            val intent = Intent(context, ResponseTrackingService::class.java)
            context.stopService(intent)
        }
    }
}
