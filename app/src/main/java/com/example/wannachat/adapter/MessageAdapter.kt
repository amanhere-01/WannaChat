package com.example.wannachat.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.wannachat.R
import com.example.wannachat.databinding.ReceivedMessageBinding
import com.example.wannachat.databinding.SentMessageBinding
import com.example.wannachat.model.Message
import com.google.firebase.auth.FirebaseAuth

class MessageAdapter(val context:Context, val messageList: ArrayList<Message>): RecyclerView.Adapter<RecyclerView.ViewHolder> (){

    private val ITEM_RECEIVE =1
    private val ITEM_SENT=2

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if(viewType==1){
            val view = LayoutInflater.from(parent.context).inflate(R.layout.received_message,parent,false)
            return ReceivedMessageHolder(view)
        }
        else{
            val view = LayoutInflater.from(parent.context).inflate(R.layout.sent_message,parent,false)
            return SentMessageHolder(view)
        }
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(holder.javaClass== SentMessageHolder::class.java){
            val viewHolder = holder as SentMessageHolder
            holder.binding.sentMsg.text= messageList[position].message
        }
        else{
            val viewHolder = holder as ReceivedMessageHolder
            holder.binding.recivdMsg.text= messageList[position].message
        }
    }

    override fun getItemViewType(position: Int): Int {
        val currentId = messageList[position].senderId
        return if(FirebaseAuth.getInstance().currentUser?.uid.equals(currentId)){
            ITEM_SENT
        } else{
            ITEM_RECEIVE
        }
    }

    override fun getItemCount(): Int {
        return messageList.size
    }

    inner class SentMessageHolder(itemView:View) : RecyclerView.ViewHolder(itemView){
        var binding: SentMessageBinding = SentMessageBinding.bind(itemView)
    }

    inner class ReceivedMessageHolder(itemView:View): RecyclerView.ViewHolder(itemView){
        var binding: ReceivedMessageBinding = ReceivedMessageBinding.bind(itemView)
    }
}