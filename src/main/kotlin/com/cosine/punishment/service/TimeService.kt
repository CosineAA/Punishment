package com.cosine.punishment.service

import java.time.ZonedDateTime

interface TimeService {

    fun timeDeserialization(value: String): List<Long>

    fun getNowTime(): ZonedDateTime

    fun getTimeFromString(time: String): ZonedDateTime

    fun getStringFromTime(time: ZonedDateTime): String

    fun getKoreanFromTime(time: String): String

    fun isTimeAfter(after: String): Boolean
}