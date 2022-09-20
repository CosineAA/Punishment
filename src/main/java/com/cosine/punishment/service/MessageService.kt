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
}