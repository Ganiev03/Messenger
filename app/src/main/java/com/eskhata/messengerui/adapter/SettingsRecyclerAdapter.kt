package com.eskhata.messengerui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.SwitchCompat
import androidx.recyclerview.widget.RecyclerView
import com.eskhata.messengerui.R
import com.eskhata.messengerui.model.ImageTextItem
import com.eskhata.messengerui.model.PreferencesText
import com.eskhata.messengerui.model.SwitchImageItem
import com.eskhata.messengerui.model.TextImageTextItem

class SettingsRecyclerAdapter(
    private val items: List<Any>,
    private val onItemClickListener: OnItemClickListener?
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val DEFAULT_TYPE = 0
        const val SWITCH_TYPE = 1
        const val IMAGE_TEXT_TYPE = 2
        const val PREFERENCES_TYPE = 3
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            DEFAULT_TYPE ->
                DefaultViewHolder(inflater.inflate(R.layout.settings_item, parent, false))

            SWITCH_TYPE -> SwitchImageViewHolder(
                inflater.inflate(
                    R.layout.switch_item,
                    parent,
                    false
                )
            )

            IMAGE_TEXT_TYPE ->
                ImageTextViewHolder(inflater.inflate(R.layout.settings_item, parent, false))

            PREFERENCES_TYPE ->
                PreferencesViewHolder(inflater.inflate(R.layout.preferences_item, parent, false))

            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = items[position]
        when (holder.itemViewType) {
            DEFAULT_TYPE -> {
                val textItem = item as TextImageTextItem
                (holder as DefaultViewHolder).apply {
                    bind(textItem)
                    itemView.setOnClickListener { _ ->
                        onItemClickListener?.onItemClick(position)
                    }
                }
            }

            SWITCH_TYPE -> {
                val imageItem = item as SwitchImageItem
                (holder as SwitchImageViewHolder).apply {
                    bind(imageItem)
                    itemView.setOnClickListener { _ ->
                        onItemClickListener?.onItemClick(position)
                    }
                }
            }

            IMAGE_TEXT_TYPE -> {
                val imageItem = item as ImageTextItem
                (holder as ImageTextViewHolder).apply {
                    bind(imageItem)
                    itemView.setOnClickListener { _ ->
                        onItemClickListener?.onItemClick(position)
                    }
                }
            }

            PREFERENCES_TYPE -> {
                val imageItem = item as PreferencesText
                (holder as PreferencesViewHolder).apply {
                    bind(imageItem)
                    itemView.setOnClickListener { _ ->
                        onItemClickListener?.onItemClick(position)
                    }
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun getItemViewType(position: Int): Int {
        return when (items[position]) {
            is TextImageTextItem -> DEFAULT_TYPE
            is SwitchImageItem -> SWITCH_TYPE
            is ImageTextItem -> IMAGE_TEXT_TYPE
            is PreferencesText -> PREFERENCES_TYPE
            else -> throw IllegalArgumentException("Invalid item type")
        }
    }

    class DefaultViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val settingsIconItem: ImageView = itemView.findViewById(R.id.settings_icon_item)
        private val settingsTitle: TextView = itemView.findViewById(R.id.settings_title)
        private val settingsDescription: TextView = itemView.findViewById(R.id.settings_description)
        fun bind(item: TextImageTextItem) {
            settingsIconItem.setImageResource(item.iconImage)
            settingsTitle.text = item.title
            settingsDescription.text = item.textView
        }
    }

    class ImageTextViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val settingsIconItem: ImageView = itemView.findViewById(R.id.settings_icon_item)
        private val settingsTitle: TextView = itemView.findViewById(R.id.settings_title)

        fun bind(item: ImageTextItem) {
            settingsIconItem.setImageResource(item.iconImage)
            settingsTitle.text = item.title
//            settingsImageText.setImageResource(item.icon)
        }
    }

    class PreferencesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val settingsTitle: TextView = itemView.findViewById(R.id.preferences_textview)
        fun bind(item: PreferencesText) {
            settingsTitle.text = item.title
        }
    }

    class SwitchImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val settingsIconItem: ImageView = itemView.findViewById(R.id.state_icon_item)
        private val settingsTitle: TextView = itemView.findViewById(R.id.state_title)
        private val switch: SwitchCompat = itemView.findViewById(R.id.state)

        fun bind(item: SwitchImageItem) {
            settingsIconItem.setImageResource(item.iconImage)
            settingsTitle.text = item.title
            switch.isChecked = item.state
        }
    }

    fun interface OnItemClickListener {
        fun onItemClick(viewType: Int)
    }
}