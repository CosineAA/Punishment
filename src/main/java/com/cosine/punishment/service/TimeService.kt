package com.cosine.punishment.service

interface TimeService {

    fun timeDeserialization(value: String): List<String>

    fun getNowTime(): String

    fun getPlusDayTime(time: String, day: Int): String

    fun getPlusHourTime(time: String, hour: Int): String

    fun getPlusMinuteTime(time: String, minute: Int): String

    fun isTimeAfter(after: String): Boolean
}