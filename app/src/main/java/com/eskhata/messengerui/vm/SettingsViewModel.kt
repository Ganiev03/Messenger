package com.eskhata.messengerui.vm

import android.widget.EditText
import androidx.lifecycle.ViewModel
import com.eskhata.messengerui.R
import com.eskhata.messengerui.model.ImageTextItem
import com.eskhata.messengerui.model.PreferencesText
import com.eskhata.messengerui.model.SwitchImageItem
import com.eskhata.messengerui.model.TextImageTextItem
import com.eskhata.messengerui.model.User

class SettingsViewModel:ViewModel() {
    fun getString(userName:String) = listOf(
            SwitchImageItem(R.drawable.dark, "Dark Mode", true),
            TextImageTextItem(R.drawable.active_status, "Active Status", "On") ,
            TextImageTextItem(R.drawable.user_name, "Username", userName),
            TextImageTextItem(R.drawable.phone, "Phone", "+1 202 555 0147"),
            PreferencesText("PREFERENCES"),
            ImageTextItem(R.drawable.notifications, "Notifications & Sounds"),
            ImageTextItem(R.drawable.people_settings, "People"),
            ImageTextItem(R.drawable.bask_settings, "Back"),
        )

}
