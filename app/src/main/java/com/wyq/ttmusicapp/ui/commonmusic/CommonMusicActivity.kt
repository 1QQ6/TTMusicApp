package com.wyq.ttmusicapp.ui.commonmusic

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.xubo.statusbarutils.StatusBarUtils
import com.wyq.ttmusicapp.R
import com.wyq.ttmusicapp.adapter.CommonRecyclerViewAdapter
import com.wyq.ttmusicapp.common.Constant
import com.wyq.ttmusicapp.core.PlayMusicManager
import com.wyq.ttmusicapp.entity.SongInfo
import com.wyq.ttmusicapp.ui.playmusic.PlayMusicActivity
import com.wyq.ttmusicapp.ui.playmusicbar.PlayBarBaseActivity
import com.wyq.ttmusicapp.utils.CoverLoader
import kotlinx.android.synthetic.main.activity_common.*

/**
 * Created by Roman on 2021/2/1
 */
class CommonMusicActivity:PlayBarBaseActivity(),CommonMusicContract.View{

    private var presenter:CommonMusicContract.Presenter? = null
    var songRecyclerViewAdapter: CommonRecyclerViewAdapter? = null
    private var musicInfoList: ArrayList<SongInfo> = ArrayList()
    private var commonInfo:String? = null
    private var from:String? = null

    private val receiver = object :BroadcastReceiver(){
        override fun onReceive(context: Context?, intent: Intent?) {
            when(intent?.action){
                Constant.PLAY_MUSIC_VIEW_UPDATE->{
                    updateListView()
                }
                else->{

                }
            }
        }
    }
    companion object{
        /**
         * 音乐组传过来歌手名字
         * 专辑组则穿过来专辑id
         */
        private const val COMMON_INFO = "common_info"

        /**
         * 来自哪个组
         */
        private const val COMMON_FROM = "from"



        fun startActivity(
            ctx: Context,
            commonInfo: String,
            from:String
        ){
            val intent = Intent(ctx, CommonMusicActivity::class.java)
            intent.putExtra(COMMON_INFO,commonInfo)
            intent.putExtra(COMMON_FROM,from)
            ctx.startActivity(intent)
        }
    }

    override fun getLayout(): Int {
        return R.layout.activity_common
    }


    override fun initData() {
        handleIntent()
        CommonMusicPresenter(this)
        presenter!!.loadData(commonInfo!!,from!!)
        initReceiver()
    }

    private fun initReceiver() {
        val intentFilter = IntentFilter()
        intentFilter.addAction(Constant.PLAY_MUSIC_VIEW_UPDATE)
        registerReceiver(receiver,intentFilter)
    }

    private fun handleIntent() {
        commonInfo = intent?.getStringExtra(COMMON_INFO)
        from = intent?.getStringExtra(COMMON_FROM)
    }

    override fun initViews() {
        super.initViews()
    }

    private fun initMusicList() {
        songRecyclerViewAdapter = CommonRecyclerViewAdapter(musicInfoList)
        val linearLayoutManager = LinearLayoutManager(this)
        linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
        common_music_rv.layoutManager = linearLayoutManager
        common_music_rv.adapter = songRecyclerViewAdapter

        songRecyclerViewAdapter!!.setOnItemClickListener(object :CommonRecyclerViewAdapter.OnItemClickListener{
            override fun onOpenMenuClick(position: Int) {

            }
            override fun onItemClick(position: Int) {
                val isPlaying = PlayMusicManager.getMusicManager()!!.isPlaying
                val nowPlayingIndex = PlayMusicManager.getMusicManager()!!.nowPlayingIndex
                //如果当前点击的歌曲正在播放并且和当前播放的index相等，那么跳转到播放主页
                if(isPlaying && nowPlayingIndex == position){
                    val intent = Intent(this@CommonMusicActivity, PlayMusicActivity::class.java)
                    startActivity(intent)
                    return
                }
                PlayMusicManager.getMusicManager()!!.prepareAndPlay(position,musicInfoList)

            }
        })
    }



    override fun setupToolbar() {
        StatusBarUtils.setStatusBarTransparenLight(this)
    }
    fun updateListView(){
        songRecyclerViewAdapter?.notifyDataSetChanged()
    }
    override fun initListView(musicInfoList:ArrayList<SongInfo>) {
        this.musicInfoList = musicInfoList
        initMusicList()
        initHead()
    }

    /**
     * 加载顶部image
     */
    private fun initHead() {
        CoverLoader.loadBitmap(this, musicInfoList[0].coverUrl,R.drawable.default_cover) {
            common_head_iv.setImageBitmap(it)
        }
    }

    override fun showBottomMenu() {

    }

    override fun setPresenter(presenter: CommonMusicContract.Presenter) {
        this.presenter = presenter
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(receiver)
    }
}