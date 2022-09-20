package com.cosine.punishment.manager

import com.cosine.punishment.service.InstanceService
import com.cosine.punishment.service.MessageService

class MessageManager(instance: InstanceService) : MessageService {

    private val minusWarningMessage = instance.optionFile.getConfig().getString("메시지.경고차감")
    private val clearMuteMessage = instance.optionFile.getConfig().getString("메시지.뮤트해제")
    private val clearBanMessage = instance.optionFile.getConfig().getString("메시지.차단해제")
    private val muteMessage = instance.optionFile.getConfig().getString("메시지.뮤트")

    private val punishMessage = instance.optionFile.getConfig().getStringList("메시지.처벌")
    private val defaultBanMessage = instance.optionFile.getConfig().getStringList("메시지.일반밴")
    private val maxWarningMessage = instance.optionFile.getConfig().getStringList("메시지.누적밴")

    override fun minusWarningReplacer(targetReplace: String, warningReplace: String): String {
        return minusWarningMessage.apply {
            replace("%target%", targetReplace)
            replace("%warning%", warningReplace)
        }
    }

    override fun clearMuteReplacer(targetReplace: String): String {
        return clearMuteMessage.apply { replace("%target%", targetReplace) }
    }

    override fun clearBanReplacer(targetReplace: String): String {
        return clearBanMessage.apply { replace("%target%", targetReplace) }
    }

    override fun punishMessageReplacer(
        targetReplace: String,
        managerReplace: String,
        punishReplace: String
    ): List<String> {
        val message = punishMessage
        for(value: String in message) {
            value.replace("%target%", targetReplace)
            value.replace("%manager%", managerReplace)
            value.replace("%punish%", punishReplace)
        }
        return message
    }

    override fun muteMessageReplacer(timeReplace: String): String {
        return muteMessage.replace("%time%", timeReplace)
    }

    override fun defaultBanMessageReplacer(
        reasonReplace: String,
        managerReplace: String,
        targetReplace: String,
        timeReplace: String
    ): String {
        val messageList = defaultBanMessage
        for (value: String in messageList) {
            value.replace("%reason%", reasonReplace)
            value.replace("%manager%", managerReplace)
            value.replace("%target%", targetReplace)
            value.replace("%time%", timeReplace)
        }
        val message = messageList.toString()
        message.apply {
            replace("[", "")
            replace("]", "")
            replace(", ", "\n")
        }
        return message
    }

    override fun maxWarningBanMessageReplacer(warningReplace: String, targetReplace: String): String {

    }

}