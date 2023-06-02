package com.eskhata.messengerui.activity

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentContainerView
import com.eskhata.messengerui.R
import com.eskhata.messengerui.fragment.MainFragment
import com.eskhata.messengerui.fragment.RegisterFragment
import com.eskhata.messengerui.fragment.SignInFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class MainActivity : AppCompatActivity(R.layout.activity_main) {
    lateinit var fragmentContainer: FragmentContainerView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportFragmentManager.beginTransaction()
            .replace(R.id.activity_container, RegisterFragment())
            .commit()
        fragmentContainer = findViewById(R.id.activity_container)

    }

    private fun updateUserStatus(status: String, time: Long) {
        val uid = FirebaseAuth.getInstance().uid ?: ""
        val userRef = FirebaseDatabase.getInstance().getReference("/users/$uid")
        userRef.child("status").setValue(status)
        userRef.child("timeStatus").setValue(time)

    }


    override fun onResume() {
        super.onResume()
        val currentFragment = supportFragmentManager.findFragmentById(fragmentContainer.id)
        if (currentFragment is MainFragment) {
            updateUserStatus("online", -1)
        }
    }

    override fun onStop() {
        super.onStop()
        val currentFragment = supportFragmentManager.findFragmentById(fragmentContainer.id)
        if (currentFragment !is RegisterFragment && currentFragment !is SignInFragment ) {
            updateUserStatus("offline", System.currentTimeMillis())
        }
    }
}