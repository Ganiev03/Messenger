package com.eskhata.messengerui.fragment

import android.os.Bundle
import android.view.View

import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat.getSystemService
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import android.content.Context
import android.content.Context.INPUT_METHOD_SERVICE
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import com.eskhata.messengerui.R
import com.eskhata.messengerui.adapter.ChatRecyclerAdapter
import com.eskhata.messengerui.model.User
import com.eskhata.messengerui.vm.MessengerViewModel
import de.hdodenhof.circleimageview.CircleImageView
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class MessengerFragment : BaseFragment(R.layout.message_fragment) {
    private val viewModel: MessengerViewModel by lazy { ViewModelProvider(this)[MessengerViewModel::class.java] }
    private lateinit var editText: EditText
    private lateinit var toolbar: Toolbar
    private lateinit var chatUserName: TextView
    private lateinit var stateText: TextView
    private lateinit var chatRecyclerview: RecyclerView
    private lateinit var sendSms: Button
    private lateinit var buttonSmile: Button
    private lateinit var chatProfileImageview: CircleImageView
    private var user: User? = null
    private val adapter by lazy { ChatRecyclerAdapter() }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews(view)
        click()
        initListeners()
    }
    private fun initListeners() {
        toolbar.setNavigationOnClickListener {
            activity?.onBackPressedDispatcher?.onBackPressed()
        }
    }

    private fun initViews(view: View) {
        editText = view.findViewById(R.id.message_input)
        chatProfileImageview = view.findViewById(R.id.chat_profile_imageview)
        chatUserName = view.findViewById(R.id.chat_user_name)
        stateText = view.findViewById(R.id.state_text)
        buttonSmile = view.findViewById(R.id.button_smile)
        sendSms = view.findViewById(R.id.send_sms)
        toolbar = view.findViewById(R.id.toolbar)
        chatRecyclerview = view.findViewById(R.id.message_recyclerView)
    }

    private fun click() {
        chatUserName.text = user?.userName
        Glide.with(chatProfileImageview).load(user?.profileImageUrl).into(chatProfileImageview)
        viewModel.listenerForMessages(adapter, chatRecyclerview, user)
        chatRecyclerview.layoutManager = LinearLayoutManager(context)
        chatRecyclerview.adapter = adapter
        sendSms.setOnClickListener {
            viewModel.preFromSendMessage(user, editText, adapter, chatRecyclerview)
        }
        buttonSmile.setOnClickListener {

        }
        toolbar.setOnClickListener {
            startTransaction(R.id.activity_container, ProfileFragment.newInstance(user!!))
        }
        if (user?.status == "online") {
            stateText.text = "в сети"
        } else {
            val currentTimeMillis = user?.timeStatus
            val date = Date(currentTimeMillis!!)
            val format = SimpleDateFormat("HH:mm", Locale.getDefault())
            val timeString = format.format(date)
            stateText.text = "был(а) в ${timeString}"
        }
    }
    companion object {
        fun newInstance(user: User): MessengerFragment {
            val fragment = MessengerFragment()
            fragment.user = user
            return fragment
        }
    }
}
