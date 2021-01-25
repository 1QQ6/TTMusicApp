package com.wyq.ttmusicapp.base

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.wyq.ttmusicapp.common.Constant
import com.wyq.ttmusicapp.utils.SPUtil

/**
 * @author Roman
 * @des
 * @version $
 * @updateAuthor $
 * @updateDes
 */
 abstract class BaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       if (getLayout() != 0) {
          setContentView(getLayout())
       }
       initData()
       initViews()
       setupToolbar()
       registerBroadcast()
    }

   private fun registerBroadcast() {
      val intentFilter = IntentFilter()
      intentFilter.addAction(Constant.PLAY_BAR_UPDATE)
      registerReceiver(receiver, intentFilter)
   }

   /**
    * 解决在另一个进程无法保存sp问题
    */
   private val receiver: BroadcastReceiver = object : BroadcastReceiver() {
      override fun onReceive(context: Context, intent: Intent) {
         when (intent.action) {
            Constant.PLAY_BAR_UPDATE -> {
               val musicId = intent.getLongExtra(Constant.NOW_PLAY_MUSIC_ID,0)
               SPUtil.saveRecentMusicId(musicId)
               val recentMusicId = SPUtil.getRecentMusicId()
                Log.e("recentMusicId", recentMusicId.toString())
            }
         }
      }
   }

   override fun onDestroy() {
      super.onDestroy()
      unregisterReceiver(receiver)
   }
    /**
     * 获取布局文件ID
     *
     * @return
     */
    abstract fun getLayout(): Int

    /**
     * 初始化数据
     */
    abstract fun initData()

    /**
     * 初始化view
     */
    abstract fun initViews()

    /**
     * 设置toolbar
     */
    abstract fun setupToolbar()
}