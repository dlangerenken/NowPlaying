package dlangere.nowplaying

import android.app.Application
import com.orm.SugarContext

class NowPlayingApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        SugarContext.init(this)
    }

    override fun onTerminate() {
        super.onTerminate()
        SugarContext.terminate()
    }
}