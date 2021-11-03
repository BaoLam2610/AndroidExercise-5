package com.example.noteapp.adapter

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.example.noteapp.ui.main.fragments.CalendarFragment
import java.time.LocalDate

class ViewPagerAdapter(
    var fragmentList: MutableList<CalendarFragment>,
    fm: FragmentManager,
    // behavior: Int
//fragmentActivity: FragmentActivity
) : FragmentStatePagerAdapter(fm) {
    override fun getCount(): Int = fragmentList.size

    override fun getItem(position: Int): Fragment {
        return fragmentList[position]
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun setCalendar(currentDate: LocalDate) {
        fragmentList[0] = CalendarFragment.newInstance(currentDate.minusMonths(1))
        fragmentList[1] = CalendarFragment.newInstance(currentDate)
        fragmentList[2] = CalendarFragment.newInstance(currentDate.plusMonths(1))
        notifyDataSetChanged()
    }

    override fun getItemPosition(`object`: Any): Int {
        return POSITION_NONE
    }
}