package com.eskhata.messengerui.fragment

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.eskhata.messengerui.R
import com.eskhata.messengerui.adapter.SettingsRecyclerAdapter
import com.eskhata.messengerui.model.User
import com.eskhata.messengerui.vm.SettingsViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import java.util.UUID


class SettingsFragment : BaseFragment(R.layout.setting_fragment),
    SettingsRecyclerAdapter.OnItemClickListener {
    private val settingsViewModel: SettingsViewModel by lazy { ViewModelProvider(this)[SettingsViewModel::class.java] }

    private lateinit var profileImageview: CircleImageView
    private lateinit var done: TextView
    private lateinit var nameUser: EditText
    private var selectedPhotoUri: Uri? = null
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val settingsAdapter by lazy { SettingsRecyclerAdapter(settingsViewModel.getString(currentUser?.userName.toString()), this
        )
        }
        profileImageview = view.findViewById(R.id.profiles_image)
        done = view.findViewById(R.id.done)
        nameUser = view.findViewById(R.id.name_user)
        currentUserName(profileImageview,nameUser)
        val chatRecyclerview: RecyclerView = view.findViewById(R.id.settings_recyclerView)
        chatRecyclerview.layoutManager = LinearLayoutManager(context)
        chatRecyclerview.adapter = settingsAdapter
        profileImageview.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, 0)
        }
        profileImageview.setOnLongClickListener {
            startTransaction(R.id.activity_container,SeePhotoProfileFragment.newInstance(currentUser!!))
        }
        done.setOnClickListener {
            uploadImageToFirebaseStorage()
            Toast.makeText(context, "ok", Toast.LENGTH_SHORT).show()
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
            profileImageview.setImageBitmap(bitmap)
        }
    }

    private fun uploadImageToFirebaseStorage() {
        if (selectedPhotoUri == null) return
        val fileName = UUID.randomUUID().toString()
        val ref = FirebaseStorage.getInstance().getReference("/images/$fileName")
        ref.putFile(selectedPhotoUri!!)
            .addOnSuccessListener {
                ref.downloadUrl.addOnSuccessListener {
                    saveUserToFirebaseStorage(it.toString())
                }
            }.addOnFailureListener {
                Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
            }
    }
    private fun currentUserName(imageView: ImageView, textView: EditText) {
        val uid = FirebaseAuth.getInstance().uid ?: ""
        val ref = FirebaseDatabase.getInstance().getReference("/users/$uid")
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                currentUser = snapshot.getValue(User::class.java)
                Glide.with(imageView).load(currentUser?.profileImageUrl).into(imageView)
                textView.hint = currentUser?.userName
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }
    private fun saveUserToFirebaseStorage(profileImageUrl: String) {
        val uid = FirebaseAuth.getInstance().uid ?: ""
        val ref = FirebaseDatabase.getInstance().getReference("/users/$uid")
        val user = User(uid, nameUser.text.toString(), profileImageUrl,"")
        ref.setValue(user)
            .addOnSuccessListener {
                Toast.makeText(context, "File is save", Toast.LENGTH_LONG).show()
            }
            .addOnFailureListener {
                Toast.makeText(context, it.message, Toast.LENGTH_LONG).show()
            }
    }

    override fun onItemClick(viewType: Int) {
        when (viewType) {
            7 -> {
                firebaseAuth.signOut()
                startTransaction(R.id.activity_container, RegisterFragment())
                updateUserStatus("offline", System.currentTimeMillis())
            }
        }
    }
    private fun updateUserStatus(status: String, time: Long) {
        val uid = FirebaseAuth.getInstance().uid ?: ""
        val userRef = FirebaseDatabase.getInstance().getReference("/users/$uid")
        userRef.child("status").setValue(status)
        userRef.child("timeStatus").setValue(time)
    }
}

