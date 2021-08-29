package com.hamusuke.plugin.flycommandplugin.commands;

import java.util.Collection;
import java.util.Collections;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;

import net.minecraft.server.v1_16_R3.ArgumentEntity;
import net.minecraft.server.v1_16_R3.ChatComponentText;
import net.minecraft.server.v1_16_R3.ChatMessage;
import net.minecraft.server.v1_16_R3.CommandListenerWrapper;
import net.minecraft.server.v1_16_R3.EntityPlayer;
import net.minecraft.server.v1_16_R3.EnumGamemode;
import net.minecraft.server.v1_16_R3.GameRules;
import net.minecraft.server.v1_16_R3.SystemUtils;

public class GMCommand {
	public static void register(CommandDispatcher<CommandListenerWrapper> dispatcher) {
		LiteralArgumentBuilder<CommandListenerWrapper> literalArgumentBuilder = net.minecraft.server.v1_16_R3.CommandDispatcher.a("gm_flycmdpl").requires((permission) -> permission.hasPermission(2));

		for (String gamemode : new String[]{"survival", "s", "0", "creative", "c", "1", "adventure", "a", "2", "spectator", "sp", "3"}) {
			literalArgumentBuilder.then(net.minecraft.server.v1_16_R3.CommandDispatcher.a(gamemode).executes((execute) -> {
				return a(execute.getSource(), Collections.singleton(execute.getSource().h()), gamemode);
			}).then(net.minecraft.server.v1_16_R3.CommandDispatcher.a("targets", ArgumentEntity.d()).executes((execute) -> {
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
			if (a.getWorld().getGameRules().getBoolean(GameRules.SEND_COMMAND_FEEDBACK)) {
				b.sendMessage(new ChatMessage("gameMode.changed", chatmessage), SystemUtils.b);
			}
			a.sendMessage(new ChatMessage("commands.gamemode.success.other", b.getScoreboardDisplayName(), chatmessage), true);
		}
	}

	private static void setGamemode(CommandListenerWrapper a, Collection<EntityPlayer> b, EnumGamemode c) {
		for (EntityPlayer entityplayer : b) {
			if (entityplayer.playerInteractManager.getGameMode() != c) {
				entityplayer.a(c);
				if (entityplayer.playerInteractManager.getGameMode() != c) {
					a.sendFailureMessage(new ChatComponentText("Failed to set the gamemode of '" + entityplayer.getName() + "'"));
				} else {
					sendGamemodeChanged(a, entityplayer, c);
				}
			}
		}
	}

	private static EnumGamemode getGamemode(String args) {
		switch (args) {
			case "creative":
			case "c":
			case "1":
				return EnumGamemode.CREATIVE;
			case "adventure":
			case "a":
			case "2":
				return EnumGamemode.ADVENTURE;
			case "spectator":
			case "sp":
			case "3":
				return EnumGamemode.SPECTATOR;
			case "survival":
			case "s":
			case "0":
			default:
				return EnumGamemode.SURVIVAL;
		}
	}
}
