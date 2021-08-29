package com.hamusuke.plugin.flycommandplugin.commands;

import net.minecraft.server.v1_16_R3.*;
import com.hamusuke.plugin.flycommandplugin.settings.FlyCommandPluginSettings;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;

public class FlyCommandPluginSettingCommand extends FlyCommandPluginSettings {
	public static void register(CommandDispatcher<CommandListenerWrapper> dispatcher) {
		LiteralArgumentBuilder<CommandListenerWrapper> literalArgumentBuilder = net.minecraft.server.v1_16_R3.CommandDispatcher.a("flycommandpluginsetting").requires((p_115721_) -> {
			return p_115721_.hasPermission(2);
		});

		ALL_SETTINGS.forEach(setting -> setting.registerCommand(literalArgumentBuilder));
		dispatcher.register(literalArgumentBuilder);
	}
}
