package com.example.noteapp.ui.main.fragments

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import com.example.noteapp.R
import com.example.noteapp.adapter.CalendarAdapter
import com.example.noteapp.event.IOnClickItem
import com.example.noteapp.ui.note.NoteActivity
import com.example.noteapp.until.Constant.DAY
import com.example.noteapp.until.Constant.MONTH
import com.example.noteapp.until.Constant.TYPE_ACTIVITY
import com.example.noteapp.until.Constant.YEAR
import kotlinx.android.synthetic.main.fragment_calendar.*
import java.time.LocalDate
import java.time.YearMonth

class CalendarFragment : Fragment(R.layout.fragment_calendar) {

    private var adapter: CalendarAdapter? = null
    lateinit var selectedDate: LocalDate
    var yearMonth: YearMonth? = null
    var daysInMonth: Int = 0

    companion object {
        fun newInstance(date: LocalDate): CalendarFragment {
            val args = Bundle()
            args.putSerializable("date", date)
            val fragment = CalendarFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onResume() {
        super.onResume()
        adapter?.notifyDataSetChanged()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        selectedDate = arguments?.getSerializable("date") as LocalDate

        yearMonth = YearMonth.from(selectedDate)
        daysInMonth = yearMonth!!.lengthOfMonth()
        adapter = CalendarAdapter(requireContext(), getDaysInMonth(selectedDate), selectedDate)
        rvDayOfMonth.adapter = adapter
        rvDayOfMonth.layoutManager = GridLayoutManager(requireContext(), 7)
//        rvDayOfMonth.addItemDecoration(DividerItemDecoration(
//            requireContext(), DividerItemDecoration.VERTICAL
//        ))

        adapter!!.setIOnClickItem(object : IOnClickItem.ICalendar {
            override fun onDoubleClickItem(day: Int) {
                Intent(requireContext(), NoteActivity::class.java).also {
                    it.putExtra(TYPE_ACTIVITY, "note")
                    it.putExtra(DAY, day)
                    it.putExtra(MONTH, selectedDate.month.value)
                    it.putExtra(YEAR, selectedDate.year)
                    startActivity(it)
                }
            }
        })
    }

    private fun getDaysInMonth(date: LocalDate): MutableList<Int> {
        var arr = mutableListOf<Int>()
        var yearMonth = YearMonth.from(date)
        var daysInMonth = yearMonth.lengthOfMonth()
        var daysInPreviousMonth = YearMonth.from(date).minusMonths(1).lengthOfMonth()
        var firstOfMonth = selectedDate.withDayOfMonth(1)
        var dayOfWeek = firstOfMonth.dayOfWeek.value
        var dayOfNextWeek = 1
        if (dayOfWeek != 7 && dayOfWeek >= 0)
            for (i in 1..42) {
                when {
                    i <= dayOfWeek -> {
                        arr.add(daysInPreviousMonth - dayOfWeek + i)
                    }
                    i - dayOfWeek <= daysInMonth -> {
                        arr.add(i - dayOfWeek)
                    }
                    i - dayOfWeek > daysInMonth -> {
                        arr.add(dayOfNextWeek)
                        dayOfNextWeek++
                    }

                }
            }
        else if (dayOfWeek == 7)
            for (i in 1..(42)) {
                when {
                    i <= daysInMonth -> {
                        arr.add(i)
                    }
                    i > daysInMonth -> {
                        arr.add(dayOfNextWeek)
                        dayOfNextWeek++
                    }
                }
            }
        else if (dayOfWeek < 0) {
            var d = 1
            for (i in 1..(42)) {
                when {
                    i <= 7 + dayOfWeek -> {
                        arr.add(daysInPreviousMonth - 7 + i - dayOfWeek)
                    }
                    i in 8 + dayOfWeek until (daysInMonth + 8 + dayOfWeek) -> {    // mon - i = 6
                        arr.add(d)
                        d++
                    }
                    else -> {
                        arr.add(dayOfNextWeek)
                        dayOfNextWeek++
                    }
                }
            }
        }
        return arr
    }
}