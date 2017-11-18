package dlangere.nowplaying

import java.util.*

/**
 * Created by dlangere on 11/18/17.
 */
class Song(val title: String, val author: String, val listenedAt: Date) {

    override fun toString(): String {
        return title
    }
}
