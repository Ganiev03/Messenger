package com.eskhata.messengerui.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.eskhata.messengerui.R
import com.eskhata.messengerui.adapter.ContactRecyclerAdapter
import com.eskhata.messengerui.model.User
import com.eskhata.messengerui.vm.ContactViewModel


class ContactFragment : BaseFragment(R.layout.contact_fragment),
    ContactRecyclerAdapter.AdapterEvents {
    private val adapter by lazy { ContactRecyclerAdapter(this) }
    private val viewModel: ContactViewModel by viewModels()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init(view)
        viewModel.users.observe(viewLifecycleOwner){
            adapter.setUser(it)
        }
        viewModel.listenForLatestMessages()
    }

        override fun clickItemView(viewType: ContactRecyclerAdapter.ItemContactViewTypes,user: User?) {
        when (viewType) {
            ContactRecyclerAdapter.ItemContactViewTypes.ITEM_VIEW -> startTransaction(
                R.id.activity_container,
                MessengerFragment.newInstance(user!!)
            )
            ContactRecyclerAdapter.ItemContactViewTypes.IMAGE_VIEW -> startTransaction(
                R.id.activity_container,
                ProfileFragment.newInstance(user!!)
            )
        }
    }
    private fun init(view: View){
        val contactRecyclerview: RecyclerView = view.findViewById(R.id.contact_recyclerview)
        contactRecyclerview.layoutManager = LinearLayoutManager(context)
        contactRecyclerview.adapter = adapter

    }
}
