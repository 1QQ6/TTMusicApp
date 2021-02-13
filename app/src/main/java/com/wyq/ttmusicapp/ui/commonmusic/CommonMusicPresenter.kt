package com.wyq.ttmusicapp.ui.commonmusic

import com.wyq.ttmusicapp.common.Constant
import com.wyq.ttmusicapp.entity.Artist
import com.wyq.ttmusicapp.entity.SongInfo
import com.wyq.ttmusicapp.musicapi.NetEaseApiServiceImpl
import com.wyq.ttmusicapp.net.APIManager
import com.wyq.ttmusicapp.net.RequestCallBack
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

    override fun loadNetEasyData(artistId: String) {
        val observable = NetEaseApiServiceImpl.getArtistSongs(artistId, 0, 10)
        APIManager.getInstance()?.request(observable, object : RequestCallBack<Artist> {
            override fun success(result: Artist) {
                val musicLists = result.songs
                val iterator = musicLists.iterator()
                /*while (iterator.hasNext()) {
                    val temp = iterator.next()
                    //if (temp.isCp) {
                        //list.remove(temp);// 出现java.util.ConcurrentModificationException
                        iterator.remove()// 推荐使用
                    //}
                }*/
                view.initNetListView(result)
            }

            override fun error(msg: String?) {
            }
        })
    }

    override fun start() {

    }
}