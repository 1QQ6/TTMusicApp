package com.wyq.ttmusicapp.ui.fragment.singer

import com.wyq.ttmusicapp.common.MusicApplication.Companion.context
import com.wyq.ttmusicapp.dao.DatabaseManager
import com.wyq.ttmusicapp.utils.PlayMusicHelper
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

/**
 * Created by Roman on 2021/1/31
 */
class SingerPresenter(val view:SingerContract.View):SingerContract.Presenter {
    private var databaseManager:DatabaseManager? = null
    init {
        view.setPresenter(this)
        databaseManager = DatabaseManager(context!!)
    }

    override fun onClickMenu(musicId: Long) {

    }

    override fun onItemClick(musicId: Long) {

    }

    override fun start() {
        doAsync {
            val singersInfoList = PlayMusicHelper.getGroupBySingersInfo()
            uiThread {
                view.getSingersData(singersInfoList)
            }
        }
    }
}