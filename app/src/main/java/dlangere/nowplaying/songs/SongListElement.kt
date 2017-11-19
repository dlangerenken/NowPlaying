package dlangere.nowplaying.songs

import java.util.*

class SongListElement(val title: String, val author: String, val listenedAt: Date) : ListElement() {
    override fun getType(): Int {
        return ListElement.TYPE_CONTENT
    }

    override fun toString(): String {
        return title
    }
}
