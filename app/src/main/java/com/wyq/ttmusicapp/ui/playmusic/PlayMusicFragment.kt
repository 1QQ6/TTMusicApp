package com.wyq.ttmusicapp.ui.playmusic

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.widget.SeekBar
import com.wyq.ttmusicapp.R
import com.wyq.ttmusicapp.base.BaseFragment
import com.wyq.ttmusicapp.common.Constant
import com.wyq.ttmusicapp.core.PlayMusicManager
import com.wyq.ttmusicapp.entity.SongInfo
import com.wyq.ttmusicapp.home.HomeActivity
import com.wyq.ttmusicapp.utils.CoverLoader
import com.wyq.ttmusicapp.utils.SPUtil
import com.wyq.ttmusicapp.utils.TimeUtil
import com.wyq.ttmusicapp.utils.toast
import kotlinx.android.synthetic.main.fragment_playing_music.*

/**
 * Created by Roman on 2021/1/18
 */
class PlayMusicFragment : BaseFragment(), PlayMusicContract.View {
    private lateinit var presenter: PlayMusicContract.Presenter
    private var playMusicManager: PlayMusicManager? = null
    private var intentFilter: IntentFilter? = null

    /**
     * 是否正在拖动进度条
     * true：拖动
     * false：停止拖动
     */
    private var mProgressBarLock: Boolean = false

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (!isAdded){
                return
            }
            when (intent!!.action) {
                Constant.PLAY_MUSIC_VIEW_UPDATE->{
                    //设置默认状态正常为0
                    val musicErrorCode = intent.getIntExtra(Constant.PLAY_MUSIC_ERROR, 0)
                    if (musicErrorCode == Constant.PLAY_MUSIC_ERROR_SIZE){
                        context!!.toast(getString(R.string.play_music_error))
                        updateMusicBarUI(true)
                        return
                    }
                    val isPlayMusic = intent.getBooleanExtra(Constant.IS_PLAYING, false)
                    val songInfo = intent.getParcelableExtra<SongInfo>(Constant.NOW_PLAY_MUSIC)
                    if (songInfo!=null){
                        updateMusicBarUI(isPlayMusic)
                    }
                }
                Constant.CURRENT_UPDATE->{
                    val currentTime = intent.getIntExtra(Constant.SEEK_BAR_CURRENT_TIME, 0)
                    playpage_playtime_tv?.text = TimeUtil.mill2mmss(currentTime.toLong())
                    if(!mProgressBarLock){
                        play_page_progressbar?.progress = currentTime
                    }
                }
                Constant.UPRATE_MUSIC_QUEUE->{

                }
                Constant.BUFFER_UPDATE->{

                }
                else->{

                }
            }
        }
    }

    private fun updateMusicBarUI( isPlaying: Boolean) {
        if (!isAdded){
            return
        }
        updateSongInfoView()
        setImageAnimation(isPlaying)
    }

    /**
     * 初始化页面
     */
    private fun initMusicView() {
        val isPlaying = PlayMusicManager.getMusicManager()!!.isPlaying
        playpage_play!!.isChecked = isPlaying
        val recentMusicProgress = SPUtil.getRecentMusicProgress()
        if (recentMusicProgress!=0 && !isPlaying){
            play_page_progressbar?.max = PlayMusicManager.getMusicManager()!!.nowPlayingSong!!.musicDuration!!
            play_page_progressbar?.progress = recentMusicProgress
            playpage_playtime_tv!!.text = TimeUtil.mill2mmss(recentMusicProgress.toLong())
        }
        updateSongInfoView()
        setImageAnimation(isPlaying)
    }

    private fun setImageAnimation(isPlaying: Boolean) {
        if (!isAdded){
            return
        }
        playpage_play?.isChecked = isPlaying
        if (isPlaying) {
            rotateView?.start()
        } else {
            rotateView?.pause()
        }
    }

    private fun updateSongInfoView() {
        if (!isAdded){
            return
        }
        val songInfo = PlayMusicManager.getMusicManager()!!.nowPlayingSong

        CoverLoader.loadBitmap(context, songInfo!!.coverUrl) {
            rotateView.setImageBitmap(it)
            iv_playing_bg.setImageDrawable(CoverLoader.createBlurredImageFromBitmap(it))
        }
        if (songInfo != null) {
            play_page_title_tv?.text = songInfo.musicName
            play_page_artist_tv?.text = songInfo.musicSinger

            play_page_duration_tv?.text = TimeUtil.mill2mmss(songInfo.musicDuration!!.toLong())
            play_page_progressbar?.max = songInfo.musicDuration!!
        }
    }

    override fun togglePanel() {

    }

    override fun setPresenter(presenter: PlayMusicContract.Presenter) {
        this.presenter = presenter
    }

    override fun getLayout(): Int {
        return R.layout.fragment_playing_music
    }

    override fun initData() {
        registerBroadcast()
        playMusicManager = PlayMusicManager.getMusicManager()
    }

    private fun registerBroadcast() {
        if (!isAdded){
            return
        }
        intentFilter = IntentFilter()
        intentFilter!!.addAction(Constant.PLAY_MUSIC_VIEW_UPDATE)
        intentFilter!!.addAction(Constant.CURRENT_UPDATE)
        intentFilter!!.addAction(Constant.UPRATE_MUSIC_QUEUE)
        intentFilter!!.addAction(Constant.BUFFER_UPDATE)
        context!!.registerReceiver(receiver,intentFilter)
    }

    override fun initViews() {
        initMusicView()
        initClickEvent()
    }

    private fun initClickEvent() {
        if (!isAdded){
            return
        }
        play_page_progressbar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            var pro: Int = 0
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                pro = progress
                playpage_playtime_tv!!.text = TimeUtil.mill2mmss(progress.toLong())
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                mProgressBarLock = true

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                mProgressBarLock = false
                PlayMusicManager.getMusicManager()!!.seekTo(pro)
            }
        })

        playpage_play.setOnCheckedChangeListener { buttonView, isChecked ->

            if (isChecked == playMusicManager!!.isPlaying) {
                return@setOnCheckedChangeListener
            }
            if (isChecked) {
                playMusicManager!!.play()
            } else {
                playMusicManager!!.pause()
            }

        }
        play_page_previous.setOnClickListener {
            PlayMusicManager.getMusicManager()!!.preSong()
        }
        play_page_next.setOnClickListener {
            PlayMusicManager.getMusicManager()!!.nextSong()
        }
        playpage_return.setOnClickListener {
            HomeActivity.startActivity(context!!)
            activity?.finish()
        }
    }
}
