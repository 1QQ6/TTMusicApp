package com.wyq.ttmusicapp.ui.activity

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.apkfuns.logutils.LogUtils
import com.wyq.ttmusicapp.base.BaseActivity
import com.wyq.ttmusicapp.entity.SongInfo
import com.wyq.ttmusicapp.home.HomeActivity
import com.wyq.ttmusicapp.login.MusicLoginActivity
import com.wyq.ttmusicapp.ui.scanmusic.MusicScanHelper
import com.wyq.ttmusicapp.ui.scanmusic.OnScanMusicFinishListener
import com.wyq.ttmusicapp.utils.DisplayUtil
import com.wyq.ttmusicapp.utils.PlayMusicHelper
import com.wyq.ttmusicapp.utils.SPUtil
import kotlinx.coroutines.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.lang.ref.WeakReference
import java.util.*


class SplashActivity : BaseActivity() {
    private val PERMISSION_REQUEST_CODE = 1
    private var musicInfoList: ArrayList<SongInfo> = ArrayList()
    private var mainScope = MainScope()
    private var isLoadFinished = false
    // 静态常量
    private  val splashHandler: SplashHandler = SplashHandler(this)

    companion object {
        const val START_HOME_ACTIVITY = 1001

        class SplashHandler(val activity: SplashActivity) : Handler() {
            private var splashActivity: WeakReference<SplashActivity> = WeakReference(activity)
            override fun handleMessage(msg: Message) {
                super.handleMessage(msg)
                when(msg.what){
                    START_HOME_ACTIVITY->{
                        splashActivity.get()?.startMusicActivity()
                    }
                    else->{

                    }
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initPermission()
    }

    override fun getLayout(): Int {
       return 0
    }

    override fun initData() {
    }

    private fun initMusicData(){
        MusicScanHelper.startScanLocalMusic(this,true,object :OnScanMusicFinishListener{
            override fun scanMusicError() {

            }

            override fun scanMusicSuccess(type: Int) {
                mainScope.launch {
                    val allMusic = async(Dispatchers.IO) {
                        PlayMusicHelper.getAllMusic()
                    }
                    musicInfoList.clear()
                    musicInfoList.addAll(allMusic.await())
                    checkSkip()
                }
            }

            override fun scanMusicUpdate(path: String, currentProgress: Int) {
            }
        })
    }

    override fun initViews() {
       DisplayUtil.openFullScreenModel(this)
       DisplayUtil.showStatusBar(this,false)
    }

    override fun setupToolbar() {
    }

    private fun initPermission() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            initMusicData()
            return
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                PERMISSION_REQUEST_CODE
            )
        } else {
            initMusicData()
        }
    }

    private fun checkSkip() {
            val obtain = Message.obtain()
            obtain.what = START_HOME_ACTIVITY
            splashHandler.sendMessageDelayed(obtain,2000)
    }

     fun startMusicActivity() {
        if (SPUtil.isLogin()){
            HomeActivity.startActivity(this@SplashActivity,musicInfoList)
            finish()
        }else{
            MusicLoginActivity.startActivity(this)
            finish()
        }
    }

    override fun onRequestPermissionsResult(
            requestCode: Int,
            permissions: Array<out String>,
            grantResults: IntArray
    ) {
        when (requestCode) {
            PERMISSION_REQUEST_CODE -> if (grantResults[0] == PackageManager.PERMISSION_GRANTED
            ) {
                initMusicData()
            } else {
                Toast.makeText(this, "必须同意所有权限才能使用本程序", Toast.LENGTH_SHORT).show()
                finish()
            }
            else -> {
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mainScope.cancel()
    }
}