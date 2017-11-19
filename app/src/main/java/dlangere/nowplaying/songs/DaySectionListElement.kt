package dlangere.nowplaying.songs

/**
 * Created by dlangere on 11/18/17.
 */
class DaySectionListElement(val date: Long) : ListElement() {
    override fun getType(): Int {
        return TYPE_HEADER
    }
}