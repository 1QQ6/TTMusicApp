package com.wyq.ttmusicapp.ui.fragment.singer

import androidx.recyclerview.widget.LinearLayoutManager
import com.wyq.ttmusicapp.R
import com.wyq.ttmusicapp.adapter.SingerRVAdapter
import com.wyq.ttmusicapp.base.BaseFragment
import com.wyq.ttmusicapp.common.Constant
import com.wyq.ttmusicapp.entity.Artist
import com.wyq.ttmusicapp.ui.commonmusic.CommonMusicActivity
import kotlinx.android.synthetic.main.fragment_singer.*

/**
 * Created by Roman on 2021/1/10
 */
class SingerFragment: BaseFragment(),SingerContract.View {

    private var presenter:SingerContract.Presenter? = null

    private var artistList:ArrayList<Artist>? = null
    private var singerAdapter:SingerRVAdapter? = null

    override fun getLayout(): Int {
        return R.layout.fragment_singer
    }

    override fun initData() {
        SingerPresenter(this)
        presenter?.start()
    }

    override fun initViews() {

    }

    /**
     * 初始化列表
     */
    private fun initRecycleView() {
        singerAdapter = SingerRVAdapter(artistList!!)
        val linearLayoutManager = LinearLayoutManager(context)
        linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
        singer_recycler_view.layoutManager = linearLayoutManager
        singer_recycler_view.adapter = singerAdapter

        singerAdapter?.setOnItemClickListener(object :SingerRVAdapter.OnItemClickListener{
            override fun onOpenMenuClick(position: Int) {

            }

            override fun onItemClick(position: Int) {
                CommonMusicActivity.startActivity(context!!,
                    artistList!![position].singerName!!,Constant.MUSIC_FROM_SINGER)
            }
        })
    }

    /**
     * 更新列表
     */
    override fun updateListView() {

    }

    /**
     * 展示底部弹窗
     */
    override fun showBottomMenu() {

    }

    /**
     * 获取列表数据
     */
    override fun getSingersData(singerList: ArrayList<Artist>) {
        artistList = singerList
        initRecycleView()
    }

    override fun setPresenter(presenter: SingerContract.Presenter) {
        this.presenter = presenter
    }
}