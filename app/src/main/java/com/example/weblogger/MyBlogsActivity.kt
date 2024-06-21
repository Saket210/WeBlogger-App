package com.example.weblogger

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.AdapterView.OnItemClickListener
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weblogger.Adapter.BlogAdapter
import com.example.weblogger.Model.RecyclerItemModel
import com.example.weblogger.databinding.ActivityMyBlogsBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MyBlogsActivity : AppCompatActivity() {
    private val binding:ActivityMyBlogsBinding by lazy {
        ActivityMyBlogsBinding.inflate(layoutInflater)
    }
    private lateinit var databaseReference: DatabaseReference
    private val auth = FirebaseAuth.getInstance()
    private lateinit var adapter:BlogAdapter
    private val BLOG_EDIT_CODE = 23

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.backBtn4.setOnClickListener {
            finish()
        }

        val userId = auth.currentUser?.uid
        val recyclerView = binding.recyclerViewMyblogs
        recyclerView.layoutManager = LinearLayoutManager(this)

        if(userId!=null){
            adapter = BlogAdapter(this, emptyList(),object : BlogAdapter.OnItemClickListener{
                override fun EditClicked(blogItem: RecyclerItemModel) {
                    val intent = Intent(this@MyBlogsActivity,EditBlogActivity::class.java)
                    intent.putExtra("blogItem",blogItem)
                    startActivityForResult(intent, BLOG_EDIT_CODE)
                }

                override fun ReadMoreClicked(blogItem: RecyclerItemModel) {
                    val intent = Intent(this@MyBlogsActivity,ReadBlogActivity::class.java)
                    intent.putExtra("blogItem",blogItem)
                    startActivity(intent)
                }

                override fun DeleteClicked(blogItem: RecyclerItemModel) {
                    deleteBlog(blogItem)
                }

            })
        }

        recyclerView.adapter = adapter

        databaseReference = FirebaseDatabase.getInstance("https://weblogger-9e863-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("Blogs")
        databaseReference.addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val blogSavedList = ArrayList<RecyclerItemModel>()
                for(blogsnapshot in snapshot.children){
                    val blogSaved = blogsnapshot.getValue(RecyclerItemModel::class.java)
                    if(blogSaved!=null || userId == blogSaved?.userId) {
                        blogSavedList.add(blogSaved!!)
                    }
                }
                adapter.setData(blogSavedList)
            }

            override fun onCancelled(error: DatabaseError) {
            }

        })

    }

    private fun deleteBlog(blogItem: RecyclerItemModel) {
        val blogId = blogItem.blogPostId
        val reference = databaseReference.child(blogId!!)
        reference.removeValue()
            .addOnSuccessListener {
                Toast.makeText(this,"Blog deleted!",Toast.LENGTH_SHORT).show()
            }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == BLOG_EDIT_CODE && resultCode == Activity.RESULT_OK){}
    }
}