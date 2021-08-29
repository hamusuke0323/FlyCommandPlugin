package com.hamusuke.plugin.flycommandplugin.commands;

import java.util.Collection;

import com.hamusuke.plugin.flycommandplugin.chat.Chat;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;

import net.minecraft.server.v1_16_R3.ArgumentEntity;
import net.minecraft.server.v1_16_R3.ArgumentNBTTag;
import net.minecraft.server.v1_16_R3.ChatClickable;
import net.minecraft.server.v1_16_R3.ChatClickable.EnumClickAction;
import net.minecraft.server.v1_16_R3.ChatHoverable;
import net.minecraft.server.v1_16_R3.ChatHoverable.EnumHoverAction;
import net.minecraft.server.v1_16_R3.ChatMessage;
import net.minecraft.server.v1_16_R3.CommandListenerWrapper;
import net.minecraft.server.v1_16_R3.EntityPlayer;
import net.minecraft.server.v1_16_R3.IChatBaseComponent;
import net.minecraft.server.v1_16_R3.IChatMutableComponent;
import net.minecraft.server.v1_16_R3.ItemStack;
import net.minecraft.server.v1_16_R3.NBTTagCompound;

public class ItemCommand {
	public static void register(CommandDispatcher<CommandListenerWrapper> dispatcher) {
		LiteralArgumentBuilder<CommandListenerWrapper> literalArgumentBuilder = net.minecraft.server.v1_16_R3.CommandDispatcher.a("item_flycmdpl").requires((permission) -> {
			return permission.hasPermission(2);
		});

		literalArgumentBuilder.then(net.minecraft.server.v1_16_R3.CommandDispatcher.a("setTag").then(net.minecraft.server.v1_16_R3.CommandDispatcher.a("targets", ArgumentEntity.d()).then(net.minecraft.server.v1_16_R3.CommandDispatcher.a("tag", ArgumentNBTTag.a()).executes((execute) -> {
			return setTag(execute.getSource(), ArgumentEntity.f(execute, "targets"), ArgumentNBTTag.a(execute, "tag"));
		}))));

		literalArgumentBuilder.then(net.minecraft.server.v1_16_R3.CommandDispatcher.a("setDamage").then(net.minecraft.server.v1_16_R3.CommandDispatcher.a("targets", ArgumentEntity.d()).then(net.minecraft.server.v1_16_R3.CommandDispatcher.a("damage", IntegerArgumentType.integer()).executes((execute) -> {
			return setDamage(execute.getSource(), ArgumentEntity.f(execute, "targets"), IntegerArgumentType.getInteger(execute, "damage"));
		}))));

		literalArgumentBuilder.then(net.minecraft.server.v1_16_R3.CommandDispatcher.a("setDurability").then(net.minecraft.server.v1_16_R3.CommandDispatcher.a("targets", ArgumentEntity.d()).then(net.minecraft.server.v1_16_R3.CommandDispatcher.a("durability", IntegerArgumentType.integer()).executes((execute) -> {
			return setDurability(execute.getSource(), ArgumentEntity.f(execute, "targets"), IntegerArgumentType.getInteger(execute, "durability"));
		}))));

		literalArgumentBuilder.then(net.minecraft.server.v1_16_R3.CommandDispatcher.a("setAmount").then(net.minecraft.server.v1_16_R3.CommandDispatcher.a("targets", ArgumentEntity.d()).then(net.minecraft.server.v1_16_R3.CommandDispatcher.a("amount", IntegerArgumentType.integer(0, 127)).executes((execute) -> {
			return setAmount(execute.getSource(), ArgumentEntity.f(execute, "targets"), IntegerArgumentType.getInteger(execute, "amount"));
		}))));

		literalArgumentBuilder.then(net.minecraft.server.v1_16_R3.CommandDispatcher.a("getTag").then(net.minecraft.server.v1_16_R3.CommandDispatcher.a("targets", ArgumentEntity.d()).executes((execute) -> {
			return getTag(execute.getSource(), ArgumentEntity.f(execute, "targets"));
		})));

		literalArgumentBuilder.then(net.minecraft.server.v1_16_R3.CommandDispatcher.a("getDamage").then(net.minecraft.server.v1_16_R3.CommandDispatcher.a("targets", ArgumentEntity.d()).executes((execute) -> {
			return getDamage(execute.getSource(), ArgumentEntity.f(execute, "targets"));
		})));

		literalArgumentBuilder.then(net.minecraft.server.v1_16_R3.CommandDispatcher.a("getDurability").then(net.minecraft.server.v1_16_R3.CommandDispatcher.a("targets", ArgumentEntity.d()).executes((execute) -> {
			return getDurability(execute.getSource(), ArgumentEntity.f(execute, "targets"));
		})));

		literalArgumentBuilder.then(net.minecraft.server.v1_16_R3.CommandDispatcher.a("getAmount").then(net.minecraft.server.v1_16_R3.CommandDispatcher.a("targets", ArgumentEntity.d()).executes((execute) -> {
			return getAmount(execute.getSource(), ArgumentEntity.f(execute, "targets"));
		})));

		dispatcher.register(literalArgumentBuilder);
	}

	private static int setTag(CommandListenerWrapper commandListenerWrapper, Collection<EntityPlayer> players, NBTTagCompound nbtTagCompound) {
		for (EntityPlayer entityPlayer : players) {
			ItemStack itemStack = entityPlayer.getItemInMainHand();
			IChatBaseComponent nbtComponent = cast(nbtTagCompound.l()).format((mod) -> {
				return mod.setChatClickable(new ChatClickable(EnumClickAction.COPY_TO_CLIPBOARD, nbtTagCompound.asString())).setChatHoverable(new ChatHoverable(EnumHoverAction.SHOW_TEXT, new ChatMessage("chat.copy.click")));
			});
			if (itemStack.isEmpty()) {
				commandListenerWrapper.sendFailureMessage(new ChatMessage("commands.enchant.failed.itemless", entityPlayer.getScoreboardDisplayName()));
			} else {
				ItemStack beforeItemStack = itemStack.cloneItemStack();
				itemStack.setTag(nbtTagCompound);
				Chat.sendMessage(commandListenerWrapper, "command.item.settag.success", true, entityPlayer.getScoreboardDisplayName(), beforeItemStack.C(), nbtComponent);
			}
		}
		return players.size();
	}

	private static int setDamage(CommandListenerWrapper commandListenerWrapper, Collection<EntityPlayer> players, int damage) {
		for (EntityPlayer entityPlayer : players) {
			ItemStack itemStack = entityPlayer.getItemInMainHand();
			if (itemStack.isEmpty()) {
				commandListenerWrapper.sendFailureMessage(new ChatMessage("commands.enchant.failed.itemless", entityPlayer.getScoreboardDisplayName()));
			} else {
				ItemStack beforeItemStack = itemStack.cloneItemStack();
				itemStack.setDamage(damage);
				Chat.sendMessage(commandListenerWrapper, "command.item.setdamage.success", true, entityPlayer.getScoreboardDisplayName(), beforeItemStack.C(), damage);
			}
		}
		return players.size();
	}

	private static int setDurability(CommandListenerWrapper commandListenerWrapper, Collection<EntityPlayer> players, int durability) {
		for (EntityPlayer entityPlayer : players) {
			ItemStack itemStack = entityPlayer.getItemInMainHand();
			if (itemStack.isEmpty()) {
				commandListenerWrapper.sendFailureMessage(new ChatMessage("commands.enchant.failed.itemless", entityPlayer.getScoreboardDisplayName()));
			} else {
				ItemStack beforeItemStack = itemStack.cloneItemStack();
				itemStack.setDamage(itemStack.getItem().getMaxDurability() - durability);
				Chat.sendMessage(commandListenerWrapper, "command.item.setdurability.success", true, entityPlayer.getScoreboardDisplayName(), beforeItemStack.C(), durability);
			}
		}
		return players.size();
	}

	private static int setAmount(CommandListenerWrapper commandListenerWrapper, Collection<EntityPlayer> players, int amount) {
		for (EntityPlayer entityPlayer : players) {
			ItemStack itemStack = entityPlayer.getItemInMainHand();
			if (itemStack.isEmpty()) {
				commandListenerWrapper.sendFailureMessage(new ChatMessage("commands.enchant.failed.itemless", entityPlayer.getScoreboardDisplayName()));
			} else {
				ItemStack result = itemStack.cloneItemStack();
				itemStack.setCount(amount);
				Chat.sendMessage(commandListenerWrapper, "command.item.setamount.success", true, entityPlayer.getScoreboardDisplayName(), result.C(), amount);
			}
		}
		return players.size();
	}

	private static int getTag(CommandListenerWrapper commandListenerWrapper, Collection<EntityPlayer> players) {
		for (EntityPlayer entityPlayer : players) {
			ItemStack itemStack = entityPlayer.getItemInMainHand();
			if (itemStack.isEmpty()) {
				commandListenerWrapper.sendFailureMessage(new ChatMessage("commands.enchant.failed.itemless", entityPlayer.getScoreboardDisplayName()));
			} else if (itemStack.hasTag()) {
				IChatBaseComponent nbtComponent = cast(itemStack.getTag().l()).format((mod) -> {
					return mod.setChatClickable(new ChatClickable(EnumClickAction.COPY_TO_CLIPBOARD, itemStack.getTag().asString())).setChatHoverable(new ChatHoverable(EnumHoverAction.SHOW_TEXT, new ChatMessage("chat.copy.click")));
				});
				Chat.sendMessage(commandListenerWrapper, "command.item.gettag.success", false, entityPlayer.getScoreboardDisplayName(), itemStack.C(), nbtComponent);
			} else {
				Chat.sendFailureMessage(commandListenerWrapper, "command.item.gettag.failed", entityPlayer.getScoreboardDisplayName(), itemStack.C());
			}
		}
		return players.size();
	}

	private static int getDurability(CommandListenerWrapper commandListenerWrapper, Collection<EntityPlayer> players) {
		for (EntityPlayer entityPlayer : players) {
			ItemStack itemStack = entityPlayer.getItemInMainHand();
			if (itemStack.isEmpty()) {
				commandListenerWrapper.sendFailureMessage(new ChatMessage("commands.enchant.failed.itemless", entityPlayer.getScoreboardDisplayName()));
			} else if (itemStack.getItem().getMaxDurability() != 0) {
				Chat.sendMessage(commandListenerWrapper, "command.item.getdurability.success", false, entityPlayer.getScoreboardDisplayName(), itemStack.C(), itemStack.getItem().getMaxDurability() - itemStack.getDamage());
			} else {
				Chat.sendFailureMessage(commandListenerWrapper, "command.item.getdurability.failed", entityPlayer.getScoreboardDisplayName(), itemStack.C());
			}
		}
		return players.size();
	}

	private static int getDamage(CommandListenerWrapper commandListenerWrapper, Collection<EntityPlayer> players) {
		for (EntityPlayer entityPlayer : players) {
			ItemStack itemStack = entityPlayer.getItemInMainHand();
			if (itemStack.isEmpty()) {
				commandListenerWrapper.sendFailureMessage(new ChatMessage("commands.enchant.failed.itemless", entityPlayer.getScoreboardDisplayName()));
			} else if (itemStack.getDamage() != 0) {
				Chat.sendMessage(commandListenerWrapper, "command.item.getdamage.success", false, entityPlayer.getScoreboardDisplayName(), itemStack.C(), itemStack.getDamage());
			} else {
				Chat.sendFailureMessage(commandListenerWrapper, "command.item.getdamage.failed", entityPlayer.getScoreboardDisplayName(), itemStack.C());
			}
		}
		return players.size();
	}

	private static int getAmount(CommandListenerWrapper commandListenerWrapper, Collection<EntityPlayer> players) {
		for (EntityPlayer entityPlayer : players) {
			ItemStack itemStack = entityPlayer.getItemInMainHand();
			if (itemStack.isEmpty()) {
				commandListenerWrapper.sendFailureMessage(new ChatMessage("commands.enchant.failed.itemless", entityPlayer.getScoreboardDisplayName()));
			} else {
				Chat.sendMessage(commandListenerWrapper, "command.item.getamount.success", false, entityPlayer.getScoreboardDisplayName(), itemStack.C(), itemStack.getCount());
			}
		}
		return players.size();
	}

	private static IChatMutableComponent cast(IChatBaseComponent iChatBaseComponent) {
		return (IChatMutableComponent) iChatBaseComponent;
	}
}
