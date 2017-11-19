package dlangere.nowplaying.songs

abstract class ListElement {
    companion object {
        val TYPE_HEADER = 0
        val TYPE_CONTENT = 1
    }

    abstract fun getType(): Int
}