package com.eskhata.messengerui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.eskhata.messengerui.R
import com.eskhata.messengerui.model.BlockProfile
import com.eskhata.messengerui.model.ImageTextItem
import com.eskhata.messengerui.model.PreferencesText


class ProfileRecyclerAdapter(private val items: List<Any>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            DEFAULT_TYPE ->
                DefaultViewHolder(inflater.inflate(R.layout.profile_settings_item, parent, false))

            BLOCK_TYPE -> BlockImageViewHolder(
                inflater.inflate(
                    R.layout.profile_settings_item,
                    parent,
                    false
                )
            )

            PREFERENCES_TYPE ->
                PreferencesViewHolder(inflater.inflate(R.layout.preferences_item, parent, false))

            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = items[position]
        when (holder.itemViewType) {
            DEFAULT_TYPE -> {
                val textItem = item as ImageTextItem
                (holder as DefaultViewHolder).bind(textItem)
            }

            BLOCK_TYPE -> {
                val imageItem = item as BlockProfile
                (holder as BlockImageViewHolder).bind(imageItem)
            }

            PREFERENCES_TYPE -> {
                val imageItem = item as PreferencesText
                (holder as PreferencesViewHolder).bind(imageItem)
            }
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun getItemViewType(position: Int): Int {
        return when (items[position]) {
            is ImageTextItem -> DEFAULT_TYPE
            is BlockProfile -> BLOCK_TYPE
            is PreferencesText -> PREFERENCES_TYPE
            else -> throw IllegalArgumentException("Invalid item type")
        }
    }

    class DefaultViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val settingsIconItem: ImageView = itemView.findViewById(R.id.profile_icon_item)
        private val settingsTitle: TextView = itemView.findViewById(R.id.profile_title)
        fun bind(item: ImageTextItem) {
            settingsIconItem.setImageResource(item.iconImage)
            settingsTitle.text = item.title
        }
    }

    class PreferencesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val settingsTitle: TextView = itemView.findViewById(R.id.preferences_textview)
        fun bind(item: PreferencesText) {
            settingsTitle.text = item.title
        }
    }

    class BlockImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val settingsTitle: TextView = itemView.findViewById(R.id.profile_title)
        fun bind(item: BlockProfile) {
            settingsTitle.text = item.title
        }
    }

    companion object {
        private const val DEFAULT_TYPE = 0
        private const val BLOCK_TYPE = 1
        private const val PREFERENCES_TYPE = 2
    }
}
