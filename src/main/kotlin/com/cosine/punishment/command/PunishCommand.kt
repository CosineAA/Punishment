package com.cosine.punishment.command

import com.cosine.punishment.service.InstanceService
import com.cosine.punishment.util.getName
import com.cosine.punishment.util.isInt
import com.cosine.punishment.util.sendMessages
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import java.util.UUID

class PunishCommand(private val instance: InstanceService) : CommandExecutor {

    private val optionConfig = instance.optionFile.getConfig()
    private val playerConfig = instance.playerFile.getConfig()

    private val message = instance.messageManager
    private val punish = instance.punishManager

    private val prefix by lazy { optionConfig.getString("메시지.접두사") }

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (sender is Player) {
            val player: Player = sender
            if (!player.isOp || !player.hasPermission("punish")) return false
            if (args.isEmpty()) {
                help(player)
                return false
            }
            when (args[0]) {
                "제재" -> {
                    sameFunction(player, args)
                    val target = Bukkit.getOfflinePlayer(args[1])
                    if (args.size == 2) {
                        player.sendMessage("$prefix 코드를 적어주세요.")
                        return false
                    }
                    if (!isInt(args[2])) {
                        player.sendMessage("$prefix 숫자가 아닙니다.")
                        return false
                    }
                    val code = args[2].toInt()
                    if (!optionConfig.contains("처벌.$code")) {
                        player.sendMessage("$prefix 존재하지 않는 코드입니다.")
                        return false
                    }
                    punishmentSanctions(player, target.uniqueId, code)
                }
                "경고차감" -> {
                    sameFunction(player, args)
                    val target = Bukkit.getOfflinePlayer(args[1])
                    if (args.size == 2) {
                        player.sendMessage("$prefix 횟수를 적어주세요.")
                        return false
                    }
                    if (!isInt(args[2])) {
                        player.sendMessage("$prefix 숫자가 아닙니다.")
                        return false
                    }
                    val warning = args[2].toInt()
                    subtractWarning(player, target.uniqueId, warning)
                }
                "뮤트해제" -> {
                    sameFunction(player, args)
                    val target = Bukkit.getOfflinePlayer(args[1])
                    if (!playerConfig.contains("${player.uniqueId}.뮤트")) {
                        player.sendMessage("$prefix 해당 플레이어에게 적용된 뮤트가 없습니다.")
                        return false
                    }
                    clearPlayerMute(player, target.uniqueId)
                }
                "차단해제" -> {
                    sameFunction(player, args)
                    val target = Bukkit.getOfflinePlayer(args[1])
                    if (!playerConfig.contains("${player.uniqueId}.밴")) {
                        player.sendMessage("$prefix 해당 플레이어에게 적용된 밴이 없습니다.")
                        return false
                    }
                    clearPlayerBan(player, target.uniqueId)
                }
                "리로드" -> {
                    if (!player.isOp) return false
                    instance.optionFile.reloadConfig()
                    instance.playerFile.reloadConfig()
                    player.sendMessage("$prefix 플러그인이 리로드 되었습니다.")
                }
            }
        }
        return false
    }
    private fun help(player: Player) {
        player.sendMessages(
            "$prefix 처벌 시스템 도움말",
            "",
            "$prefix /처벌 제재 [닉네임] [코드]",
            "$prefix /처벌 경고차감 [닉네임] [횟수]",
            "$prefix /처벌 뮤트해제 [닉네임]",
            "$prefix /처벌 차단해제 [닉네임]"
        )
        if (player.isOp) "$prefix /처벌 리로드"
    }
    private fun sameFunction(player: Player, args: Array<out String>) {
        if (args.size == 1) {
            player.sendMessage("$prefix 닉네임을 적어주세요.")
            return
        }
        val target = Bukkit.getOfflinePlayer(args[1]) ?: run {
            player.sendMessage("$prefix 존재하지 않는 유저입니다.")
            return@sameFunction
        }
        if (!target.hasPlayedBefore()) {
            player.sendMessage("$prefix 존재하지 않는 유저입니다.")
            return
        }
    }
    private fun punishmentSanctions(player: Player, target: UUID, code: Int) {
        val punishName = optionConfig.getString("처벌.$code.이름")

        punish.addWarning(target, code)
        punish.applyMute(target, code)
        punish.applyBan(player, target, code)

        message.punishMessageReplacer(getName(target), player.name, punishName)
    }
    private fun subtractWarning(player: Player, target: UUID, warning: Int) {
        punish.subtractWarning(target, warning)
        player.sendMessage("$prefix ${message.minusWarningReplacer(getName(target), warning.toString())}")
    }
    private fun clearPlayerMute(player: Player, target: UUID) {
        punish.clearPlayerMute(target)
        player.sendMessage("$prefix ${message.clearMuteReplacer(getName(target))}")
    }
    private fun clearPlayerBan(player: Player, target: UUID) {
        punish.clearPlayerMute(target)
        player.sendMessage("$prefix ${message.clearBanReplacer(getName(target))}")
    }
}