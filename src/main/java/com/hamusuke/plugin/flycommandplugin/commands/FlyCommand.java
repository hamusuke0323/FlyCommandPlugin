package com.hamusuke.plugin.flycommandplugin.commands;

import java.util.Collection;

import org.bukkit.util.Vector;

import com.hamusuke.plugin.flycommandplugin.chat.Chat;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;

import net.minecraft.server.v1_16_R3.ArgumentEntity;
import net.minecraft.server.v1_16_R3.ChatMessage;
import net.minecraft.server.v1_16_R3.CommandListenerWrapper;
import net.minecraft.server.v1_16_R3.EntityPlayer;

public class FlyCommand {
	public static void register(CommandDispatcher<CommandListenerWrapper> dispatcher) {
		LiteralArgumentBuilder<CommandListenerWrapper> literalArgumentBuilder = net.minecraft.server.v1_16_R3.CommandDispatcher.a("fly_flycmdpl").requires((permission) -> {
			return permission.hasPermission(2);
		});

		literalArgumentBuilder.then(net.minecraft.server.v1_16_R3.CommandDispatcher.a("targets", ArgumentEntity.d()).then(net.minecraft.server.v1_16_R3.CommandDispatcher.a("allow").executes((execute) -> {
			return a(execute.getSource(), ArgumentEntity.f(execute, "targets"), "allow", 0.0f, true, 0.0, 0.0, 0.0);
		})));

		literalArgumentBuilder.then(net.minecraft.server.v1_16_R3.CommandDispatcher.a("targets", ArgumentEntity.d()).then(net.minecraft.server.v1_16_R3.CommandDispatcher.a("noAllow").executes((execute) -> {
			return a(execute.getSource(), ArgumentEntity.f(execute, "targets"), "noAllow", 0.0f, true, 0.0, 0.0, 0.0);
		})));

		literalArgumentBuilder.then(net.minecraft.server.v1_16_R3.CommandDispatcher.a("targets", ArgumentEntity.d()).then(net.minecraft.server.v1_16_R3.CommandDispatcher.a("getFlySpeed").executes((execute) -> {
			return a(execute.getSource(), ArgumentEntity.f(execute, "targets"), "getFlySpeed", 0.0f, true, 0.0, 0.0, 0.0);
		})));

		literalArgumentBuilder.then(net.minecraft.server.v1_16_R3.CommandDispatcher.a("targets", ArgumentEntity.d()).then(net.minecraft.server.v1_16_R3.CommandDispatcher.a("getWalkSpeed").executes((execute) -> {
			return a(execute.getSource(), ArgumentEntity.f(execute, "targets"), "getWalkSpeed", 0.0f, true, 0.0, 0.0, 0.0);
		})));

		literalArgumentBuilder.then(net.minecraft.server.v1_16_R3.CommandDispatcher.a("targets", ArgumentEntity.d()).then(net.minecraft.server.v1_16_R3.CommandDispatcher.a("disableDamage").then(net.minecraft.server.v1_16_R3.CommandDispatcher.a("boolean", BoolArgumentType.bool()).executes((execute) -> {
			return a(execute.getSource(), ArgumentEntity.f(execute, "targets"), "disableDamage", 0.0f, BoolArgumentType.getBool(execute, "boolean"), 0.0, 0.0, 0.0);
		}))));

		literalArgumentBuilder.then(net.minecraft.server.v1_16_R3.CommandDispatcher.a("targets", ArgumentEntity.d()).then(net.minecraft.server.v1_16_R3.CommandDispatcher.a("isFlying").then(net.minecraft.server.v1_16_R3.CommandDispatcher.a("boolean", BoolArgumentType.bool()).executes((execute) -> {
			return a(execute.getSource(), ArgumentEntity.f(execute, "targets"), "isFlying", 0.0f, BoolArgumentType.getBool(execute, "boolean"), 0.0, 0.0, 0.0);
		}))));

		literalArgumentBuilder.then(net.minecraft.server.v1_16_R3.CommandDispatcher.a("targets", ArgumentEntity.d()).then(net.minecraft.server.v1_16_R3.CommandDispatcher.a("isInvisible").then(net.minecraft.server.v1_16_R3.CommandDispatcher.a("boolean", BoolArgumentType.bool()).executes((execute) -> {
			return a(execute.getSource(), ArgumentEntity.f(execute, "targets"), "isInvisible", 0.0f, BoolArgumentType.getBool(execute, "boolean"), 0.0, 0.0, 0.0);
		}))));

		literalArgumentBuilder.then(net.minecraft.server.v1_16_R3.CommandDispatcher.a("targets", ArgumentEntity.d()).then(net.minecraft.server.v1_16_R3.CommandDispatcher.a("setFlySpeed").then(net.minecraft.server.v1_16_R3.CommandDispatcher.a("float", FloatArgumentType.floatArg()).executes((execute) -> {
			return a(execute.getSource(), ArgumentEntity.f(execute, "targets"), "setFlySpeed", FloatArgumentType.getFloat(execute, "float"), true, 0.0, 0.0, 0.0);
		}))));

		literalArgumentBuilder.then(net.minecraft.server.v1_16_R3.CommandDispatcher.a("targets", ArgumentEntity.d()).then(net.minecraft.server.v1_16_R3.CommandDispatcher.a("setWalkSpeed").then(net.minecraft.server.v1_16_R3.CommandDispatcher.a("float", FloatArgumentType.floatArg()).executes((execute) -> {
			return a(execute.getSource(), ArgumentEntity.f(execute, "targets"), "setWalkSpeed", FloatArgumentType.getFloat(execute, "float"), true, 0.0, 0.0, 0.0);
		}))));

		literalArgumentBuilder.then(net.minecraft.server.v1_16_R3.CommandDispatcher.a("targets", ArgumentEntity.d()).then(net.minecraft.server.v1_16_R3.CommandDispatcher.a("setVelocity").then(net.minecraft.server.v1_16_R3.CommandDispatcher.a("vector_x", DoubleArgumentType.doubleArg()).then(net.minecraft.server.v1_16_R3.CommandDispatcher.a("vector_y", DoubleArgumentType.doubleArg()).then(net.minecraft.server.v1_16_R3.CommandDispatcher.a("vector_z", DoubleArgumentType.doubleArg()).executes((execute) -> {
			return a(execute.getSource(), ArgumentEntity.f(execute, "targets"), "setVelocity", 0.0f, true, DoubleArgumentType.getDouble(execute, "vector_x"), DoubleArgumentType.getDouble(execute, "vector_y"), DoubleArgumentType.getDouble(execute, "vector_z"));
		}))))));

		/*
		literalArgumentBuilder.then(net.minecraft.server.v1_16_R3.CommandDispatcher.a("targets", ArgumentEntity.d()).then(net.minecraft.server.v1_16_R3.CommandDispatcher.a("canBuild").then(net.minecraft.server.v1_16_R3.CommandDispatcher.a("boolean", BoolArgumentType.bool()).executes(commandContext -> {
			return a(commandContext.getSource(), ArgumentEntity.f(commandContext, "targets"), "canBuild", 0.0f, BoolArgumentType.getBool(commandContext, "boolean"), 0.0d, 0.0d, 0.0d);
		}))));
		*/

		dispatcher.register(literalArgumentBuilder);
	}

	private static int a(CommandListenerWrapper commandListenerWrapper, Collection<EntityPlayer> players, String enumfly, float f, boolean b, double d1, double d2, double d3) {
		int i = 0;

		if (enumfly.equalsIgnoreCase("allow")) {
			for (EntityPlayer entityPlayer : players) {
				if (!entityPlayer.abilities.canFly) {
					entityPlayer.abilities.canFly = true;
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
				if (entityPlayer.abilities.canFly) {
					entityPlayer.abilities.canFly = false;
					entityPlayer.abilities.isFlying = false;
					entityPlayer.fallDistance = 0.0F - (float) entityPlayer.locY();
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
				if (entityPlayer.abilities.flySpeed != f) {
					entityPlayer.abilities.flySpeed = f;
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
				if (entityPlayer.abilities.walkSpeed != f) {
					entityPlayer.abilities.walkSpeed = f;
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
					if (!entityPlayer.abilities.isFlying) {
						entityPlayer.abilities.isFlying = true;
						entityPlayer.updateAbilities();
						++i;
					}
				} else {
					if (entityPlayer.abilities.isFlying) {
						entityPlayer.abilities.isFlying = false;
						entityPlayer.fallDistance = -(float) entityPlayer.locY();
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
				Chat.sendMessage(commandListenerWrapper, "command.fly.getflyspeed.success", false, entityPlayer.getScoreboardDisplayName(), entityPlayer.abilities.flySpeed);
				++i;
			}
		} else if (enumfly.equalsIgnoreCase("getwalkspeed")) {
			for (EntityPlayer entityPlayer : players) {
				Chat.sendMessage(commandListenerWrapper, "command.fly.getwalkspeed.success", false, entityPlayer.getScoreboardDisplayName(), entityPlayer.abilities.walkSpeed);
				++i;
			}
		} else if (enumfly.equalsIgnoreCase("disableDamage")) {
			for (EntityPlayer entityPlayer : players) {
				if (b) {
					if (!entityPlayer.abilities.isInvulnerable) {
						entityPlayer.abilities.isInvulnerable = true;
						entityPlayer.updateAbilities();
						++i;
					}
				} else {
					if (entityPlayer.abilities.isInvulnerable) {
						entityPlayer.abilities.isInvulnerable = false;
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
		}/* else if (enumfly.equalsIgnoreCase("canBuild")) {
			EntityPlayer entityPlayer = null;

			for (EntityPlayer player : players) {
				if (player.abilities.canInstantlyBuild != b) {
					player.abilities.canInstantlyBuild = b;
					player.updateAbilities();
					i++;
					entityPlayer = player;
				}
			}

			if (i == 1) {
				Chat.sendMessage(commandListenerWrapper, "command.fly.canbuild." + b + ".single", true, entityPlayer.getScoreboardDisplayName());
			} else if (i > 1) {
				Chat.sendMessage(commandListenerWrapper, "command.fly.canbuild." + b + ".multiple", true, i);
			}
		}*/

		return i;
	}
}
