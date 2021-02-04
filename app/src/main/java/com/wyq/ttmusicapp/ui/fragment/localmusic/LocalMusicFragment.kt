package com.wyq.ttmusicapp.ui.fragment.localmusic

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.wyq.ttmusicapp.R
import com.wyq.ttmusicapp.adapter.SongRVAdapter
import com.wyq.ttmusicapp.base.BaseFragment
import com.wyq.ttmusicapp.common.Constant
import com.wyq.ttmusicapp.core.PlayMusicManager
import com.wyq.ttmusicapp.dao.DatabaseManager
import com.wyq.ttmusicapp.entity.SongInfo
import com.wyq.ttmusicapp.interfaces.IRefreshListener
import com.wyq.ttmusicapp.ui.playmusic.PlayMusicActivity
import com.wyq.ttmusicapp.utils.SPUtil
import kotlinx.android.synthetic.main.fragment_song.*

/**
 * Created by Roman on 2021/1/10
 */
class LocalMusicFragment: BaseFragment(), LocalMusicContract.View {
    var songRVAdapter:SongRVAdapter? = null
    val FINISH = 123
    private var musicInfoList: ArrayList<SongInfo> = ArrayList()
    private var presenter: LocalMusicContract.Presenter? = null

    private var dbManager:DatabaseManager? = null

    private val receiver = object : BroadcastReceiver(){
        override fun onReceive(context: Context?, intent: Intent?) {
            when(intent?.action){
                Constant.PLAY_MUSIC_VIEW_UPDATE->{
                    songRVAdapter?.notifyDataSetChanged()
                }
                else->{

                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dbManager = DatabaseManager.getInstance(context)
        updateView()
    }

    override fun getLayout(): Int {
        return R.layout.fragment_song
    }

    override fun initData() {
        LocalMusicPresenter(this)
        initReceiver()
        musicInfoList.sortBy { it.musicFirstLetter }
        //initDefaultPlayModeView()
    }

    override fun initViews() {
        initMusicList()
        setClickEvent()
    }
    private fun initReceiver() {
        val intentFilter = IntentFilter()
        intentFilter.addAction(Constant.PLAY_MUSIC_VIEW_UPDATE)
        context!!.registerReceiver(receiver,intentFilter)
    }

    private fun setClickEvent() {
/*        local_music_play_mode_rl.setOnClickListener {
            setMusicMode()
        }*/
    }
    /**
     * 初始化音乐模式UI
     */
/*    private fun initDefaultPlayModeView() {
        when (SPUtil.getIntShared(Constant.KEY_PLAY_MODE)) {
            Constant.PLAY_MODE_SEQUENCE -> local_music_play_mode_tv.text = getString(R.string.play_mode_sequence)
            Constant.PLAY_MODE_RANDOM -> local_music_play_mode_tv.text = getString(R.string.play_mode_random)
            Constant.PLAY_MODE_SINGLE_REPEAT -> local_music_play_mode_tv.text = getString(R.string.play_mode_repeat)
        }
        initPlayMode()
    }*/

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
   /* private fun setMusicMode() {
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
    }*/

    override fun onResume() {
        super.onResume()
        updateView()
        initMusicList()
    }

    private fun updateView() {
        musicInfoList.clear()
        musicInfoList.add(SongInfo(-1,"","",-1,"",-1,"","","","",-1))
        musicInfoList.addAll(dbManager!!.getAllMusicFromMusicTable())
    }


    private fun initMusicList() {
        songRVAdapter = SongRVAdapter(musicInfoList)
        val linearLayoutManager = LinearLayoutManager(context)
        linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
        local_recycler_view.layoutManager = linearLayoutManager
        local_recycler_view.adapter = songRVAdapter
        local_recycler_view.setOnRefreshListener(object :IRefreshListener{
            override fun onRefresh() {
                presenter?.requestData()
            }
        })


        songRVAdapter!!.setOnItemClickListener(object :SongRVAdapter.OnItemClickListener{
            override fun onOpenMenuClick(position: Int) {

            }
            override fun onItemClick(position: Int) {
                val isPlaying = PlayMusicManager.getMusicManager()!!.isPlaying
                val nowPlayingIndex = PlayMusicManager.getMusicManager()!!.nowPlayingIndex
                //如果当前点击的歌曲正在播放并且和当前播放的index相等，那么跳转到播放主页
                if(isPlaying && nowPlayingIndex == position){
                    PlayMusicActivity.startActivity(context!!)
                    return
                }
                PlayMusicManager.getMusicManager()!!.prepareAndPlay(position,musicInfoList)
                songRVAdapter?.notifyDataSetChanged()
            }
        })
    }

    override fun updateListView(musicList: ArrayList<SongInfo>) {
        musicInfoList.clear()
        musicInfoList.add(SongInfo(-1,"","",-1,"",-1,"","","","",-1))
        musicInfoList.addAll(musicList)
        initMusicList()
        local_recycler_view.refreshComplete()
    }

    override fun showBottomMenu() {

    }

    override fun setPresenter(presenter: LocalMusicContract.Presenter) {
        this.presenter = presenter
    }

    override fun onDestroy() {
        super.onDestroy()
        context!!.unregisterReceiver(receiver)
    }

}