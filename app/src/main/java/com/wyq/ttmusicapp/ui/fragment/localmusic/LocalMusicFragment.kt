package com.wyq.ttmusicapp.ui.fragment.localmusic

import android.app.AlertDialog
import android.content.*
import android.view.LayoutInflater
import android.view.View
import android.widget.CheckBox
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.wyq.ttmusicapp.R
import com.wyq.ttmusicapp.adapter.SongRVAdapter
import com.wyq.ttmusicapp.base.BaseFragment
import com.wyq.ttmusicapp.common.Constant
import com.wyq.ttmusicapp.core.PlayMusicManager
import com.wyq.ttmusicapp.entity.SongInfo
import com.wyq.ttmusicapp.interfaces.IRefreshListener
import com.wyq.ttmusicapp.ui.commonmusic.CommonMusicActivity
import com.wyq.ttmusicapp.ui.playmusic.PlayMusicActivity
import com.wyq.ttmusicapp.utils.SPUtil
import kotlinx.android.synthetic.main.fragment_song.*



/**
 * Created by Roman on 2021/1/10
 */
class LocalMusicFragment: BaseFragment(), LocalMusicContract.View {
    var songRVAdapter:SongRVAdapter? = null
    val FINISH = 123
    private var musicInfoList: ArrayList<SongInfo> = ArrayList()
    private var presenter: LocalMusicContract.Presenter? = null
    var bottomSheetDialog :BottomSheetDialog? = null

    /**
     * 切换音乐时，更新当前播放列表音乐选中状态
     */
    private val receiver = object : BroadcastReceiver(){
        override fun onReceive(context: Context?, intent: Intent?) {
            when(intent?.action){
                Constant.PLAY_MUSIC_VIEW_UPDATE->{
                    songRVAdapter?.notifyDataSetChanged()
                }
                else->{

                }
            }
        }
    }

    override fun getLayout(): Int {
        return R.layout.fragment_song
    }

    override fun initData() {
        musicInfoList.add(SongInfo())
        musicInfoList.addAll(arguments?.getParcelableArrayList("musicList")!!)
        LocalMusicPresenter(this)
        initReceiver()
        //initDefaultPlayModeView()
    }

    override fun initViews() {
        initMusicList()
    }
    private fun initReceiver() {
        val intentFilter = IntentFilter()
        intentFilter.addAction(Constant.PLAY_MUSIC_VIEW_UPDATE)
        context!!.registerReceiver(receiver,intentFilter)
    }

    /**
     * 初始化音乐模式UI
     */
/*    private fun initDefaultPlayModeView() {
        when (SPUtil.getIntShared(Constant.KEY_PLAY_MODE)) {
            Constant.PLAY_MODE_SEQUENCE -> local_music_play_mode_tv.text = getString(R.string.play_mode_sequence)
            Constant.PLAY_MODE_RANDOM -> local_music_play_mode_tv.text = getString(R.string.play_mode_random)
            Constant.PLAY_MODE_SINGLE_REPEAT -> local_music_play_mode_tv.text = getString(R.string.play_mode_repeat)
        }
        initPlayMode()
    }*/

    /**
     * 初始化音乐模式
     */
    private fun initPlayMode() {
        var playMode: Int = SPUtil.getIntShared(Constant.KEY_PLAY_MODE)
        if (playMode == -1) {
            playMode = 0
        }
    }

    /**
     * 设置音乐模式
     * 随机
     * 单曲
     * 顺序
     */
   /* private fun setMusicMode() {
            when (SPUtil.getIntShared(Constant.KEY_PLAY_MODE)) {
                Constant.PLAY_MODE_SEQUENCE -> {
                    local_music_play_mode_tv.text = getString(R.string.play_mode_random)
                    SPUtil.setPlayMusicModeShared(Constant.PLAY_MODE_RANDOM)
                }
                Constant.PLAY_MODE_RANDOM -> {
                    local_music_play_mode_tv.text = getString(R.string.play_mode_repeat)
                    SPUtil.setPlayMusicModeShared(Constant.PLAY_MODE_SINGLE_REPEAT)
                }
                Constant.PLAY_MODE_SINGLE_REPEAT -> {
                    local_music_play_mode_tv.text = getString(R.string.play_mode_sequence)
                    SPUtil.setPlayMusicModeShared(Constant.PLAY_MODE_SEQUENCE)
                }
            }
    }*/

    private fun initMusicList() {
        songRVAdapter = SongRVAdapter(musicInfoList)
        val linearLayoutManager = LinearLayoutManager(context)
        linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
        local_recycler_view.layoutManager = linearLayoutManager
        local_recycler_view.adapter = songRVAdapter
        local_recycler_view.setOnRefreshListener(object :IRefreshListener{
            override fun onRefresh() {
                presenter?.requestData()
            }
        })
        songRVAdapter!!.setOnItemClickListener(object :SongRVAdapter.OnItemClickListener{
            override fun onOpenMenuClick(position: Int) {
                showBottomMenu(position)
            }
            override fun onItemClick(position: Int) {
                val isPlaying = PlayMusicManager.getMusicManager()!!.isPlaying
                val nowPlayingIndex = PlayMusicManager.getMusicManager()!!.nowPlayingIndex
                //如果当前点击的歌曲正在播放并且和当前播放的index相等，那么跳转到播放主页
                if(isPlaying && nowPlayingIndex == position){
                    PlayMusicActivity.startActivity(context!!)
                    return
                }
                PlayMusicManager.getMusicManager()!!.prepareAndPlay(position,musicInfoList)
                songRVAdapter?.notifyDataSetChanged()
            }
            override fun updateLoveStatus(musicId:Long) {
                presenter?.updateLoveStatus(musicId)
            }
        })
    }

    private fun setMenuItemClick(
        view: View,
        songInfo: SongInfo
    ) {
        val nextSong = view.findViewById<LinearLayout>(R.id.next_song_ll)
        val songNameTV = view.findViewById<TextView>(R.id.song_name_bottom_dialog)
        val songAlbumTV = view.findViewById<TextView>(R.id.song_album_bottom_dialog)
        val queryAlbum = view.findViewById<LinearLayout>(R.id.query_album_ll)
        val querySinger = view.findViewById<LinearLayout>(R.id.query_singer_ll)
        val deleteSongs = view.findViewById<LinearLayout>(R.id.delete_songs_ll)
        val shareSongs = view.findViewById<LinearLayout>(R.id.share_songs_ll)

        songNameTV.text = songInfo?.musicName
        songAlbumTV.text = songInfo?.musicAlbum

        nextSong.setOnClickListener {
            PlayMusicManager.getMusicManager()!!.nextSong()
            bottomSheetDialog?.dismiss()
        }
        queryAlbum.setOnClickListener {
            songInfo.musicAlbumId?.let { musicAlbum ->
                CommonMusicActivity.startActivity(
                    context!!,
                    musicAlbum.toString(), Constant.MUSIC_FROM_ALBUM
                )
            }
            bottomSheetDialog?.dismiss()
        }
        querySinger.setOnClickListener {
            songInfo.musicSinger?.let { musicSinger ->
                CommonMusicActivity.startActivity(
                    context!!,
                    musicSinger, Constant.MUSIC_FROM_SINGER
                )
            }
            bottomSheetDialog?.dismiss()
        }
        deleteSongs.setOnClickListener {
            val inflater = LayoutInflater.from(context)
            val view: View = inflater.inflate(R.layout.dialog_delete_file, null)
            val deleteFileCheckBox = view.findViewById<CheckBox>(R.id.dialog_delete_cb)
            val builder: AlertDialog.Builder = AlertDialog.Builder(context)
            builder.create()
            builder.setView(view)
            builder.setPositiveButton(getString(R.string.delete_music)
            ) { dialog, which ->
                if (deleteFileCheckBox.isChecked){
                    songInfo.music_id?.let { music_id -> presenter?.deleteMusic(context!!, music_id) }
                }
                dialog.dismiss()
            }
            builder.setNegativeButton(getString(R.string.cancle)
            ) { dialog, which ->
                dialog.dismiss()
            }
            builder.show()
        }
        shareSongs.setOnClickListener {
            songInfo.music_id?.let { musicId-> presenter?.sharedMusic(musicId) }
        }
    }

    override fun updateListView(musicList: ArrayList<SongInfo>) {
        musicInfoList.clear()
        musicInfoList.add(SongInfo())
        musicInfoList.addAll(musicList)
        musicInfoList.sortBy { it.musicFirstLetter }
        songRVAdapter?.setNewData(musicInfoList)
        local_recycler_view.refreshComplete()
        bottomSheetDialog?.dismiss()
    }

    override fun showBottomMenu(position: Int) {
        val songInfo = musicInfoList[position]
        bottomSheetDialog = BottomSheetDialog(context!!,R.style.BottomSheetDialog)
        val view: View = layoutInflater.inflate(R.layout.dialog_bottom_list, null)
        bottomSheetDialog?.setContentView(view)
        bottomSheetDialog?.show()
        setMenuItemClick(view,songInfo)
    }

    override fun setPresenter(presenter: LocalMusicContract.Presenter) {
        this.presenter = presenter
    }

    override fun onDestroy() {
        super.onDestroy()
        context!!.unregisterReceiver(receiver)
    }
}