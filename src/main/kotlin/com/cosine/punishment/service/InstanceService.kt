package com.cosine.punishment.service

import com.cosine.punishment.Punishment
import com.cosine.punishment.manager.MessageManager
import com.cosine.punishment.manager.PunishManager
import com.cosine.punishment.manager.TimeManager
import com.cosine.punishment.util.CustomConfig

interface InstanceService {

    val plugin: Punishment

    val optionFile: CustomConfig

    val playerFile: CustomConfig

    val messageManager: MessageManager

    val timeManager: TimeManager

    val punishManager: PunishManager
}