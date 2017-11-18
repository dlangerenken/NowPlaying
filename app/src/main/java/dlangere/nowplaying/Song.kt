package dlangere.nowplaying

import java.util.*

class Song(val title: String, val author: String, val listenedAt: Date) {

    override fun toString(): String {
        return title
    }
}
