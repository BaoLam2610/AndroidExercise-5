package com.example.noteapp.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.view.*
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.noteapp.R
import com.example.noteapp.event.INoteModify
import com.example.noteapp.event.IOnClickItem
import com.example.noteapp.presenter.NotePresenter
import kotlinx.android.synthetic.main.item_day_of_month.view.*
import java.time.LocalDate
import java.time.YearMonth


class CalendarAdapter(
    private var mContext: Context,
    var dayOfMonth: MutableList<Int>,
    var date: LocalDate,
) : RecyclerView.Adapter<CalendarAdapter.CalendarViewHolder>(), INoteModify.Item {

    private lateinit var notePresenter: NotePresenter
    private var iOnDoubleClickItem: IOnClickItem.ICalendar? = null
    private var monthLength = YearMonth.from(date).lengthOfMonth()

    fun setIOnClickItem(iOnClickItem: IOnClickItem.ICalendar) {
        this.iOnDoubleClickItem = iOnClickItem
    }

    inner class CalendarViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CalendarViewHolder {
        return CalendarViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_day_of_month,
                parent,
                false
            )
        )
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onBindViewHolder(
        holder: CalendarViewHolder,
        position: Int,
    ) {
        notePresenter = NotePresenter(mContext, this)
        holder.itemView.apply {
            tvDay.text = dayOfMonth[position].toString()
            changeTextColorToDefault(position, tvDay)
            if (!check(position))
                notePresenter.checkItem(position, date, tvDay)
            val gestureDetector =
                GestureDetector(mContext, object : GestureDetector.SimpleOnGestureListener() {
                    override fun onDoubleTap(e: MotionEvent?): Boolean {
                        iOnDoubleClickItem?.onDoubleClickItem(tvDay.text.toString().toInt())
                        return true
                    }
                })
            setOnTouchListener { _, motionEvent ->
                gestureDetector.onTouchEvent(motionEvent)
                true
            }
        }
    }

    private fun checkDayBelongMonth(position: Int): Boolean {
        return if (dayOfMonth.indexOf(1) < dayOfMonth.lastIndexOf(monthLength))
            position >= dayOfMonth.indexOf(1) && position <= dayOfMonth.lastIndexOf(monthLength)
        else
            position <= dayOfMonth.lastIndexOf(monthLength)
    }

    private fun changeTextColorToDefault(position: Int, textView: TextView) {
        if (checkDayBelongMonth(position)) {
            textView.setTextColor(Color.BLACK)
            textView.setTypeface(null, Typeface.BOLD)
        } else
            textView.setTextColor(Color.GRAY)
    }

    private fun check(position: Int): Boolean {
        if (dayOfMonth.indexOf(1) < dayOfMonth.lastIndexOf(monthLength))
            if (position < dayOfMonth.indexOf(1) || position > dayOfMonth.lastIndexOf(monthLength))
                return true
            else
                if (position < dayOfMonth.lastIndexOf(monthLength))
                    return false
        return false
    }

    override fun getItemCount(): Int = dayOfMonth.size

    override fun onChangeBackground(position: Int, textView: TextView) {
        textView.setBackgroundResource(R.drawable.custom_selected_day)
    }

    override fun onNotChangeBackground() {

    }

}