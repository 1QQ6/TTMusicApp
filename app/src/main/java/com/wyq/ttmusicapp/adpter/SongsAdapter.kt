package com.wyq.ttmusicapp.adpter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.wyq.ttmusicapp.entity.SongInfo
import com.wyq.ttmusicapp.R

/**
 * Created by Roman on 2021/1/10
 */
class SongsAdapter(var context: Context, var listSongInfo: List<SongInfo>) : BaseAdapter() {

    inner class ViewHolder{
        var songName: TextView?=null
        var singer:TextView?=null
        var duration: TextView?=null
        var position: TextView?=null
    }
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var view = convertView
        val holder:ViewHolder
        if (convertView==null){
            //引入布局
            view = LayoutInflater.from(context).inflate(R.layout.item_songs_list_view,null)
            holder = ViewHolder()
            holder.songName = view!!.findViewById(R.id.item_song_name)
            holder.duration = view!!.findViewById(R.id.item_duration)
            holder.position = view!!.findViewById(R.id.item_position)
            holder.singer = view!!.findViewById(R.id.item_singer)
            view.tag = holder
        }else{
            holder = (view?.tag) as ViewHolder
        }
            val songItemInfo = listSongInfo[position]
            holder.singer!!.text = songItemInfo.musicSinger
            holder.songName!!.text = songItemInfo.musicName
            //val time: String = MusicScanUtils.formatTime().toString()
            holder.duration!!.text = songItemInfo.musicDuration
            holder.position!!.text = (position+1).toString()
            holder.singer!!.text = songItemInfo.musicSinger

        return view!!
    }

    override fun getItem(position: Int): Any {
        return listSongInfo[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return listSongInfo.size
    }
}