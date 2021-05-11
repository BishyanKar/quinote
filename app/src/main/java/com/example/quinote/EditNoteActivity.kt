package com.example.quinote

import android.Manifest
import android.Manifest.permission.CAMERA
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import androidx.activity.result.contract.ActivityResultContracts
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.quinote.adapters.ImagesAdapter
import com.example.quinote.camera.CameraPreview
import com.example.quinote.models.Image
import com.example.quinote.models.Note
import com.example.quinote.viewmodel.ImageViewModel
import com.example.quinote.viewmodel.ImageViewModelFactory
import com.example.quinote.viewmodel.NoteViewModel
import com.example.quinote.viewmodel.NoteViewModelFactory
import kotlin.properties.Delegates

class EditNoteActivity : AppCompatActivity() {
    private lateinit var noteViewModel: NoteViewModel
    private lateinit var noteViewModelFactory: NoteViewModelFactory

    private lateinit var imageViewModel: ImageViewModel
    private lateinit var imageViewModelFactory: ImageViewModelFactory

    private lateinit var titleView : EditText
    private lateinit var contentView : EditText

    private lateinit var updatedNote: Note

    private lateinit var addImage: ImageButton
    private lateinit var addImageG: ImageButton

    private lateinit var recyclerView: RecyclerView
    private lateinit var imagesAdapter: ImagesAdapter

    private lateinit var constraintLayout: ConstraintLayout

    private var currentNoteId by Delegates.notNull<Int>()
    private var titleText: String? = ""
    private var contentText: String? = ""

    private var tmpImages: ArrayList<Image>? = null
    private var uid: Int? = -1

    private val TAG = "AddNoteActivity"
    private val PERMISSION_REQUEST_CAMERA = 120
    private val PERMISSION_REQUEST_READ = 190

    private val startForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if(result.resultCode == Activity.RESULT_OK){
            val data = result.data
            val image = Image(data?.data.toString(),currentNoteId)
            imageViewModel.insert(image)

            Log.d(TAG, "pickPhoto: IMAGE_PICKED_DATE : ${data?.data?.path}")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_note)

        val sharedPref = getSharedPreferences(getString(R.string.Preference_key_file_name_1110), Context.MODE_PRIVATE)
        uid = sharedPref.getInt("UID",-1)

        tmpImages = ArrayList()

        constraintLayout = findViewById(R.id.top_CL)


        currentNoteId = intent.getIntExtra("NOTE_ID",0)
        titleText = intent.getStringExtra("TITLE")
        contentText = intent.getStringExtra("CONTENT")


        imagesAdapter = ImagesAdapter()
        recyclerView = findViewById(R.id.image_recycler_view)
        recyclerView.adapter = imagesAdapter
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recyclerView.setHasFixedSize(true)

        titleView = findViewById(R.id.note_title)
        contentView = findViewById(R.id.note_content)
        addImage = findViewById(R.id.from_camera)
        addImageG = findViewById(R.id.from_gallery)

        noteViewModelFactory = NoteViewModelFactory((application as NoteApp).noteRepository)
        noteViewModel = ViewModelProvider(this, noteViewModelFactory).get(NoteViewModel::class.java)

        imageViewModelFactory = ImageViewModelFactory((application as NoteApp).imageRepository)
        imageViewModel = ViewModelProvider(this,imageViewModelFactory).get(ImageViewModel::class.java)

        titleView.setText(titleText)
        contentView.setText(contentText)

        updatedNote = Note(currentNoteId, titleText, contentText, "",uid!!)


        titleView.addTextChangedListener {
            var path: String? = ""
            if(tmpImages!!.isNotEmpty())
                path = tmpImages!![0].path
            updatedNote = Note(currentNoteId,titleView.text.toString(),contentView.text.toString(),path,uid!!)
            noteViewModel.update(updatedNote)
        }
        contentView.addTextChangedListener{
            var path: String? = ""
            if(tmpImages!!.isNotEmpty())
                path = tmpImages!![0].path

            updatedNote = Note(currentNoteId,titleView.text.toString(),contentView.text.toString(),path,uid!!)
            noteViewModel.update(updatedNote)
        }

        addImage.setOnClickListener {
            if(tmpImages!!.size < 10)
                startCamera()
            else {
                val builder = androidx.appcompat.app.AlertDialog.Builder(this)
                builder.setTitle("Warning")
                builder.setMessage("You can have at most 10 images per note")
                builder.setPositiveButton("Ok") { dialog, which ->
                    dialog.dismiss()
                }
                val dialog = builder.create()
                dialog.show()
            }
        }

        addImageG.setOnClickListener {
            if(tmpImages!!.size < 10)
                pickPhoto()
            else {
                val builder = androidx.appcompat.app.AlertDialog.Builder(this)
                builder.setTitle("Warning")
                builder.setMessage("You can have at most 10 images per note")
                builder.setPositiveButton("Ok") { dialog, which ->
                    dialog.dismiss()
                }
                val dialog = builder.create()
                dialog.show()
            }
        }

        imageViewModel.getAllForNote(currentNoteId).observe(this, { images ->
            images?.let {
                if(it.isNotEmpty())
                    constraintLayout.visibility = View.VISIBLE
                else constraintLayout.visibility = View.GONE

                tmpImages!!.clear()
                tmpImages!!.addAll(it)

                if(tmpImages!!.isNotEmpty()) {
                    updatedNote = Note(
                        currentNoteId,
                        titleView.text.toString(),
                        contentView.text.toString(),
                        tmpImages!![0].path,
                        uid!!
                    )
                    noteViewModel.update(updatedNote)
                }
                imagesAdapter.submitList(it)
                imagesAdapter.notifyDataSetChanged()
            }
        })
    }

    override fun onBackPressed() {
        if(updatedNote.title?.length!! < 5 || updatedNote.title?.length!! > 100){
            val builder = androidx.appcompat.app.AlertDialog.Builder(this)
            builder.setTitle("Warning")
            builder.setMessage("Title must have min 5 and max 100 characters")
            builder.setPositiveButton("Ok") { dialog, which ->
                dialog.dismiss()
            }
                .setNegativeButton("Discard") { dialog, which ->
                    dialog.dismiss()
                    noteViewModel.delete(updatedNote)
                    imageViewModel.deleteAllForNote(currentNoteId)
                    super.onBackPressed()
                }
            val dialog = builder.create()
            dialog.show()
        }
        else if(updatedNote.desc?.length!! < 100 || updatedNote.desc?.length!! > 1000){
            val builder = androidx.appcompat.app.AlertDialog.Builder(this)
            builder.setTitle("Warning")
            builder.setMessage("Description must have min 100 and max 1000 characters")
            builder.setPositiveButton("Ok") { dialog, which ->
                dialog.dismiss()
            }
                .setNegativeButton("Discard") { dialog, which ->
                    dialog.dismiss()
                    noteViewModel.delete(updatedNote)
                    imageViewModel.deleteAllForNote(currentNoteId)
                    super.onBackPressed()
                }
            val dialog = builder.create()
            dialog.show()
        }
        else super.onBackPressed()
    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST_CAMERA) {
            // Request for camera permission.
            if (grantResults.size == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission has been granted. Start camera preview Activity.
                startCamera()
            } else {
                // Permission request was denied.
            }
        }
        else if (requestCode == PERMISSION_REQUEST_READ) {
            // Request for camera permission.
            if (grantResults.size == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission has been granted. Start camera preview Activity.
                pickPhoto()
            } else {
                // Permission request was denied.
            }
        }
    }
    fun pickPhoto()
    {
        if (ContextCompat.checkSelfPermission(this.applicationContext, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            intent.type = "image/*"
            startForResult.launch(intent)
        } else {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                PERMISSION_REQUEST_READ);
        }
    }
    private fun startCamera()
    {
        if (ContextCompat.checkSelfPermission(this.applicationContext, CAMERA) == PackageManager.PERMISSION_GRANTED) {
            val intent = Intent(this, CameraPreview::class.java)
            intent.putExtra("NOTE_ID",currentNoteId)
            startActivity(intent)
        } else {
            ActivityCompat.requestPermissions(this, arrayOf(CAMERA),
                PERMISSION_REQUEST_CAMERA);
        }
    }
}