package com.cosine.punishment.manager

import com.cosine.punishment.service.InstanceService
import com.cosine.punishment.service.PunishService
import com.cosine.punishment.util.getName
import org.bukkit.OfflinePlayer
import org.bukkit.entity.Player
import java.time.ZonedDateTime
import java.util.*
import kotlin.math.max

class PunishManager(private val instance: InstanceService) : PunishService {

    private val optionConfig = instance.optionFile.getConfig()
    private val playerConfig = instance.playerFile.getConfig()

    private val time = instance.timeManager
    private val message = instance.messageManager

    override fun addWarning(target: OfflinePlayer, code: Int) {
        val targetWarning = playerConfig.getInt("${target.uniqueId}.경고")
        val codeWarning = optionConfig.getInt("처벌.$code.경고")
        playerConfig.set("${target.uniqueId}.경고", (targetWarning + codeWarning))
        instance.playerFile.saveConfig()
    }

    override fun applyMute(target: OfflinePlayer, code: Int) {
        if (!optionConfig.contains("처벌.$code.뮤트")) return
        val targetMute: ZonedDateTime = if (playerConfig.contains("${target.uniqueId}.뮤트")) {
            time.getTimeFromString(playerConfig.getString("${target.uniqueId}.뮤트"))
        } else {
            time.getNowTime()
        }

        val punishMute = time.timeDeserialization(optionConfig.getString("처벌.$code.뮤트"))
        targetMute.plusDays(punishMute[0]).plusHours(punishMute[1]).plusMinutes(punishMute[2])

        playerConfig.set("${target.uniqueId}.뮤트", targetMute)
        instance.playerFile.saveConfig()
    }

    override fun applyBan(player: Player, target: OfflinePlayer, code: Int) {
        if (!optionConfig.contains("처벌.$code.밴")) return
        val targetBan: ZonedDateTime = if (playerConfig.contains("${target.uniqueId}.밴")) {
            time.getTimeFromString(playerConfig.getString("${target.uniqueId}.밴"))
        } else {
            time.getNowTime()
        }

        val punishReason = optionConfig.getString("처벌.$code.사유")
        val punishBan = time.timeDeserialization(optionConfig.getString("처벌.$code.밴"))
        targetBan.plusDays(punishBan[0]).plusHours(punishBan[1]).plusMinutes(punishBan[2])

        playerConfig.set("${target.uniqueId}.사유", punishReason)
        playerConfig.set("${target.uniqueId}.처리자", player.name)
        playerConfig.set("${target.uniqueId}.밴", targetBan)
        instance.playerFile.saveConfig()
    }

    override fun subtractWarning(target: OfflinePlayer, warning: Int) {
        val targetWarning = playerConfig.getInt("${target.uniqueId}.경고")
        playerConfig.set("${target.uniqueId}.경고", (targetWarning - warning))
        instance.playerFile.saveConfig()
    }

    override fun clearPlayerMute(target: OfflinePlayer) {
        if (playerConfig.contains("${target.uniqueId}.뮤트")) {
            playerConfig.set("${target.uniqueId}.뮤트", null)
            instance.playerFile.saveConfig()
        }
    }

    override fun clearPlayerBan(target: OfflinePlayer) {
        if (playerConfig.contains("${target.uniqueId}.밴")) {
            playerConfig.set("${target.uniqueId}.사유", null)
            playerConfig.set("${target.uniqueId}.처리자", null)
            playerConfig.set("${target.uniqueId}.밴", null)
            instance.playerFile.saveConfig()
        }
    }

    override fun defaultBanScreenMessage(target: UUID): String {
        val reason = playerConfig.getString("$target.사유")
        val manager = playerConfig.getString("$target.처리자")
        val time = playerConfig.getString("$target.밴")
        return message.defaultBanMessageReplacer(reason, manager, getName(target), time)
    }

    override fun maxWarningBanScreenMessage(target: UUID): String {
        val maxWarning = optionConfig.getInt("설정.최대경고")
        return message.maxWarningBanMessageReplacer(maxWarning.toString(), getName(target))
    }

}