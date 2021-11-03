package com.example.noteapp.ui.note.fragments

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.noteapp.R
import com.example.noteapp.event.INoteModify
import com.example.noteapp.model.Note
import com.example.noteapp.presenter.NotePresenter
import com.example.noteapp.until.Constant.DELETE_NOTE_SUCCESSFUL
import com.example.noteapp.until.Constant.SAVE_NOTE_SUCCESSFUL
import com.example.noteapp.until.Constant.UPDATE_NOTE_SUCCESSFUL
import com.example.noteapp.until.Constant.convertToDate
import kotlinx.android.synthetic.main.fragment_note.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NoteFragment : Fragment(R.layout.fragment_note), INoteModify, INoteModify.Check {

    lateinit var presenter: NotePresenter
    var d: String? = null
    var m: String? = null
    var y: String? = null

    companion object {
        fun newInstance(day: String, month: String, year: String): NoteFragment {
            val args = Bundle()
            args.putString("d", day)
            args.putString("m", month)
            args.putString("y", year)
            val fragment = NoteFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter = NotePresenter(requireContext(), this, this)
        d = arguments?.getString("d")
        m = arguments?.getString("m")
        y = arguments?.getString("y")
        tvDate.text = "$d/$m/$y"
        presenter.checkNote(d, m, y)

        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        (activity as AppCompatActivity).supportActionBar?.title = "Note"
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        setHasOptionsMenu(true)
        btnSave.setOnClickListener {
            presenter.save(
                Note(0,
                    convertToDate("$d/$m/$y"),
                    etTitle.text.toString(),
                    etContent.text.toString())
            )
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.note_toolbar, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> activity?.onBackPressed()
            R.id.acDelete -> {
                presenter.delete(d, m, y)

            }
        }
        return true
    }

    override fun onSuccessful(type: Int) {
        when (type) {
            DELETE_NOTE_SUCCESSFUL -> CoroutineScope(Dispatchers.Main).launch {
                Toast.makeText(requireContext(), "Deleted note successful", Toast.LENGTH_SHORT)
                    .show()
                activity?.onBackPressed()
            }

            SAVE_NOTE_SUCCESSFUL -> CoroutineScope(Dispatchers.Main).launch {
                Toast.makeText(requireContext(),
                    "Saved note successful",
                    Toast.LENGTH_SHORT).show()
            }
            UPDATE_NOTE_SUCCESSFUL -> CoroutineScope(Dispatchers.Main).launch {
                Toast.makeText(requireContext(),
                    "Updated note successful",
                    Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onFailure(mes: String) {
        Toast.makeText(requireContext(), mes, Toast.LENGTH_SHORT).show()
    }

    override fun onExists(note: Note) {
        etTitle.setText(note.title)
        etContent.setText(note.content)
    }
}