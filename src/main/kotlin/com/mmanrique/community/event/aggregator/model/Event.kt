package com.mmanrique.community.event.aggregator.model

data class Event(
    val id: String,
    val name: String,
    val confirmed: Long,
    val time: Long,
    val dateString:String,
    val city: String = "",
    val communityName: String,
    val url: String,
    var eventUrl:String = ""
){

}

