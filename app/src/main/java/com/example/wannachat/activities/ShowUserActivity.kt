package com.example.wannachat.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.wannachat.R
import com.example.wannachat.adapter.UserAdapter
import com.example.wannachat.model.UserData
import com.example.wannachat.databinding.ActivityShowUserBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase

class ShowUserActivity : AppCompatActivity() {
    private lateinit var binding: ActivityShowUserBinding
    private lateinit var auth: FirebaseAuth

    //private val database = Firebase.database("https://wannachat-96be5-default-rtdb.asia-southeast1.firebasedatabase.app/")
    //  private val ref = database.getReference("Username")
    private lateinit var listUser: ArrayList<UserData>
    private lateinit var dbRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShowUserBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = Firebase.auth
        getData()
        val menu_option: ImageView = findViewById(R.id.showoption)
        binding.showoption.setOnClickListener {
            showOptionsMenu(menu_option)
        }

    }

    private fun showOptionsMenu(anchorView: View) {
        val popupMenu = PopupMenu(this, anchorView)
        popupMenu.inflate(R.menu.option_menu)

        popupMenu.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.setting -> {
                    Toast.makeText(this, "Setting Selected", Toast.LENGTH_SHORT).show()
                    true
                }

                R.id.logOut -> {
                    auth.signOut()
                    startActivity((Intent(this, SignInActivity::class.java)))
                    true
                }

                R.id.exit -> {
                    this.finishAffinity()
                    true
                }

                else -> false

            }
        }
        popupMenu.show()
    }

    private fun getData() {
        dbRef =
            FirebaseDatabase.getInstance("https://wannachat-96be5-default-rtdb.asia-southeast1.firebasedatabase.app/")
                .getReference("UserData")
        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    listUser = arrayListOf()
                    for (data in snapshot.children) {
                        val model = data.getValue(UserData::class.java)

                        if (model!!.email != auth.currentUser?.email) {
                            listUser.add(model)
                        }
                    }
                    //list.shuffle()
                    val rv = binding.recyclerView
                    rv.layoutManager = LinearLayoutManager(this@ShowUserActivity)
                    val addapter = UserAdapter(listUser)
                    rv.adapter = addapter
//                    addapter.setOnItemClickListener(object : UserAdapter.onItemClickListener {
//                        override fun onItemClick(userData: UserData) {
//                            //Toast.makeText(this@ShowUserActivity,"Clicked on item no $position", Toast.LENGTH_SHORT).show()
//                            val intent = Intent(this@ShowUserActivity, ChatActivity::class.java)
//                            intent.putExtra("userData", userData)
//                            startActivity(intent)
//                        }
//                    })

                    rv.addItemDecoration(
                        DividerItemDecoration(
                            baseContext,
                            LinearLayoutManager(
                                this@ShowUserActivity,
                                LinearLayoutManager.VERTICAL,
                                false
                            ).orientation
                        )
                    )
                    addapter.notifyDataSetChanged()


                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("dbError", error.toString())
            }
        })
    }

    public override fun onStart() {
        super.onStart()
        // to check if user is signed in or not
        val currentUser = auth.currentUser
        updateUI(currentUser)
    }

    private fun updateUI(user: FirebaseUser?) {
        if (user != null) {
            val uid = auth.currentUser?.uid.toString()
            //Toast.makeText(this,uid,Toast.LENGTH_SHORT).show()
        } else {

            startActivity(Intent(this, SignInActivity::class.java))
        }
    }

}





