package com.wyq.ttmusicapp.ui.scanmusic

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.wyq.ttmusicapp.HomeActivity
import com.wyq.ttmusicapp.R
import com.wyq.ttmusicapp.base.BaseActivity
import com.wyq.ttmusicapp.common.Constant
import kotlinx.android.synthetic.main.activity_scan.*


/**
 * Created by Roman on 2021/1/10
 */
class ScanActivity:BaseActivity(), ScanContract.View {
    //是否正在扫描
    private var isScanning = false
    private var musicScanPresenter: ScanContract.Presenter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun getLayout(): Int {
        return R.layout.activity_scan
    }

    override fun initData() {
        ScanPresenter(this)
    }

    override fun initViews() {

        start_scan_btn.setOnClickListener {
            if (!isScanning){
                isScanning = true
                scan_view.start()
                scan_path.visibility = View.VISIBLE
                musicScanPresenter!!.startScanMusic(this,isScanning)
            }else{
                isScanning = false
                scan_path.visibility = View.GONE
                scan_view.stop()
            }
        }
    }
    override fun setupToolbar() {
        setSupportActionBar(scan_music_toolbar)
        supportActionBar?.title = "扫描本地音乐"
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (item.itemId == android.R.id.home) {
            HomeActivity.startActivity(this)
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    /**
     * 扫描完毕，点击关闭当前activity
     */
    override fun scanMusicFinished(type: Int) {
        start_scan_btn.text = getString(R.string.scan_finish)
        isScanning = false
        start_scan_btn.setOnClickListener {
            if (!isScanning){
                finish()
            }
            scan_view.stop()
        }
        if (type == Constant.HAS_MUSIC){
            Toast.makeText(this, getString(R.string.scan_finish), Toast.LENGTH_SHORT).show()
        }else if(type==Constant.HAS_NO_MUSIC){
            Toast.makeText(this, getString(R.string.scan_finish_no_music), Toast.LENGTH_SHORT).show()
        }
    }

    override fun scanMusicFailed() {
        Toast.makeText(this, getString(R.string.scan_error), Toast.LENGTH_LONG).show()
    }

    override fun showScanProgress(path: String, currentProgress: Int) {
        scan_count.text = getString(R.string.scanning_music_count_tv,currentProgress.toString())
        scan_path.text = path
    }

    override fun setPresenter(presenter: ScanContract.Presenter) {
        musicScanPresenter = presenter
    }
}