package com.eskhata.messengerui.fragment

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.eskhata.messengerui.R
import com.eskhata.messengerui.adapter.OnlineRecyclerAdapter
import com.eskhata.messengerui.model.OnlineFirstIcon
import com.eskhata.messengerui.model.User
import com.eskhata.messengerui.vm.ChatViewModel
import de.hdodenhof.circleimageview.CircleImageView

class ChatsFragment : BaseFragment(R.layout.chats_fragment), OnlineRecyclerAdapter.AdapterEvents {
    private val adapter by lazy { OnlineRecyclerAdapter(this) }
    private val viewModel: ChatViewModel by viewModels()
    private lateinit var chatCurrentPhoto: CircleImageView
    private lateinit var chatCurrentName: TextView
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        startTransaction(R.id.chat_container, ContactFragment())
        init(view)
        initObservers()
    }

    override fun onClickItem(user: User) {
        startTransaction(R.id.activity_container, MessengerFragment.newInstance(user))
    }

    private fun init(view: View) {
        chatCurrentPhoto = view.findViewById(R.id.chat_current_photo)
        chatCurrentName = view.findViewById(R.id.chat_current_name)
        val chatRecyclerview: RecyclerView = view.findViewById(R.id.chat_recyclerview)
        currentUser(chatCurrentPhoto, chatCurrentName)
        chatRecyclerview.adapter = adapter
        chatRecyclerview.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
    }
    private fun initObservers() {
        viewModel.fetchUsers()
        viewModel.users.observe(viewLifecycleOwner) { users ->
            adapter.setUser(users)
        }
    }
}

