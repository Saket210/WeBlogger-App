package com.example.weblogger.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.AdapterView.OnItemClickListener
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.weblogger.Model.RecyclerItemModel
import com.example.weblogger.databinding.RecyclerItemMyblogBinding

class BlogAdapter (
    private val context:Context,
    private var blogList: List<RecyclerItemModel>,
    private val itemClickListener: OnItemClickListener
):RecyclerView.Adapter<BlogAdapter.BlogViewHolder>(){

    interface OnItemClickListener{
        fun EditClicked(blogItem: RecyclerItemModel)
        fun ReadMoreClicked(blogItem: RecyclerItemModel)
        fun DeleteClicked(blogItem: RecyclerItemModel)
    }

    inner class BlogViewHolder(private val binding:RecyclerItemMyblogBinding):RecyclerView.ViewHolder(binding.root) {
        fun bind(blogItem: RecyclerItemModel) {
            binding.blogTitle.text = blogItem.title
            Glide.with(binding.imageViewIcon.context)
                .load(blogItem.profileUrl)
                .into(binding.imageViewIcon)
            binding.bloggerName.text = blogItem.bloggerName
            binding.date.text = blogItem.date
            binding.blogContent.text = blogItem.blogContent

            binding.readmore.setOnClickListener {
                itemClickListener.ReadMoreClicked(blogItem)
            }
            binding.editBtn.setOnClickListener {
                itemClickListener.EditClicked(blogItem)
            }
            binding.deleteBtn.setOnClickListener {
                itemClickListener.DeleteClicked(blogItem)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BlogAdapter.BlogViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = RecyclerItemMyblogBinding.inflate(inflater,parent,false)
        return BlogViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BlogAdapter.BlogViewHolder, position: Int) {
        val blogItem = blogList[position]
        holder.bind(blogItem)
    }

    override fun getItemCount(): Int {
        return blogList.size
    }

    fun setData(blogSavedList: ArrayList<RecyclerItemModel>) {
        this.blogList = blogSavedList
        notifyDataSetChanged()
    }

}