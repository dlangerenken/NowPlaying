package dlangere.nowplaying.persistence

import com.orm.SugarRecord

/**
 * Created by dlangere on 11/18/17.
 */
class PlayingNowNotification : SugarRecord {

    internal var title: String? = null
    internal var date: Long = -1
    internal var longitude: Long? = null
    internal var latitude: Long? = null

    // Default constructor is necessary for SugarRecord
    constructor()

    constructor(title: String, date: Long, longitude: Long?, latitude: Long?) {
        this.title = title
        this.date = date
        this.longitude = longitude
        this.latitude = latitude
    }
}