package com.example.weblogger

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.weblogger.Adapter.RecyclerAdapter
import com.example.weblogger.Model.RecyclerItemModel
import com.example.weblogger.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.Locale

class MainActivity : AppCompatActivity() {
    private val binding:ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    private lateinit var databaseReference : DatabaseReference
    private val blogs = mutableListOf<RecyclerItemModel>()
    private lateinit var auth:FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.profileImg.setOnClickListener {
            startActivity(Intent(this,ProfileActivity::class.java))
        }

        binding.imageButton.setOnClickListener {
            startActivity(Intent(this,SavedBlogsActivity::class.java))
        }

        auth = FirebaseAuth.getInstance()
        databaseReference = FirebaseDatabase.getInstance("https://weblogger-9e863-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("Blogs")

        val userId = auth.currentUser?.uid
        Log.d("main","outside finish main activity ${auth.currentUser}")

        if(userId!=null){
            loadProfileImage(userId)
        } else {
            Log.d("main","finish main activity")
            finish()
        }

        val recyclerView = binding.recyclerView
        val adapter = RecyclerAdapter(blogs)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        databaseReference.addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot){
                blogs.clear()
                for (snapshot in snapshot.children){
                    val item = snapshot.getValue(RecyclerItemModel::class.java)
                    if(item!=null){
                        blogs.add(item)
                    }
                }
                blogs.reverse()
                adapter.showFilteredList(blogs)
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })

        binding.searchBlog.setOnQueryTextListener(object :SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                val newBlogList = mutableListOf<RecyclerItemModel>()
                for (blogs in blogs) {
                    if (blogs.title?.lowercase(Locale.ROOT)!!.contains(newText!!)) {
                        newBlogList.add(blogs)
                    }
                }
                adapter.showFilteredList(newBlogList)
                return true
            }
        })




        binding.addItemButton.setOnClickListener {
            startActivity(Intent(this,AddBlogActivity::class.java))
        }

    }

    private fun loadProfileImage(userId: String) {
        val userReference = FirebaseDatabase.getInstance("https://weblogger-9e863-default-rtdb.asia-southeast1.firebasedatabase.app").reference.child("Users").child(userId)
        userReference.child("profileUrl").addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val profileUrl = snapshot.getValue(String::class.java)
                if(profileUrl != null){
                    Glide.with(this@MainActivity)
                        .load(profileUrl)
                        .into(binding.profileImg)
                } else {
                    binding.profileImg.setImageResource(R.drawable.icon_profile)
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }

    override fun onResume() {
        super.onResume()
        if(auth.currentUser == null) {
            val intent = Intent(this,WelcomeActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent)
            finish()
        }
    }
}