package com.hamusuke.plugin.flycommandplugin.chat;

import org.spigotmc.SpigotConfig;

import net.minecraft.server.v1_16_R3.ChatComponentText;
import net.minecraft.server.v1_16_R3.ChatMessage;
import net.minecraft.server.v1_16_R3.CommandListenerWrapper;
import net.minecraft.server.v1_16_R3.EntityPlayer;
import net.minecraft.server.v1_16_R3.EnumChatFormat;
import net.minecraft.server.v1_16_R3.GameRules;
import net.minecraft.server.v1_16_R3.IChatBaseComponent;
import net.minecraft.server.v1_16_R3.IChatMutableComponent;
import net.minecraft.server.v1_16_R3.SystemUtils;

public class Chat {
	public static void sendMessage(CommandListenerWrapper commandListenerWrapper, String key, boolean flag, Object... objects) {
		IChatBaseComponent ichatbasecomponent = new ChatMessage(LanguageManager.getTranslatedText(commandListenerWrapper, key), objects);

		if (commandListenerWrapper.base.shouldSendSuccess()) {
			commandListenerWrapper.base.sendMessage(ichatbasecomponent, SystemUtils.b);
		}

		if (flag && commandListenerWrapper.base.shouldBroadcastCommands()) {
			sendAdminMessage(commandListenerWrapper, key, objects);
		}
	}

	public static void sendFailureMessage(CommandListenerWrapper commandListenerWrapper, String key, Object... objects) {
		IChatBaseComponent ichatbasecomponent = new ChatMessage(LanguageManager.getTranslatedText(commandListenerWrapper, key), objects);
		if (commandListenerWrapper.base.shouldSendFailure()) {
			commandListenerWrapper.base.sendMessage(new ChatComponentText("").addSibling(ichatbasecomponent).a(EnumChatFormat.RED), SystemUtils.b);
		}
	}

	private static void sendAdminMessage(CommandListenerWrapper commandListenerWrapper, String key, Object... objects) {
		if (commandListenerWrapper.getServer().getGameRules().getBoolean(GameRules.SEND_COMMAND_FEEDBACK)) {

			for (EntityPlayer entityplayer : commandListenerWrapper.getServer().getPlayerList().getPlayers()) {
				IChatMutableComponent ichatmutablecomponent = new ChatMessage("chat.type.admin", new Object[]{commandListenerWrapper.getScoreboardDisplayName(), new ChatMessage(LanguageManager.getTranslatedText(entityplayer, key), objects)}).a(EnumChatFormat.GRAY, EnumChatFormat.ITALIC);

				if (entityplayer != commandListenerWrapper.base && entityplayer.getBukkitEntity().hasPermission("minecraft.admin.command_feedback")) {
					entityplayer.sendMessage(ichatmutablecomponent, SystemUtils.b);
				}
			}
		}

		if (commandListenerWrapper.base != commandListenerWrapper.getServer() && commandListenerWrapper.getServer().getGameRules().getBoolean(GameRules.LOG_ADMIN_COMMANDS) && !SpigotConfig.silentCommandBlocks) {
			IChatMutableComponent ichatmutablecomponent = new ChatMessage("chat.type.admin", new Object[]{commandListenerWrapper.getScoreboardDisplayName(), new ChatMessage(LanguageManager.getTranslatedText(commandListenerWrapper.getServer().getServerCommandListener(), key), objects)}).a(EnumChatFormat.GRAY, EnumChatFormat.ITALIC);
			commandListenerWrapper.getServer().sendMessage(ichatmutablecomponent, SystemUtils.b);
		}
	}
}
