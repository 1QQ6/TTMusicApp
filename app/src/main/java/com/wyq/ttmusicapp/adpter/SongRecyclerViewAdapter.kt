package com.wyq.ttmusicapp.adpter

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SectionIndexer
import androidx.recyclerview.widget.RecyclerView
import com.wyq.ttmusicapp.R
import com.wyq.ttmusicapp.common.Constant
import com.wyq.ttmusicapp.common.MyApplication.Companion.context
import com.wyq.ttmusicapp.dao.DatabaseManager
import com.wyq.ttmusicapp.entity.SongInfo
import com.wyq.ttmusicapp.utils.PlayMusicSPUtil
import kotlinx.android.synthetic.main.local_music_item.view.*

/**
 * Created by Roman on 2021/1/12
 */
class SongRecyclerViewAdapter(private val musicInfoList:ArrayList<SongInfo>)
    : RecyclerView.Adapter<SongRecyclerViewAdapter.ViewHolder>(), SectionIndexer{

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
        fun onDeleteMenuClick(content: View?, position: Int)
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener?) {
        this.onItemClickListener = onItemClickListener
    }

    class ViewHolder(itemView:View): RecyclerView.ViewHolder(itemView) {
        fun bind(musicInfoList:ArrayList<SongInfo>, dbManager:DatabaseManager, onItemClickListener:OnItemClickListener){
            val musicInfo = musicInfoList[adapterPosition]

            with(itemView){
                local_music_name.text = musicInfo.musicName
                local_index.text = (adapterPosition+1).toString()
                local_music_singer.text = musicInfo.musicSinger
                initTheme()
                //TODO 设置当前正在播放的itemView
                if (musicInfo.music_id == PlayMusicSPUtil.getIntShared(Constant.KEY_MUSIC_ID)) {

                }else{

                }
                //点击播放
                local_music_item_ll.setOnClickListener {
                    Log.i(TAG, "onClick: 播放 " + musicInfo.musicName)
                    onItemClickListener.onItemClick(adapterPosition)
                }
                //点击菜单
                local_music_item_never_menu.setOnClickListener {
                    onItemClickListener.onOpenMenuClick(adapterPosition)
                }
                //删除音乐
                swip_delete_menu_btn.setOnClickListener {
                    onItemClickListener.onDeleteMenuClick(swipemenu_layout, adapterPosition)
                }
            }
        }

        private fun initTheme() {
            //获取主题颜色
            //        int defaultColor = 0xFFFA7298;
            //        int[] attrsArray = {R.attr.colorAccent};
            //        TypedArray typedArray = context.obtainStyledAttributes(attrsArray);
            //        int appbg = typedArray.getColor(0, defaultColor);
            //        typedArray.recycle();
            //val appbg = CustomAttrValueUtil.getAttrColorValue(R.attr.colorAccent, -0x58d68, context)
            //val defaultTvColor =
            //    CustomAttrValueUtil.getAttrColorValue(R.attr.text_color, R.color.grey700, context)

            //        int[] attrs = {R.attr.text_color};
            //        TypedArray typed = context.obtainStyledAttributes(attrs);
            //        int defaultTvColor = typed.getColor(0, context.getResources().getColor(R.color.grey700));
            //        typedArray.recycle();
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(context).inflate(R.layout.local_music_item, parent, false))
    }

    override fun getItemCount(): Int {
        return musicInfoList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.bind(musicInfoList,dbManager!!,onItemClickListener!!)

        val musicInfo = musicInfoList[position]
        val section = getSectionForPosition(position)
                /*val firstPosition = getPositionForSection(section)
                //        Log.i(TAG, "onBindViewHolder: section = "+section + "  firstPosition = "+firstPosition);
                if (firstPosition == holder.adapterPosition) {
                    holder.letterIndex.visibility = View.VISIBLE
                    holder.letterIndex.text = "" + musicInfo.firstLetter
                } else {
                    holder.letterIndex.visibility = View.GONE
                }*/

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
        private val TAG = SongRecyclerViewAdapter::class.java.name
    }

}