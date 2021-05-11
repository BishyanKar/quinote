package com.example.quinote.camera

import android.content.Context
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.example.quinote.R
import com.google.android.material.floatingactionbutton.FloatingActionButton

private const val ARG_PARAM1 = "image"
private const val ARG_PARAM2 = "param2"

class PreviewFragment : Fragment() {

    private var param1: String? = null
    private var param2: String? = null

    private lateinit var callbacks: PreviewFragmentCallback

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_preview, container, false)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            callbacks = context as PreviewFragmentCallback
        } catch (e: ClassCastException) {
            e.printStackTrace()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val saveFab = view.findViewById<FloatingActionButton>(R.id.save)
        val cancelFab = view.findViewById<FloatingActionButton>(R.id.cancel)
        val preview = view.findViewById<ImageView>(R.id.image_preview)

        preview.setImageBitmap(BitmapFactory.decodeFile(param1))

        saveFab.setOnClickListener {
            param1?.let { it1 -> callbacks.save(it1) }
        }

        cancelFab.setOnClickListener {
            param1?.let { it1 -> callbacks.cancel(it1) }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            PreviewFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    interface PreviewFragmentCallback{
        fun save(path: String)
        fun cancel(path: String)
    }
}