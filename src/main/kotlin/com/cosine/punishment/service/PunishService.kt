package com.cosine.punishment.service

import org.bukkit.OfflinePlayer
import org.bukkit.entity.Player
import java.util.UUID

interface PunishService {

    fun addWarning(target: OfflinePlayer, code: Int)
    fun applyMute(target: OfflinePlayer, code: Int)
    fun applyBan(player: Player, target: OfflinePlayer, code: Int)

    fun subtractWarning(target: OfflinePlayer, warning: Int)
    fun clearPlayerMute(target: OfflinePlayer)
    fun clearPlayerBan(target: OfflinePlayer)
    
    fun defaultBanScreenMessage(target: UUID): String
    fun maxWarningBanScreenMessage(target: UUID): String
}