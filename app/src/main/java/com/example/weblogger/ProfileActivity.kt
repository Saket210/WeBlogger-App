package com.example.weblogger

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.example.weblogger.databinding.ActivityProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ProfileActivity : AppCompatActivity() {
    private val binding: ActivityProfileBinding by lazy {
        ActivityProfileBinding.inflate(layoutInflater)
    }
    private lateinit var databaseReference:DatabaseReference
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.btnLogout.setOnClickListener{
            auth.signOut()
            val intent = Intent(this,WelcomeActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent)
            finish()
        }

        binding.btnMyblogs.setOnClickListener{
            startActivity(Intent(this,MyBlogsActivity::class.java))
        }

        auth = FirebaseAuth.getInstance()
        databaseReference = FirebaseDatabase.getInstance("https://weblogger-9e863-default-rtdb.asia-southeast1.firebasedatabase.app").reference.child("Users")

        val userId = auth.currentUser?.uid
        if(userId!=null){
            val userReference = databaseReference.child(userId)
            userReference.child("profileUrl").addValueEventListener(object :ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    val profileUrl = snapshot.getValue(String::class.java)
                    if(profileUrl!=null){
                        Glide.with(this@ProfileActivity)
                            .load(profileUrl)
                            .into(binding.profilePic)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                }
            })

            userReference.child("name").addValueEventListener(object :ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    val profileName = snapshot.getValue(String::class.java)
                    if(profileName!=null){
                        binding.profileName.text = profileName
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                }

            })

        }

    }
}