package com.wyq.ttmusicapp.ui.localmusic

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.wyq.ttmusicapp.R
import com.wyq.ttmusicapp.adpter.SongRecyclerViewAdapter
import com.wyq.ttmusicapp.base.BaseFragment
import com.wyq.ttmusicapp.common.Constant
import com.wyq.ttmusicapp.core.PlayMusicManager
import com.wyq.ttmusicapp.dao.DatabaseManager
import com.wyq.ttmusicapp.entity.SongInfo
import com.wyq.ttmusicapp.utils.SPUtil
import kotlinx.android.synthetic.main.fragment_song.*

/**
 * Created by Roman on 2021/1/10
 */
class LocalMusicFragment: BaseFragment() {
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
    /**
     * 初始化音乐模式UI
     */
    private fun initDefaultPlayModeView() {
        when (SPUtil.getIntShared(Constant.KEY_PLAY_MODE)) {
            Constant.PLAY_MODE_SEQUENCE -> local_music_play_mode_tv.text = getString(R.string.play_mode_sequence)
            Constant.PLAY_MODE_RANDOM -> local_music_play_mode_tv.text = getString(R.string.play_mode_random)
            Constant.PLAY_MODE_SINGLE_REPEAT -> local_music_play_mode_tv.text = getString(R.string.play_mode_repeat)
        }
        initPlayMode()
    }

    /**
     * 初始化音乐模式
     */
    private fun initPlayMode() {
        var playMode: Int = SPUtil.getIntShared(Constant.KEY_PLAY_MODE)
        if (playMode == -1) {
            playMode = 0
        }
    }

    /**
     * 设置音乐模式
     * 随机
     * 单曲
     * 顺序
     */
    private fun setMusicMode() {
            when (SPUtil.getIntShared(Constant.KEY_PLAY_MODE)) {
                Constant.PLAY_MODE_SEQUENCE -> {
                    local_music_play_mode_tv.text = getString(R.string.play_mode_random)
                    SPUtil.setPlayMusicModeShared(Constant.PLAY_MODE_RANDOM)
                }
                Constant.PLAY_MODE_RANDOM -> {
                    local_music_play_mode_tv.text = getString(R.string.play_mode_repeat)
                    SPUtil.setPlayMusicModeShared(Constant.PLAY_MODE_SINGLE_REPEAT)
                }
                Constant.PLAY_MODE_SINGLE_REPEAT -> {
                    local_music_play_mode_tv.text = getString(R.string.play_mode_sequence)
                    SPUtil.setPlayMusicModeShared(Constant.PLAY_MODE_SEQUENCE)
                }
            }
    }

    override fun onResume() {
        super.onResume()
        updateView()
        initMusicList()
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
                PlayMusicManager.getMusicManager()!!.prepareAndPlay(position,musicInfoList)
            }
        })
    }

}