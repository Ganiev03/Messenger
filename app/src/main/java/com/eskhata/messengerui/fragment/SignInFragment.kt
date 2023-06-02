package com.eskhata.messengerui.fragment

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.eskhata.messengerui.R
import com.google.android.material.appbar.MaterialToolbar


class SignInFragment : BaseFragment(R.layout.sign_in_fragment) {

    private lateinit var editTextUser: EditText
    private lateinit var editTextPassword: EditText
    private lateinit var buttonSignIn: Button
    private lateinit var toolbar: MaterialToolbar
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        editTextPassword=view.findViewById(R.id.editPassword)
        editTextUser=view.findViewById(R.id.user_name)
        buttonSignIn=view.findViewById(R.id.button_sign_in)
        toolbar=view.findViewById(R.id.toolbar)
        initListeners(toolbar)
        buttonSignIn.setOnClickListener {
            if (editTextUser.text.toString() != "" && editTextPassword.text.toString() != "") {
                val editTextUser = editTextUser.text.toString()
                val password = editTextPassword.text.toString()
                firebaseAuth.signInWithEmailAndPassword(editTextUser, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            register()
                        }
                    }.addOnFailureListener {
                        Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                    }
            }
        }
    }
    private fun register() {
        if (firebaseAuth.currentUser != null) {
            parentFragmentManager.beginTransaction()
                .replace(R.id.activity_container, MainFragment())
                .commit()
        }
    }

}