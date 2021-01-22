package com.wyq.ttmusicapp

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import com.wyq.ttmusicapp.adpter.LocalFragmentAdapter
import com.wyq.ttmusicapp.base.BaseFragment
import com.wyq.ttmusicapp.ui.fragment.AlbumFragment
import com.wyq.ttmusicapp.ui.fragment.FolderFragment
import com.wyq.ttmusicapp.ui.fragment.SingerFragment
import com.wyq.ttmusicapp.ui.localmusic.LocalMusicFragment
import kotlinx.android.synthetic.main.fragment_home.*

/**
 * Created by Roman on 2021/1/20
 */
class HomeFragment:BaseFragment() {
    private var localMusicFragment: LocalMusicFragment? = null
    private var fragmentList = ArrayList<Fragment>()
    private var singerFragment: SingerFragment? = null
    private var albumFragment: AlbumFragment? = null
    private var folderFragment: FolderFragment? = null
    private val mTitlesArrays =
        arrayOf("单曲", "歌手", "专辑", "文件")
    private var fragmentAdapter: LocalFragmentAdapter?=null

    override fun getLayout(): Int {
        return R.layout.fragment_home
    }

    override fun initData() {
       initFragment()
        fragmentAdapter = LocalFragmentAdapter(
            activity!!.supportFragmentManager,
            FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT,
            mTitlesArrays, fragmentList
        )
    }

    override fun initViews() {
        initFragmentList()
    }
    private fun initFragmentList() {
        local_viewPager.adapter = fragmentAdapter
        local_viewPager.offscreenPageLimit = 2
        local_tab.setViewPager(local_viewPager,mTitlesArrays)
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
}