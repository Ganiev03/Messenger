package com.eskhata.messengerui.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.eskhata.messengerui.R
import com.eskhata.messengerui.model.User
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView


class PeopleRecyclerAdapter(private val onClickItem: AdapterEvents) :
    RecyclerView.Adapter<PeopleRecyclerAdapter.MyViewHolder>() {
    private var items = ArrayList<User>()

    fun setUser(user:ArrayList<User>) {
        items=user
        this.notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.people_item, parent, false)
        return MyViewHolder(itemView)
    }
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val state: User = items[position]
        holder.namePeople.text = state.userName
        Picasso.get().load(state.profileImageUrl).into(holder.profileImageItem)
        holder.waveImageItem.setImageResource(R.drawable.wave)
        if (state.status=="online"){
            holder.stateIcon.setImageResource(R.drawable.online_icon)
        }else holder.stateIcon.setImageDrawable(null)
        holder.itemView.setOnClickListener { onClickItem.clickItemView(ItemViewTypes.ITEM_VIEW,state) }
        holder.profileImageItem.setOnClickListener { onClickItem.clickItemView(ItemViewTypes.PROFILE_CLICK,state) }
        holder.waveImageItem.setOnClickListener { onClickItem.clickItemView(ItemViewTypes.WAVE_CLICK,state) }
        Log.d("onBindView", "onBindViewHolder: $items")
    }

    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val namePeople: TextView = view.findViewById(R.id.name_people)
        val profileImageItem: CircleImageView = view.findViewById(R.id.people_image_item)
        val waveImageItem: ImageView = view.findViewById(R.id.wave)
        val stateIcon: ImageView = view.findViewById(R.id.online_icon_people)
    }

    override fun getItemCount(): Int = items.size
    interface AdapterEvents {
        fun clickItemView(viewType: ItemViewTypes, user:User)
    }
    enum class ItemViewTypes {
        PROFILE_CLICK,
        ITEM_VIEW,
        WAVE_CLICK
    }
}
