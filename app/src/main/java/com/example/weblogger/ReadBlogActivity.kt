package com.example.weblogger

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.weblogger.Model.RecyclerItemModel
import com.example.weblogger.databinding.ActivityReadBlogBinding

class ReadBlogActivity : AppCompatActivity() {
    private lateinit var binding:ActivityReadBlogBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityReadBlogBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        binding.backBtn1.setOnClickListener {
            finish()
        }
        val blog = intent.getParcelableExtra<RecyclerItemModel>("Item")
        if(blog!=null){
            binding.txtTitle.text = blog.title
            binding.txtName.text = blog.bloggerName
            binding.date.text = blog.date
            binding.txtDescription.text = blog.blogContent
            val profileurl = blog.profileUrl
            Glide.with(this)
                .load(profileurl)
                .apply(RequestOptions.circleCropTransform())
                .into(binding.profileImg)
        }
    }
}