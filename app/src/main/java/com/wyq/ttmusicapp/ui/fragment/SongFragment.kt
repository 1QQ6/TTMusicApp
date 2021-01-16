package com.wyq.ttmusicapp.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.wyq.ttmusicapp.R
import com.wyq.ttmusicapp.adpter.SongRecyclerViewAdapter
import com.wyq.ttmusicapp.base.BaseFragment
import com.wyq.ttmusicapp.common.Constant
import com.wyq.ttmusicapp.dao.DatabaseManager
import com.wyq.ttmusicapp.mvp.model.entity.SongInfo
import com.wyq.ttmusicapp.utils.PlayMusicSPUtil
import kotlinx.android.synthetic.main.fragment_song.*

/**
 * Created by Roman on 2021/1/10
 */
class SongFragment: BaseFragment() {
    var songRecyclerViewAdapter:SongRecyclerViewAdapter? = null
    private var musicInfoList: ArrayList<SongInfo> = ArrayList()

    private var dbManager:DatabaseManager? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dbManager = DatabaseManager.getInstance(context)
        updateView()
    }

    override fun getLayout(): Int {
        return R.layout.fragment_song
    }

    override fun initData() {
        musicInfoList.sortBy { it.musicFirstLetter }
        initDefaultPlayModeView()
    }

    override fun initViews() {
        initMusicList()
        setClickEvent()
    }

    private fun setClickEvent() {
        local_music_play_mode_rl.setOnClickListener {
            setMusicMode()
        }
    }

    private fun initDefaultPlayModeView() {
        when (PlayMusicSPUtil.getIntShared(Constant.KEY_PLAY_MODE)) {
            Constant.PLAY_MODE_SEQUENCE -> local_music_play_mode_tv.text = getString(R.string.play_mode_sequence)
            Constant.PLAY_MODE_RANDOM -> local_music_play_mode_tv.text = getString(R.string.play_mode_random)
            Constant.PLAY_MODE_SINGLE_REPEAT -> local_music_play_mode_tv.text = getString(R.string.play_mode_repeat)
        }
        initPlayMode()
    }

    private fun initPlayMode() {
        var playMode: Int = PlayMusicSPUtil.getIntShared(Constant.KEY_PLAY_MODE)
        if (playMode == -1) {
            playMode = 0
        }
    }

    private fun setMusicMode() {
            when (PlayMusicSPUtil.getIntShared(Constant.KEY_PLAY_MODE)) {
                Constant.PLAY_MODE_SEQUENCE -> {
                    local_music_play_mode_tv.text = getString(R.string.play_mode_random)
                    PlayMusicSPUtil.setPlayMusicModeShared(Constant.PLAY_MODE_RANDOM)
                }
                Constant.PLAY_MODE_RANDOM -> {
                    local_music_play_mode_tv.text = getString(R.string.play_mode_repeat)
                    PlayMusicSPUtil.setPlayMusicModeShared(Constant.PLAY_MODE_SINGLE_REPEAT)
                }
                Constant.PLAY_MODE_SINGLE_REPEAT -> {
                    local_music_play_mode_tv.text = getString(R.string.play_mode_sequence)
                    PlayMusicSPUtil.setPlayMusicModeShared(Constant.PLAY_MODE_SEQUENCE)
                }
            }
    }

    override fun onResume() {
        super.onResume()
    }

    private fun updateView() {
        musicInfoList = dbManager!!.getAllMusicFromMusicTable()
    }


    private fun initMusicList() {
        songRecyclerViewAdapter = SongRecyclerViewAdapter(musicInfoList)
        val linearLayoutManager = LinearLayoutManager(context)
        linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
        local_recycler_view.layoutManager = linearLayoutManager
        local_recycler_view.adapter = songRecyclerViewAdapter

        songRecyclerViewAdapter!!.setOnItemClickListener(object :SongRecyclerViewAdapter.OnItemClickListener{
            override fun onOpenMenuClick(position: Int) {

            }

            override fun onDeleteMenuClick(content: View?, position: Int) {

            }

            override fun onItemClick(position: Int) {
                PlayMusicSPUtil.setShared(Constant.KEY_LIST, Constant.LIST_ALL_MUSIC)
            }
        })
    }

}