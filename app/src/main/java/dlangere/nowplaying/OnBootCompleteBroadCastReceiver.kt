package dlangere.nowplaying

import android.content.Intent
import android.content.BroadcastReceiver
import android.content.Context


/**
 * Created by dlangere on 11/18/17.
 */
class OnBootCompleteBroadCastReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action!!.equals(Intent.ACTION_BOOT_COMPLETED, ignoreCase = true)) {
            val serviceIntent = Intent(context, NotificationListener::class.java)
            context.startService(serviceIntent)
        }
    }
}