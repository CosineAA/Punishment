package com.cosine.punishment

import com.cosine.punishment.command.PunishCommand
import com.cosine.punishment.listener.PunishListener
import com.cosine.punishment.manager.MessageManager
import com.cosine.punishment.manager.PunishManager
import com.cosine.punishment.manager.TimeManager
import com.cosine.punishment.service.InstanceService
import com.cosine.punishment.util.CustomConfig
import org.bukkit.plugin.java.JavaPlugin

class Punishment : JavaPlugin(), InstanceService {

    override lateinit var plugin: Punishment

    override lateinit var optionFile: CustomConfig
    override lateinit var playerFile: CustomConfig

    override val messageManager: MessageManager by lazy { MessageManager(this) }
    override val timeManager: TimeManager by lazy { TimeManager() }
    override val punishManager: PunishManager by lazy { PunishManager(this) }

    override fun onLoad() {
        plugin = this
    }

    override fun onEnable() {
        logger.info("처벌 플러그인 활성화")

        optionFile = CustomConfig(this, "option.yml")
        playerFile = CustomConfig(this, "player.yml")

        optionFile.saveDefaultConfig()
        playerFile.saveDefaultConfig()

        optionFile.loadConfig()
        playerFile.loadConfig()

        getCommand("처벌").executor = PunishCommand(this)
        server.pluginManager.registerEvents(PunishListener(this), this)
    }

    override fun onDisable() {
        logger.info("처벌 플러그인 비활성화")
    }
}