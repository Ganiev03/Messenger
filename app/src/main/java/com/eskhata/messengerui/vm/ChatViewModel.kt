package com.eskhata.messengerui.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.eskhata.messengerui.adapter.OnlineRecyclerAdapter
import com.eskhata.messengerui.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase

class ChatViewModel : BaseViewModel() {
    private val _users: MutableLiveData<ArrayList<User>> = MutableLiveData()
    val users: LiveData<ArrayList<User>> =_users
    private val onlineUsersHashMap = HashMap <String, User>()


    fun fetchUsers() {
        val ref = FirebaseDatabase.getInstance().getReference("/users")
        ref.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                setUserIfOnline(snapshot)
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                setUserIfOnline(snapshot)
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                TODO("Not yet implemented")
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                TODO("Not yet implemented")
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })

    }
    fun setUserIfOnline(snapshot: DataSnapshot){
        val user = snapshot.getValue(User::class.java)
        if (FirebaseAuth.getInstance().uid != user?.uid) {
            user?.apply {
                if (status == "online") {
                    onlineUsersHashMap[uid] = this

                }else onlineUsersHashMap.remove(uid)
                val onlineUsers = onlineUsersHashMap.map { (_, u)->
                    u
                } as ArrayList<User>
                _users.postValue(onlineUsers)
            }


        }
    }
}