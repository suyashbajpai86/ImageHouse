package com.example.imagehouse.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.imagehouse.R
import com.example.imagehouse.ui.model.BaseRVItem
import com.example.imagehouse.ui.model.PhotoUiModel
import com.example.imagehouse.ui.model.TextUiModel

class PhotosAdapter(val onItemClick: MutableLiveData<Int>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object{
        const val VIEW_TYPE_PHOTO = 1
        const val VIEW_TYPE_TEXT = 2
    }

    var list: MutableList<BaseRVItem>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == VIEW_TYPE_PHOTO) {
            return PhotoViewHolder.from(parent)
        } else {
            return TextViewHolder.from(parent)
        }
    }

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        val viewType = getItemViewType(position)
        if (viewType == VIEW_TYPE_PHOTO) {
            (viewHolder as? PhotoViewHolder)?.bindData(list?.get(position), onItemClick)
        } else {
            (viewHolder as? TextViewHolder)?.bindData(list?.get(position), onItemClick)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (list?.get(position) is PhotoUiModel){
            VIEW_TYPE_PHOTO
        } else {
            VIEW_TYPE_TEXT
        }
    }

    override fun getItemCount(): Int {
        return list?.size ?: 0
    }

    class PhotoViewHolder(view: View): RecyclerView.ViewHolder(view){

        companion object{
            fun from(parent: ViewGroup): PhotoViewHolder{
                val view = LayoutInflater.from(parent.context).inflate(R.layout.photo_item, parent, false)
                return PhotoViewHolder(view)
            }
        }

        fun bindData(get: BaseRVItem?, onItemClick: MutableLiveData<Int>) {
            val iv = itemView.findViewById<ImageView>(R.id.image)
            Glide.with(itemView.context).load((get as? PhotoUiModel)?.url).into(iv)
            iv.setOnClickListener {
                onItemClick.postValue(adapterPosition)
            }
        }
    }

    class TextViewHolder(view: View): RecyclerView.ViewHolder(view){

        companion object{
            fun from(parent: ViewGroup): TextViewHolder{
                val view = LayoutInflater.from(parent.context).inflate(R.layout.text_item, parent, false)
                return TextViewHolder(view)
            }
        }

        fun bindData(get: BaseRVItem?, onItemClick: MutableLiveData<Int>) {
            val iv = itemView.findViewById<TextView>(R.id.text)
            iv.text = (get as? TextUiModel)?.text
            iv.setOnClickListener {
                onItemClick.postValue(adapterPosition)
            }
        }
    }
}