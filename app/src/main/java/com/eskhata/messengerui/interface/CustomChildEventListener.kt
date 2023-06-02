package com.eskhata.messengerui.`interface`

import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError

interface CustomChildEventListener : ChildEventListener {
    override fun onChildRemoved(snapshot: DataSnapshot) = Unit
    override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) = Unit
    override fun onCancelled(error: DatabaseError) = Unit
}