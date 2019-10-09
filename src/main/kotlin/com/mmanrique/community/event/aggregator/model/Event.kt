package com.mmanrique.community.event.aggregator.model

data class Event(
    val id: String,
    val name: String,
    val confirmed: Long,
    val time: Long,
    val utcOffset: Long,
    val city: String = "",
    val communityName: String,
    val url: String,
    var eventUrl:String = ""
){

}

