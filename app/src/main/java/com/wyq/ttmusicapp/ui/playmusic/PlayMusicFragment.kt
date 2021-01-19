package com.wyq.ttmusicapp.ui.playmusic

import com.wyq.ttmusicapp.R
import com.wyq.ttmusicapp.base.BaseFragment
import com.wyq.ttmusicapp.core.PlayMusicManager
import kotlinx.android.synthetic.main.fragment_playing_music.*

/**
 * Created by Roman on 2021/1/18
 */
class PlayMusicFragment : BaseFragment(), PlayMusicContract.View {
    private lateinit var presenter: PlayMusicContract.Presenter
    private var playMusicManager: PlayMusicManager? = null
    override fun togglePanel() {

    }

    override fun setPresenter(presenter: PlayMusicContract.Presenter) {
        this.presenter = presenter
    }

    override fun getLayout(): Int {
        return R.layout.fragment_playing_music
    }

    override fun initData() {
        playMusicManager = PlayMusicManager.getMusicManager()
    }

    override fun initViews() {
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
    }


}
