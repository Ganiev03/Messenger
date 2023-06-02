package com.eskhata.messengerui.vm

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.eskhata.messengerui.adapter.ContactRecyclerAdapter
import com.eskhata.messengerui.`interface`.CustomChildEventListener
import com.eskhata.messengerui.model.ChatMessages
import com.eskhata.messengerui.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase

class ContactViewModel : ViewModel(), CustomChildEventListener {
    private val _users: MutableLiveData<ArrayList<ChatMessages>> = MutableLiveData()
    val users: LiveData<ArrayList<ChatMessages>> =_users
    private fun refreshRecyclerViewMessages(snapshot:DataSnapshot) {
        val chatMessage = snapshot.getValue(ChatMessages::class.java) ?: return
        latestMessengerMap[checkNotNull(snapshot.key)] = chatMessage
        val dataList = latestMessengerMap.map { it.value } as ArrayList<ChatMessages>
        _users.postValue(dataList)
    }

    private val latestMessengerMap = HashMap<String, ChatMessages>()
    fun listenForLatestMessages() {
        val fromId = FirebaseAuth.getInstance().uid
        val ref = FirebaseDatabase.getInstance().getReference("/latest-messages/$fromId")
        ref.addChildEventListener(this)
    }
        override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
            refreshRecyclerViewMessages(snapshot)
        }
        override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
            refreshRecyclerViewMessages(snapshot)
        }
    }
