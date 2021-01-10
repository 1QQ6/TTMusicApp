package com.wyq.ttmusicapp.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

/**
 * @author Roman
 * @des
 * @version $
 * @updateAuthor $
 * @updateDes
 */
 abstract class BaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       if (getLayout() != 0) {
          setContentView(getLayout())
       }
       initData()
       initViews()
       setupToolbar()
    }
    /**
     * 获取布局文件ID
     *
     * @return
     */
    abstract fun getLayout(): Int

    /**
     * 初始化数据
     */
    abstract fun initData()

    /**
     * 初始化view
     */
    abstract fun initViews()

    /**
     * 设置toolbar
     */
    abstract fun setupToolbar()
}