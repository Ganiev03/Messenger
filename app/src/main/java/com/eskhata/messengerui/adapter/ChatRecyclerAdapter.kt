package com.eskhata.messengerui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.eskhata.messengerui.R
import com.eskhata.messengerui.model.ChatFrom
import com.eskhata.messengerui.model.ChatTo
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class ChatRecyclerAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var items = ArrayList<Any>()
    fun setUser(user: Any) {
        items.add(user)
        this.notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            TO_TYPE ->
                ToChatViewHolder(inflater.inflate(R.layout.chat_to_item, parent, false))

            FROM_TYPE -> FromChatViewHolder(
                inflater.inflate(
                    R.layout.chat_from_item,
                    parent,
                    false
                )
            )

            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = items[position]
        when (holder.itemViewType) {
            TO_TYPE -> {
                val textItem = item as ChatTo
                (holder as ToChatViewHolder).bind(textItem)
            }

            FROM_TYPE -> {
                val text = item as ChatFrom
                (holder as FromChatViewHolder).bind(text)
            }
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun getItemViewType(position: Int): Int {
        return when (items[position]) {
            is ChatTo -> TO_TYPE
            is ChatFrom -> FROM_TYPE
            else -> throw IllegalArgumentException("Invalid item type")
        }
    }

    class ToChatViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textChat: TextView = itemView.findViewById(R.id.text_to_messeng)
        private val timeFromMessenger: TextView = itemView.findViewById(R.id.time_to_messeng)
        private val profileImage: ImageView = itemView.findViewById(R.id.profiles_to_image)
        fun bind(item: ChatTo) {
            textChat.text = item.text
            val currentTimeMillis = item.time
            val date = Date(currentTimeMillis!!)
            val format = SimpleDateFormat("HH:mm", Locale.getDefault())
            val timeString = format.format(date)
            timeFromMessenger.text = timeString

        }
    }

    class FromChatViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textChat: TextView = itemView.findViewById(R.id.text_from_messeng)
        private val timeFromMessenger: TextView = itemView.findViewById(R.id.time_from_messeng)
        private val profileImage: CircleImageView = itemView.findViewById(R.id.profiles_from_image)
        fun bind(item: ChatFrom) {
            textChat.text = item.text
            Picasso.get().load(item.user?.profileImageUrl).into(profileImage)
            val currentTimeMillis = item.time
            val date = Date(currentTimeMillis!!)
            val format = SimpleDateFormat("HH:mm", Locale.getDefault())
            val timeString = format.format(date)
            timeFromMessenger.text = timeString
        }
    }

    companion object {
        private const val TO_TYPE = 0
        private const val FROM_TYPE = 1
    }
}
