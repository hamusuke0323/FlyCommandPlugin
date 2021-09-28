package com.hamusuke.plugin.flycommandplugin.commands;

import java.util.Collection;

import net.minecraft.commands.CommandListenerWrapper;
import net.minecraft.commands.arguments.ArgumentEntity;
import net.minecraft.network.chat.ChatMessage;
import net.minecraft.server.level.EntityPlayer;
import org.bukkit.util.Vector;

import com.hamusuke.plugin.flycommandplugin.chat.Chat;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;

public class FlyCommand {
	public static void register(CommandDispatcher<CommandListenerWrapper> dispatcher) {
		LiteralArgumentBuilder<CommandListenerWrapper> literalArgumentBuilder = net.minecraft.commands.CommandDispatcher.a("fly_flycmdpl").requires((permission) -> {
			return permission.hasPermission(2);
		});

		literalArgumentBuilder.then(net.minecraft.commands.CommandDispatcher.a("targets", ArgumentEntity.d()).then(net.minecraft.commands.CommandDispatcher.a("allow").executes((execute) -> {
			return a(execute.getSource(), ArgumentEntity.f(execute, "targets"), "allow", 0.0f, true, 0.0, 0.0, 0.0);
		})));

		literalArgumentBuilder.then(net.minecraft.commands.CommandDispatcher.a("targets", ArgumentEntity.d()).then(net.minecraft.commands.CommandDispatcher.a("noAllow").executes((execute) -> {
			return a(execute.getSource(), ArgumentEntity.f(execute, "targets"), "noAllow", 0.0f, true, 0.0, 0.0, 0.0);
		})));

		literalArgumentBuilder.then(net.minecraft.commands.CommandDispatcher.a("targets", ArgumentEntity.d()).then(net.minecraft.commands.CommandDispatcher.a("getFlySpeed").executes((execute) -> {
			return a(execute.getSource(), ArgumentEntity.f(execute, "targets"), "getFlySpeed", 0.0f, true, 0.0, 0.0, 0.0);
		})));

		literalArgumentBuilder.then(net.minecraft.commands.CommandDispatcher.a("targets", ArgumentEntity.d()).then(net.minecraft.commands.CommandDispatcher.a("getWalkSpeed").executes((execute) -> {
			return a(execute.getSource(), ArgumentEntity.f(execute, "targets"), "getWalkSpeed", 0.0f, true, 0.0, 0.0, 0.0);
		})));

		literalArgumentBuilder.then(net.minecraft.commands.CommandDispatcher.a("targets", ArgumentEntity.d()).then(net.minecraft.commands.CommandDispatcher.a("disableDamage").then(net.minecraft.commands.CommandDispatcher.a("boolean", BoolArgumentType.bool()).executes((execute) -> {
			return a(execute.getSource(), ArgumentEntity.f(execute, "targets"), "disableDamage", 0.0f, BoolArgumentType.getBool(execute, "boolean"), 0.0, 0.0, 0.0);
		}))));

		literalArgumentBuilder.then(net.minecraft.commands.CommandDispatcher.a("targets", ArgumentEntity.d()).then(net.minecraft.commands.CommandDispatcher.a("isFlying").then(net.minecraft.commands.CommandDispatcher.a("boolean", BoolArgumentType.bool()).executes((execute) -> {
			return a(execute.getSource(), ArgumentEntity.f(execute, "targets"), "isFlying", 0.0f, BoolArgumentType.getBool(execute, "boolean"), 0.0, 0.0, 0.0);
		}))));

		literalArgumentBuilder.then(net.minecraft.commands.CommandDispatcher.a("targets", ArgumentEntity.d()).then(net.minecraft.commands.CommandDispatcher.a("isInvisible").then(net.minecraft.commands.CommandDispatcher.a("boolean", BoolArgumentType.bool()).executes((execute) -> {
			return a(execute.getSource(), ArgumentEntity.f(execute, "targets"), "isInvisible", 0.0f, BoolArgumentType.getBool(execute, "boolean"), 0.0, 0.0, 0.0);
		}))));

		literalArgumentBuilder.then(net.minecraft.commands.CommandDispatcher.a("targets", ArgumentEntity.d()).then(net.minecraft.commands.CommandDispatcher.a("setFlySpeed").then(net.minecraft.commands.CommandDispatcher.a("float", FloatArgumentType.floatArg()).executes((execute) -> {
			return a(execute.getSource(), ArgumentEntity.f(execute, "targets"), "setFlySpeed", FloatArgumentType.getFloat(execute, "float"), true, 0.0, 0.0, 0.0);
		}))));

		literalArgumentBuilder.then(net.minecraft.commands.CommandDispatcher.a("targets", ArgumentEntity.d()).then(net.minecraft.commands.CommandDispatcher.a("setWalkSpeed").then(net.minecraft.commands.CommandDispatcher.a("float", FloatArgumentType.floatArg()).executes((execute) -> {
			return a(execute.getSource(), ArgumentEntity.f(execute, "targets"), "setWalkSpeed", FloatArgumentType.getFloat(execute, "float"), true, 0.0, 0.0, 0.0);
		}))));

		literalArgumentBuilder.then(net.minecraft.commands.CommandDispatcher.a("targets", ArgumentEntity.d()).then(net.minecraft.commands.CommandDispatcher.a("setVelocity").then(net.minecraft.commands.CommandDispatcher.a("vector_x", DoubleArgumentType.doubleArg()).then(net.minecraft.commands.CommandDispatcher.a("vector_y", DoubleArgumentType.doubleArg()).then(net.minecraft.commands.CommandDispatcher.a("vector_z", DoubleArgumentType.doubleArg()).executes((execute) -> {
			return a(execute.getSource(), ArgumentEntity.f(execute, "targets"), "setVelocity", 0.0f, true, DoubleArgumentType.getDouble(execute, "vector_x"), DoubleArgumentType.getDouble(execute, "vector_y"), DoubleArgumentType.getDouble(execute, "vector_z"));
		}))))));

		dispatcher.register(literalArgumentBuilder);
	}

	private static int a(CommandListenerWrapper commandListenerWrapper, Collection<EntityPlayer> players, String enumfly, float f, boolean b, double d1, double d2, double d3) {
		int i = 0;

		if (enumfly.equalsIgnoreCase("allow")) {
			for (EntityPlayer entityPlayer : players) {
				if (!entityPlayer.getAbilities().c) {
					entityPlayer.getAbilities().c = true;
					entityPlayer.updateAbilities();
					++i;
				}
			}
			if (i == 1) {
				Chat.sendMessage(commandListenerWrapper, "command.fly.allow.success.single", true, players.iterator().next().getScoreboardDisplayName());
			} else if (i > 1) {
				Chat.sendMessage(commandListenerWrapper, "command.fly.allow.success.multiple", true, i);
			}
		} else if (enumfly.equalsIgnoreCase("noAllow")) {
			for (EntityPlayer entityPlayer : players) {
				if (entityPlayer.getAbilities().c) {
					entityPlayer.getAbilities().c = false;
					entityPlayer.getAbilities().b = false;
					entityPlayer.K = 0.0F - (float) entityPlayer.locY();
					entityPlayer.updateAbilities();
					++i;
				}
			}
			if (i == 1) {
				Chat.sendMessage(commandListenerWrapper, "command.fly.noallow.success.single", true, players.iterator().next().getScoreboardDisplayName());
			} else if (i > 1) {
				Chat.sendMessage(commandListenerWrapper, "command.fly.noallow.success.multiple", true, i);
			}
		} else if (enumfly.equalsIgnoreCase("setFlySpeed")) {
			for (EntityPlayer entityPlayer : players) {
				if (entityPlayer.getAbilities().f != f) {
					entityPlayer.getAbilities().f = f;
					entityPlayer.updateAbilities();
					++i;
				}
			}
			if (i == 1) {
				Chat.sendMessage(commandListenerWrapper, "command.fly.setflyspeed.success.single", true, players.iterator().next().getScoreboardDisplayName(), f);
			} else if (i > 1) {
				Chat.sendMessage(commandListenerWrapper, "command.fly.setflyspeed.success.multiple", true, i, f);
			}
		} else if (enumfly.equalsIgnoreCase("setWalkSpeed")) {
			for (EntityPlayer entityPlayer : players) {
				if (entityPlayer.getAbilities().g != f) {
					entityPlayer.getAbilities().g = f;
					entityPlayer.updateAbilities();
					++i;
				}
			}
			if (i == 1) {
				Chat.sendMessage(commandListenerWrapper, "command.fly.setwalkspeed.success.single", true, players.iterator().next().getScoreboardDisplayName(), f);
			} else if (i > 1) {
				Chat.sendMessage(commandListenerWrapper, "command.fly.setwalkspeed.success.multiple", true, i, f);
			}
		} else if (enumfly.equalsIgnoreCase("isflying")) {
			for (EntityPlayer entityPlayer : players) {
				if (b) {
					if (!entityPlayer.getAbilities().b) {
						entityPlayer.getAbilities().b = true;
						entityPlayer.updateAbilities();
						++i;
					}
				} else {
					if (entityPlayer.getAbilities().b) {
						entityPlayer.getAbilities().b = false;
						entityPlayer.K = -(float) entityPlayer.locY();
						entityPlayer.updateAbilities();
						++i;
					}
				}
			}
			if (i == 1) {
				Chat.sendMessage(commandListenerWrapper, "command.fly.isflying." + b + ".success.single", true, players.iterator().next().getScoreboardDisplayName());
			} else if (i > 1) {
				Chat.sendMessage(commandListenerWrapper, "command.fly.isflying." + b + ".success.multiple", true, i);
			}
		} else if (enumfly.equalsIgnoreCase("getflyspeed")) {
			for (EntityPlayer entityPlayer : players) {
				Chat.sendMessage(commandListenerWrapper, "command.fly.getflyspeed.success", false, entityPlayer.getScoreboardDisplayName(), entityPlayer.getAbilities().f);
				++i;
			}
		} else if (enumfly.equalsIgnoreCase("getwalkspeed")) {
			for (EntityPlayer entityPlayer : players) {
				Chat.sendMessage(commandListenerWrapper, "command.fly.getwalkspeed.success", false, entityPlayer.getScoreboardDisplayName(), entityPlayer.getAbilities().g);
				++i;
			}
		} else if (enumfly.equalsIgnoreCase("disableDamage")) {
			for (EntityPlayer entityPlayer : players) {
				if (b) {
					if (!entityPlayer.getAbilities().a) {
						entityPlayer.getAbilities().a = true;
						entityPlayer.updateAbilities();
						++i;
					}
				} else {
					if (entityPlayer.getAbilities().a) {
						entityPlayer.getAbilities().a = false;
						entityPlayer.updateAbilities();
						++i;
					}
				}
			}
			if (i == 1) {
				Chat.sendMessage(commandListenerWrapper, "command.fly.disabledamage." + b + ".success.single", true, players.iterator().next().getScoreboardDisplayName());
			} else if (i > 1) {
				Chat.sendMessage(commandListenerWrapper, "command.fly.disabledamage." + b + ".success.multiple", true, i);
			}
		} else if (enumfly.equalsIgnoreCase("isInvisible")) {
			for (EntityPlayer entityPlayer : players) {
				if (b) {
					if (!entityPlayer.isInvisible()) {
						entityPlayer.setInvisible(true);
						++i;
					}
				} else {
					if (entityPlayer.isInvisible()) {
						entityPlayer.setInvisible(false);
						++i;
					}
				}
			}
			if (i == 1) {
				if (b) {
					commandListenerWrapper.sendMessage(new ChatMessage("commands.effect.give.success.single", new ChatMessage("effect.minecraft.invisibility"), players.iterator().next().getScoreboardDisplayName()), true);
				} else {
					commandListenerWrapper.sendMessage(new ChatMessage("commands.effect.clear.specific.success.single", new ChatMessage("effect.minecraft.invisibility"), players.iterator().next().getScoreboardDisplayName()), true);
				}
			} else if (i > 1) {
				if (b) {
					commandListenerWrapper.sendMessage(new ChatMessage("commands.effect.give.success.multiple", new ChatMessage("effect.minecraft.invisibility"), i), true);
				} else {
					commandListenerWrapper.sendMessage(new ChatMessage("commands.effect.clear.specific.success.multiple", new ChatMessage("effect.minecraft.invisibility"), i), true);
				}
			}
		} else if (enumfly.equalsIgnoreCase("setVelocity")) {
			for (EntityPlayer entityPlayer : players) {
				entityPlayer.getBukkitEntity().setVelocity(new Vector(d1, d2, d3));
				++i;
			}
			if (i == 1) {
				Chat.sendMessage(commandListenerWrapper, "command.fly.setvelocity.success.single", true, players.iterator().next().getScoreboardDisplayName(), d1, d2, d3);
			} else if (i > 1) {
				Chat.sendMessage(commandListenerWrapper, "command.fly.setvelocity.success.multiple", true, i, d1, d2, d3);
			}
		}

		return i;
	}
}
