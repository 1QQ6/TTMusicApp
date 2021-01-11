package com.wyq.ttmusicapp.utils

import android.content.Context
import com.wyq.ttmusicapp.receiver.PlayerManagerReceiver

/**
 * Created by Roman on 2021/1/12
 */
class UpdateUIThreadHelper(playerManagerReceiver: PlayerManagerReceiver,context: Context,threadNumber:Int): Thread() {
    private val threadNumber: Int
    private val context: Context
    private val playerManagerReceiver: PlayerManagerReceiver
    private var duration = 0
    private var curPosition = 0

    companion object {
        private val TAG = UpdateUIThreadHelper::class.java.name
    }
    init {
        this.playerManagerReceiver = playerManagerReceiver
        this.context = context
        this.threadNumber = threadNumber
    }

    //此线程只是用于循环发送广播，通知更改歌曲播放进度
    override fun run() {
        super.run()

    }

}