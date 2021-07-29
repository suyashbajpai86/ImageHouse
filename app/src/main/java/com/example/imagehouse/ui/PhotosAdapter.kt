package com.example.imagehouse.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.imagehouse.R
import com.example.imagehouse.ui.model.PhotoUiModel
import java.net.URI

class PhotosAdapter: RecyclerView.Adapter<PhotosAdapter.ViewHolder>() {

    var list: MutableList<PhotoUiModel>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindData(list?.get(position))
    }

    override fun getItemCount(): Int {
        return list?.size ?: 0
    }

    class ViewHolder(val view: View): RecyclerView.ViewHolder(view){

        companion object{
            fun from(parent: ViewGroup): ViewHolder{
                val view = LayoutInflater.from(parent.context).inflate(R.layout.photo_item, parent, false)
                return ViewHolder(view)
            }
        }

        fun bindData(get: PhotoUiModel?) {
            val iv = itemView.findViewById<ImageView>(R.id.image)
            Glide.with(itemView.context).load(get?.url).into(iv)
        }
    }
}