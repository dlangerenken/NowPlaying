package dlangere.nowplaying

import android.content.Intent
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.support.v4.content.LocalBroadcastManager
import android.util.Log
import com.orm.SugarContext
import dlangere.nowplaying.persistence.PlayingNowNotification
import java.util.*

/**
 * Created by dlangere on 11/18/17.
 */
class NotificationListener : NotificationListenerService() {
    companion object {
        val SERVICE_NAME = "dlangere.nowplaying"
        private val NOW_PLAYING_PACKAGE_NAME = "com.google.intelligence.sense"
    }

    override fun onListenerConnected() {
        super.onListenerConnected()
        SugarContext.init(applicationContext)
        Log.i("NotificationListener", "connected")
    }

    override fun onNotificationPosted(notification: StatusBarNotification) {
        val packageName = notification.packageName
        if (NOW_PLAYING_PACKAGE_NAME == packageName) {
            val extras = notification.notification.extras
            val title = extras.getString("android.title")
            Log.i("Title", title)

            val now = Date()
            PlayingNowNotification(title, now.time, null, null).save()
            val intent = Intent(SERVICE_NAME)
            intent.putExtra("title", title)
            intent.putExtra("date", now.time)
            LocalBroadcastManager.getInstance(applicationContext).sendBroadcast(intent)
        }
    }

    override fun onNotificationRemoved(sbn: StatusBarNotification) {
        Log.i("Msg", "Notification Removed")

    }
}