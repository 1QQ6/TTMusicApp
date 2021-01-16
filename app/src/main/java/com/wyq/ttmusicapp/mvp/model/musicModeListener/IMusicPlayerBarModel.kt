package com.wyq.ttmusicapp.mvp.model.musicModeListener

import android.content.Context
import com.wyq.ttmusicapp.mvp.presenter.`interface`.OnMusicPlayerBarListener

/**
 * Created by Roman on 2021/1/16
 */
interface IMusicPlayerBarModel {
    fun startOrPauseMusic(context: Context, isNext:Boolean, onMusicPlayerBarListener: OnMusicPlayerBarListener)
}