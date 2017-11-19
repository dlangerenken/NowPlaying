package dlangere.nowplaying.songs

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import dlangere.nowplaying.R

import dlangere.nowplaying.songs.SongFragment.SongFragmentInteractionListener
import org.ocpsoft.prettytime.PrettyTime
import java.text.SimpleDateFormat
import java.util.*

/**
 * [RecyclerView.Adapter] that can display a [SongListElement] and makes a call to the
 * specified [SongFragmentInteractionListener].
 */
 class SongRecyclerViewAdapter(private val mValues:List<ListElement>, private val mListener: SongFragmentInteractionListener?):RecyclerView.Adapter<RecyclerView.ViewHolder>() {

 override fun getItemCount(): Int {
  return mValues.size
 }

 override fun getItemViewType(position: Int): Int {
  return mValues[position].getType()
 }

 override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
  val inflater = LayoutInflater.from(parent.context)
  return when (viewType) {
   ListElement.TYPE_HEADER -> {
    val itemView = inflater.inflate(R.layout.fragment_song_header, parent, false)
    HeaderViewHolder(itemView)
   }
   ListElement.TYPE_CONTENT -> {
    val itemView = inflater.inflate(R.layout.fragment_song, parent, false)
    SongViewHolder(itemView)
   }
   else -> throw IllegalStateException("unsupported item type")
  }
 }

 override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
  val element = mValues[position]
  when (element.getType()) {
   ListElement.TYPE_HEADER -> {
    val header = element as DaySectionListElement
    val headerViewHolder = holder as HeaderViewHolder

    headerViewHolder.mDateView.text = SimpleDateFormat("MMMM dd yyyy", Locale.getDefault()).format(Date(header.date))
   }
   ListElement.TYPE_CONTENT -> {
    val song = element as SongListElement
    val songViewHolder = holder as SongViewHolder
    songViewHolder.mTitleView.text = song.title
    songViewHolder.mAuthorView.text = song.author
    songViewHolder.mPlayTimeView.text = PrettyTime(Date()).format(song.listenedAt)
    songViewHolder.itemView.setOnClickListener { mListener?.onSongClicked(song) }
   }
   else -> throw IllegalStateException("unsupported item type")
  }

  holder.itemView.tag = element
 }

 inner class SongViewHolder(mView: View) : RecyclerView.ViewHolder(mView) {
  val mTitleView: TextView = mView.findViewById(R.id.song_title)
  val mAuthorView: TextView = mView.findViewById(R.id.author)
  val mPlayTimeView: TextView = mView.findViewById(R.id.play_time)
 }

 inner class HeaderViewHolder(mView: View) : RecyclerView.ViewHolder(mView) {
  val mDateView: TextView = mView.findViewById(R.id.date)
 }
}
