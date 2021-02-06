package com.wyq.ttmusicapp.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SectionIndexer
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.wyq.ttmusicapp.R
import com.wyq.ttmusicapp.common.Constant
import com.wyq.ttmusicapp.common.MusicApplication.Companion.context
import com.wyq.ttmusicapp.core.PlayMusicManager
import com.wyq.ttmusicapp.entity.SongInfo
import com.wyq.ttmusicapp.interfaces.IRefreshHeader
import kotlinx.android.synthetic.main.item_local_music.view.*

/**
 * Created by Roman on 2021/1/12
 */
class SongRVAdapter(private var musicInfoList:ArrayList<SongInfo>)
    : RecyclerView.Adapter<SongRVAdapter.ViewHolder>(), SectionIndexer{


    private var onItemClickListener: OnItemClickListener? = null
    private var mRefreshHeader: IRefreshHeader? = null
    /**
     * 点击事件监听回调
     */
    interface OnItemClickListener {
        fun onOpenMenuClick(position: Int)
        fun onItemClick(position: Int)
        fun updateLoveStatus(musicId: Long)
    }

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener?) {
        this.onItemClickListener = onItemClickListener
    }

    class ViewHolder(itemView:View, private val viewType:Int): RecyclerView.ViewHolder(itemView) {
        fun bind(musicInfoList:ArrayList<SongInfo>,  onItemClickListener:OnItemClickListener){
            if(viewType == Constant.TYPE_REFRESH_HEADER){
                return
            }
            val musicInfo = musicInfoList[adapterPosition]
            val musicId = PlayMusicManager.getMusicManager()?.nowPlayingSong?.music_id
            with(itemView){
                    local_item_name.text = musicInfo.musicName
                    local_item_index.text = (adapterPosition).toString()
                    local_music_singer.text = musicInfo.musicSinger
                    updateLoveStatus(musicInfo)
                    initTheme()
                    //设置当前正在播放的itemView
                    if (musicInfo.music_id == musicId) {
                        local_item_name.setTextColor(ContextCompat.getColor(context,R.color.colorAccent))
                        local_music_singer.setTextColor(ContextCompat.getColor(context,R.color.colorAccent))
                        local_item_iv.visibility = View.VISIBLE
                        local_item_index.visibility = View.INVISIBLE
                    }else{
                        local_item_name.setTextColor(ContextCompat.getColor(context,R.color.white))
                        local_music_singer.setTextColor(ContextCompat.getColor(context,R.color.white))
                        local_item_iv.visibility = View.INVISIBLE
                        local_item_index.visibility = View.VISIBLE
                    }
                    local_item_love_fl.setOnClickListener {
                        musicInfo.music_id?.let { music_id -> onItemClickListener.updateLoveStatus(music_id) }
                        if (musicInfo.musicLove == Constant.NULL_LOVE_STATUS) {
                            local_item_love.setBackgroundResource(R.drawable.ic_love_highlight)
                        } else {
                            local_item_love.setBackgroundResource(R.drawable.ic_no_love)
                        }
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

        private fun View.updateLoveStatus(musicInfo: SongInfo) {
            if (musicInfo.musicLove == Constant.NULL_LOVE_STATUS) {
                local_item_love.setBackgroundResource(R.drawable.ic_no_love)
            } else {
                local_item_love.setBackgroundResource(R.drawable.ic_love_highlight)
            }
        }

        private fun initTheme() {
        }
    }

    /**
     * 可以根据不同的position可以返回不同的类型;
     */
    override fun getItemViewType(position: Int): Int {
        return if (position == 0) {
            Constant.TYPE_REFRESH_HEADER
        } else Constant.TYPE_NORMAL
    }

    fun setRefreshHeader(header: IRefreshHeader?) {
        if (header != null) {
            mRefreshHeader = header
        }
    }

    /**
     * 用来创建新View;
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        if (viewType == Constant.TYPE_REFRESH_HEADER){
            return ViewHolder(mRefreshHeader?.getHeaderView()!!,viewType)
        }
        return ViewHolder(
            LayoutInflater.from(context).inflate(R.layout.item_local_music, parent, false),viewType)
    }

    /**
     * 获得需要显示数据的总数。
     */
    override fun getItemCount(): Int {
        return musicInfoList.size
    }

    fun setNewData(musicList:ArrayList<SongInfo>){
        musicInfoList = musicList
        notifyDataSetChanged()
    }

    /**
     * 将数据与视图绑定;
     */
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.bind(musicInfoList,onItemClickListener!!)
    }

    override fun getSections(): Array<Any?> {
        return arrayOfNulls(0)
    }

    override fun getSectionForPosition(position: Int): Int {
        return musicInfoList[position].musicFirstLetter!![0].toInt()
    }
    /**
     * 根据分类的首字母的Char ascii值获取其第一次出现该首字母的item的位置
     */
    override fun getPositionForSection(sectionIndex: Int): Int {
        Log.i(TAG, "getPositionForSection: section = $sectionIndex")
        for (i in 0 until itemCount) {
            val firstChar = musicInfoList[i].musicFirstLetter!![0]
            if (firstChar.toInt() == sectionIndex) {
                return i
            }
        }
        return -1
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
        private val TAG = SongRVAdapter::class.java.name
    }

}