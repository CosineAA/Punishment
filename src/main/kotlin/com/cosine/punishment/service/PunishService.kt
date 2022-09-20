package com.cosine.punishment.service

import org.bukkit.entity.Player
import java.util.UUID

interface PunishService {

    fun addWarning(target: UUID, code: Int)

    fun applyMute(target: UUID, code: Int)

    fun applyBan(player: Player, target: UUID, code: Int)

    fun subtractWarning(target: UUID, warning: Int)

    fun clearPlayerMute(target: UUID)

    fun clearPlayerBan(target: UUID)
    
    fun defaultBanScreenMessage(target: UUID): String

    fun maxWarningBanScreenMessage(target: UUID): String

    fun getWordInReasonList(word: String): List<Int>
}