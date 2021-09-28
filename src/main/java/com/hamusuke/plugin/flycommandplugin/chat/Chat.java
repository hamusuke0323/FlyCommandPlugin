package com.hamusuke.plugin.flycommandplugin.chat;

import net.minecraft.EnumChatFormat;
import net.minecraft.SystemUtils;
import net.minecraft.commands.CommandListenerWrapper;
import net.minecraft.network.chat.ChatComponentText;
import net.minecraft.network.chat.ChatMessage;
import net.minecraft.network.chat.IChatBaseComponent;
import net.minecraft.network.chat.IChatMutableComponent;
import net.minecraft.server.level.EntityPlayer;
import net.minecraft.world.level.GameRules;
import org.spigotmc.SpigotConfig;

public class Chat {
	public static void sendMessage(CommandListenerWrapper commandListenerWrapper, String key, boolean flag, Object... objects) {
		IChatBaseComponent ichatbasecomponent = new ChatMessage(LanguageManager.getTranslatedText(commandListenerWrapper, key), objects);

		if (commandListenerWrapper.c.shouldSendSuccess()) {
			commandListenerWrapper.c.sendMessage(ichatbasecomponent, SystemUtils.b);
		}

		if (flag && commandListenerWrapper.c.shouldBroadcastCommands()) {
			sendAdminMessage(commandListenerWrapper, key, objects);
		}
	}

	public static void sendFailureMessage(CommandListenerWrapper commandListenerWrapper, String key, Object... objects) {
		IChatBaseComponent ichatbasecomponent = new ChatMessage(LanguageManager.getTranslatedText(commandListenerWrapper, key), objects);
		if (commandListenerWrapper.c.shouldSendFailure()) {
			commandListenerWrapper.c.sendMessage(new ChatComponentText("").addSibling(ichatbasecomponent).a(EnumChatFormat.m), SystemUtils.b);
		}
	}

	private static void sendAdminMessage(CommandListenerWrapper commandListenerWrapper, String key, Object... objects) {
		if (commandListenerWrapper.getServer().getGameRules().getBoolean(GameRules.o)) {

			for (EntityPlayer entityplayer : commandListenerWrapper.getServer().getPlayerList().getPlayers()) {
				IChatMutableComponent ichatmutablecomponent = new ChatMessage("chat.type.admin", new Object[]{commandListenerWrapper.getScoreboardDisplayName(), new ChatMessage(LanguageManager.getTranslatedText(entityplayer, key), objects)}).a(EnumChatFormat.h, EnumChatFormat.u);

				if (entityplayer != commandListenerWrapper.c && entityplayer.getBukkitEntity().hasPermission("minecraft.admin.command_feedback")) {
					entityplayer.sendMessage(ichatmutablecomponent, SystemUtils.b);
				}
			}
		}

		if (commandListenerWrapper.c != commandListenerWrapper.getServer() && commandListenerWrapper.getServer().getGameRules().getBoolean(GameRules.l) && !SpigotConfig.silentCommandBlocks) {
			IChatMutableComponent ichatmutablecomponent = new ChatMessage("chat.type.admin", new Object[]{commandListenerWrapper.getScoreboardDisplayName(), new ChatMessage(LanguageManager.getTranslatedText(commandListenerWrapper.getServer().getServerCommandListener(), key), objects)}).a(EnumChatFormat.h, EnumChatFormat.u);
			commandListenerWrapper.getServer().sendMessage(ichatmutablecomponent, SystemUtils.b);
		}
	}
}
