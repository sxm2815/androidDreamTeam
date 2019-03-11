package com.estime.moon.dreamteam

class Message {
    constructor()

    var text: String? = null
    var user: String = "user:"
    var timeStamp: Long = System.currentTimeMillis()

    constructor(messageText: String){
        text = messageText
    }
}