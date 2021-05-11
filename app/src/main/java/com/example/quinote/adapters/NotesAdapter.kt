package com.example.quinote.adapters

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.quinote.EditNoteActivity
import com.example.quinote.MainActivity
import com.example.quinote.R
import com.example.quinote.models.Note
import com.example.quinote.util.BitMapHelper
import com.example.quinote.viewmodel.ImageViewModel
import com.example.quinote.viewmodel.NoteViewModel
import java.lang.Exception
import java.lang.NullPointerException

class NotesAdapter : ListAdapter<Note, NoteViewHolder>(DiffUtilItemCallback) {

    private lateinit var noteViewModel: NoteViewModel
    private lateinit var imageViewModel: ImageViewModel
    private lateinit var context: Context
    private lateinit var activity: Activity

    object DiffUtilItemCallback : DiffUtil.ItemCallback<Note>() {
        override fun areItemsTheSame(oldItem: Note, newItem: Note): Boolean {
            return oldItem.title == newItem.title && oldItem.path == newItem.path && oldItem.uid == newItem.uid
        }

        override fun areContentsTheSame(oldItem: Note, newItem: Note): Boolean {
            return oldItem.desc == newItem.desc && oldItem.path == newItem.path
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        return NoteViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val current: Note = getItem(position)
        holder.bind(current)

        holder.itemView.setOnClickListener {
            val intent = Intent(context, EditNoteActivity::class.java)
            intent.putExtra("NOTE_ID", current.id)
            intent.putExtra("TITLE", current.title)
            intent.putExtra("CONTENT", current.desc)

            activity.startActivity(intent)
        }

        holder.itemView.setOnLongClickListener{
            noteViewModel.delete(getItem(holder.adapterPosition))
            imageViewModel.deleteAllForNote(getItem(holder.adapterPosition).id)
            true
        }
    }

    fun setActvityAndContext(activity: Activity,context: Context){
        this.context = context
        this.activity = activity
    }

    fun setViewModel(noteViewModel: NoteViewModel, imageViewModel: ImageViewModel){
        this.noteViewModel = noteViewModel
        this.imageViewModel = imageViewModel
    }
}

class NoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val titleView: TextView = itemView.findViewById(R.id.title)
    private val contentView: TextView = itemView.findViewById(R.id.content)
    private val imageView: ImageView = itemView.findViewById(R.id.img_view)

    private val TAG = "NotesAdapter"

    fun bind(note: Note) {
        var desc: String? = note.title
        if(note.desc?.length!! > 100)
            desc = note.desc!!.substring(0,100)+"..."
        titleView.text = note.title
        contentView.text = desc

        var path = note.path

        Log.d(TAG, "bind: {PATH} :  $path")

        try {
            imageView.setImageBitmap(BitMapHelper.decodeSampledBitmapFromResource(path,300,200))
            Log.d(TAG, "bind: Image loaded successfully $path")
            imageView.visibility = View.VISIBLE
        }
        catch (e: NullPointerException){
            Log.d(TAG, "bind: $e")
            if(path!=null && path != "") {
                imageView.setImageURI(path.toUri())
                imageView.visibility = View.VISIBLE
            }
            else imageView.visibility = View.GONE
        }
        catch (e: Exception){
            Log.d(TAG, "bind: $e")
        }
    }

    companion object {
        fun create(parent: ViewGroup): NoteViewHolder {
            val view: View = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_note, parent, false)
            return NoteViewHolder(view)
        }
    }
}
