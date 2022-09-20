package com.cosine.punishment.service

import org.bukkit.entity.Player

interface PunishService {

    fun addWarning(target: Player, code: Int)
    fun applyMute(target: Player, code: Int)
    fun applyBan(target: Player, code: Int)

    fun clearPlayerMute(target: Player)
    fun clearPlayerBan(target: Player)
}