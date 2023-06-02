package com.eskhata.messengerui.fragment

import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toolbar
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.eskhata.messengerui.R
import com.eskhata.messengerui.adapter.ProfileRecyclerAdapter
import com.eskhata.messengerui.model.User
import com.eskhata.messengerui.vm.ProfileViewModel
import com.google.android.material.appbar.MaterialToolbar
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView

class ProfileFragment : BaseFragment(R.layout.profile_fragment) {
    private val profileViewModel: ProfileViewModel by lazy { ViewModelProvider(this)[ProfileViewModel::class.java] }
    private lateinit var profileImageview: CircleImageView
    private lateinit var profileName: TextView
    private lateinit var toolbar: MaterialToolbar
    private var user: User? = null
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        profileImageview = view.findViewById(R.id.profile_imageview)
        profileName = view.findViewById(R.id.profile_name)
        toolbar = view.findViewById(R.id.back_profile)
        profileName.text=user?.userName
        Picasso.get().load(user?.profileImageUrl).into(profileImageview)
        profileImageview.setOnClickListener {
            startTransaction(R.id.activity_container,SeePhotoProfileFragment.newInstance(user!!))
        }
        initListeners(toolbar)
        val adapter by lazy { ProfileRecyclerAdapter(profileViewModel.getString()) }
        val chatRecyclerview: RecyclerView = view.findViewById(R.id.recycler_profile)
        chatRecyclerview.layoutManager = LinearLayoutManager(context)
        chatRecyclerview.adapter = adapter
    }



    companion object {
        fun newInstance(user: User): ProfileFragment {
            val fragment = ProfileFragment()
            fragment.user = user
            return fragment
        }
    }
}


