package com.mmanrique.community.event.aggregator.schedule

import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
class ScheduledTasks {
    @Scheduled(cron = "0 0 * * *")
    fun testSomething() {

    }
}