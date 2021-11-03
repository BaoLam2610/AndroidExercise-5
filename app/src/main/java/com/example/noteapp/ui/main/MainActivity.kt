package com.example.noteapp.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.example.noteapp.R
import com.example.noteapp.adapter.ViewPagerAdapter
import com.example.noteapp.ui.main.fragments.CalendarFragment
import com.example.noteapp.ui.note.NoteActivity
import com.example.noteapp.until.Constant
import kotlinx.android.synthetic.main.activity_main.*
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class MainActivity : AppCompatActivity() {

    val dateFormatter = DateTimeFormatter.ofPattern("MMMM yyyy")
    private lateinit var viewPagerAdapter: ViewPagerAdapter
    private var currentDate: LocalDate = LocalDate.now()
    private val fragmentList = mutableListOf(
        CalendarFragment.newInstance(currentDate.minusMonths(1)),
        CalendarFragment.newInstance(currentDate),
        CalendarFragment.newInstance(currentDate.plusMonths(1)),
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupActionBar()

        tvMonthYear.text = currentDate.format(dateFormatter)
        viewPagerAdapter = ViewPagerAdapter(fragmentList, supportFragmentManager)
        viewPager.adapter = viewPagerAdapter
        viewPager.setCurrentItem(1, false)

        var focusPage = 0
        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int,
            ) {

            }

            override fun onPageSelected(position: Int) {
                focusPage = position
            }

            override fun onPageScrollStateChanged(state: Int) {
                if (state == ViewPager.SCROLL_STATE_IDLE) {
                    if (focusPage < 1) {
                        currentDate = currentDate.minusMonths(1)
                    } else if (focusPage > 1) {
                        currentDate = currentDate.plusMonths(1)
                    }
                    tvMonthYear.text = currentDate.format(dateFormatter)
                    viewPagerAdapter.setCalendar(currentDate)
                    viewPager.setCurrentItem(1, false)
                }
            }
        })
    }

    private fun setupActionBar(){
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Calendar"
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.calendar_toolbar, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.acNoteList ->{
                Intent(this, NoteActivity::class.java).also {
                    it.putExtra(Constant.TYPE_ACTIVITY, "list")
                    startActivity(it)
                }
            }
        }
        return true
    }
}