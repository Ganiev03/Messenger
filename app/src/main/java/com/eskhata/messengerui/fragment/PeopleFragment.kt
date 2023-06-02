package com.eskhata.messengerui.fragment

import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.eskhata.messengerui.R
import com.eskhata.messengerui.adapter.PeopleRecyclerAdapter
import com.eskhata.messengerui.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import de.hdodenhof.circleimageview.CircleImageView

class PeopleFragment : BaseFragment(R.layout.people_fragment), PeopleRecyclerAdapter.AdapterEvents {
    private val adapter by lazy { PeopleRecyclerAdapter(this) }
    private lateinit var peopleCurrentPhoto: CircleImageView
    private lateinit var peopleCurrentName: TextView
    private val onlineUsersHashMap = HashMap <String, User>()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fetchUsers()
        peopleCurrentPhoto = view.findViewById(R.id.people_current_photo)
        peopleCurrentName = view.findViewById(R.id.people_current_name)
        currentUser(peopleCurrentPhoto,peopleCurrentName)
        val peopleRecyclerView: RecyclerView =
            view.findViewById(R.id.people_recycler)
        peopleRecyclerView.layoutManager = LinearLayoutManager(context)
        peopleRecyclerView.adapter = adapter
    }

    private fun fetchUsers() {
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
                    onlineUsersHashMap[uid] = this
                val onlineUsers = onlineUsersHashMap.map { (_, u)->
                    u
                } as ArrayList<User>
             adapter.setUser(onlineUsers)
            }
        }

    }
    override fun clickItemView(viewType: PeopleRecyclerAdapter.ItemViewTypes,user:User) {
        when (viewType) {
            PeopleRecyclerAdapter.ItemViewTypes.ITEM_VIEW -> startTransaction(
                R.id.activity_container,
                MessengerFragment.newInstance(user)
            )

            PeopleRecyclerAdapter.ItemViewTypes.PROFILE_CLICK -> startTransaction(
                R.id.activity_container,
                ProfileFragment()
            )

            PeopleRecyclerAdapter.ItemViewTypes.WAVE_CLICK -> Toast.makeText(
                context,
                "Hi",
                Toast.LENGTH_SHORT
            ).show()
        }
    }


}
