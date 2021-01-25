package com.wyq.ttmusicapp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.core.content.ContextCompat
import com.wyq.ttmusicapp.common.Constant
import com.wyq.ttmusicapp.ui.fragment.WorkFragment
import com.wyq.ttmusicapp.ui.me.MeFragment
import com.wyq.ttmusicapp.ui.playmusicbar.PlayBarBaseActivity
import com.wyq.ttmusicapp.ui.scanmusic.ScanActivity
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : PlayBarBaseActivity(),HomeContract.View {

    private var mPresenter: HomeContract.Presenter? = null

    /**
     * 当前页面tab索引
     */
    private var currentTab = 1

    /**
     * 上一个点击的页面tab值
     */
    private var lastTab = -1

    /**
     * 主页的fragment：里面有四个fragment
     */
    private var homeFragment: HomeFragment? = null

    /**
     *
     */
    private var workFragment: WorkFragment? = null

    /**
     * 我的页面
     */
    private var meFragment:MeFragment? = null

    companion object{
        fun startActivity(ctx: Context){
            val i = Intent(ctx, HomeActivity::class.java)
            ctx.startActivity(i)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    private fun setupFragment() {
        val transaction =
            supportFragmentManager.beginTransaction()
        homeFragment = HomeFragment()
        transaction.add(R.id.fragment_layout, homeFragment!!)
        try {
            transaction.commitAllowingStateLoss()
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        onChecked()
    }

    private fun checkBottomTabView(currentTab: Int) {
        tv_main.setTextColor(-0x6d6d6e)
        tv_workout.setTextColor(-0x6d6d6e)
        tv_me.setTextColor(-0x6d6d6e)
        iv_main.setImageResource(R.drawable.ic_homepage)
        iv_workout.setImageResource(R.drawable.ic_discover)
        iv_me.setImageResource(R.drawable.ic_me)
        when (currentTab) {
            Constant.TAB_MAIN -> {
                tv_main.setTextColor(ContextCompat.getColor(this,R.color.colorAccent))
                iv_main.setImageResource(R.drawable.ic_homepage_highlight)
            }
            Constant.TAB_WORK -> {
                tv_workout.setTextColor(ContextCompat.getColor(this,R.color.colorAccent))
                iv_workout.setImageResource(R.drawable.ic_discover_highlight)
            }
            else -> {
                tv_me.setTextColor(ContextCompat.getColor(this,R.color.colorAccent))
                iv_me.setImageResource(R.drawable.ic_me_highlight)
            }
        }
    }

    private fun initBottomTabAction() {
        ll_tab_main.setOnClickListener{ setTab(Constant.TAB_MAIN) }
        ll_tab_workout.setOnClickListener{ setTab(Constant.TAB_WORK) }
        ll_tab_me.setOnClickListener{ setTab(Constant.TAB_ME) }
    }

    private fun setTab(tab: Int) {
        currentTab = tab
        onChecked()
    }

    private fun onChecked() {
        if (lastTab == currentTab) {
            if (currentTab == Constant.TAB_MAIN) {
                //mainFragment.scrollTop()
            }
            return
        }
        val transaction =
            supportFragmentManager.beginTransaction()
        if (homeFragment != null) {
            transaction.hide(homeFragment!!)
        }
        if (workFragment != null) {
            transaction.hide(workFragment!!)
        }
        if (meFragment != null) {
            transaction.hide(meFragment!!)
        }
        when (currentTab) {
            Constant.TAB_MAIN -> {
                if (homeFragment == null) {
                    homeFragment = HomeFragment()
                    transaction.add(R.id.fragment_layout, homeFragment!!)
                } else {
                    transaction.show(homeFragment!!)
                    invalidateOptionsMenu()
                }
                setupToolbar()
                checkBottomTabView(Constant.TAB_MAIN)
            }
            Constant.TAB_WORK -> {
                if (workFragment == null) {
                    workFragment = WorkFragment()
                    transaction.add(R.id.fragment_layout, workFragment!!)
                } else {
                    transaction.show(workFragment!!)
                    invalidateOptionsMenu()
                }
                setupToolbar()
                checkBottomTabView(Constant.TAB_WORK)
            }
            Constant.TAB_ME -> {
                if (meFragment == null) {
                    meFragment = MeFragment()
                    transaction.add(R.id.fragment_layout, meFragment!!)
                } else {
                    transaction.show(meFragment!!)
                    invalidateOptionsMenu()
                }
                setupToolbar()
                checkBottomTabView(Constant.TAB_ME)
            }
        }
        try {
            transaction.commitAllowingStateLoss()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        invalidateOptionsMenu()
        lastTab = currentTab
        /*
        if (current_tab == Constant.TAB_REPORT) {
            reportFragment.update()
        }*/
    }

    override fun getLayout(): Int {
        return R.layout.activity_home
    }

    override fun initData() {
        HomePresenter(this,this)
        mPresenter!!.bindMusicController()
    }

    override fun initViews() {
        super.initViews()
        setupFragment()
        checkBottomTabView(currentTab)
        initBottomTabAction()
    }

    override fun setupToolbar() {
        setSupportActionBar(local_music_toolbar)
        supportActionBar?.title = getString(R.string.local_music)
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.local_music_menu,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.scan_local_menu) {
            val intent = Intent(this, ScanActivity::class.java)
            startActivity(intent)
        }
        return true
    }

    override fun onDestroy() {
        super.onDestroy()
        mPresenter!!.unbindMusicController()
    }

    override fun setPresenter(presenter: HomeContract.Presenter) {
        mPresenter = presenter
    }
}