package com.eskhata.messengerui.fragment

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import com.eskhata.messengerui.R
import com.google.android.material.navigation.NavigationBarView
import com.google.firebase.database.FirebaseDatabase


class MainFragment : BaseFragment(R.layout.main_fragment), NavigationBarView.OnItemSelectedListener {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bottomNavigationView = view.findViewById(R.id.bottomNavView)
        bottomNavigationView.setOnItemSelectedListener(this)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
       return when (item.itemId) {
            R.id.chat_bottom -> startTransaction(R.id.main_container,ChatsFragment())
            R.id.people_bottom -> startTransaction(R.id.main_container,PeopleFragment())
            R.id.settings_bottom -> startTransaction(R.id.main_container,SettingsFragment())
           else -> false
       }
     }

    override fun onResume() {
        super.onResume()
        when (bottomNavigationView.selectedItemId) {
            R.id.chat_bottom -> startTransaction(R.id.main_container,ChatsFragment())
            R.id.people_bottom -> startTransaction(R.id.main_container,PeopleFragment())
            R.id.settings_bottom -> startTransaction(R.id.main_container,SettingsFragment())
        }
    }
}
