package com.example.wannachat.activities

import android.content.Intent

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import android.util.Log
import android.util.Patterns

import android.widget.Toast

import com.example.wannachat.model.UserData
import com.example.wannachat.databinding.ActivitySignUpBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class SignUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding
    private lateinit var auth : FirebaseAuth
    private val database= Firebase.database("https://wannachat-96be5-default-rtdb.asia-southeast1.firebasedatabase.app/")
    private val nameRef = database.getReference("UserData")
    private lateinit var showName: String



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = Firebase.auth

        binding.SignIn.setOnClickListener{
            startActivity(Intent(this,SignInActivity::class.java))
        }

        binding.btnSignUp.setOnClickListener{
            val email = binding.inputEmail.text.toString()
            val passcode = binding.inputPassword.text.toString()
            if(checkFields()){
                auth.createUserWithEmailAndPassword(email, passcode).addOnCompleteListener{
                    if(it.isSuccessful){
                        saveUserData()
                        showToast("Account created")
                        val intent = Intent(this,ProfileActivity::class.java)
                        intent.putExtra("key_name", showName)
                        startActivity(intent)

                    }
                    else{
                        Log.e("ERROR",it.exception.toString())
                    }
                }
            }

        }
    }



    private fun saveUserData(){
        if (checkFields()){
            val email = binding.inputEmail.text.toString()
            val nameText = binding.inputName.text.toString()
            val uid = auth.currentUser?.uid.toString()
            showName= nameText
            val saveData = UserData(nameText, email, uid)
            nameRef.child(uid).setValue(saveData)

        }
    }

    private fun checkFields() : Boolean  {
        val email = binding.inputEmail.text.toString()
        val passcode = binding.inputPassword.text.toString()
        val confrmPasscode = binding.confirmPassword.text.toString()

        if (binding.inputName.text.toString()==""){
            showToast("Please enter name")
            return false
        }
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
        if (confrmPasscode==""){
            showToast("Please confirm your password")
            return false
        }
        if(confrmPasscode!=passcode){
            showToast("Password must be same ")
            return false
        }
        else{
        return true}
    }

    private fun showToast(message : String){
        Toast.makeText(this , message, Toast.LENGTH_SHORT).show()
    }


}
