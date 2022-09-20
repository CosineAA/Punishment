package com.cosine.punishment.manager

import com.cosine.punishment.service.TimeService
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

class TimeManager : TimeService {

    private val location = "Asia/Seoul"

    override fun timeDeserialization(value: String): List<String> {
        return value.split(",")
    }

    override fun getNowTime(): String {
        return ZonedDateTime.now(ZoneId.of(location))
            .format(DateTimeFormatter.ofPattern("yyyy-MM-dd | HH:mm:ss"))
    }

    override fun getPlusDayTime(time: String, day: Int): String {
        return ZonedDateTime.now(ZoneId.of(location))
            .plusDays(day.toLong())
            .format(DateTimeFormatter.ofPattern("yyyy-MM-dd | HH:mm:ss"))
    }

    override fun getPlusHourTime(time: String, hour: Int): String {
        return ZonedDateTime.now(ZoneId.of(location))
            .plusHours(hour.toLong())
            .format(DateTimeFormatter.ofPattern("yyyy-MM-dd | HH:mm:ss"))
    }

    override fun getPlusMinuteTime(time: String, minute: Int): String {
        return ZonedDateTime.now(ZoneId.of(location))
            .plusMinutes(minute.toLong())
            .format(DateTimeFormatter.ofPattern("yyyy-MM-dd | HH:mm:ss"))
    }

    override fun isTimeAfter(after: String): Boolean {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd | HH:mm:ss")
        val now = ZonedDateTime.parse(getNowTime(), formatter)
        val future = ZonedDateTime.parse(after, formatter)
        return now.isAfter(future)
    }
}