package com.example.wannachat.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.wannachat.adapter.MessageAdapter
import com.example.wannachat.adapter.UserAdapter
import com.example.wannachat.model.UserData
import com.example.wannachat.databinding.ActivityChatBinding
import com.example.wannachat.model.Message
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage


class ChatActivity : AppCompatActivity() {
    private lateinit var auth : FirebaseAuth
    private lateinit var binding: ActivityChatBinding
    private val database = Firebase.database("https://wannachat-96be5-default-rtdb.asia-southeast1.firebasedatabase.app/")
    private val ref=database.getReference("message")
    private lateinit var msgList : ArrayList<Message>
    private var senderRoom: String? = null
    private var receiverRoom: String? = null
    private val senderUid=FirebaseAuth.getInstance().currentUser?.uid

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth
        val senderUid= auth.currentUser?.uid.toString()
        chatUser()
        showChat()
        binding.sendBtn.setOnClickListener{
            if(binding.msgBox.text.toString()!=""){
                sendFunc()
            }
        }

        binding.info.setOnClickListener {
            Toast.makeText(this, "Profile" , Toast.LENGTH_SHORT).show()
        }
        binding.backUser.setOnClickListener {
            startActivity(Intent(this, ShowUserActivity::class.java))
        }
    }

    private fun showChat(){
        val receiverUid = intent.getStringExtra("uid")
        senderRoom= receiverUid + senderUid
        msgList = arrayListOf()
        val rv = binding.chatRecyclerView
        rv.layoutManager = LinearLayoutManager(this@ChatActivity)
        val adaptr = MessageAdapter(this@ChatActivity,msgList)
        rv.adapter = adaptr
        ref.child(senderRoom!!).child("chat")
            .addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {

                    msgList.clear()
                    if(snapshot.exists()){

                        for (data in snapshot.children){
                            val model = data.getValue(Message::class.java)
                            msgList.add(model!!)
                        }
                    }

                    adaptr.notifyDataSetChanged()
                }


                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
    }
    private fun sendFunc(){
        val receiverUid = intent.getStringExtra("uid")
        senderRoom= receiverUid + senderUid
        receiverRoom = senderUid + receiverUid
        val message= binding.msgBox.text.toString()
        val messageObj = Message(message,senderUid)
        ref.child(senderRoom!!).child("chat").push()
            .setValue(messageObj).addOnSuccessListener{
                ref.child(receiverRoom!!).child("chat").push()
                    .setValue(messageObj)

            }
        binding.msgBox.setText("")
    }

    private fun chatUser(){
        val receiverUid = intent.getStringExtra("uid")
        val userName = intent.getStringExtra("name")
        binding.chatName.text = userName

        val imageRef = FirebaseStorage.getInstance().getReference("Profile/$receiverUid/pfp/")
        imageRef.downloadUrl.addOnSuccessListener {uri ->
            Glide.with(this)
                .load(uri)
                .into(binding.chatPfp)
        }.addOnFailureListener{exception ->
            Log.e("PFP", "Chat user pfp error: $exception "  )
        }
    }



    }

