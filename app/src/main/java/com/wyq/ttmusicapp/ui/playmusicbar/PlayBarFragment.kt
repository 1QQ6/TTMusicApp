package com.wyq.ttmusicapp.ui.playmusicbar

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.widget.SeekBar
import androidx.core.content.ContextCompat
import com.wyq.ttmusicapp.R
import com.wyq.ttmusicapp.base.BaseFragment
import com.wyq.ttmusicapp.common.Constant
import com.wyq.ttmusicapp.entity.SongInfo
import com.wyq.ttmusicapp.ui.playmusic.PlayMusicActivity
import com.wyq.ttmusicapp.utils.PlayMusicHelper
import com.wyq.ttmusicapp.utils.PlayMusicSPUtil
import kotlinx.android.synthetic.main.fragment_play_bar.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread


/**
 * Created by Roman on 2021/1/16
 */
class PlayBarFragment:BaseFragment(), PlayBarContract.View {

    var musicPlayerBarPresenter:PlayBarContract.Presenter? = null
    var isReceiverRegistered = false
    var isPlayMusic = false
    override fun getLayout(): Int {
        return R.layout.fragment_play_bar
    }

    override fun initData() {
        initReceiver()
        PlayerBarPresenter(this)
    }

    override fun initViews() {
        initMusicInfo()
        clickEvent()
    }


    private fun initMusicInfo() {
        val musicId: Long = PlayMusicSPUtil.getCurrentMusicId()
        if (musicId == -1L) {
            home_music_name_tv.text = "天天音乐"
            home_singer_name_tv.text = "好音质"
        }else{
            doAsync {
                val musicInfo = PlayMusicHelper.getMusicInfoById(musicId)
                uiThread {
                    if (musicInfo!=null){
                        home_music_name_tv.text = musicInfo.musicName
                        home_singer_name_tv.text = musicInfo.musicSinger

                    }
                }
            }
            if (isPlayMusic) {
                player_bar_iv.start()
                play_iv.setImageDrawable(ContextCompat.getDrawable(context!!, R.drawable.ic_playing))
            } else {
                player_bar_iv.pause()
                play_iv.setImageDrawable(ContextCompat.getDrawable(context!!, R.drawable.ic_play_stop))
            }
        }
    }

    private fun clickEvent() {

        home_seek_bar.setOnSeekBarChangeListener(object :SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {

            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {

            }

        })
        //点击整个bar进入音乐详情页
        root_player_bar_view.setOnClickListener {
            val intent = Intent(activity, PlayMusicActivity::class.java)
            startActivity(intent)
        }

        //点击start或pause
        play_iv.setOnClickListener {
            musicPlayerBarPresenter!!.startPlayMusic()
        }
        //点击播放下一首
        next_iv.setOnClickListener{
            musicPlayerBarPresenter!!.startNextMusic()
        }
        //点击菜单按钮
        play_menu_iv.setOnClickListener {

        }

    }

    companion object {

        @JvmStatic
        @Synchronized
        fun newInstance(): PlayBarFragment {
            return PlayBarFragment()
        }
    }


    override fun setMusicInfo(songInfo: SongInfo) {

    }

    override fun startMusicFailed() {

    }

    override fun setPresenter(presenter: PlayBarContract.Presenter) {
        musicPlayerBarPresenter = presenter
    }

    override fun onDestroy() {
        super.onDestroy()
        if (isReceiverRegistered){
            context!!.unregisterReceiver(receiver)
        }
    }

    /**
     * 动态注册广播，接收上一次播放的音乐并保存
     */
    private fun initReceiver() {
        val intentFilter = IntentFilter()
        //bar当前状态
        intentFilter.addAction(Constant.PLAY_BAR_UPDATE)
        //当前进度条
        intentFilter.addAction(Constant.CURRENT_UPDATE)
        context!!.registerReceiver(receiver,intentFilter)
        isReceiverRegistered = true
    }

    /**
     * 更新UI
     */
    private val receiver = object : BroadcastReceiver(){
        override fun onReceive(context: Context?, intent: Intent?) {
            when(intent!!.action){
                Constant.PLAY_BAR_UPDATE->{
                    isPlayMusic = intent.getBooleanExtra(Constant.IS_PLAYING, false)
                    val songInfo = intent.getParcelableExtra<SongInfo>(Constant.NOW_PLAY_MUSIC)
                    if (songInfo!=null){
                        updateMusicBarUI(songInfo,isPlayMusic)
                    }
                }
                Constant.CURRENT_UPDATE->{
                    home_seek_bar.progress = intent.getIntExtra(Constant.SEEK_BAR_CURRENT_TIME, 0)
                }
            }
        }
    }

    private fun updateMusicBarUI(songInfo:SongInfo,isPlaying: Boolean) {
        home_music_name_tv.text =songInfo.musicName
        home_singer_name_tv.text = songInfo.musicSinger
        home_seek_bar.max = songInfo.musicDuration!!
        if (isPlaying) {
            player_bar_iv.start()
            play_iv.setImageDrawable(ContextCompat.getDrawable(context!!, R.drawable.ic_playing))
        } else {
            player_bar_iv.pause()
            play_iv.setImageDrawable(ContextCompat.getDrawable(context!!, R.drawable.ic_play_stop))
        }
    }
}