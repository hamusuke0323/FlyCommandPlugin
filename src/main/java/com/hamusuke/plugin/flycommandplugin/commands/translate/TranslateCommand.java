package com.hamusuke.plugin.flycommandplugin.commands.translate;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import javax.net.ssl.HttpsURLConnection;

import net.minecraft.server.v1_16_R3.*;
import org.bukkit.command.ConsoleCommandSender;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;

public class TranslateCommand {
	public static void register(CommandDispatcher<CommandListenerWrapper> dispatcher) {
		LiteralArgumentBuilder<CommandListenerWrapper> literalArgumentBuilder = net.minecraft.server.v1_16_R3.CommandDispatcher.a("translate");

		Languages[] languages = Languages.values();

		for (Languages sLanguage : languages) {
			if (sLanguage != Languages.Chinese_Traditional && sLanguage != Languages.Chinese_Simplified) {
				for (Languages tLanguage : languages) {
					if (tLanguage != Languages.DetectLanguage && tLanguage != Languages.Chinese) {
						literalArgumentBuilder.then(net.minecraft.server.v1_16_R3.CommandDispatcher.a(sLanguage.getLanguageName()).then(net.minecraft.server.v1_16_R3.CommandDispatcher.a(tLanguage.getLanguageName()).then(net.minecraft.server.v1_16_R3.CommandDispatcher.a("text", StringArgumentType.greedyString()).executes(e -> {
							return translate(e.getSource(), StringArgumentType.getString(e, "text"), sLanguage.getLanguage(), tLanguage.getLanguage());
						}))));
					}
				}
			}
		}

		dispatcher.register(literalArgumentBuilder);
	}

	private static int translate(CommandListenerWrapper commandListenerWrapper, String text, String sl, String tl) {
		try {
			URL url = new URL("https://script.google.com/macros/s/AKfycbwRfNXWeTH-4dqjcjD7dMBejYmSkNMk2Cv1b7IfhLe1WbVdjJ0/exec?text=" + URLEncoder.encode(text, "UTF-8") + "&source=" + sl + "&target=" + tl);
			HttpsURLConnection con = (HttpsURLConnection) url.openConnection();

			BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8));
			JsonElement body = new JsonParser().parse(reader.readLine());

			IChatBaseComponent iChatBaseComponent = IChatBaseComponent.ChatSerializer.a(body).format(mod -> {
				return mod.setChatClickable(new ChatClickable(ChatClickable.EnumClickAction.COPY_TO_CLIPBOARD, IChatBaseComponent.ChatSerializer.a(body).getString())).setChatHoverable(new ChatHoverable(ChatHoverable.EnumHoverAction.SHOW_TEXT, new ChatMessage("chat.copy.click")));
			});

			if (commandListenerWrapper.getBukkitSender() instanceof ConsoleCommandSender) {
				MinecraftServer.LOGGER.info("[Translated] " + IChatBaseComponent.ChatSerializer.a(body).getString());
			} else {
				commandListenerWrapper.sendMessage(iChatBaseComponent, false);
			}

			reader.close();
			con.disconnect();
		} catch (Exception ignored) {
		}

		return text.length();
	}
}
