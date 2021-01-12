package com.wyq.ttmusicapp.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.wyq.ttmusicapp.R
import com.wyq.ttmusicapp.adpter.SongRecyclerViewAdapter
import com.wyq.ttmusicapp.base.BaseFragment
import com.wyq.ttmusicapp.dao.DatabaseManager
import com.wyq.ttmusicapp.entity.SongInfo
import kotlinx.android.synthetic.main.fragment_song.*

/**
 * Created by Roman on 2021/1/10
 */
class SongFragment: BaseFragment() {
    var songRecyclerViewAdapter:SongRecyclerViewAdapter? = null
    private var musicInfoList: ArrayList<SongInfo> = ArrayList()

    private var dbManager:DatabaseManager? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dbManager = DatabaseManager.getInstance(context)
        updateView()
    }

    override fun getLayout(): Int {
        return R.layout.fragment_song
    }

    override fun initData() {
        musicInfoList.sortBy { it.musicFirstLetter }
    }

    override fun initViews() {
        initMusicList()
    }

    override fun onResume() {
        super.onResume()
    }

    private fun updateView() {
        musicInfoList = dbManager!!.getAllMusicFromMusicTable()
    }


    private fun initMusicList() {
        songRecyclerViewAdapter = SongRecyclerViewAdapter(musicInfoList)
        val linearLayoutManager = LinearLayoutManager(context)
        linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
        local_recycler_view.layoutManager = linearLayoutManager
        local_recycler_view.adapter = songRecyclerViewAdapter

        songRecyclerViewAdapter!!.setOnItemClickListener(object :SongRecyclerViewAdapter.OnItemClickListener{
            override fun onOpenMenuClick(position: Int) {

            }

            override fun onDeleteMenuClick(content: View?, position: Int) {

            }

            override fun onItemClick(position: Int) {

            }
        })
    }

}