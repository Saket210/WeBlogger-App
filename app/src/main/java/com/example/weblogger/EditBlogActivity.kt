package com.example.weblogger

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.weblogger.Model.RecyclerItemModel
import com.example.weblogger.databinding.ActivityEditBlogBinding
import com.google.firebase.database.FirebaseDatabase

class EditBlogActivity : AppCompatActivity() {
    private val binding:ActivityEditBlogBinding by lazy {
        ActivityEditBlogBinding.inflate(layoutInflater)
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

        binding.backBtn.setOnClickListener {
            finish()
        }

        val recyclerItemModel = intent.getParcelableExtra<RecyclerItemModel>("blogItem")

        binding.blogTitle.editText?.setText(recyclerItemModel?.title)
        binding.blogDescription.editText?.setText(recyclerItemModel?.blogContent)
        binding.postBlogBtn.setOnClickListener {
            val newTitle = binding.blogTitle.editText?.text.toString().trim()
            val newDescription = binding.blogDescription.editText?.text.toString().trim()
            if(newTitle.isEmpty() || newDescription.isEmpty())
            {
                Toast.makeText(this,"Please fill all details",Toast.LENGTH_SHORT).show()
            } else {
                recyclerItemModel?.title = newTitle
                recyclerItemModel?.blogContent = newDescription

                if(recyclerItemModel!=null){
                    updateFirebaseData(recyclerItemModel)
                }

            }
        }

    }

    private fun updateFirebaseData(recyclerItemModel: RecyclerItemModel) {
        val databaseReference = FirebaseDatabase.getInstance("https://weblogger-9e863-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("Blogs")
        val blogId = recyclerItemModel.blogPostId
        databaseReference.child(blogId!!).setValue(recyclerItemModel)
            .addOnSuccessListener {
                Toast.makeText(this,"Blog Updated!",Toast.LENGTH_SHORT).show()
                finish()
            }
    }
}