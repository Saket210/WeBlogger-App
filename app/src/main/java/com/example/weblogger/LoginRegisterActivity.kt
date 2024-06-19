package com.example.weblogger

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.weblogger.Model.UserData
import com.example.weblogger.databinding.ActivityLoginRegisterBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage

class LoginRegisterActivity : AppCompatActivity() {
    val binding:ActivityLoginRegisterBinding by lazy {
        ActivityLoginRegisterBinding.inflate(layoutInflater)
    }
    private lateinit var auth:FirebaseAuth
    private lateinit var database:FirebaseDatabase
    private lateinit var storage:FirebaseStorage
    private val PICK_IMAGE_REQUEST = 1
    private var profileUri:Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance("https://weblogger-9e863-default-rtdb.asia-southeast1.firebasedatabase.app")
        storage = FirebaseStorage.getInstance()

        val actionType = intent.getStringExtra("actiontype")

        if(actionType == "login"){
            binding.registerName.visibility = View.GONE
            binding.registerEmail.visibility = View.GONE
            binding.registerPassword.visibility = View.GONE
            binding.loginEmail.visibility = View.VISIBLE
            binding.loginPassword.visibility = View.VISIBLE
            binding.registerButton.isClickable = false
            binding.registerButton.alpha = 0.5f

            binding.loginButton.setOnClickListener {
                val loginEmail = binding.loginEmail.text.toString()
                val loginPassword = binding.loginPassword.text.toString()
                if (loginEmail.isEmpty() || loginPassword.isEmpty()){
                    Toast.makeText(this,"Please provide all details",Toast.LENGTH_SHORT).show()
                } else {
                    auth.signInWithEmailAndPassword(loginEmail,loginPassword)
                        .addOnCompleteListener { task ->
                            if(task.isSuccessful){
                                Toast.makeText(this,"Login Successful!",Toast.LENGTH_SHORT).show()
                                startActivity(Intent(this,MainActivity::class.java))
                                finish()
                            } else {
                                Toast.makeText(this,"Please enter correct details",Toast.LENGTH_SHORT).show()
                            }
                        }
                }
            }

        } else if (actionType == "register") {
            binding.loginButton.isClickable = false
            binding.loginButton.alpha = 0.5f
            binding.registerButton.setOnClickListener {
                val registerName = binding.registerName.text.toString()
                val registerEmail = binding.registerEmail.text.toString()
                val registerPassword = binding.registerPassword.text.toString()

                if(registerName.isEmpty() || registerEmail.isEmpty() || registerPassword.isEmpty()){
                    Toast.makeText(this,"Please provide all details",Toast.LENGTH_SHORT).show()
                } else {
                    auth.createUserWithEmailAndPassword(registerEmail,registerPassword)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                Toast.makeText(this,"Registration Successful!",Toast.LENGTH_SHORT).show()
                                startActivity(Intent(this,WelcomeActivity::class.java))
                                val user = auth.currentUser
                                auth.signOut()
                                user?.let {
                                    val userReference = database.getReference("Users")
                                    val userId = user.uid
                                    val userData = UserData(
                                        registerName,
                                        registerEmail
                                    )
                                    userReference.child(userId).setValue(userData)

                                    val storageReference = storage.reference.child("ProfilePic/$userId.jpg")
                                    storageReference.putFile(profileUri!!)
                                    finish()
                                }
                            } else {
                                Toast.makeText(this,"Please enter correct details",Toast.LENGTH_SHORT).show()
                            }
                        }
                }
            }

        }

        binding.cardView.setOnClickListener {
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(Intent.createChooser(intent,"Select Profile Pic"),
                PICK_IMAGE_REQUEST
            )

        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data!= null && data.data!=null)
            profileUri = data.data
            Glide.with(this)
                .load(profileUri)
                .apply(RequestOptions.circleCropTransform())
                .into(binding.registerImage)
    }
}