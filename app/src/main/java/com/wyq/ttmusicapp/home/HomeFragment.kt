package com.wyq.ttmusicapp.home

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import com.wyq.ttmusicapp.R
import com.wyq.ttmusicapp.adapter.LocalRVAdapter
import com.wyq.ttmusicapp.base.BaseFragment
import com.wyq.ttmusicapp.entity.SongInfo
import com.wyq.ttmusicapp.ui.fragment.album.AlbumFragment
import com.wyq.ttmusicapp.ui.fragment.localmusic.LocalMusicFragment
import com.wyq.ttmusicapp.ui.fragment.singer.SingerFragment
import kotlinx.android.synthetic.main.fragment_home.*

/**
 * Created by Roman on 2021/1/20
 */
class HomeFragment:BaseFragment() {
    private var localMusicFragment: LocalMusicFragment? = null
    private var fragmentList = ArrayList<Fragment>()
    private var singerFragment: SingerFragment? = null
    private var albumFragment: AlbumFragment? = null
    private val mTitlesArrays =
        arrayOf("单曲", "歌手", "专辑")
    private var RVAdapter: LocalRVAdapter?=null
    private var musicInfoList: ArrayList<SongInfo> = ArrayList()

    override fun getLayout(): Int {
        return R.layout.fragment_home
    }

    override fun initData() {
        musicInfoList = arguments?.getParcelableArrayList<SongInfo>("musicList")!!
        initFragment()
        RVAdapter = LocalRVAdapter(
            activity!!.supportFragmentManager,
            FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT,
            mTitlesArrays, fragmentList
        )
    }

    override fun initViews() {
        setUpToolBar()
        initFragmentList()
    }
    private fun initFragmentList() {
        local_viewPager.adapter = RVAdapter
        local_viewPager.offscreenPageLimit = 2
        local_tab.setViewPager(local_viewPager,mTitlesArrays)
    }

    private fun setUpToolBar(){
        setHasOptionsMenu(true)
        (activity as AppCompatActivity).setSupportActionBar(local_music_toolbar)
        (activity as AppCompatActivity).supportActionBar?.title = getString(R.string.local_music)
    }

    private fun initFragment() {
        if (localMusicFragment == null) {
            localMusicFragment = LocalMusicFragment()
            val bundle = Bundle()
            bundle.putParcelableArrayList("musicList",musicInfoList)
            localMusicFragment!!.arguments = bundle
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
    }
}