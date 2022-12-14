package com.cosine.punishment.manager

import com.cosine.punishment.service.InstanceService
import com.cosine.punishment.service.MessageService
import com.cosine.punishment.util.applyColor

class MessageManager(instance: InstanceService) : MessageService {

    private val optionConfig = instance.optionFile.getConfig()

    private val checkWarningMessage by lazy { applyColor(optionConfig.getString("메시지.경고확인")) }
    private val minusWarningMessage by lazy { applyColor(optionConfig.getString("메시지.경고차감")) }
    private val clearMuteMessage by lazy { applyColor(optionConfig.getString("메시지.뮤트해제")) }
    private val clearBanMessage by lazy { applyColor(optionConfig.getString("메시지.차단해제")) }
    private val muteMessage by lazy { applyColor(optionConfig.getString("메시지.뮤트")) }

    private val punishMessage by lazy { optionConfig.getStringList("메시지.처벌") }
    private val defaultBanMessage by lazy { optionConfig.getStringList("메시지.일반밴") }
    private val maxWarningMessage by lazy { optionConfig.getStringList("메시지.누적밴") }
    private val punishListMessage by lazy { optionConfig.getStringList("메시지.처벌목록") }

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
            applyColor(value)
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
            applyColor(value)
            value.replace("%reason%", reasonReplace)
            value.replace("%manager%", managerReplace)
            value.replace("%target%", targetReplace)
            value.replace("%time%", timeReplace)
        }
        return messageList.joinToString("\n")
    }

    override fun maxWarningBanMessageReplacer(warningReplace: String, targetReplace: String): String {
        val messageList = maxWarningMessage
        for (value: String in messageList) {
            applyColor(value)
            value.replace("%max%", warningReplace)
            value.replace("%target%", targetReplace)
        }
        return messageList.joinToString("\n")
    }

    override fun checkWarningReplacer(targetReplace: String, warningReplace: String): String {
        return checkWarningMessage.replace("%target%", targetReplace).replace("%warning%", warningReplace)
    }

    override fun checkPunishListReplacer(code: Int): List<String> {
        val reasonReplace = optionConfig.getString("처벌.$code.사유")
        val warningReplace = optionConfig.getString("처벌.$code.경고")

        val muteReplace = replaceTimeToString(code, "뮤트")
        val banReplace = replaceTimeToString(code, "밴")

        val message = punishListMessage
        for(value: String in message) {
            applyColor(value)
            value.replace("%reason%", reasonReplace)
            value.replace("%code%", code.toString())
            value.replace("%warning%", warningReplace)
            value.replace("%mute%", muteReplace)
            value.replace("%ban%", banReplace)
        }
        return message
    }

    private fun replaceTimeToString(code: Int, select: String): String {
        return if (optionConfig.contains("처벌.$code.$select")) {
            val time = optionConfig.getString("처벌.$code.$select").split(",")
            "${time[0]}일 ${time[1]}시 ${time[2]}분"
        } else {
            "X"
        }
    }
}