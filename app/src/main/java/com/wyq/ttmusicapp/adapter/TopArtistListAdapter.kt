package com.wyq.ttmusicapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.wyq.ttmusicapp.R
import com.wyq.ttmusicapp.common.MusicApplication.Companion.context
import com.wyq.ttmusicapp.entity.Artist
import com.wyq.ttmusicapp.interfaces.IItemClickListener
import com.wyq.ttmusicapp.utils.CoverLoader
import com.wyq.ttmusicapp.utils.PlayMusicHelper
import com.wyq.ttmusicapp.utils.PlayMusicHelper.PIC_SIZE_SMALL
import kotlinx.android.synthetic.main.item_play_list.view.*

/**
 * Created by Roman on 2021/2/10
 */
class TopArtistListAdapter(val list: List<Artist>):RecyclerView.Adapter<TopArtistListAdapter.ViewHolder>() {

    private var onItemClickListener: IItemClickListener? = null

    fun setOnItemClickListener(onItemClickListener: IItemClickListener?) {
        this.onItemClickListener = onItemClickListener
    }

    class ViewHolder(itemView:View):RecyclerView.ViewHolder(itemView){

        fun bind(list: List<Artist>, onItemClickListener: IItemClickListener?) {
            val artistInfo = list[adapterPosition]
            with(itemView){
                tv_name.text = artistInfo.singerName
                CoverLoader.loadImageView(context, PlayMusicHelper.getAlbumPic(artistInfo.picUrl,PIC_SIZE_SMALL),iv_artist_cover)
                artist_root_item.setOnClickListener {
                    onItemClickListener?.onItemClick(adapterPosition)
                }
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_play_list,parent,false))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list,onItemClickListener)
    }

}