package com.hamusuke.plugin.flycommandplugin.commands;

import net.minecraft.commands.CommandListenerWrapper;
import com.hamusuke.plugin.flycommandplugin.settings.FlyCommandPluginSettings;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;

public class FlyCommandPluginSettingCommand {
	public static void register(CommandDispatcher<CommandListenerWrapper> dispatcher) {
		LiteralArgumentBuilder<CommandListenerWrapper> literalArgumentBuilder = net.minecraft.commands.CommandDispatcher.a("flycommandpluginsetting").requires(p_115721_ -> p_115721_.hasPermission(2));

		FlyCommandPluginSettings.ALL_SETTINGS.forEach(setting -> setting.registerCommand(literalArgumentBuilder));
		dispatcher.register(literalArgumentBuilder);
	}
}
