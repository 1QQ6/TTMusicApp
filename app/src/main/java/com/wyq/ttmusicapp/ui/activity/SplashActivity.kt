package com.wyq.ttmusicapp.ui.activity

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.wyq.ttmusicapp.HomeActivity
import com.wyq.ttmusicapp.R
import com.wyq.ttmusicapp.base.BaseActivity
import com.wyq.ttmusicapp.core.PlayMusicManager
import com.wyq.ttmusicapp.ui.scanmusic.MusicScanHelper
import com.wyq.ttmusicapp.ui.scanmusic.OnScanMusicFinishListener
import com.wyq.ttmusicapp.utils.PlayMusicHelper
import java.util.*

class SplashActivity : BaseActivity() {
    private val PERMISSION_REQUEST_CODE = 1


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initPermission()
    }

    override fun getLayout(): Int {
       return R.layout.activity_splash
    }

    override fun initData() {
    }

    private fun initMusicData(){
        MusicScanHelper.startScanLocalMusic(this,true,object :OnScanMusicFinishListener{
            override fun scanMusicError() {

            }

            override fun scanMusicSuccess(type: Int) {

            }

            override fun scanMusicUpdate(path: String, currentProgress: Int) {

            }
        })
    }

    override fun initViews() {
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
        timer.schedule(task, 3000)
    }

    private fun startMusicActivity() {
        val intent = Intent()
        intent.setClass(this, HomeActivity::class.java)
        startActivity(intent)
        finish()
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