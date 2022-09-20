package com.cosine.punishment.manager

import com.cosine.punishment.service.InstanceService
import com.cosine.punishment.service.PunishService
import org.bukkit.entity.Player

class PunishManager(private val instance: InstanceService) : PunishService {

    private val optionConfig = instance.optionFile.getConfig()
    private val playerConfig = instance.playerFile.getConfig()

    private val time = instance.timeManager

    override fun addWarning(target: Player, code: Int) {
        val targetCount = playerConfig.getInt("${target.uniqueId}.경고")
        val codeCount = optionConfig.getInt("처벌.$code.경고")
        playerConfig.set("${target.uniqueId}.경고", (targetCount + codeCount))
        instance.playerFile.saveConfig()
    }

    override fun applyMute(target: Player, code: Int) {
        val targetMute = if (playerConfig.contains("${target.uniqueId}.뮤트")) {
            playerConfig.getString("${target.uniqueId}.뮤트")
        } else {
            time.getNowTime()
        }

        val punishMute = time.timeDeserialization(optionConfig.getString("처벌.$code.뮤트"))
        val punishMuteDay = punishMute[0].toInt()
        val punishMuteHour = punishMute[1].toInt()
        val punishMuteMinute = punishMute[2].toInt()

        time.apply {
            getPlusDayTime(targetMute, punishMuteDay)
            getPlusHourTime()
        }
    }

    override fun applyBan(target: Player, code: Int) {
        TODO("Not yet implemented")
    }

    override fun clearPlayerMute(target: Player) {
        TODO("Not yet implemented")
    }

    override fun clearPlayerBan(target: Player) {
        TODO("Not yet implemented")
    }

}