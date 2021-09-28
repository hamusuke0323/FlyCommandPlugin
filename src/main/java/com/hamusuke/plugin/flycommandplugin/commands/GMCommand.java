package com.hamusuke.plugin.flycommandplugin.commands;

import java.util.Collection;
import java.util.Collections;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;

import net.minecraft.SystemUtils;
import net.minecraft.commands.CommandListenerWrapper;
import net.minecraft.commands.arguments.ArgumentEntity;
import net.minecraft.network.chat.ChatComponentText;
import net.minecraft.network.chat.ChatMessage;
import net.minecraft.server.level.EntityPlayer;
import net.minecraft.world.level.EnumGamemode;
import net.minecraft.world.level.GameRules;

public class GMCommand {
	public static void register(CommandDispatcher<CommandListenerWrapper> dispatcher) {
		LiteralArgumentBuilder<CommandListenerWrapper> literalArgumentBuilder = net.minecraft.commands.CommandDispatcher.a("gm_flycmdpl").requires((permission) -> permission.hasPermission(2));

		for (String gamemode : new String[]{"survival", "s", "0", "creative", "c", "1", "adventure", "a", "2", "spectator", "sp", "3"}) {
			literalArgumentBuilder.then(net.minecraft.commands.CommandDispatcher.a(gamemode).executes((execute) -> {
				return a(execute.getSource(), Collections.singleton(execute.getSource().h()), gamemode);
			}).then(net.minecraft.commands.CommandDispatcher.a("targets", ArgumentEntity.d()).executes((execute) -> {
				return a(execute.getSource(), ArgumentEntity.f(execute, "targets"), gamemode);
			})));
		}

		dispatcher.register(literalArgumentBuilder);
	}

	private static int a(CommandListenerWrapper commandListenerWrapper, Collection<EntityPlayer> players, String gamemodestr) {
		setGamemode(commandListenerWrapper, players, getGamemode(gamemodestr));
		return 0;
	}

	private static void sendGamemodeChanged(CommandListenerWrapper a, EntityPlayer b, EnumGamemode c) {
		ChatMessage chatmessage = new ChatMessage("gameMode." + c.b(), new Object[0]);
		if (a.getEntity() == b) {
			a.sendMessage(new ChatMessage("commands.gamemode.success.self", chatmessage), true);
		} else {
			if (a.getWorld().getGameRules().getBoolean(GameRules.o)) {
				b.sendMessage(new ChatMessage("gameMode.changed", chatmessage), SystemUtils.b);
			}
			a.sendMessage(new ChatMessage("commands.gamemode.success.other", b.getScoreboardDisplayName(), chatmessage), true);
		}
	}

	private static void setGamemode(CommandListenerWrapper a, Collection<EntityPlayer> b, EnumGamemode c) {
		for (EntityPlayer entityplayer : b) {
			if (entityplayer.d.getGameMode() != c) {
				entityplayer.a(c);
				if (entityplayer.d.getGameMode() != c) {
					a.sendFailureMessage(new ChatComponentText("Failed to set the gamemode of '" + entityplayer.getName() + "'"));
				} else {
					sendGamemodeChanged(a, entityplayer, c);
				}
			}
		}
	}

	private static EnumGamemode getGamemode(String args) {
		return switch (args) {
			case "creative", "c", "1" -> EnumGamemode.b;
			case "adventure", "a", "2" -> EnumGamemode.c;
			case "spectator", "sp", "3" -> EnumGamemode.d;
			default -> EnumGamemode.a;
		};
	}
}
