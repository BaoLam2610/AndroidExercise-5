package com.example.noteapp.ui.note

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.noteapp.R
import com.example.noteapp.ui.note.fragments.NoteFragment
import com.example.noteapp.ui.note.fragments.NoteListFragment
import com.example.noteapp.until.Constant.DAY
import com.example.noteapp.until.Constant.MONTH
import com.example.noteapp.until.Constant.TYPE_ACTIVITY
import com.example.noteapp.until.Constant.YEAR

class NoteActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note)

        if (intent.hasExtra(TYPE_ACTIVITY)) {
            when (intent.getStringExtra(TYPE_ACTIVITY)) {
                "note" -> setFragment(
                    NoteFragment.newInstance(
                        intent.getStringExtra(DAY)!!,
                        intent.getStringExtra(MONTH)!!,
                        intent.getStringExtra(YEAR)!!
                    )
                )
                "list" -> setFragment(NoteListFragment.newInstance())
            }
        }

    }

    private fun setFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.frameLayout, fragment)
            .addToBackStack(null)
            .commit()
    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount == 1)
            finish()
        else
            super.onBackPressed()
    }
}