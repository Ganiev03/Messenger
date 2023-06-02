package com.eskhata.messengerui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.eskhata.messengerui.R
import com.eskhata.messengerui.model.ChatMessages
import com.eskhata.messengerui.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class ContactRecyclerAdapter(private val onClickItem: AdapterEvents) :
    RecyclerView.Adapter<ContactRecyclerAdapter.MyViewHolder>() {
    private var items = ArrayList<ChatMessages>()

    fun setUser(users: ArrayList<ChatMessages>) {
        items = users
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.contact_item, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ContactRecyclerAdapter.MyViewHolder, position: Int) {
        val state: ChatMessages = items[position]
        var user: User?
        val chatParentId: String
        state.let {
            if (state.fromId == FirebaseAuth.getInstance().uid) {
                chatParentId = state.toID
            } else chatParentId = state.fromId
        }
        val ref = FirebaseDatabase.getInstance().getReference("/users/$chatParentId")
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                user = snapshot.getValue(User::class.java)
                holder.nickTextView.text = user?.userName
                Glide.with(holder.imageView).load(user?.profileImageUrl).into(holder.imageView)
                if (user?.status == "online") {
                    holder.stateIcon.setImageResource(R.drawable.online_icon)
                } else holder.stateIcon.setImageDrawable(null)
                holder.imageView.setOnClickListener {
                    onClickItem.clickItemView(
                        ItemContactViewTypes.IMAGE_VIEW,
                        user
                    )
                }
                holder.itemView.setOnClickListener {
                    onClickItem.clickItemView(
                        ItemContactViewTypes.ITEM_VIEW,
                        user
                    )
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
//        if (user?.status=="online"){
//            holder.stateIcon.setImageResource(R.drawable.online_icon)
//        }
        val currentTimeMillis = state.timestamp
        val date = Date(currentTimeMillis)
        val format = SimpleDateFormat("HH:mm", Locale.getDefault())
        val timeString = format.format(date)
        if (state.fromId == FirebaseAuth.getInstance().uid) {
            holder.heTextView.text = "You: ${state.text} ·$timeString"
        } else {
            holder.heTextView.text =  "${state.text} ·$timeString"
        }
    }

    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nickTextView: TextView = view.findViewById(R.id.nick_textview)
        val heTextView: TextView = view.findViewById(R.id.he_textview)
        val stateIcon: ImageView = view.findViewById(R.id.online_icon_contact)
        val imageView: ImageView = view.findViewById(R.id.image_contact)
    }

    override fun getItemCount(): Int = items.size
    interface AdapterEvents {
        fun clickItemView(viewType: ItemContactViewTypes, user: User?)
    }

    enum class ItemContactViewTypes {
        IMAGE_VIEW,
        ITEM_VIEW
    }
}