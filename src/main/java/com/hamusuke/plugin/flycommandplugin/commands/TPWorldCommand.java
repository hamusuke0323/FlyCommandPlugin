package com.hamusuke.plugin.flycommandplugin.commands;

import java.util.Collection;
import java.util.UUID;

import net.minecraft.commands.CommandListenerWrapper;
import net.minecraft.commands.arguments.ArgumentEntity;
import net.minecraft.network.chat.ChatClickable;
import net.minecraft.network.chat.ChatComponentText;
import net.minecraft.network.chat.ChatHoverable;
import net.minecraft.network.chat.IChatBaseComponent;
import net.minecraft.world.entity.Entity;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

import com.google.gson.JsonParser;
import com.hamusuke.plugin.flycommandplugin.chat.Chat;
import com.hamusuke.plugin.flycommandplugin.chat.LanguageManager;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.suggestion.SuggestionProvider;

public class TPWorldCommand {
	public static void register(CommandDispatcher<CommandListenerWrapper> p_012041_) {
		LiteralArgumentBuilder<CommandListenerWrapper> field_001931_a_ = net.minecraft.commands.CommandDispatcher.a("tpworld_flycmdpl").requires((lambda_002151_a_) -> lambda_002151_a_.hasPermission(2));

		SuggestionProvider<CommandListenerWrapper> suggestionProvider = (commandContext, suggestionsbuilder) -> {
			Bukkit.getWorlds().forEach(world -> {
				if (world.getName().startsWith(suggestionsbuilder.getRemaining().toLowerCase())) {
					suggestionsbuilder.suggest(world.getName());
				}
			});
			return suggestionsbuilder.buildFuture();
		};

		field_001931_a_.then(net.minecraft.commands.CommandDispatcher.a("targets", ArgumentEntity.multipleEntities()).then(net.minecraft.commands.CommandDispatcher.a("world", StringArgumentType.greedyString()).suggests(suggestionProvider).executes((lambda_002152_a_) -> {
			return teleport(lambda_002152_a_.getSource(), ArgumentEntity.b(lambda_002152_a_, "targets"), StringArgumentType.getString(lambda_002152_a_, "world"));
		})));

		p_012041_.register(field_001931_a_);
	}

	private static int teleport(CommandListenerWrapper p_018311_, Collection<? extends Entity> p_890124_, String p_183901_) {
		World field_589031_a_ = Bukkit.getWorld(p_183901_);
		if (field_589031_a_ == null) {
			p_018311_.getBukkitSender().sendMessage(ChatColor.RED + LanguageManager.getTranslatedText(p_018311_, "command.tpworld.failed.notfoundworld"));
			return 0;
		}
		UUID field_087023_b_ = field_589031_a_.getUID();
		String field_087021_b_ = field_589031_a_.getName();
		@SuppressWarnings("deprecation") String field_081021_c_ = "\"" + field_589031_a_.getWorldType().getName().toLowerCase() + "\"";
		Location field_381021_d_ = new Location(field_589031_a_, field_589031_a_.getSpawnLocation().getX(), field_589031_a_.getSpawnLocation().getY(), field_589031_a_.getSpawnLocation().getZ());
		p_890124_.forEach(entity -> entity.getBukkitEntity().teleport(field_381021_d_, TeleportCause.COMMAND));
		if (p_890124_.size() == 1) {
			Chat.sendMessage(p_018311_, "command.tpworld.success.single", true, p_890124_.iterator().next().getScoreboardDisplayName(), toChatComponent(field_087021_b_, "/tpworld_flycmdpl @s " + field_087021_b_, field_081021_c_, field_087023_b_, field_087021_b_), field_381021_d_.getX(), field_381021_d_.getY(), field_381021_d_.getZ());
		} else {
			Chat.sendMessage(p_018311_, "command.tpworld.success.multiple", true, p_890124_.size(), toChatComponent(field_087021_b_, "/tpworld_flycmdpl @s " + field_087021_b_, field_081021_c_, field_087023_b_, field_087021_b_), field_381021_d_.getX(), field_381021_d_.getY(), field_381021_d_.getZ());
		}
		return p_890124_.size();
	}

	private static IChatBaseComponent toChatComponent(String chattext, String suggestcommand, String types, UUID uuid, String ichatstring) {
		return new ChatComponentText(chattext).format((modifier) -> {
			return modifier.setChatClickable(new ChatClickable(ChatClickable.EnumClickAction.d, suggestcommand)).setChatHoverable(toChatHoverable(types, uuid, ichatstring)).setInsertion(chattext);
		});
	}

	private static ChatHoverable toChatHoverable(String types, UUID uuid, String text) {
		text = "[\"\",{\"text\":\"" + text + "\\n\"}";
		if (types != null) {
			text += ",{\"translate\":\"gui.entity_tooltip.type\",\"with\":[" + types + "]},{\"text\":\"\\n\"}";
		}
		text += ",{\"text\":\"" + uuid + "\"}]";
		return new ChatHoverable(ChatHoverable.EnumHoverAction.a, IChatBaseComponent.ChatSerializer.a(new JsonParser().parse(text)));
	}
}
