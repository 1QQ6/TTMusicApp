package com.wyq.ttmusicapp

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import com.wyq.ttmusicapp.core.MusicControllerService
import com.wyq.ttmusicapp.core.PlayMusicManager
import com.wyq.ttmusicapp.dao.DatabaseManager
import com.wyq.ttmusicapp.utils.SPUtil

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
        Thread{
            val recentMusicId = SPUtil.getRecentMusicId()
            val allMusicList =
                DatabaseManager.getInstance(context)!!.getAllMusicFromMusicTable()
            //如果本地音乐为0
            if (allMusicList.size==0){

            }
            if (recentMusicId != -1L){
                val songInfo = DatabaseManager.getInstance(context)!!.getSongInfo(recentMusicId)
                val indexOf = allMusicList.indexOf(songInfo)
                PlayMusicManager.getMusicManager()!!.preparePlayingList(indexOf, allMusicList)
            }else{
                PlayMusicManager.getMusicManager()!!.preparePlayingList(0, allMusicList)
            }
        }.start()
    }
    override fun start() {

    }
}