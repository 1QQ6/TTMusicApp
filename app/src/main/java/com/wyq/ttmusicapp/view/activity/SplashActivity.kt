package com.wyq.ttmusicapp.view.activity

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
import java.util.*

class SplashActivity : BaseActivity() {
    private val PERMISSON_REQUESTCODE = 1


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initPermission()
    }

    override fun getLayout(): Int {
       return R.layout.activity_splash
    }

    override fun initData() {
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
        if (ContextCompat.checkSelfPermission(
                        this,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                ) !== PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    PERMISSON_REQUESTCODE
            )
        } else {
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
            PERMISSON_REQUESTCODE -> if (grantResults[0] == PackageManager.PERMISSION_GRANTED
            ) {
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