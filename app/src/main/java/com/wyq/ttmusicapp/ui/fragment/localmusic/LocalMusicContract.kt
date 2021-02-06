package com.wyq.ttmusicapp.ui.fragment.localmusic

import android.content.Context
import com.wyq.ttmusicapp.base.BasePresenter
import com.wyq.ttmusicapp.base.BaseView
import com.wyq.ttmusicapp.entity.SongInfo
/**
 * Created by Roman on 2021/1/28
 */
interface LocalMusicContract {

    interface View:BaseView<Presenter>{
        /**
         * 更新 recycleview 列表
         */
        fun updateListView(musicList: ArrayList<SongInfo>)

        /**
         * 显示底部弹框
         */
        fun showBottomMenu(position: Int)
    }

    interface Presenter:BasePresenter{
        /**
         * 设置播放模式
         */
        fun setMusicMode()

        /**
         * 分享音乐
         */
        fun sharedMusic(musicId:Long)

        /**
         * 删除音乐
         */
        fun deleteMusic(context:Context,musicId:Long)


        /**
         * 下拉刷新数据
         */
        fun requestData()
        /**
         * 更新歌曲爱心状态
         */
        fun updateLoveStatus(musicId: Long)

        /**
         * 单曲删除完的回调
         */
        interface ILocalDeleteListener {
            fun onDeleteComplete()
        }
    }

}