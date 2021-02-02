package com.wyq.ttmusicapp.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

/**
 * Created by Roman on 2021/1/10
 */
class LocalRVAdapter(fm: FragmentManager, behavior: Int, private val titleList: Array<String>, private val fragmentsList:ArrayList<Fragment>) :
    FragmentPagerAdapter(fm, behavior) {

    override fun getPageTitle(position: Int): CharSequence? {
        return titleList[position]
    }

    override fun getItem(position: Int): Fragment {
        return fragmentsList[position]
    }

    override fun getCount(): Int {
        return fragmentsList.size
    }
}