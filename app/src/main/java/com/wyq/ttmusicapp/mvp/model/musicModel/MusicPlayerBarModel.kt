package com.wyq.ttmusicapp.mvp.model.musicModel

import android.content.Context
import com.wyq.ttmusicapp.common.Constant
import com.wyq.ttmusicapp.mvp.model.musicModeListener.IMusicPlayerBarModel
import com.wyq.ttmusicapp.mvp.presenter.`interface`.OnMusicPlayerBarListener
import com.wyq.ttmusicapp.utils.PlayMusicHelper

/**
 * Created by Roman on 2021/1/16
 */
class MusicPlayerBarModel : IMusicPlayerBarModel {


    override fun startOrPauseMusic(
        context: Context,
        isNext: Boolean,
        onMusicPlayerBarListener: OnMusicPlayerBarListener
    ) {
        if (!isNext) {
            when (PlayMusicHelper.playCurrentMusic(context)) {
                Constant.STATUS_PAUSE -> {
                    onMusicPlayerBarListener.getCurrentMusicSuccess(Constant.STATUS_PAUSE)
                }
                Constant.STATUS_PLAY -> {
                    onMusicPlayerBarListener.getCurrentMusicSuccess(Constant.STATUS_PLAY)
                }
                Constant.STATUS_ERROR -> {
                    onMusicPlayerBarListener.getCurrentMusicError()
                }
            }
        } else {
            var songInfo = PlayMusicHelper.playNextMusic(context)
            if (songInfo!=null){
                onMusicPlayerBarListener.getNextMusicSuccess(songInfo)
            }else{
                onMusicPlayerBarListener.getNextMusicError()
            }
        }
    }
}