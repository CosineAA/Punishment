package com.cosine.punishment.manager

import com.cosine.punishment.service.TimeService
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

class TimeManager : TimeService {

    private val location = "Asia/Seoul"

    override fun timeDeserialization(value: String): List<Long> {
        val timeStringList = value.split(",")
        return timeStringList.map { it.toLong() }
    }

    override fun getNowTime(): ZonedDateTime {
        return ZonedDateTime.now(ZoneId.of(location))
    }

    override fun getTimeFromString(time: String): ZonedDateTime {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd | HH:mm:ss")
        return ZonedDateTime.parse(time, formatter)
    }

    override fun getStringFromTime(time: ZonedDateTime): String {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd | HH:mm:ss")
        return time.format(formatter)
    }

    override fun getKoreanFromTime(time: String): String {
        val targetTime = getTimeFromString(time)
        val year = targetTime.year
        val month = targetTime.monthValue
        val day = targetTime.dayOfMonth
        val hour = targetTime.hour
        val minute = targetTime.minute
        val second = targetTime.second
        return "${year}년 ${month}월 ${day}일 ${hour}시 ${minute}분 ${second}초"
    }

    override fun isTimeAfter(after: String): Boolean {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd | HH:mm:ss")
        val now = ZonedDateTime.parse(getNowTime().format(formatter), formatter)
        val future = ZonedDateTime.parse(after, formatter)
        return now.isAfter(future)
    }
}