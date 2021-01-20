package com.wyq.ttmusicapp

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import com.wyq.ttmusicapp.core.MusicControllerService
import com.wyq.ttmusicapp.core.PlayMusicManager
import com.wyq.ttmusicapp.dao.DatabaseManager
import com.wyq.ttmusicapp.entity.SongInfo
import com.wyq.ttmusicapp.utils.PlayMusicDBHelper
import com.wyq.ttmusicapp.utils.PlayMusicSPUtil
import java.util.ArrayList

/**
 * Created by Roman on 2021/1/19
 */
class HomePresenter(val context: Context,view: HomeContract.View):HomeContract.Presenter {
    private var isBinding = false
    init {
        view.setPresenter(this)
    }
    override fun bindMusicController() {
        if (!isBinding){
            val connection = object :ServiceConnection{

                override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
                    isBinding = true
                    PlayMusicManager.getMusicManager()!!.bindService(IMusicControllerService.Stub.asInterface(service))
                    loadMusicPlayAgo()
                }

                override fun onServiceDisconnected(name: ComponentName?) {
                    isBinding = true
                    PlayMusicManager.musicManagerInstance!!.bindService(null)
                }
            }
            val intent = Intent(context,MusicControllerService::class.java)
            context.bindService(intent,connection,Context.BIND_AUTO_CREATE)
        }
    }

    override fun unbindMusicController() {
        if (isBinding){
            val intent = Intent(context, MusicControllerService::class.java)
            context.stopService(intent)
        }
    }

    override fun loadMusicPlayAgo() {
        val recentMusicId = PlayMusicSPUtil.getRecentMusicId()
        if (recentMusicId != -1){
            val songInfo = DatabaseManager.getInstance(context)!!.getSongInfo(recentMusicId)
            val allMusicList =
                DatabaseManager.getInstance(context)!!.getAllMusicFromMusicTable()
            val indexOf = allMusicList.indexOf(songInfo)
            PlayMusicManager.getMusicManager()!!.preparePlayingList(indexOf, allMusicList)
        }

    }
    override fun start() {

    }
}