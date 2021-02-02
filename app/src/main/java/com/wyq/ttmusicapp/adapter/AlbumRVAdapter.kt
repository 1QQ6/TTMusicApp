package com.wyq.ttmusicapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.wyq.ttmusicapp.R
import com.wyq.ttmusicapp.common.MusicApplication
import com.wyq.ttmusicapp.entity.AlbumInfo
import com.wyq.ttmusicapp.utils.CoverLoader
import kotlinx.android.synthetic.main.item_album_music.view.*

/**
 * Created by Roman on 2021/1/31
 */
class AlbumRVAdapter(private val albumInfoList:List<AlbumInfo>):
    RecyclerView.Adapter<AlbumRVAdapter.ViewHolder>() {

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
        fun bind(albumInfoList:List<AlbumInfo>, onItemClickListener: OnItemClickListener){
            val albumInfo = albumInfoList[adapterPosition]
            with(itemView){

                album_item_name.text = albumInfo.albumName
                album_music_singer.text = albumInfo.singer
                CoverLoader.loadBitmap(context,albumInfo.albumUrl,R.drawable.default_cover){
                    album_music_iv.setImageBitmap(it)
                }
                //item点击
                album_root_view.setOnClickListener {
                    onItemClickListener.onItemClick(adapterPosition)
                }
                //菜单栏点击
                album_music_item_menu.setOnClickListener {
                    onItemClickListener.onOpenMenuClick(adapterPosition)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(MusicApplication.context)
                .inflate(R.layout.item_album_music, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return albumInfoList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(albumInfoList,onItemClickListener!!)
    }
}