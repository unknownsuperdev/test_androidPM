package com.fiction.me.appbase.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class BaseViewPagerAdapter(fragment: Fragment, private val pages: List<Fragment>) :
    FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = pages.size

    override fun createFragment(position: Int) = pages[position]
}