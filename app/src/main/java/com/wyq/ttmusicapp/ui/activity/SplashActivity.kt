package com.wyq.ttmusicapp.ui.activity

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
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
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.util.*


class SplashActivity : BaseActivity() {
    private val PERMISSION_REQUEST_CODE = 1
    private var musicInfoList: ArrayList<SongInfo> = ArrayList()

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
                doAsync {
                    val allMusic = PlayMusicHelper.getAllMusic()
                    uiThread {
                        musicInfoList = allMusic
                    }
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
            checkSkip()
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
            checkSkip()
        }
    }

    private fun checkSkip() {
        val timer = Timer()
        val task: TimerTask = object : TimerTask() {
            override fun run() {
                startMusicActivity()
            }
        }
        timer.schedule(task, 2000)
    }

    private fun startMusicActivity() {
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
                checkSkip()
            } else {
                Toast.makeText(this, "必须同意所有权限才能使用本程序", Toast.LENGTH_SHORT).show()
                finish()
            }
            else -> {
            }
        }
    }
}