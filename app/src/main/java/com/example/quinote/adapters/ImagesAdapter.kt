package com.example.quinote.adapters

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.quinote.R
import com.example.quinote.models.Image
import com.example.quinote.models.Note
import com.example.quinote.util.BitMapHelper
import com.example.quinote.viewmodel.ImageViewModel
import com.example.quinote.viewmodel.NoteViewModel
import java.lang.Exception
import java.lang.NullPointerException

class ImagesAdapter: ListAdapter<Image, ImageViewHolder>(DiffUtilItemCallback)  {

    private lateinit var viewModel: ImageViewModel

    object DiffUtilItemCallback : DiffUtil.ItemCallback<Image>() {
        override fun areItemsTheSame(oldItem: Image, newItem: Image): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Image, newItem: Image): Boolean {
            return oldItem.path == newItem.path
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        return ImageViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val current: Image = getItem(position)
        holder.bind(current.path, current.id)
    }
}
class ImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val imageView: ImageView = itemView.findViewById(R.id.imageView)
    private val TAG = "ImagesAdapter"

    private val rendermap = HashMap<Int, Boolean>()

     fun bind(path: String?, id: Int) {
        //set image to imageview
         if(rendermap[id]==true)
             return
         Log.d(TAG, "Path :  $path")
         rendermap[id] = true
        try {
            imageView.setImageBitmap(BitMapHelper.decodeSampledBitmapFromResource(path,300,200))
            Log.d(TAG, "bind: Image loaded successfully $path")
        }
        catch (e: NullPointerException){
            Log.d(TAG, "bind: $e")
            imageView.setImageURI(path?.toUri())
        }
         catch (e: Exception){
             Log.d(TAG, "bind: $e")
         }
    }

    companion object {
        fun create(parent: ViewGroup): ImageViewHolder {
            val view: View = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_image_list, parent, false)
            return ImageViewHolder(view)
        }
    }
}