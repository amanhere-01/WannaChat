package com.example.wannachat.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import com.example.wannachat.databinding.ActivitySignInBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class SignInActivity : AppCompatActivity() {
    private lateinit var auth : FirebaseAuth
    private lateinit var binding: ActivitySignInBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = Firebase.auth

        binding.textSignUp.setOnClickListener{
            val intent = Intent(this,SignUpActivity::class.java)
            startActivity(intent)
        }

        binding.btnSignIn.setOnClickListener{
            val email = binding.inputEmail.text.toString()
            val passcode = binding.inputPassword.text.toString()
            if(checkFields()) {
                auth.signInWithEmailAndPassword(email, passcode)
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
                            showToast("Login Successful")
                            startActivity(Intent(this, ShowUserActivity::class.java))
                            finish()    // finish this activity
                        } else {
                            showToast("Check email and password")
                            Log.e("ERROR", it.exception.toString())
                        }
                    }
            }
        }
    }

    private fun checkFields() : Boolean  {
        val email = binding.inputEmail.text.toString()
        val passcode = binding.inputPassword.text.toString()

        if(email==""){
            showToast("Please enter email")
            return false
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            showToast("Check your email")
            return false
        }
        if(passcode==""){
            showToast("Please enter password")
            return false
        }
        else{
            return true}
    }
    private fun showToast(message : String){
        Toast.makeText(this , message, Toast.LENGTH_SHORT).show()
    }


}