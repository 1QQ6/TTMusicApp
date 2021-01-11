package com.wyq.ttmusicapp.service

import android.app.Service
import android.content.Intent
import android.content.IntentFilter
import android.os.IBinder
import android.util.Log
import com.wyq.ttmusicapp.receiver.PlayerManagerReceiver

/**
 * Created by Roman on 2021/1/11
 */
class MusicPlayerService:Service() {

    companion object{

        private val TAG = MusicPlayerService::class.java.name
        const val PLAYER_MANAGER_ACTION =
            "com.wyq.ttmusicapp.service.MusicPlayerService.player.action"
    }

    private var mReceiver: PlayerManagerReceiver? = null

    override fun onBind(intent: Intent?): IBinder? {
        throw UnsupportedOperationException("Not yet implemented")
    }

    override fun onCreate() {
        super.onCreate()
        Log.e(TAG, "onCreate: ")
        register()
    }

    private fun register() {
        mReceiver = PlayerManagerReceiver(this)
        var intentFilter = IntentFilter()
        intentFilter.addAction(PLAYER_MANAGER_ACTION)
        registerReceiver(mReceiver,intentFilter)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.e(TAG, "onStartCommand: ")
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onUnbind(intent: Intent?): Boolean {
        return super.onUnbind(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.e(TAG, "onDestroy: ")
        unRegister()
    }

    private fun unRegister() {
        if (mReceiver != null) {
            unregisterReceiver(mReceiver)
        }
    }


}