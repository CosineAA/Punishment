package com.cosine.punishment.util

import org.bukkit.Bukkit
import org.bukkit.OfflinePlayer
import org.bukkit.entity.Player
import java.util.UUID

// internal lateinit var plugins: Exchange
fun Player.sendMessages(vararg message: String?) = message.filterNotNull().forEach(::sendMessage)
fun getPlayer(uuid: UUID): Player = Bukkit.getPlayer(uuid)
fun isInt(str: String): Boolean {
    return try {
        str.toInt()
        true
    } catch (e: NumberFormatException) {
        false
    }
}

fun getName(uuid: UUID): String = Bukkit.getOfflinePlayer(uuid).name
fun applyColor(message: String): String = message.replace("&", "§")