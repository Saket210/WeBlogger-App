package com.example.weblogger

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.transition.Visibility
import com.example.weblogger.databinding.ActivityLoginRegisterBinding

class LoginRegisterActivity : AppCompatActivity() {
    val binding:ActivityLoginRegisterBinding by lazy {
        ActivityLoginRegisterBinding.inflate(layoutInflater)
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

        val actionType = intent.getStringExtra("actiontype")

        if(actionType == "login"){
            binding.registerName.visibility = View.GONE
            binding.registerEmail.visibility = View.GONE
            binding.registerPassword.visibility = View.GONE
            binding.loginEmail.visibility = View.VISIBLE
            binding.loginPassword.visibility = View.VISIBLE
            binding.registerButton.isClickable = false
            binding.registerButton.alpha = 0.5f
        } else if (actionType == "register") {
            binding.loginButton.isClickable = false
            binding.loginButton.alpha = 0.5f
        }

    }
}