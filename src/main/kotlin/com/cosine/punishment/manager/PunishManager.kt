package com.cosine.punishment.manager

import com.cosine.punishment.service.InstanceService
import com.cosine.punishment.service.PunishService
import com.cosine.punishment.util.getName
import com.cosine.punishment.util.getPlayer
import com.cosine.punishment.util.isInt
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import java.time.ZonedDateTime
import java.util.*
import kotlin.collections.List
import kotlin.math.max

class PunishManager(private val instance: InstanceService) : PunishService {

    private val optionConfig = instance.optionFile.getConfig()
    private val playerConfig = instance.playerFile.getConfig()

    private val time = instance.timeManager
    private val message = instance.messageManager
    private val punish = instance.punishManager

    override fun addWarning(target: UUID, code: Int) {
        val targetWarning = playerConfig.getInt("$target.경고")
        val codeWarning = optionConfig.getInt("처벌.$code.경고")

        playerConfig.set("$target.경고", (targetWarning + codeWarning))
        instance.playerFile.saveConfig()
    }

    override fun applyMute(target: UUID, code: Int) {
        if (!optionConfig.contains("처벌.$code.뮤트")) return
        val targetMute: ZonedDateTime = if (playerConfig.contains("$target.뮤트")) {
            time.getTimeFromString(playerConfig.getString("$target.뮤트"))
        } else {
            time.getNowTime()
        }

        val punishMute = time.timeDeserialization(optionConfig.getString("처벌.$code.뮤트"))
        targetMute.plusDays(punishMute[0]).plusHours(punishMute[1]).plusMinutes(punishMute[2])

        playerConfig.set("$target.뮤트", targetMute)
        instance.playerFile.saveConfig()
    }

    override fun applyBan(player: Player, target: UUID, code: Int) {
        if (!optionConfig.contains("처벌.$code.밴")) return
        val targetBan: ZonedDateTime = if (playerConfig.contains("$target.밴")) {
            time.getTimeFromString(playerConfig.getString("$target.밴"))
        } else {
            time.getNowTime()
        }

        val punishReason = optionConfig.getString("처벌.$code.사유")
        val punishBan = time.timeDeserialization(optionConfig.getString("처벌.$code.밴"))
        targetBan.plusDays(punishBan[0]).plusHours(punishBan[1]).plusMinutes(punishBan[2])

        playerConfig.set("$target.사유", punishReason)
        playerConfig.set("$target.처리자", player.name)
        playerConfig.set("$target.밴", targetBan)
        instance.playerFile.saveConfig()

        banPlayerWithWarning(target)
    }

    override fun subtractWarning(target: UUID, warning: Int) {
        val targetWarning = playerConfig.getInt("$target.경고")
        playerConfig.set("$target.경고", (targetWarning - warning))
        instance.playerFile.saveConfig()
    }

    override fun clearPlayerMute(target: UUID) {
        if (playerConfig.contains("$target.뮤트")) {
            playerConfig.set("$target.뮤트", null)
            instance.playerFile.saveConfig()
        }
    }

    override fun clearPlayerBan(target: UUID) {
        if (playerConfig.contains("$target.밴")) {
            playerConfig.set("$target.사유", null)
            playerConfig.set("$target.처리자", null)
            playerConfig.set("$target.밴", null)
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

    override fun getWordInReasonList(word: String): List<Int> {
        val codeList = mutableListOf<Int>()
        for(code: String in optionConfig.getConfigurationSection("처벌").getKeys(false)) {
            if (code.contains("예시코드")) continue
            val reason = optionConfig.getString("처벌.$code.사유")
            if (isInt(code) && reason.contains(word)) {
                codeList.add(code.toInt())
            } else {
                continue
            }
        }
        return codeList
    }

    override fun banPlayerWithWarning(target: UUID) {
        val targetWarning = playerConfig.getInt("$target.경고")
        val maxWarning = optionConfig.getInt("설정.최대경고")
        if (targetWarning >= maxWarning) {
            if (Bukkit.getPlayer(target) != null) {
                getPlayer(target).kickPlayer(punish.maxWarningBanScreenMessage(target))
            }
        } else {
            if (Bukkit.getPlayer(target) != null) {
                getPlayer(target).kickPlayer(punish.defaultBanScreenMessage(target))
            }
        }
    }

}