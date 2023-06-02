package com.eskhata.messengerui.vm

import android.net.Uri
import android.widget.EditText
import androidx.lifecycle.ViewModel
import com.eskhata.messengerui.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import java.util.UUID

class RegisterViewModel : ViewModel() {
    fun uploadImageToFirebaseStorage(selectedPhotoUri: Uri, editTextUser: EditText) {
        if (selectedPhotoUri == null) return
        val fileName = UUID.randomUUID().toString()
        val ref = FirebaseStorage.getInstance().getReference("/images/$fileName")
        ref.putFile(selectedPhotoUri!!)
            .addOnSuccessListener {
                ref.downloadUrl.addOnSuccessListener {
                    saveUserToFirebaseStorage(it.toString(), editTextUser)
                }
            }
    }

    private fun saveUserToFirebaseStorage(profileImageUrl: String, editTextUser: EditText) {
        val uid = FirebaseAuth.getInstance().uid ?: ""
        val ref = FirebaseDatabase.getInstance().getReference("/users/$uid")
        val user = User(uid, editTextUser.text.toString(), profileImageUrl, "")
        ref.setValue(user)
    }
}