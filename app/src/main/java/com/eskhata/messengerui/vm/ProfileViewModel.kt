package com.eskhata.messengerui.vm

import androidx.lifecycle.ViewModel
import com.eskhata.messengerui.R
import com.eskhata.messengerui.model.BlockProfile
import com.eskhata.messengerui.model.ImageTextItem
import com.eskhata.messengerui.model.PreferencesText

class ProfileViewModel : ViewModel() {
    fun getString() = listOf(
        ImageTextItem(R.drawable.color_profile, "Color"),
        ImageTextItem(R.drawable.emoji, "Emoji"),
        ImageTextItem(R.drawable.arrow_right, "Nicknames"),
        PreferencesText("MORE ACTIONS"),
        ImageTextItem(R.drawable.search_profile, "Search in Conversation"),
        ImageTextItem(R.drawable.create_profile, "Create group"),
        PreferencesText("PRIVACY"),
        ImageTextItem(R.drawable.ignore_profile, "Ignore Messages"),
        BlockProfile( "Block")
    )
}