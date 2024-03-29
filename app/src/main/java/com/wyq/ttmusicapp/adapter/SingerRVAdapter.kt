package com.wyq.ttmusicapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.wyq.ttmusicapp.R
import com.wyq.ttmusicapp.common.MusicApplication
import com.wyq.ttmusicapp.entity.Artist
import kotlinx.android.synthetic.main.item_singer_music.view.*
/**
 * Created by Roman on 2021/1/31
 */
class SingerRVAdapter(private val artistList:List<Artist>):
    RecyclerView.Adapter<SingerRVAdapter.ViewHolder>() {

    private var onItemClickListener: OnItemClickListener? = null
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
        fun bind(artistList:List<Artist>, onItemClickListener: OnItemClickListener){
            val singerInfo = artistList[adapterPosition]
            with(itemView){

                singer_item_name.text = singerInfo.singerName
                singer_music_singer.text = context.getString(R.string.singer_count_album_count,singerInfo.albumCount.toString(),singerInfo.songsCount.toString())
                singer_item_iv.setBackgroundResource(R.drawable.ic_ktv)

                //item点击
                singer_music_item.setOnClickListener {
                    onItemClickListener.onItemClick(adapterPosition)
                }
                //菜单栏点击
                singer_music_item_menu.setOnClickListener {
                    onItemClickListener.onOpenMenuClick(adapterPosition)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(MusicApplication.context)
                .inflate(R.layout.item_singer_music, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return artistList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(artistList,onItemClickListener!!)
    }
}