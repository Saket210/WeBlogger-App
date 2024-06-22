package com.example.weblogger.Adapter

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.weblogger.Model.RecyclerItemModel
import com.example.weblogger.R
import com.example.weblogger.ReadBlogActivity
import com.example.weblogger.databinding.RecyclerItemBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class RecyclerAdapter(private var items: MutableList<RecyclerItemModel>) :
    RecyclerView.Adapter<RecyclerAdapter.RecyclerViewHolder>() {

    private val databaseReference: DatabaseReference = FirebaseDatabase.getInstance("https://weblogger-9e863-default-rtdb.asia-southeast1.firebasedatabase.app").reference
    private val user = FirebaseAuth.getInstance().currentUser

    inner class RecyclerViewHolder(private val binding: RecyclerItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(recyclerItemModel: RecyclerItemModel) {
            val blogPostId = recyclerItemModel.blogPostId
            binding.blogTitle.text = recyclerItemModel.title
            Glide.with(binding.imageViewIcon.context)
                .load(recyclerItemModel.profileUrl)
                .into(binding.imageViewIcon)
            binding.bloggerName.text = recyclerItemModel.bloggerName
            binding.date.text = recyclerItemModel.date
            binding.blogContent.text = recyclerItemModel.blogContent
            binding.likeCount.text = recyclerItemModel.likeCount.toString()

            binding.root.setOnClickListener {
                val context = binding.root.context
                val intent = Intent(context, ReadBlogActivity::class.java)
                intent.putExtra("Item", recyclerItemModel)
                context.startActivity(intent)
            }
            binding.readmore.setOnClickListener {
                val context = binding.root.context
                val intent = Intent(context, ReadBlogActivity::class.java)
                intent.putExtra("Item", recyclerItemModel)
                context.startActivity(intent)
            }

            val likeReference  = databaseReference.child("Blogs").child(blogPostId!!).child("likes")
            val currentUserLiked = user?.uid?.let { uid ->
                likeReference.child(uid)?.addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            if (snapshot.exists()) {
                                binding.buttonLike.setImageResource(R.drawable.red_like)
                            } else {
                                binding.buttonLike.setImageResource(R.drawable.black_like)
                            }
                        }

                        override fun onCancelled(error: DatabaseError) {
                        }

                    })
            }
            binding.buttonLike.setOnClickListener {
                if (user != null) {
                    likeButtonClick(blogPostId, recyclerItemModel, binding)
                }
            }
            val savedReference = databaseReference.child("Users").child(user!!.uid).child("SavedPosts").child(blogPostId)
            savedReference.addListenerForSingleValueEvent(object :ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    if(snapshot.exists()){
                        binding.buttonSave.setImageResource(R.drawable.black_save)
                    } else {
                        binding.buttonSave.setImageResource(R.drawable.red_save)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                }

            })

            binding.buttonSave.setOnClickListener {
                if(user!=null) {
                    saveButtonClick(blogPostId, recyclerItemModel, binding)
                }
            }
        }

    }

    private fun likeButtonClick(
        blogPostId: String?,
        recyclerItemModel: RecyclerItemModel,
        binding: RecyclerItemBinding
    ) {
        val userReference = databaseReference.child("Users").child(user!!.uid)
        val likeReference = databaseReference.child("Blogs").child(blogPostId!!).child("likes")
        likeReference.child(user.uid).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    userReference.child("likes").child(blogPostId).removeValue()
                        .addOnSuccessListener {
                            likeReference.child(user.uid).removeValue()
                            recyclerItemModel.likedBy?.remove(user.uid)
                            updateLikeButton(binding, false)

                            val updatedLikeCount = recyclerItemModel.likeCount-1
                            recyclerItemModel.likeCount = updatedLikeCount
                            databaseReference.child("Blogs").child(blogPostId).child("likeCount")
                                .setValue(updatedLikeCount)
                            notifyDataSetChanged()

                        }
                        .addOnFailureListener { e ->
                            Log.e("Update Like Count", "on UpdatelikecountFailed: $e ")
                        }
                } else {
                    userReference.child("likes").child(blogPostId).setValue(true)
                        .addOnSuccessListener {
                            likeReference.child(user.uid).setValue(true)
                            recyclerItemModel.likedBy?.add(user.uid)
                            updateLikeButton(binding, true)
                            val updatedLikeCount = recyclerItemModel.likeCount+1
                            recyclerItemModel.likeCount = updatedLikeCount
                            databaseReference.child("Blogs").child(blogPostId).child("likeCount")
                                .setValue(updatedLikeCount)
                            notifyDataSetChanged()
                        }
                        .addOnFailureListener { e ->
                            Log.e("Update Like Count", "on UpdatelikecountFailed: $e ")
                        }
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }

        })
    }

    private fun updateLikeButton(binding: RecyclerItemBinding, liked: Boolean) {
        if (liked) {
            binding.buttonLike.setImageResource(R.drawable.black_like)
        } else {
            binding.buttonLike.setImageResource(R.drawable.red_like)
        }
    }


    private fun saveButtonClick(blogPostId: String, recyclerItemModel: RecyclerItemModel, binding: RecyclerItemBinding) {
        val userReference = databaseReference.child("Users").child(user!!.uid)
        userReference.child("SavedPosts").child(blogPostId).addListenerForSingleValueEvent(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    userReference.child("SavedPosts").child(blogPostId).removeValue()
                        .addOnSuccessListener {
                            updateSavedButton(binding,false)
                            val clickedItem = items.find { it.blogPostId == blogPostId }
                            clickedItem?.isSaved = false
                            notifyDataSetChanged()
                        }
                } else {
                    userReference.child("SavedPosts").child(blogPostId).setValue(true)
                        .addOnSuccessListener {
                            updateSavedButton(binding,true)
                            val clickedItem = items.find { it.blogPostId == blogPostId }
                            clickedItem?.isSaved = true
                            notifyDataSetChanged()
                        }
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }

        })
    }

    private fun updateSavedButton(binding: RecyclerItemBinding, saved: Boolean) {
        if (!saved) {
            binding.buttonSave.setImageResource(R.drawable.red_save)
        } else {
            binding.buttonSave.setImageResource(R.drawable.black_save)
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
        val item = items[position]
        holder.bind(item)
    }

    fun update(savedBlogs: List<RecyclerItemModel>) {
        items.clear()
        items.addAll(savedBlogs)
        notifyDataSetChanged()
    }

    fun showFilteredList(newBlogList: MutableList<RecyclerItemModel>) {
        this.items = newBlogList
        notifyDataSetChanged()
    }
}