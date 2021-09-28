package com.hamusuke.plugin.flycommandplugin;

import com.hamusuke.plugin.flycommandplugin.settings.FlyCommandPluginSettings;
import net.minecraft.commands.CommandListenerWrapper;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.craftbukkit.v1_17_R1.CraftServer;
import org.bukkit.plugin.java.JavaPlugin;

import com.hamusuke.plugin.flycommandplugin.commands.CloseInventoryCommand;
import com.hamusuke.plugin.flycommandplugin.commands.ContainerCommand;
import com.hamusuke.plugin.flycommandplugin.commands.DamageCommand;
import com.hamusuke.plugin.flycommandplugin.commands.FlyCommand;
import com.hamusuke.plugin.flycommandplugin.commands.FlyCommandPluginSettingCommand;
import com.hamusuke.plugin.flycommandplugin.commands.GMCommand;
import com.hamusuke.plugin.flycommandplugin.commands.ItemCommand;
import com.hamusuke.plugin.flycommandplugin.commands.TPWorldCommand;
import com.hamusuke.plugin.flycommandplugin.commands.translate.TranslateCommand;

public final class FlyCommandPlugin extends JavaPlugin {
	private static FlyCommandPlugin flyCommandPlugin;

	public void onEnable() {
		flyCommandPlugin = this;
		this.getServer().getPluginManager().registerEvents(new FlyCommandPluginEvents(), this);
		this.saveDefaultConfig();
		FlyCommandPluginSettings.saveConfig(this, this.getConfig());
		this.saveResource("ja_jp.yml", true);
		this.saveResource("en_us.yml", true);

		FlyCommandPluginSettings.ServerSettings.readAll(this.getConfig());
		FlyCommandPluginSettings.PlayerSettings.readAll(this.getConfig());

		Bukkit.getOnlinePlayers().forEach(player -> FlyCommandPluginSettings.savePlayerConfig(this, this.getConfig(), player.getUniqueId()));
		Bukkit.getOnlinePlayers().forEach(player -> FlyCommandPluginEvents.loadPlayerAbilities(this, this.getConfig(), player));

		this.registerCommands();
		this.getLogger().info("FlyCommandPlugin is now Enabled!");
	}

	public void onDisable() {
		Bukkit.getOnlinePlayers().forEach(player -> FlyCommandPluginEvents.savePlayerAbilities(this, this.getConfig(), player));
		this.unregisterCommands();
		this.getLogger().info("FlyCommandPlugin is now Disabled!");
	}

	public static FlyCommandPlugin getInstance() {
		return flyCommandPlugin;
	}

	public static FileConfiguration getFlyCommandPluginConfig() {
		return getInstance().getConfig();
	}

	private void registerCommands() {
		com.mojang.brigadier.CommandDispatcher<CommandListenerWrapper> dispatcher = (((CraftServer) Bukkit.getServer()).getServer()).vanillaCommandDispatcher.a();

		FlyCommandPluginSettingCommand.register(dispatcher);
		this.getLogger().info("registered flycommandpluginsetting command");
		CloseInventoryCommand.register(dispatcher);
		this.getLogger().info("registered closeinventory command");
		ContainerCommand.register(dispatcher);
		this.getLogger().info("registered container command");
		DamageCommand.register(dispatcher);
		this.getLogger().info("registered damage command");
		FlyCommand.register(dispatcher);
		this.getLogger().info("registered fly command");
		GMCommand.register(dispatcher);
		this.getLogger().info("registered gm command");
		ItemCommand.register(dispatcher);
		this.getLogger().info("registered item command");
		TPWorldCommand.register(dispatcher);
		this.getLogger().info("registered tpworld command");
		TranslateCommand.register(dispatcher);
		this.getLogger().info("registered translate command");
	}

	private void unregisterCommands() {
		com.mojang.brigadier.CommandDispatcher<CommandListenerWrapper> dispatcher = (((CraftServer) Bukkit.getServer()).getServer()).vanillaCommandDispatcher.a();

		dispatcher.getRoot().removeCommand("flycommandpluginsetting");
		this.getLogger().info("unregistered flycommandpluginsetting command");
		dispatcher.getRoot().removeCommand("closeinventory_flycmdpl");
		this.getLogger().info("unregistered closeinventory command");
		dispatcher.getRoot().removeCommand("container_flycmdpl");
		this.getLogger().info("unregistered container command");
		dispatcher.getRoot().removeCommand("damage_flycmdpl");
		this.getLogger().info("unregistered damage command");
		dispatcher.getRoot().removeCommand("fly_flycmdpl");
		this.getLogger().info("unregistered fly command");
		dispatcher.getRoot().removeCommand("gm_flycmdpl");
		this.getLogger().info("unregistered gm command");
		dispatcher.getRoot().removeCommand("item_flycmdpl");
		this.getLogger().info("unregistered item command");
		dispatcher.getRoot().removeCommand("tpworld_flycmdpl");
		this.getLogger().info("unregistered tpworld command");
		dispatcher.getRoot().removeCommand("translate");
		this.getLogger().info("unregistered translate command");
	}
}
