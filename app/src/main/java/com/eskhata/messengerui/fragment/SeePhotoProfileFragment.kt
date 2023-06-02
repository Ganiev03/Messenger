package com.eskhata.messengerui.fragment

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.eskhata.messengerui.R
import com.eskhata.messengerui.model.User
import com.google.android.material.appbar.MaterialToolbar
import com.squareup.picasso.Picasso


class SeePhotoProfileFragment : BaseFragment(R.layout.fragment_see_photo_profile) {
    lateinit var profilePhotoSee: ImageView
    lateinit var toolbar: MaterialToolbar
    private var user: User? = null
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        profilePhotoSee = view.findViewById(R.id.profile_photo_see)
        toolbar = view.findViewById(R.id.back_photo)
        initListeners(toolbar)
         Picasso.get().load(user?.profileImageUrl).into(profilePhotoSee)
    }
    companion object {
        fun newInstance(user: User): SeePhotoProfileFragment {
            val fragment = SeePhotoProfileFragment()
            fragment.user = user
            return fragment
        }
    }
}