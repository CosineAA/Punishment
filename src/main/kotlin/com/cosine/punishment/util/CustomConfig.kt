package com.cosine.punishment.util

import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.plugin.java.JavaPlugin
import java.io.*

class CustomConfig(plugin: JavaPlugin, fileName: String) {

    private var fileName: String
    private var plugin: JavaPlugin
    private var file: File
    private lateinit var config: YamlConfiguration

    init {
        this.plugin = plugin
        this.fileName = fileName
        val dataFolder: File = plugin.dataFolder ?: throw IllegalStateException()
        this.file = File("$dataFolder\\${this.fileName}")
    }

    fun loadConfig() {
        config = YamlConfiguration.loadConfiguration(file)
    }
    fun reloadConfig() {
        config.load(file)
    }
    fun getConfig(): YamlConfiguration {
        return config
    }
    fun saveConfig() {
        try {
            getConfig().save(file)
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
    fun saveDefaultConfig() {
        if (!file.exists()) {
            this.plugin.saveResource(fileName, false)
        }
    }
}