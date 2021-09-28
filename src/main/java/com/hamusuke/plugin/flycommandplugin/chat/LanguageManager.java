package com.hamusuke.plugin.flycommandplugin.chat;

import java.io.File;

import net.minecraft.commands.CommandListenerWrapper;
import net.minecraft.server.level.EntityPlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import com.hamusuke.plugin.flycommandplugin.FlyCommandPlugin;

public class LanguageManager {
	public static String getTranslatedText(CommandListenerWrapper commandListenerWrapper, String key) {
		String lang = commandListenerWrapper.getBukkitSender() instanceof Player ? ((Player) commandListenerWrapper.getBukkitSender()).getLocale() : "en_us";
		if (!new File(FlyCommandPlugin.getInstance().getDataFolder(), lang + ".yml").exists()) {
			lang = "en_us";
		}
		FileConfiguration config = YamlConfiguration.loadConfiguration(new File(FlyCommandPlugin.getInstance().getDataFolder(), lang + ".yml"));

		return config.contains(key) ? config.getString(key) : key;
	}

	public static String getTranslatedText(EntityPlayer entityPlayer, String key) {
		return getTranslatedText(entityPlayer.getCommandListener(), key);
	}
}
