package com.estime.moon.dreamteam

class Notes {
    constructor()

    var title: String? = null
    var description: String? = null
    var timeStamp: Long = System.currentTimeMillis()

    constructor(notesTitle: String, notesDescription: String){
        title = notesTitle
        description = notesDescription
    }
}