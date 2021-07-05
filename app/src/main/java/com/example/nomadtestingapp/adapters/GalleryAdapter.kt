package com.example.nomadtestingapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.nomadtestingapp.R
import com.example.nomadtestingapp.data.models.File
import com.example.nomadtestingapp.databinding.ButtonItemBinding
import com.example.nomadtestingapp.databinding.PhotoItemBinding


class GalleryAdapter: RecyclerView.Adapter<GalleryAdapter.GalleryViewHolder>() {

    class GalleryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun getItemViewType(position: Int): Int {
        return if (position == 0) R.layout.button_item else R.layout.photo_item
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GalleryViewHolder {
        return if(viewType == R.layout.photo_item){
            GalleryViewHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.photo_item,
                    parent,
                    false
                )
            )
        }else{
            GalleryViewHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.button_item,
                    parent,
                    false
                )
            )
        }
    }

    override fun onBindViewHolder(holder: GalleryViewHolder, position: Int) {
        if(position == 0){
            holder.itemView.apply {
                setOnClickListener {
                    onItemClickListener?.let { click ->
                        click()
                    }
                }
            }
        }else{
            val photo = photoList[position]
            val binding = PhotoItemBinding.bind(holder.itemView)

            holder.itemView.apply {
                binding.ivPhoto.setImageURI(photo.path.toUri())
            }
        }
    }

    override fun getItemCount(): Int {
        return photoList.size
    }

    val diffCallback = object : DiffUtil.ItemCallback<File>(){
        override fun areItemsTheSame(oldItem: File, newItem: File): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: File, newItem: File): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }

    }

    val differ = AsyncListDiffer(this, diffCallback)

    var photoList: List<File>
        get() = differ.currentList
        set(value) = differ.submitList(value)

    private var onItemClickListener: (() -> Unit)? = null

    fun setItemClickListener(listener: () -> Unit){
        onItemClickListener = listener
    }
}