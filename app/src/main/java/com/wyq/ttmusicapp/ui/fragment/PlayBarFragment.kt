package com.wyq.ttmusicapp.ui.fragment

import android.content.Intent
import android.widget.SeekBar
import androidx.core.content.ContextCompat
import com.wyq.ttmusicapp.R
import com.wyq.ttmusicapp.base.BaseFragment
import com.wyq.ttmusicapp.common.Constant
import com.wyq.ttmusicapp.mvp.model.entity.SongInfo
import com.wyq.ttmusicapp.mvp.presenter.musicPresenter.MusicPlayerBarPresenter
import com.wyq.ttmusicapp.mvp.view.MusicPlayerBarView
import com.wyq.ttmusicapp.ui.activity.PlayMusicActivity
import com.wyq.ttmusicapp.utils.PlayMusicHelper
import com.wyq.ttmusicapp.utils.PlayMusicSPUtil
import kotlinx.android.synthetic.main.fragment_play_bar.*

/**
 * Created by Roman on 2021/1/16
 */
class PlayBarFragment:BaseFragment(), MusicPlayerBarView {

    var musicPlayerBarPresenter:MusicPlayerBarPresenter? = null

    override fun getLayout(): Int {
        return R.layout.fragment_play_bar
    }

    override fun initData() {
        musicPlayerBarPresenter = MusicPlayerBarPresenter(this)
    }

    override fun initViews() {
        initMusicInfo()
        initPlayBarUI()
        clickEvent()
    }

    private fun initPlayBarUI() {
        when (PlayMusicHelper.getPlayStatus()) {
            Constant.STATUS_STOP -> play_iv.isSelected = false
            Constant.STATUS_PLAY -> play_iv.isSelected = true
            Constant.STATUS_PAUSE -> play_iv.isSelected = false
            Constant.STATUS_RUN -> play_iv.isSelected = true
        }
    }

    private fun initMusicInfo() {
        val musicId: Int = PlayMusicSPUtil.getIntShared(Constant.KEY_MUSIC_ID)
        if (musicId == -1) {
            home_music_name_tv.text = "听听音乐"
            home_singer_name_tv.text = "好音质"
        }else{
            val musicInfo = PlayMusicHelper.getMusicInfoById(musicId)
            if (musicInfo!=null){
                home_music_name_tv.text = musicInfo.musicName
                home_singer_name_tv.text = musicInfo.musicSinger
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
            musicPlayerBarPresenter!!.startPlayMusic(context!!,false)
        }
        //点击播放下一首
        next_iv.setOnClickListener{
            musicPlayerBarPresenter!!.startPlayMusic(context!!,true)
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

    override fun updatePlayerViewUI(status: Int) {
        if (status == Constant.STATUS_PLAY){
            play_iv.setImageDrawable(ContextCompat.getDrawable(context!!,R.drawable.ic_playing))
        }else if(status == Constant.STATUS_PAUSE){
            play_iv.setImageDrawable(ContextCompat.getDrawable(context!!,R.drawable.ic_play_stop))
        }
    }


    override fun setMusicInfo(songInfo: SongInfo) {
        home_music_name_tv.text =songInfo.musicName
        home_singer_name_tv.text = songInfo.musicSinger
    }

    override fun startMusicFailed() {

    }
}