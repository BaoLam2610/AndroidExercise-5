package com.example.noteapp.ui.note.fragments

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.noteapp.R
import com.example.noteapp.adapter.NoteListAdapter
import com.example.noteapp.event.INoteModify
import com.example.noteapp.event.IOnClickItem
import com.example.noteapp.model.Note
import com.example.noteapp.presenter.NotePresenter
import com.example.noteapp.until.Constant.getDayOfMonthFromDate
import com.example.noteapp.until.Constant.getMonthFromDate
import com.example.noteapp.until.Constant.getYearFromDate
import kotlinx.android.synthetic.main.fragment_note_list.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class NoteListFragment : Fragment(R.layout.fragment_note_list), INoteModify.NoteList,
    INoteModify.Backup {

    private val TAG = "NoteListFragment"
    lateinit var adapter: NoteListAdapter
    private lateinit var presenter: NotePresenter

    companion object {
        fun newInstance(): NoteListFragment {
            val args = Bundle()

            val fragment = NoteListFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupActionBar()
        setHasOptionsMenu(true)
        presenter = NotePresenter(requireContext(), this, this)
        presenter.getNoteList()
    }

    override suspend fun onGetNoteList(noteList: List<Note>?) {
        withContext(Dispatchers.Main) {
            adapter = NoteListAdapter(requireContext(), noteList)
            rvNotes.adapter = adapter
            rvNotes.layoutManager = LinearLayoutManager(requireContext())
            adapter.setIOnClickItem(object : IOnClickItem.INote {
                override fun onClickItem(note: Note) {
                    setFragment(
                        NoteFragment.newInstance(
                            getDayOfMonthFromDate(note.date),
                            getMonthFromDate(note.date),
                            getYearFromDate(note.date)
                        ))
                }
            })
        }
    }

    private fun setupActionBar() {
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        (activity as AppCompatActivity).supportActionBar?.title = "Note List"
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun setFragment(fragment: Fragment) {
        parentFragmentManager.beginTransaction()
            .replace(R.id.frameLayout, fragment)
            .addToBackStack(null)
            .commit()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.note_list_toolbar, menu)

        val searchItem = menu.findItem(R.id.acSearch)
        val searchView = searchItem?.actionView as SearchView
        searchView.queryHint = "Enter title want to search"
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(query: String?): Boolean {
                adapter.filter.filter(query?.lowercase()?.trim())
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                adapter.filter.filter(newText?.lowercase()?.trim())
                return false
            }
        })
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> activity?.onBackPressed()
            R.id.acBackup -> presenter.backup()
            R.id.acRestore -> {
                val dialog = AlertDialog.Builder(requireContext())
                    .setTitle("Alert")
                    .setMessage("Do you want to restore old data?")
                    .setPositiveButton("Yes") { _, _ ->
                        presenter.restore()
                    }
                    .setNegativeButton("Cancel", null)
                    .create()
                dialog.show()
            }
        }
        return true
    }

    override fun onBackup(path: String) {
        CoroutineScope(Dispatchers.Main).launch {
            Toast.makeText(requireContext(), "File: $path", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onRestore() {
        presenter.getNoteList()
    }


}