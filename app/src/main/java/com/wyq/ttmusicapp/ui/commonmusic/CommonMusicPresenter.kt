package com.wyq.ttmusicapp.ui.commonmusic

import com.wyq.ttmusicapp.common.Constant
import com.wyq.ttmusicapp.utils.PlayMusicHelper
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

/**
 * Created by Roman on 2021/2/1
 */
class CommonMusicPresenter(val view:CommonMusicContract.View):CommonMusicContract.Presenter {

    init {
        view.setPresenter(this)
    }

    override fun onClickItem(musicId: Long) {

    }

    override fun loadData(commonInfo:String,type: String) {
        doAsync {
            when(type){

                Constant.MUSIC_FROM_SINGER->{
                    val musicListBySinger = PlayMusicHelper.getMusicListBySinger(commonInfo)
                    uiThread {
                        view.initListView(musicListBySinger)
                    }
                }

                Constant.MUSIC_FROM_ALBUM->{
                    val musicListByAlbum = PlayMusicHelper.getMusicListByAlbum(commonInfo)
                    uiThread {
                        view.initListView(musicListByAlbum)
                    }
                }
                else->{

                }
            }
        }

    }

    override fun start() {

    }
}