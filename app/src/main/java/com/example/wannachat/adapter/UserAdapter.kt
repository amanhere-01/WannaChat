package com.example.wannachat.adapter

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.wannachat.R
import com.example.wannachat.activities.ChatActivity
import com.example.wannachat.databinding.UserListBinding
import com.example.wannachat.model.UserData
import com.google.firebase.storage.FirebaseStorage
import de.hdodenhof.circleimageview.CircleImageView

//class UserAdapter(private val userList : ArrayList<UserData>) : RecyclerView.Adapter<UserViewHolder>(){
class UserAdapter(private val userList : ArrayList<UserData>) : RecyclerView.Adapter<UserAdapter.UserViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.user_list,parent, false)
        return UserViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.binding.user.text= userList[position].username
        holder.binding.email.text= userList[position].email
//        holder.tvUserName.text = userList[position].username
//        holder.tvUserEmail.text = userList[position].email
        val uniqueID = userList[position].uid

        val imageRef = FirebaseStorage.getInstance().getReference("Profile/$uniqueID/pfp/")
        imageRef.downloadUrl.addOnSuccessListener { uri ->
            Glide.with(holder.itemView.context)
                .load(uri)
                .into(holder.binding.avatar)
        }.addOnFailureListener { exception ->
            Log.e("TAG", "Error getting download URL: $exception")
        }

        holder.itemView.setOnClickListener{
            val intent = Intent(holder.itemView.context, ChatActivity::class.java)
            intent.putExtra("name", userList[position].username)
            intent.putExtra("uid", userList[position].uid)
            holder.itemView.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    inner class UserViewHolder(itemView : View) :RecyclerView.ViewHolder(itemView){
        var binding : UserListBinding = UserListBinding.bind(itemView)
//        val tvUserName : TextView = itemView.findViewById(R.id.user)
//        val tvUserEmail : TextView= itemView.findViewById(R.id.email)
//        val avatar : CircleImageView = itemView.findViewById(R.id.ggss)

        }

    }



