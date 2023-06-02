package com.eskhata.messengerui.model

class ChatMessages (
    val id: String="",
    val text: String="",
    val fromId: String="",
    val toID: String="",
    val timestamp: Long= -1,
    val profileImageUrl:String? = ""
)
