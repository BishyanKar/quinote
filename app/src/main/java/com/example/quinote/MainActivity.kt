package com.example.quinote

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.quinote.adapters.NotesAdapter
import com.example.quinote.ui.user.LoginActivity
import com.example.quinote.viewmodel.ImageViewModel
import com.example.quinote.viewmodel.ImageViewModelFactory
import com.example.quinote.viewmodel.NoteViewModel
import com.example.quinote.viewmodel.NoteViewModelFactory
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlin.math.max

class MainActivity : AppCompatActivity() {

    private lateinit var addNote : FloatingActionButton
    private lateinit var logout: FloatingActionButton

    private lateinit var noteViewModel: NoteViewModel
    private lateinit var noteViewModelFactory: NoteViewModelFactory
    private lateinit var imageViewModel: ImageViewModel
    private lateinit var imageViewModelFactory: ImageViewModelFactory


    private lateinit var notesAdapter: NotesAdapter
    private lateinit var recyclerView: RecyclerView

    private var note_Id:Int = 0
    private val TAG = "MainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val sharedPref = getSharedPreferences(getString(R.string.Preference_key_file_name_1110), Context.MODE_PRIVATE)
        val editor = sharedPref.edit()

        val uid = sharedPref?.getInt("UID",-1)
        if(uid == -1)
        {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        noteViewModelFactory = NoteViewModelFactory((application as NoteApp).noteRepository)
        noteViewModel = ViewModelProvider(this, noteViewModelFactory).get(NoteViewModel::class.java)

        imageViewModelFactory = ImageViewModelFactory((application as NoteApp).imageRepository)
        imageViewModel = ViewModelProvider(this,imageViewModelFactory).get(ImageViewModel::class.java)

        notesAdapter = NotesAdapter()
        notesAdapter.setViewModel(noteViewModel, imageViewModel)
        notesAdapter.setActvityAndContext(this,this)

        recyclerView = findViewById(R.id.note_rv)

        recyclerView.adapter = notesAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)

        addNote = findViewById(R.id.add_note_fab)
        logout = findViewById(R.id.logout_fab)


        addNote.setOnClickListener {
            val intent = Intent(this, AddNoteActivity::class.java).apply {
                putExtra("NOTE_ID",note_Id++)
            }
            startActivity(intent)
        }

        logout.setOnClickListener {
            editor.putInt("UID",-1)
            editor.apply()
            Toast.makeText(this, "Logged Out!", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }

        noteViewModel.getAllByUserId(uid!!).observe(this, { notes ->
            notes?.let {
                Log.d(TAG, "onCreate: SIZE : "+it.size)
                notesAdapter.submitList(it)

                if(it.isNotEmpty())
                    note_Id = max(it[it.size-1].id,note_Id)
            }
        })

    }
}