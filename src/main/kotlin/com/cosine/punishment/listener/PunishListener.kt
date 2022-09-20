package com.cosine.punishment.listener

import com.cosine.punishment.service.InstanceService
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.AsyncPlayerChatEvent
import org.bukkit.event.player.AsyncPlayerPreLoginEvent
import org.bukkit.event.player.PlayerJoinEvent

class PunishListener(private val instance: InstanceService) : Listener {

    private val optionConfig = instance.optionFile.getConfig()
    private val playerConfig = instance.playerFile.getConfig()

    private val message = instance.messageManager
    private val time = instance.timeManager
    private val punish = instance.punishManager

    private val prefix by lazy { optionConfig.getString("메시지.접두사") }

    @EventHandler
    fun onJoin(event: PlayerJoinEvent) {
        val player = event.player
        if (!playerConfig.contains(player.uniqueId.toString())) {
            playerConfig.set("${player.uniqueId}.경고", 0)
            instance.playerFile.saveConfig()
        }
    }

    @EventHandler
    fun onChat(event: AsyncPlayerChatEvent) {
        val player = event.player
        if (!playerConfig.contains("${player.uniqueId}.뮤트")) return
        val playerMute = playerConfig.getString("${player.uniqueId}.뮤트")
        if (!time.isTimeAfter(playerMute)) {
            event.isCancelled = true
            player.sendMessage("$prefix ${message.muteMessageReplacer(time.getKoreanFromTime(playerMute))}")
            return
        }
        punish.clearPlayerMute(player)
    }

    @EventHandler
    fun onPreJoin(event: AsyncPlayerPreLoginEvent) {
        val uuid = event.uniqueId
        val name = event.playerProfile.name

        val playerWarning = playerConfig.getInt("$uuid.경고")
        val maxWarning = optionConfig.getInt("설정.최대경고")

        if (playerWarning >= maxWarning) {
            event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_BANNED, punish.maxWarningBanScreenMessage(uuid))
        }
    }
}