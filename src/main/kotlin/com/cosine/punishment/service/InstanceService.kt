package com.cosine.punishment.service

import com.cosine.punishment.manager.MessageManager
import com.cosine.punishment.manager.PunishManager
import com.cosine.punishment.manager.TimeManager
import com.cosine.punishment.util.CustomConfig

interface InstanceService {

    var optionFile: CustomConfig

    var playerFile: CustomConfig

    val messageManager: MessageManager

    val timeManager: TimeManager

    val punishManager: PunishManager
}