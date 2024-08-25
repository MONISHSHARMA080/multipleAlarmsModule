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

        Log.d("AlarmsModule", "Alarm triggered: $message -- $notificationId")

        val hourMinute = String.format("%02d:%02d", hour, minute)

        context?.let {
        Log.d("AlarmsModule", "Alarm context --> $context ")
            showNotification(it, hourMinute, message, notificationId)
        }
    }

    private fun showNotification(context: Context, hourMinute: String, message: String, notificationId: Int) {
        val channelId = "alarm_channel"
        val channelName = "Alarm Notifications"

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH)
            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }

        val builder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle("Alarm")
            .setContentText("Time: $hourMinute\n$message")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)

        with(NotificationManagerCompat.from(context)) {
            notify(notificationId, builder.build())
        }
    }
}