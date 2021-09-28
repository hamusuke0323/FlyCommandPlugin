package com.hamusuke.plugin.flycommandplugin.commands;

import java.util.Collection;

import com.hamusuke.plugin.flycommandplugin.chat.Chat;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;

import net.minecraft.commands.CommandListenerWrapper;
import net.minecraft.commands.arguments.ArgumentEntity;
import net.minecraft.commands.arguments.ArgumentNBTTag;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.TextComponentTagVisitor;
import net.minecraft.network.chat.*;
import net.minecraft.server.level.EntityPlayer;
import net.minecraft.world.item.ItemStack;

public class ItemCommand {
	public static void register(CommandDispatcher<CommandListenerWrapper> dispatcher) {
		LiteralArgumentBuilder<CommandListenerWrapper> literalArgumentBuilder = net.minecraft.commands.CommandDispatcher.a("item_flycmdpl").requires((permission) -> {
			return permission.hasPermission(2);
		});

		literalArgumentBuilder.then(net.minecraft.commands.CommandDispatcher.a("setTag").then(net.minecraft.commands.CommandDispatcher.a("targets", ArgumentEntity.d()).then(net.minecraft.commands.CommandDispatcher.a("tag", ArgumentNBTTag.a()).executes((execute) -> {
			return setTag(execute.getSource(), ArgumentEntity.f(execute, "targets"), ArgumentNBTTag.a(execute, "tag"));
		}))));

		literalArgumentBuilder.then(net.minecraft.commands.CommandDispatcher.a("setDamage").then(net.minecraft.commands.CommandDispatcher.a("targets", ArgumentEntity.d()).then(net.minecraft.commands.CommandDispatcher.a("damage", IntegerArgumentType.integer()).executes((execute) -> {
			return setDamage(execute.getSource(), ArgumentEntity.f(execute, "targets"), IntegerArgumentType.getInteger(execute, "damage"));
		}))));

		literalArgumentBuilder.then(net.minecraft.commands.CommandDispatcher.a("setDurability").then(net.minecraft.commands.CommandDispatcher.a("targets", ArgumentEntity.d()).then(net.minecraft.commands.CommandDispatcher.a("durability", IntegerArgumentType.integer()).executes((execute) -> {
			return setDurability(execute.getSource(), ArgumentEntity.f(execute, "targets"), IntegerArgumentType.getInteger(execute, "durability"));
		}))));

		literalArgumentBuilder.then(net.minecraft.commands.CommandDispatcher.a("setAmount").then(net.minecraft.commands.CommandDispatcher.a("targets", ArgumentEntity.d()).then(net.minecraft.commands.CommandDispatcher.a("amount", IntegerArgumentType.integer(0, 127)).executes((execute) -> {
			return setAmount(execute.getSource(), ArgumentEntity.f(execute, "targets"), IntegerArgumentType.getInteger(execute, "amount"));
		}))));

		literalArgumentBuilder.then(net.minecraft.commands.CommandDispatcher.a("getTag").then(net.minecraft.commands.CommandDispatcher.a("targets", ArgumentEntity.d()).executes((execute) -> {
			return getTag(execute.getSource(), ArgumentEntity.f(execute, "targets"));
		})));

		literalArgumentBuilder.then(net.minecraft.commands.CommandDispatcher.a("getDamage").then(net.minecraft.commands.CommandDispatcher.a("targets", ArgumentEntity.d()).executes((execute) -> {
			return getDamage(execute.getSource(), ArgumentEntity.f(execute, "targets"));
		})));

		literalArgumentBuilder.then(net.minecraft.commands.CommandDispatcher.a("getDurability").then(net.minecraft.commands.CommandDispatcher.a("targets", ArgumentEntity.d()).executes((execute) -> {
			return getDurability(execute.getSource(), ArgumentEntity.f(execute, "targets"));
		})));

		literalArgumentBuilder.then(net.minecraft.commands.CommandDispatcher.a("getAmount").then(net.minecraft.commands.CommandDispatcher.a("targets", ArgumentEntity.d()).executes((execute) -> {
			return getAmount(execute.getSource(), ArgumentEntity.f(execute, "targets"));
		})));

		dispatcher.register(literalArgumentBuilder);
	}

	private static int setTag(CommandListenerWrapper commandListenerWrapper, Collection<EntityPlayer> players, NBTTagCompound nbtTagCompound) {
		for (EntityPlayer entityPlayer : players) {
			ItemStack itemStack = entityPlayer.getItemInMainHand();
			IChatBaseComponent nbtComponent = cast(nbtTagCompound).format((mod) -> {
				return mod.setChatClickable(new ChatClickable(ChatClickable.EnumClickAction.f, nbtTagCompound.asString())).setChatHoverable(new ChatHoverable(ChatHoverable.EnumHoverAction.a, new ChatMessage("chat.copy.click")));
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
				IChatBaseComponent nbtComponent = cast(itemStack.getTag()).format((mod) -> {
					return mod.setChatClickable(new ChatClickable(ChatClickable.EnumClickAction.f, itemStack.getTag().asString())).setChatHoverable(new ChatHoverable(ChatHoverable.EnumHoverAction.a, new ChatMessage("chat.copy.click")));
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

	private static IChatMutableComponent cast(NBTBase nbtBase) {
		return (IChatMutableComponent) new TextComponentTagVisitor("", 0).a(nbtBase);
	}
}
