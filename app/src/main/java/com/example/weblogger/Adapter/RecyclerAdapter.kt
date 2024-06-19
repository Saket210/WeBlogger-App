package com.example.weblogger.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.weblogger.Model.RecyclerItemModel
import com.example.weblogger.databinding.RecyclerItemBinding

class RecyclerAdapter (private val items:List<RecyclerItemModel>): RecyclerView.Adapter<RecyclerAdapter.RecyclerViewHolder>(){

    inner class RecyclerViewHolder (private val binding: RecyclerItemBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(recyclerItemModel: RecyclerItemModel) {
            binding.blogTitle.text = recyclerItemModel.title
            Glide.with(binding.imageViewIcon.context)
                .load(recyclerItemModel.profilrUrl)
                .into(binding.imageViewIcon)
            binding.bloggerName.text = recyclerItemModel.bloggerName
            binding.date.text = recyclerItemModel.date
            binding.blogContent.text = recyclerItemModel.blogContent
            binding.likeCount.text = recyclerItemModel.likeCount.toString()
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = RecyclerItemBinding.inflate(inflater, parent, false)
        return RecyclerViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {
        holder.bind(items[position])
    }
}