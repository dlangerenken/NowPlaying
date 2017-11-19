package dlangere.nowplaying.songs

import android.content.Context
import android.os.Bundle
import android.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.orm.SugarRecord
import dlangere.nowplaying.R
import dlangere.nowplaying.persistence.PlayingNowNotification
import java.util.*

/**
 * Activities containing this fragment MUST implement the [SongFragmentInteractionListener]
 * interface.
 */
class SongFragment : Fragment() {
    private var mColumnCount = 1
    private var mListener: SongFragmentInteractionListener? = null
    private var mListElements: MutableList<ListElement> = ArrayList()
    private var songRecyclerAdapter: SongRecyclerViewAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            mColumnCount = arguments.getInt(ARG_COLUMN_COUNT)
        }

        val songMap = toMap(SugarRecord.listAll(PlayingNowNotification::class.java).map { notification ->
            mListener!!.convert(notification)
        })

        for (date in songMap.keys) {
            val header = DaySectionListElement(date)
            mListElements.add(header)
            mListElements.addAll(songMap[date]!!.sortedBy { song -> song.listenedAt }.reversed())
        }
    }

    private fun toMap(songs: List<SongListElement>): Map<Long, List<SongListElement>> {
        val map = TreeMap<Long, MutableList<SongListElement>>()
        val cal = Calendar.getInstance()
        for (song in songs) {
            cal.time = song.listenedAt
            cal.set(Calendar.HOUR_OF_DAY, 0)
            cal.set(Calendar.MINUTE, 0)
            cal.set(Calendar.SECOND, 0)
            cal.set(Calendar.MILLISECOND, 0)
            val filteredTime = cal.timeInMillis

            var value: MutableList<SongListElement>? = map[filteredTime]
            if (value == null) {
                value = ArrayList()
                map.put(filteredTime, value)
            }
            value.add(song)
        }
        return map
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_song_list, container, false)
        if (view is RecyclerView) {
            val context = view.getContext()
            if (mColumnCount <= 1) {
                view.layoutManager = LinearLayoutManager(context)
            } else {
                view.layoutManager = GridLayoutManager(context, mColumnCount)
            }
            songRecyclerAdapter = SongRecyclerViewAdapter(mListElements, mListener)
            view.adapter = songRecyclerAdapter
        }
        return view
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is SongFragmentInteractionListener) {
            mListener = context
        } else {
            throw RuntimeException(context.toString() + " must implement SongFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        mListener = null
    }

    interface SongFragmentInteractionListener {
        fun onSongClicked(item: SongListElement)
        fun convert(item: PlayingNowNotification): SongListElement
    }

    companion object {
        private val ARG_COLUMN_COUNT = "column-count"

        fun newInstance(columnCount: Int): SongFragment {
            val fragment = SongFragment()
            val args = Bundle()
            args.putInt(ARG_COLUMN_COUNT, columnCount)
            fragment.arguments = args
            return fragment
        }
    }

    fun songAdded(songListElement: SongListElement) {
        mListElements.add(1, songListElement)
        songRecyclerAdapter?.notifyItemInserted(1)
    }
}
