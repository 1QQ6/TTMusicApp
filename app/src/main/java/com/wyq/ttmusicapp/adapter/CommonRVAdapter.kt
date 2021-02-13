package com.wyq.ttmusicapp.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.wyq.ttmusicapp.R
import com.wyq.ttmusicapp.common.MusicApplication.Companion.context
import com.wyq.ttmusicapp.core.PlayMusicManager
import com.wyq.ttmusicapp.dao.DatabaseManager
import com.wyq.ttmusicapp.entity.SongInfo
import kotlinx.android.synthetic.main.item_local_music.view.*

/**
 * Created by Roman on 2021/1/12
 */
class CommonRVAdapter(private val musicInfoList:ArrayList<SongInfo>)
    : RecyclerView.Adapter<CommonRVAdapter.ViewHolder>(){

    private var dbManager: DatabaseManager? = null
    private var onItemClickListener: OnItemClickListener? = null

    init {
        dbManager = DatabaseManager.getInstance(context)
    }
    /**
     * 点击事件监听回调
     */
    interface OnItemClickListener {
        fun onOpenMenuClick(position: Int)
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener?) {
        this.onItemClickListener = onItemClickListener
    }

    class ViewHolder(itemView:View): RecyclerView.ViewHolder(itemView) {
        fun bind(musicInfoList:ArrayList<SongInfo>,  onItemClickListener:OnItemClickListener){
            val musicInfo = musicInfoList[adapterPosition]

            val musicId = PlayMusicManager.getMusicManager()?.nowPlayingSong?.music_id

            with(itemView){
                local_item_name.text = musicInfo.musicName
                local_item_index.text = (adapterPosition+1).toString()
                local_music_singer.setTextColor(ContextCompat.getColor(context,R.color.black))
                local_music_singer.text = musicInfo.musicSinger +"-"+musicInfo.musicAlbum
                initTheme()
                //设置当前正在播放的itemView
                if (musicInfo.music_id == musicId) {
                    local_item_name.setTextColor(ContextCompat.getColor(context,R.color.colorPrimaryDark))

                    local_item_iv.visibility = View.VISIBLE
                    local_item_index.visibility = View.INVISIBLE
                }else{
                    local_item_name.setTextColor(ContextCompat.getColor(context,R.color.black))
                    local_item_iv.visibility = View.INVISIBLE
                    local_item_index.visibility = View.VISIBLE
                }
                //点击播放
                local_music_item.setOnClickListener {
                    Log.i(TAG, "onClick: 播放 " + musicInfo.musicName)
                    onItemClickListener.onItemClick(adapterPosition)
                }
                //点击菜单
                local_music_item.setOnLongClickListener {
                    onItemClickListener.onOpenMenuClick(adapterPosition)
                    true
                }
            }
        }

        private fun initTheme() {
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(context).inflate(R.layout.item_local_music, parent, false))
    }

    override fun getItemCount(): Int {
        return musicInfoList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.bind(musicInfoList,onItemClickListener!!)
    }

    /**
     * 更新列表
     */
    fun updateMusicInfoList(musicInfoList: ArrayList<SongInfo>) {
        this.musicInfoList.clear()
        this.musicInfoList.addAll(musicInfoList)
        notifyDataSetChanged()
    }

    companion object {
        private val TAG = CommonRVAdapter::class.java.name
    }

}