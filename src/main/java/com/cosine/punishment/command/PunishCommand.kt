package com.cosine.punishment.command

import com.cosine.punishment.service.InstanceService
import com.cosine.punishment.util.isInt
import com.cosine.punishment.util.sendMessages
import org.bukkit.Bukkit
import org.bukkit.OfflinePlayer
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class PunishCommand(private val instance: InstanceService) : CommandExecutor {

    private val optionConfig = instance.optionFile.getConfig()
    private val playerConfig = instance.playerFile.getConfig()

    private val message = instance.messageManager
    private val time = instance.timeManager

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
                    if (args.size == 1) {
                        player.sendMessage("$prefix 닉네임을 적어주세요.")
                        return false
                    }
                    val target = Bukkit.getOfflinePlayer(args[1]) ?: run {
                        player.sendMessage("$prefix 존재하지 않는 유저입니다.")
                        return@onCommand false
                    }
                    if (!target.hasPlayedBefore()) {
                        player.sendMessage("$prefix 존재하지 않는 유저입니다.")
                        return false
                    }
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
                    punishmentSanctions(player, target, code)
                }
                "경고차감" -> {}
                "뮤트해제" -> {}
                "차단해제" -> {}
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
    }
    private fun punishmentSanctions(player: Player, target: OfflinePlayer, code: Int) {
        val punishName = optionConfig.getString("처벌.$code.이름")

        val punishCount = optionConfig.getInt("처벌.$code.경고")

        val punishMute = time.timeDeserialization(optionConfig.getString("처벌.$code.뮤트"))
        val punishMuteDay = punishMute[0]
        val punishMuteHour = punishMute[1]
        val punishMuteMinute = punishMute[2]

        val punishBan = time.timeDeserialization(optionConfig.getString("처벌.$code.밴"))
        val punishBanDay = punishBan[0]
        val punishBanHour = punishBan[1]
        val punishBanMinute = punishBan[2]



        message.punishMessageReplacer(target.name, player.name, punishName)
    }
}