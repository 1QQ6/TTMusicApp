package com.wyq.ttmusicapp.ui.playmusic

import android.content.Context
import com.wyq.ttmusicapp.base.ContextPresenter

/**
 * Created by Roman on 2021/1/18
 */
class PlayMusicPresenter(context: Context,view:PlayMusicContract.View) :ContextPresenter<PlayMusicContract.View>(context, view), PlayMusicContract.Presenter  {

    init {
        view.setPresenter(this)
    }

    override fun performPanelClick() {
        view.togglePanel()
    }

    override fun start() {
    }

}