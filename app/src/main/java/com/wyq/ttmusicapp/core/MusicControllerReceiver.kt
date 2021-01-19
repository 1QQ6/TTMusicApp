package com.wyq.ttmusicapp.core

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Process
import android.os.RemoteException
import com.wyq.ttmusicapp.IMusicControllerService

/**
 * Created by Roman on 2021/1/19
 */
class MusicControllerReceiver(
    private val iBinder: IMusicControllerService,
    private val musicControllerService: MusicControllerService
) : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        when (intent!!.action) {

            //直接退出
            MusicControllerService.PLAY_PRO_EXIT -> {
                musicControllerService.stopSelf()
                //mNoticationManager.cancel(NT_PLAYBAR_ID)
                Process.killProcess(Process.myPid())
            }
            //下一首
            MusicControllerService.NEXT_SONG ->
                try {
                    iBinder.nextSong()
                } catch (e: RemoteException) {
                    e.printStackTrace()
                }
            //上一首
            MusicControllerService.PRE_SONG -> try {
                iBinder.preSong()
            } catch (e: RemoteException) {
                e.printStackTrace()
            }
            //播放或暂停
            MusicControllerService.PLAY_OR_PAUSE ->
                try {
                    if (iBinder.isPlaying) {
                        //暂停
                        iBinder.pause()
                    } else {
                        //播放
                        iBinder.play()
                    }
                } catch (e: RemoteException) {
                    e.printStackTrace()
                }
        }
    }
}