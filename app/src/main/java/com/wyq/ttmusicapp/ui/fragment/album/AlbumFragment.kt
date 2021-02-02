package com.wyq.ttmusicapp.ui.fragment.album

import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.wyq.ttmusicapp.R
import com.wyq.ttmusicapp.adapter.AlbumRVAdapter
import com.wyq.ttmusicapp.base.BaseFragment
import com.wyq.ttmusicapp.common.Constant
import com.wyq.ttmusicapp.entity.AlbumInfo
import com.wyq.ttmusicapp.ui.commonmusic.CommonMusicActivity
import kotlinx.android.synthetic.main.fragment_album.*


/**
 * Created by Roman on 2021/1/10
 */
class AlbumFragment: BaseFragment(),AlbumContract.View {

    private var presenter:AlbumContract.Presenter? = null
    private var albumInfoList:ArrayList<AlbumInfo>? = null
    private var albumAdapter: AlbumRVAdapter? = null

    override fun getLayout(): Int {
        return R.layout.fragment_album
    }

    override fun initData() {
        AlbumPresenter(this)
        presenter?.start()
    }

    override fun initViews() {

    }

    override fun updateListView() {

    }

    override fun showBottomMenu() {

    }

    override fun initAlbumData(albumList: ArrayList<AlbumInfo>) {
        albumInfoList = albumList
        initRecycleView()
    }

    /**
     * 初始化列表
     */
    private fun initRecycleView() {
        albumAdapter = AlbumRVAdapter(albumInfoList!!)
        val layoutManager =
            GridLayoutManager(context, 2, LinearLayoutManager.VERTICAL, false)
        album_recycler_view.layoutManager = layoutManager
        val spacingInPixels = resources.getDimensionPixelSize(R.dimen.spacing)
        album_recycler_view.addItemDecoration(SpacesItemDecoration(spacingInPixels,2,true))
        album_recycler_view.adapter = albumAdapter

        albumAdapter?.setOnItemClickListener(object : AlbumRVAdapter.OnItemClickListener{
            override fun onOpenMenuClick(position: Int) {

            }

            override fun onItemClick(position: Int) {
                CommonMusicActivity.startActivity(context!!,
                    albumInfoList!![position].albumId.toString(),
                    Constant.MUSIC_FROM_ALBUM)
            }
        })
    }

    override fun setPresenter(presenter: AlbumContract.Presenter) {
        this.presenter = presenter
    }

}