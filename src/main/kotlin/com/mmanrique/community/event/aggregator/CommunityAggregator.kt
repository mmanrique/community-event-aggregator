package com.mmanrique.community.event.aggregator

import com.beust.klaxon.JsonArray
import com.beust.klaxon.JsonObject
import com.beust.klaxon.Parser
import com.mmanrique.community.event.aggregator.model.Event
import org.apache.http.client.methods.HttpGet
import org.apache.http.impl.client.HttpClientBuilder
import org.springframework.stereotype.Component


@Component
class CommunityAggregator {

    fun meetUpData(uri: String): JsonObject {
        val formattedUrl = java.lang.String.format(API_MEETUP_URL, uri)
        val client = HttpClientBuilder.create().build()
        val get = HttpGet(formattedUrl)
        val response = client.execute(get)
        val responseReader = response.entity.content.reader()
        return Parser.default().parse(responseReader) as JsonObject
    }

    fun isMeetupUrl(url: String): Boolean {
        return url.startsWith("https://www.meetup.com/")
    }

    fun extractMeetUpUri(url: String): String? {
        if (isMeetupUrl(url)) {
            val split: List<String> = url.split("/")
            val offset = if (url.endsWith("/")) 2 else 1
            return split[split.size - offset]
        }
        return null;
    }
    fun getMeetupEventUrl(communityUrl:String, eventId:String): String {
        return communityUrl + "events/" + eventId
    }

    fun getCommunities(): JsonArray<JsonObject> {
        val client = HttpClientBuilder.create().build()
        val get = HttpGet(COMMUNITIES_JSON)
        val response = client.execute(get)
        val responseReader = response.entity.content.reader()
        return Parser.default().parse(responseReader) as JsonArray<JsonObject>

    }

    fun aggregateData():List<Event> {
        val communities = getCommunities()
        val events = mutableListOf<Event>()
        for (community in communities) {
            val url = community.string("url")
            val name = community.string("name")
            val city = community.string("city")
            val meetupUri = url?.let { extractMeetUpUri(it) }
            val meetupData = meetupUri?.let { meetUpData(it) }
            val nextEvent: JsonObject? = meetupData?.obj("next_event")
            val communityEvent = nextEvent?.let { convertToEvent(it, city!!, name!!, url) }
            communityEvent?.let { events += it }
        }
        return events
    }

    fun convertToEvent(o: JsonObject, city: String, communityName: String, url: String): Event {
        val id = o.string("id")
        val name = o.string("name")
        val yesRsvpCount = o.long("yes_rsvp_count")
        val time = o.long("time")
        val utcOffset = o.long("utc_offset")
        val eventUrl = getMeetupEventUrl(url, id!!)
        return Event(id, name!!, yesRsvpCount!!, time!!, utcOffset!!, city, communityName, url, eventUrl)
    }


    companion object {
        val COMMUNITIES_JSON =
            "https://raw.githubusercontent.com/peruanosdev/peruanos.github.io/master/_data/communities.json"
        val API_MEETUP_URL = "https://api.meetup.com/%s/"
    }
}