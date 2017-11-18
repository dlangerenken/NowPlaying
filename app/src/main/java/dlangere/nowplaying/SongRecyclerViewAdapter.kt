package dlangere.nowplaying

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import dlangere.nowplaying.SongFragment.OnListFragmentInteractionListener
import org.ocpsoft.prettytime.PrettyTime
import java.util.*

/**
 * [RecyclerView.Adapter] that can display a [Song] and makes a call to the
 * specified [OnListFragmentInteractionListener].
 */
 class SongRecyclerViewAdapter(private val mValues:List<Song>, private val mListener:OnListFragmentInteractionListener?):RecyclerView.Adapter<SongRecyclerViewAdapter.ViewHolder>() {

 override fun getItemCount(): Int {
  return mValues.size
 }

 override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
  val view = LayoutInflater.from(parent.context)
          .inflate(R.layout.fragment_song, parent, false)
  return ViewHolder(view)
 }

 override fun onBindViewHolder(holder: ViewHolder, position: Int) {
  val song = mValues[position]
  holder.mTitleView.text = song.title
  holder.mAuthorView.text = song.author
  holder.mPlayTimeView.text = PrettyTime(Date()).format(song.listenedAt)

  holder.itemView.tag = song
  holder.itemView.setOnClickListener { mListener?.onSongClicked(song) }
 }

 inner class ViewHolder(mView: View) : RecyclerView.ViewHolder(mView) {
  val mTitleView: TextView = mView.findViewById(R.id.song_title)
  val mAuthorView: TextView = mView.findViewById(R.id.author)
  val mPlayTimeView: TextView = mView.findViewById(R.id.play_time)
 }
}
