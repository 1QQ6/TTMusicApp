package com.wyq.ttmusicapp.ui.localmusic

import android.content.Intent
import android.view.Menu
import android.view.MenuItem
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import com.google.android.material.tabs.TabLayout
import com.wyq.ttmusicapp.R
import com.wyq.ttmusicapp.adpter.LocalFragmentAdapter
import com.wyq.ttmusicapp.ui.activity.ScanActivity
import com.wyq.ttmusicapp.ui.fragment.AlbumFragment
import com.wyq.ttmusicapp.ui.fragment.FolderFragment
import com.wyq.ttmusicapp.ui.fragment.SingerFragment
import com.wyq.ttmusicapp.ui.playmusicbar.PlayBarBaseActivity
import kotlinx.android.synthetic.main.acticity_local_music.*

/**
 * Created by Roman on 2021/1/10
 */
class LocalMusicActivity : PlayBarBaseActivity(){

    private var tabList = ArrayList<String>()
    private var fragmentList = ArrayList<Fragment>()

    private var localMusicFragment: LocalMusicFragment? = null
    private var singerFragment: SingerFragment? = null
    private var albumFragment: AlbumFragment? = null
    private var folderFragment: FolderFragment? = null

    private var fragmentAdapter:LocalFragmentAdapter?=null

    override fun getLayout(): Int {
        return R.layout.acticity_local_music
    }

    override fun initData() {

        setTabTitle()
        initFragment()

        fragmentAdapter = LocalFragmentAdapter(
            supportFragmentManager,
            FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT,
            tabList, fragmentList
        )

    }

    private fun setTabTitle() {
        tabList.add("单曲")
        tabList.add("歌手")
        tabList.add("专辑")
        tabList.add("文件")
    }

    private fun initFragment() {
        if (localMusicFragment == null) {
            localMusicFragment = LocalMusicFragment()
            fragmentList.add(localMusicFragment!!)
        }
        if (singerFragment == null) {
            singerFragment = SingerFragment()
            fragmentList.add(singerFragment!!)
        }
        if (albumFragment == null) {
            albumFragment = AlbumFragment()
            fragmentList.add(albumFragment!!)
        }
        if (folderFragment == null) {
            folderFragment = FolderFragment()
            fragmentList.add(folderFragment!!)
        }
    }

    override fun initViews() {
        super.initViews()
        local_viewPager.adapter = fragmentAdapter
        local_viewPager.offscreenPageLimit = 2

        local_tab.tabMode = TabLayout.MODE_FIXED
        local_tab.tabGravity = TabLayout.GRAVITY_FILL
        local_tab.setupWithViewPager(local_viewPager)
    }

    override fun setupToolbar() {
        setSupportActionBar(local_music_toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = getString(R.string.local_music)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.local_music_menu,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.scan_local_menu) {
            val intent = Intent(this@LocalMusicActivity, ScanActivity::class.java)
            startActivity(intent)
        } else if (item.itemId == android.R.id.home) {
            finish()
        }
        return true
    }
}