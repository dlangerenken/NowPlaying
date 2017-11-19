package dlangere.nowplaying

import android.app.Activity
import android.content.*
import android.os.Bundle
import android.provider.Settings
import android.support.v4.content.LocalBroadcastManager
import android.support.v7.app.AlertDialog
import android.text.TextUtils
import java.util.*
import android.support.design.widget.Snackbar
import android.view.View
import dlangere.nowplaying.persistence.PlayingNowNotification
import dlangere.nowplaying.songs.SongListElement
import dlangere.nowplaying.songs.SongFragment
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : SongFragment.SongFragmentInteractionListener, Activity() {
    override fun onSongClicked(item: SongListElement) {
        // TODO OPEN proper view
    }

    override fun convert(item: PlayingNowNotification) : SongListElement {
        val notificationParts = item.title!!.split(" by ")
        val title = notificationParts[0]
        val author = if (notificationParts.size > 1) "- " + notificationParts[1] else ""

        return SongListElement(title, author, Date())
    }

    companion object {
        private val ENABLED_NOTIFICATION_LISTENERS = "enabled_notification_listeners"
        private val ACTION_NOTIFICATION_LISTENER_SETTINGS = "android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // If the user did not turn the notification listener service on we prompt him to do so
        if (!isNotificationServiceEnabled()) {
            val enableNotificationListenerAlertDialog = buildNotificationServiceAlertDialog()
            enableNotificationListenerAlertDialog.show()
        } else {
            LocalBroadcastManager
                    .getInstance(this)
                    .registerReceiver(onNotice, IntentFilter(NotificationListener.SERVICE_NAME))
            showSnackbar(toolbar, "Started listening to incoming sounds.", Snackbar.LENGTH_LONG)
        }

        if (savedInstanceState == null) {
            fragmentManager
                    .beginTransaction()
                    .replace(R.id.frameLayout, SongFragment.newInstance(1))
                    .commit()
        }
    }

    /**
     * Build Notification Listener Alert Dialog.
     * Builds the alert dialog that pops up if the user has not turned
     * the Notification Listener Service on yet.
     * @return An alert dialog which leads to the notification enabling screen
     */
    private fun buildNotificationServiceAlertDialog(): AlertDialog {
        val alertDialogBuilder = AlertDialog.Builder(this)
        alertDialogBuilder.setTitle("Access required")
        alertDialogBuilder.setMessage("Please allow us to use the notification listener")
        alertDialogBuilder.setPositiveButton("Yes"
        ) { _, _ -> startActivity(Intent(ACTION_NOTIFICATION_LISTENER_SETTINGS)) }
        alertDialogBuilder.setNegativeButton("No"
        ) { _, _ ->
            showSnackbar(toolbar, "NotificationService is disabled. Songs are not recognized", Snackbar.LENGTH_INDEFINITE)
        }
        return alertDialogBuilder.create()
    }

    /**
     * Is Notification Service Enabled.
     * Verifies if the notification listener service is enabled.
     * Got it from: https://github.com/kpbird/NotificationListenerService-Example/blob/master/NLSExample/src/main/java/com/kpbird/nlsexample/NLService.java
     * @return True if eanbled, false otherwise.
     */
    private fun isNotificationServiceEnabled(): Boolean {
        val pkgName = packageName
        val flat = Settings.Secure.getString(contentResolver,
                ENABLED_NOTIFICATION_LISTENERS)
        if (!TextUtils.isEmpty(flat)) {
            val names = flat.split(":")
            names.map { ComponentName.unflattenFromString(it) }
                    .filter { it != null && TextUtils.equals(pkgName, it.packageName) }
                    .forEach { return true }
        }
        return false
    }

    private val onNotice = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            // TODO this is not working for bands with " by " in the name ;)
            val message = intent.getStringExtra("title")
            val date = intent.getLongExtra("date", -1)

            getSongFragment()?.songAdded(convert(PlayingNowNotification(message, date, null, null)))
            showSnackbar(toolbar, message, Snackbar.LENGTH_LONG)
        }
    }

    private fun getSongFragment(): SongFragment? {
        return fragmentManager.findFragmentById(R.id.frameLayout) as SongFragment
    }

    fun showSnackbar(view: View, message: String, duration: Int) {
        Snackbar.make(view, message, duration).show()
    }
}
