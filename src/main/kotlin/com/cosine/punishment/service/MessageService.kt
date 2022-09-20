package com.cosine.punishment.service

interface MessageService {

    fun minusWarningReplacer(targetReplace: String, warningReplace: String): String

    fun clearMuteReplacer(targetReplace: String): String

    fun clearBanReplacer(targetReplace: String): String

    fun punishMessageReplacer(
        targetReplace: String,
        managerReplace: String,
        punishReplace: String
    ): List<String>

    fun muteMessageReplacer(timeReplace: String): String

    fun defaultBanMessageReplacer(
        reasonReplace: String,
        managerReplace: String,
        targetReplace: String,
        timeReplace: String
    ): String

    fun maxWarningBanMessageReplacer(
        warningReplace: String,
        targetReplace: String,
    ): String

    fun checkWarningReplacer(targetReplace: String, warningReplace: String): String

    fun checkPunishListReplacer(code: Int): List<String>
}