package com.eskhata.messengerui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.eskhata.messengerui.R
import com.eskhata.messengerui.model.User

class OnlineRecyclerAdapter(private val onClickItem: AdapterEvents) :
    RecyclerView.Adapter<OnlineRecyclerAdapter.MyViewHolder>() {

    private var items = ArrayList<User>()

    fun setUser(user: ArrayList<User>) {
        items = user
        this.notifyDataSetChanged()
    }


        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
            val itemView =
                LayoutInflater.from(parent.context).inflate(R.layout.online_item, parent, false)
            return MyViewHolder(itemView)
        }


    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val state: User = items[position]
        holder.nickTextViewOnline.text = state.userName
        Glide.with(holder.imageView).load(state.profileImageUrl).into(holder.imageView)
        holder.onlineIcon.setImageResource(R.drawable.online_icon)
        holder.imageView.setOnClickListener {
            onClickItem.onClickItem(state)
        }
    }

    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nickTextViewOnline: TextView = view.findViewById(R.id.nick_textView_online)
        val imageView: ImageView = view.findViewById(R.id.online_imageView)
        val onlineIcon: ImageView = view.findViewById(R.id.online_icon)
    }


    override fun getItemCount(): Int = items.size

    interface AdapterEvents {
        fun onClickItem(user: User)
    }

}
