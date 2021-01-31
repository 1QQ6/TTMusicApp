package com.wyq.ttmusicapp.ui.fragment.singer

import androidx.recyclerview.widget.LinearLayoutManager
import com.wyq.ttmusicapp.R
import com.wyq.ttmusicapp.adapter.SingerRecycleViewAdapter
import com.wyq.ttmusicapp.base.BaseFragment
import com.wyq.ttmusicapp.entity.SingerInfo
import kotlinx.android.synthetic.main.fragment_singer.*
import kotlinx.android.synthetic.main.fragment_song.*

/**
 * Created by Roman on 2021/1/10
 */
class SingerFragment: BaseFragment(),SingerContract.View {

    private var presenter:SingerContract.Presenter? = null

    private var singerInfoList:ArrayList<SingerInfo>? = null
    private var singerAdapter:SingerRecycleViewAdapter? = null

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
        singerAdapter = SingerRecycleViewAdapter(singerInfoList!!)
        val linearLayoutManager = LinearLayoutManager(context)
        linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
        singer_recycler_view.layoutManager = linearLayoutManager
        singer_recycler_view.adapter = singerAdapter

        singerAdapter?.setOnItemClickListener(object :SingerRecycleViewAdapter.OnItemClickListener{
            override fun onOpenMenuClick(position: Int) {

            }

            override fun onItemClick(position: Int) {

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
    override fun getSingersData(singerList: ArrayList<SingerInfo>) {
        singerInfoList = singerList
        initRecycleView()
    }

    override fun setPresenter(presenter: SingerContract.Presenter) {
        this.presenter = presenter
    }
}