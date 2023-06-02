package com.eskhata.messengerui.fragment

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import com.bumptech.glide.Glide
import com.eskhata.messengerui.model.User
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import kotlin.properties.Delegates

abstract class BaseFragment(val res: Int) : Fragment(res) {
     var currentUser: User? = null
    var firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    var bottomNavigationView: BottomNavigationView by Delegates.notNull()

    fun startTransaction(container: Int, fragment: Fragment): Boolean {
        parentFragmentManager.commit {
            replace(container, fragment)
            addToBackStack(null)
        }
        return true
    }
     fun currentUser(imageView: ImageView, textView: TextView) {
        val uid = FirebaseAuth.getInstance().uid ?: ""
        val ref = FirebaseDatabase.getInstance().getReference("/users/$uid")
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                currentUser = snapshot.getValue(User::class.java)
                Glide.with(imageView).load(currentUser?.profileImageUrl).into(imageView)
            }
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }
     fun initListeners(toolbar: MaterialToolbar) {
         toolbar.setNavigationOnClickListener {
             activity?.onBackPressedDispatcher?.onBackPressed()
         }
     }
    fun backSetOnClick(view: View){
        view.setOnClickListener {
            activity?.onBackPressedDispatcher?.onBackPressed()
        }
    }
}