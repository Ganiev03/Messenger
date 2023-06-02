package com.eskhata.messengerui.vm

import android.widget.EditText
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.RecyclerView
import com.eskhata.messengerui.adapter.ChatRecyclerAdapter
import com.eskhata.messengerui.`interface`.CustomChildEventListener
import com.eskhata.messengerui.model.ChatFrom
import com.eskhata.messengerui.model.ChatMessages
import com.eskhata.messengerui.model.ChatTo
import com.eskhata.messengerui.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.FirebaseDatabase

class MessengerViewModel : ViewModel() {
    fun listenerForMessages(adapter: ChatRecyclerAdapter, recyclerView: RecyclerView, user: User?) {
        val fromId = FirebaseAuth.getInstance().uid
        val toId = user?.uid
        val ref = FirebaseDatabase.getInstance().getReference("/user-messages/$fromId/$toId")
        ref.addChildEventListener(object : CustomChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val chatMessage = snapshot.getValue(ChatMessages::class.java)
                if (chatMessage?.fromId == FirebaseAuth.getInstance().uid) {
                    adapter.setUser(ChatTo(chatMessage?.text, user, chatMessage?.timestamp))
                } else {
                    adapter.setUser(ChatFrom(chatMessage?.text, user, chatMessage?.timestamp))
                    recyclerView.scrollToPosition(adapter.itemCount - 1)
                }
            }
            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) = Unit
        })
    }

    fun preFromSendMessage(
        user: User?,
        editText: EditText,
        adapter: ChatRecyclerAdapter,
        recyclerView: RecyclerView
    ) {
        val text = editText.text.toString()
        val fromId = FirebaseAuth.getInstance().uid
        val toId = user?.uid
        if (fromId == null) return
        val reference =
            FirebaseDatabase.getInstance().getReference("/user-messages/$fromId/$toId").push()
        val toReference =
            FirebaseDatabase.getInstance().getReference("/user-messages/$toId/$fromId").push()
        if (text.isEmpty() || text.isBlank()) return
        val chatMessage =
            ChatMessages(reference.key!!, text, fromId, toId!!, System.currentTimeMillis())
        reference.setValue(chatMessage)
            .addOnSuccessListener {
                editText.text.clear()
                recyclerView.scrollToPosition(adapter.itemCount - 1)
            }
        toReference.setValue(chatMessage)
        val latestMessengerRef =
            FirebaseDatabase.getInstance().getReference("/latest-messages/$fromId/$toId")
        latestMessengerRef.setValue(chatMessage)
        val latestMessengerToRef =
            FirebaseDatabase.getInstance().getReference("/latest-messages/$toId/$fromId")
        latestMessengerToRef.setValue(chatMessage)
    }
}