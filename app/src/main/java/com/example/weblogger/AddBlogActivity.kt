package com.example.weblogger

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.weblogger.Model.RecyclerItemModel
import com.example.weblogger.Model.UserData
import com.example.weblogger.databinding.ActivityAddBlogBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.text.SimpleDateFormat
import java.util.Date

class AddBlogActivity : AppCompatActivity() {
    private val binding:ActivityAddBlogBinding by lazy {
        ActivityAddBlogBinding.inflate(layoutInflater)
    }
    private val databaseReference:DatabaseReference = FirebaseDatabase.getInstance("https://weblogger-9e863-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("Blogs")
    private val userReference:DatabaseReference = FirebaseDatabase.getInstance("https://weblogger-9e863-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("Users")
    private val auth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.backBtn.setOnClickListener {
            finish()
        }

        binding.postBlogBtn.setOnClickListener {
            val title = binding.blogTitle.editText?.text.toString().trim()
            val content = binding.blogDescription.editText?.text.toString().trim()

            if(title.isEmpty() || content.isEmpty()){
                Toast.makeText(this,"Please fill both fields",Toast.LENGTH_SHORT).show()
            }

            val user:FirebaseUser? = auth.currentUser
            if(user!=null){
                val userId = user.uid
//                val userName= user.displayName?:"Anonymous"
//                val profileUrl = user.photoUrl?:""

                userReference.child(userId).addListenerForSingleValueEvent(object :ValueEventListener{
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val userData = snapshot.getValue(UserData::class.java)
                        if(userData!=null){
                            val nameDb = userData.name
                            val imageDb = userData.profileUrl

                            val dateTime = SimpleDateFormat("yyyy-MM-dd").format(Date())

                            val blogItem = RecyclerItemModel(
                                title,
                                imageDb,
                                nameDb,
                                userId,
                                dateTime,
                                content,
                                0
                            )
                            val uniqueKey = databaseReference.push().key
                            if (uniqueKey!=null){
                                blogItem.blogPostId=uniqueKey
                                val blogReference = databaseReference.child(uniqueKey)
                                blogReference.setValue(blogItem).addOnCompleteListener {
                                    if(it.isSuccessful){
                                        finish()
                                    } else {
                                        Toast.makeText(this@AddBlogActivity,"Failed to Post Blog",Toast.LENGTH_SHORT).show()
                                    }
                                }
                            }
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {

                    }

                })

            }
        }

    }
}