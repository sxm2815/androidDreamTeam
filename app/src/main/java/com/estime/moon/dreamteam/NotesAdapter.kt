package com.estime.moon.dreamteam

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.notes_item.view.*

class NotesAdapter(val notes: ArrayList<Notes>, val itemClick: (Notes) -> Unit) :
    RecyclerView.Adapter<NotesAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.notes_item, parent, false)
        return ViewHolder(view, itemClick)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindForecast(notes[position])
    }

    override fun getItemCount() = notes.size

    class ViewHolder(view: View, val itemClick: (Notes) -> Unit) : RecyclerView.ViewHolder(view) {

        fun bindForecast(note: Notes) {
            with(note) {
                itemView.notes_item_title.text = note.title
                itemView.notes_item_description.text = note.description
                itemView.setOnClickListener { itemClick(this) }
            }
        }
    }
}