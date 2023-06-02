package com.eskhata.messengerui.fragment

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import com.eskhata.messengerui.R
import com.eskhata.messengerui.vm.RegisterViewModel
import de.hdodenhof.circleimageview.CircleImageView

class RegisterFragment : BaseFragment(R.layout.fragment_register) {
    private val viewModel: RegisterViewModel by viewModels()
    private lateinit var editTextEmail: EditText
    private lateinit var editTextPassword: EditText
    private lateinit var editTextUser: EditText
    private lateinit var buttonRegistration: Button
    private lateinit var buttonSignIn: Button
    private lateinit var profilesImage: CircleImageView
    private var selectedPhotoUri: Uri? = null
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init(view)
        register()

        editTextPassword.doOnTextChanged { text, _, _, _ ->
            val colorId = if (checkNotNull(text).length < 7) {
                android.R.color.holo_red_light
            } else R.color.black
            editTextPassword.setTextColor(ContextCompat.getColor(view.context, colorId))
        }
        profilesImage.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, 0)
        }
        buttonRegistration.setOnClickListener {
            if (editTextEmail.text.toString() != "" && editTextPassword.text.toString() != "" && profilesImage != null && editTextUser.text.toString() != "") {
                val email = editTextEmail.text.toString()
                val password = editTextPassword.text.toString()
                firebaseAuth.createUserWithEmailAndPassword(email, password)
                    .addOnSuccessListener {
                        viewModel.uploadImageToFirebaseStorage(selectedPhotoUri!!,editTextUser)
                    }.addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            register()
                        }
                    }.addOnFailureListener {
                        Toast.makeText(context, it.message, Toast.LENGTH_LONG).show()
                    }
            } else {
                Toast.makeText(
                    context,
                    "Email и password не должны быть пустим",
                    Toast.LENGTH_LONG
                ).show()
            }
        }

        buttonSignIn.setOnClickListener {
            startTransaction(R.id.activity_container, SignInFragment())
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 0 && resultCode == Activity.RESULT_OK && data != null) {
            selectedPhotoUri = data.data
            val bitmap = MediaStore.Images.Media.getBitmap(
                requireContext().contentResolver,
                selectedPhotoUri
            )
            profilesImage.setImageBitmap(bitmap)
        }
    }

    private fun register() {
        if (firebaseAuth.currentUser != null) {
            parentFragmentManager.beginTransaction()
                .replace(R.id.activity_container, MainFragment())
                .commit()
        }
    }

    private fun init(view: View) {
        editTextEmail = view.findViewById(R.id.editEmail)
        profilesImage = view.findViewById(R.id.profiles_image)
        editTextPassword = view.findViewById(R.id.editPassword)
        buttonRegistration = view.findViewById(R.id.button_registration)
        buttonSignIn = view.findViewById(R.id.button_sign_in)
        editTextUser = view.findViewById(R.id.user_name)
    }
}