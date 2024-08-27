package expo.modules.multipleAlarms

import expo.modules.kotlin.modules.Module
import expo.modules.kotlin.modules.ModuleDefinition
import android.content.Context
import android.content.Intent
import android.app.PendingIntent
import android.app.AlarmManager
import android.os.Build
import java.util.*
import android.util.Log
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

import android.app.Activity
import android.os.Bundle
import android.widget.TextView
import android.os.Handler
import 	android.media.MediaPlayer

import android.widget.Button
import java.text.SimpleDateFormat


class HelloWorldActivity : Activity() {
    private lateinit var timeTextView: TextView
    private val handler = Handler()
    private var mediaPlayer: MediaPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alarm)

        timeTextView = findViewById(R.id.timeTextView)
        val messageTextView = findViewById<TextView>(R.id.messageTextView)
        val dismissButton = findViewById<Button>(R.id.dismissButton)

        // Get and set the alarm message
        val message = intent.getStringExtra("ALARM_MESSAGE") ?: "Alarm!"
        messageTextView.text = message

        // Set up dismiss button
        dismissButton.setOnClickListener {
            stopAlarmAudio()
            finish()
        }

        // Start updating the time
        startUpdatingTime()

        // Start playing alarm sound
        playAlarmAudio()
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        intent?.let {
            // Update the activity's UI with the new message
            val message = it.getStringExtra("ALARM_MESSAGE") ?: "Alarm!"
            val messageTextView = findViewById<TextView>(R.id.messageTextView)
            messageTextView.text = message
        }
    }

    private fun startUpdatingTime() {
        handler.post(object : Runnable {
            override fun run() {
                val sdf = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
                val currentTime = sdf.format(Date())
                timeTextView.text = currentTime

                // Update every second
                handler.postDelayed(this, 1000)
            }
        })
    }

    private fun playAlarmAudio() {
        // Initialize MediaPlayer and start playing an alarm sound
        mediaPlayer = MediaPlayer.create(this, R.raw.funkyard)  // Make sure you have an alarm_sound.mp3 in res/raw folder
        mediaPlayer?.isLooping = true
        mediaPlayer?.start()
    }

    private fun stopAlarmAudio() {
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null
    }

    override fun onDestroy() {
        super.onDestroy()
        // Stop the alarm audio when the activity is destroyed
        stopAlarmAudio()

        // Remove callbacks when activity is destroyed to prevent memory leaks
        handler.removeCallbacksAndMessages(null)
    }
}


class MultipleAlarmsModule : Module() {

  override fun definition() = ModuleDefinition {
  
    Name("MultipleAlarms")

       Function("setAlarm") { hour: Int, minutes: Int, message: String, requestCode: Int ->
            setAlarm(hour, minutes, message, requestCode)
        }

        Function("cancelAlarm") { requestCode: Int ->
            cancelAlarm(requestCode)
        }
    }

    private val context get() = requireNotNull(appContext.reactContext)

    private fun setAlarm(hour: Int, minutes: Int, message: String, requestCode: Int) {
   val calendar = Calendar.getInstance().apply {
        set(Calendar.HOUR_OF_DAY, hour)
        set(Calendar.MINUTE, minutes)
        set(Calendar.SECOND, 0)
    }

    Log.d("AlarmsModule", "Setting alarm for $hour:$minutes")

    val alarmIntent = Intent(context, AlarmReceiver::class.java).apply {
        putExtra("ALARM_MESSAGE", message)
        putExtra("NOTIFICATION_ID", requestCode)
        putExtra("ALARM_HOUR", hour)
        putExtra("ALARM_MINUTE", minutes)
    }
    


    val pendingIntent = PendingIntent.getBroadcast(
        context,
        requestCode,
        alarmIntent,
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    )

    val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            pendingIntent
        )
    } else {
        alarmManager.setExact(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            pendingIntent
        )
    }
}


    private fun cancelAlarm(requestCode: Int) {
        val alarmIntent = Intent(context, AlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            requestCode,
            alarmIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.cancel(pendingIntent)
    }
}
class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val message = intent?.getStringExtra("ALARM_MESSAGE") ?: "Alarm!"
        val notificationId = intent?.getIntExtra("NOTIFICATION_ID", 0) ?: 0
        val hour = intent?.getIntExtra("ALARM_HOUR", 0) ?: 0
        val minute = intent?.getIntExtra("ALARM_MINUTE", 0) ?: 0

        context?.let {
            showFullScreenNotification(it, hour, minute, message, notificationId)
        }
    }

    private fun showFullScreenNotification(context: Context, hour: Int, minute: Int, message: String, notificationId: Int) {
        val channelId = "alarm_channel"
        val channelName = "Alarm Notifications"

        val fullScreenIntent = Intent(context, HelloWorldActivity::class.java).apply {
            putExtra("ALARM_MESSAGE", message)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        }
        val fullScreenPendingIntent = PendingIntent.getActivity(context, 0, fullScreenIntent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)

        val notificationBuilder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle("Alarm")
            .setContentText("Time: ${String.format("%02d:%02d", hour, minute)}\n$message")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setCategory(NotificationCompat.CATEGORY_ALARM)
            .setFullScreenIntent(fullScreenPendingIntent, true)
            .setAutoCancel(true)

        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH).apply {
                enableLights(true)
                enableVibration(true)
            }
            notificationManager.createNotificationChannel(channel)
        }

        notificationManager.notify(notificationId, notificationBuilder.build())
    }
}