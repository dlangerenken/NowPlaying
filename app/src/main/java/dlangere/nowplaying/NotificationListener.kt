package dlangere.nowplaying

import android.content.Intent
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.support.v4.content.LocalBroadcastManager
import android.util.Log

/**
 * Created by dlangere on 11/18/17.
 */
class NotificationListener : NotificationListenerService() {
    companion object {
        val SERVICE_NAME = "dlangere.nowplaying"
    }

    override fun onListenerConnected() {
        super.onListenerConnected()
        Log.i("NotificationListener", "connected")
    }

    override fun onNotificationPosted(notification: StatusBarNotification) {
        val packageName = notification.packageName
        if ("com.google.intelligence.sense" == packageName) {
            val extras = notification.notification.extras
            val title = extras.getString("android.title")
            Log.i("Title", title)
            val msgrcv = Intent(SERVICE_NAME)
            msgrcv.putExtra("title", title)
            LocalBroadcastManager.getInstance(applicationContext).sendBroadcast(msgrcv)
        }
    }

    override fun onNotificationRemoved(sbn: StatusBarNotification) {
        Log.i("Msg", "Notification Removed")

    }
}