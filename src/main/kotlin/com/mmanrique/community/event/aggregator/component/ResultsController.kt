package com.mmanrique.community.event.aggregator.component

import com.mmanrique.community.event.aggregator.CommunityAggregator
import com.mmanrique.community.event.aggregator.model.Event
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ResponseBody

@Controller
class ResultsController {

    @Autowired
    lateinit var communityAggregator: CommunityAggregator

    @GetMapping("/", produces = ["application/json"])
    @CrossOrigin
    @ResponseBody
    fun returnSomething(): List<Event> {
        return communityAggregator.aggregateData()
    }
}
