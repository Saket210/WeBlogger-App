package com.example.weblogger

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weblogger.Adapter.RecyclerAdapter
import com.example.weblogger.Model.RecyclerItemModel
import com.example.weblogger.databinding.ActivitySavedBlogsBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class SavedBlogsActivity : AppCompatActivity() {
    private val savedBlogs = mutableListOf<RecyclerItemModel>()
    private lateinit var adapter:RecyclerAdapter
    private val auth = FirebaseAuth.getInstance()
    private val binding:ActivitySavedBlogsBinding by lazy {
        ActivitySavedBlogsBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        adapter = RecyclerAdapter(savedBlogs.filter { it.isSaved }.toMutableList())
        binding.recyclerViewSaved.adapter = adapter
        binding.recyclerViewSaved.layoutManager = LinearLayoutManager(this)

        val userID = auth.currentUser?.uid
        if(userID!=null){
            val userReference = FirebaseDatabase.getInstance("https://weblogger-9e863-default-rtdb.asia-southeast1.firebasedatabase.app")
                .getReference("Users").child(userID).child("SavedPosts")
            userReference.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (blogSnapshot in snapshot.children){
                        val blogID = blogSnapshot.key
                        val isSaved = blogSnapshot.value as Boolean
                        if(blogID!=null && isSaved){
                            CoroutineScope(Dispatchers.IO).launch {
                                val blogItem = getBlogItems(blogID)
                                if(blogItem!=null){
                                    savedBlogs.add(blogItem)
                                }
                                launch(Dispatchers.Main){
                                    adapter.update(savedBlogs)
                                }
                            }
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                }

            })
        }

        binding.backBtn3.setOnClickListener {
            finish()
        }

    }

    private suspend fun getBlogItems(blogID: String): RecyclerItemModel? {
        val blogReference = FirebaseDatabase.getInstance("https://weblogger-9e863-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("Blogs")
        return try {
            val dataSnapshot = blogReference.child(blogID).get().await()
            val blogData = dataSnapshot.getValue(RecyclerItemModel::class.java)
            blogData
        } catch (e: Exception) {
            null
        }
    }
}