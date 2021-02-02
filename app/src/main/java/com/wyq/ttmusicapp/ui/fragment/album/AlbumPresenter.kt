package com.wyq.ttmusicapp.ui.fragment.album

import com.wyq.ttmusicapp.utils.PlayMusicHelper
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

/**
 * Created by Roman on 2021/2/2
 */
class AlbumPresenter(val view:AlbumContract.View):AlbumContract.Presenter {

    init {
        view.setPresenter(this)
    }
    override fun onClickMenu(musicId: Long) {

    }

    override fun onItemClick(musicId: Long) {

    }

    override fun start() {
        doAsync {
            val albumInfoList = PlayMusicHelper.getGroupByAlbumsInfo()
            uiThread {
                view.initAlbumData(albumInfoList)
            }
        }
    }
}