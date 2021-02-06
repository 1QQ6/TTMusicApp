package com.wyq.ttmusicapp.ui.fragment.localmusic

import android.content.Context
import com.wyq.ttmusicapp.utils.PlayMusicHelper
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

/**
 * Created by Roman on 2021/1/28
 */
class LocalMusicPresenter(val view: LocalMusicContract.View): LocalMusicContract.Presenter {

    init {
        view.setPresenter(this)
    }

    override fun setMusicMode() {

    }

    override fun sharedMusic(musicId: Long) {

    }

    override fun deleteMusic(
        context: Context,
        musicId: Long
    ) {
        doAsync {
            PlayMusicHelper.deleteMusicByID(context,musicId, object :
                LocalMusicContract.Presenter.ILocalDeleteListener {
                override fun onDeleteComplete() {
                    val allMusic = PlayMusicHelper.getAllMusic()
                    uiThread {
                        view.updateListView(allMusic)
                    }
                }
            })
        }
    }


    override fun requestData() {
        doAsync {
            val allMusic = PlayMusicHelper.getAllMusic()
            Thread.sleep(1500)
            uiThread {
                view.updateListView(allMusic)
            }
        }
    }

    override fun updateLoveStatus(musicId: Long) {
        doAsync {
            PlayMusicHelper.setLocalLoveMusic(musicId)
            val allMusic = PlayMusicHelper.getAllMusic()
            uiThread {
                view.updateListView(allMusic)
            }
        }
    }

    override fun start() {
        doAsync {
            val allMusic = PlayMusicHelper.getAllMusic()
            uiThread {
                view.updateListView(allMusic)
            }
        }
    }
}