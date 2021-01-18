package com.wyq.ttmusicapp.base

import android.content.Context
/**
 *
 * @author Ezio
 * @date 2018/01/18
 */
open class ContextPresenter<T : BaseView<out BasePresenter>>(protected val context: Context,protected val view: T)