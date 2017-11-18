package dlangere.nowplaying

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
import dlangere.nowplaying.persistence.PlayingNowNotification
import java.util.*

/**
 * Activities containing this fragment MUST implement the [SongFragmentInteractionListener]
 * interface.
 */
class SongFragment : Fragment() {
    // TODO: Customize parameters
    private var mColumnCount = 1
    private var mListener: SongFragmentInteractionListener? = null
    private var mSongs: MutableList<Song> = ArrayList()
    private var songRecyclerAdapter : SongRecyclerViewAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            mColumnCount = arguments.getInt(ARG_COLUMN_COUNT)
        }
        mSongs.addAll(SugarRecord.listAll(PlayingNowNotification::class.java).map { notification ->
            mListener!!.convert(notification)
        })
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
            songRecyclerAdapter = SongRecyclerViewAdapter(mSongs, mListener)
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
        fun onSongClicked(item: Song)
        fun convert(item: PlayingNowNotification) : Song
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

    fun songAdded(song: Song) {
        mSongs.add(song)
        songRecyclerAdapter?.notifyItemInserted(mSongs.size - 1)
    }
}
