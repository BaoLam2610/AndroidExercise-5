package com.example.noteapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.example.noteapp.R
import com.example.noteapp.event.IOnClickItem
import com.example.noteapp.model.Note
import com.example.noteapp.until.Constant.df
import kotlinx.android.synthetic.main.item_note.view.*

class NoteListAdapter(
    var mContext: Context,
    var noteList: List<Note>?
) : RecyclerView.Adapter<NoteListAdapter.NoteViewHolder>(), Filterable {

    var tempNoteList: MutableList<Note>? = null
    init {
        tempNoteList = noteList as MutableList<Note>?
    }

    var iOnClick: IOnClickItem.INote? = null

    fun setIOnClickItem(iOnClick: IOnClickItem.INote){
        this.iOnClick = iOnClick
    }

    inner class NoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        return NoteViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_note, parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val note = noteList?.get(position)
        holder.itemView.apply {
            tvDate.text = df.format(note?.date)
            tvTitle.text = "Title: ${note?.title}"
            tvContent.text = "Content: ${note?.content}"
            setOnClickListener {
                iOnClick?.onClickItem(note!!)
            }
        }
    }

    override fun getItemCount(): Int = noteList?.size ?: 0

    override fun getFilter(): Filter = object : Filter(){
        override fun performFiltering(charSequence: CharSequence?): FilterResults {
            val str = charSequence.toString()
            noteList = if(str == null){
                tempNoteList!!
            } else{
                var list = mutableListOf<Note>()
                for (item in tempNoteList!!){
                    if(item.title.lowercase().trim().contains(str))
                        list.add(item)
                }
                list
            }

            val results = FilterResults()
            results.values = noteList
            return results
        }

        override fun publishResults(charSequence: CharSequence?, filterResults: FilterResults?) {
            noteList = filterResults?.values as List<Note>
            notifyDataSetChanged()
        }
    }
}