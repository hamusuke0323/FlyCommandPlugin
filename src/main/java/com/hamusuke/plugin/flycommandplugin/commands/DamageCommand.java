package com.hamusuke.plugin.flycommandplugin.commands;

import java.util.Collection;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.DoubleArgumentType;

import net.minecraft.server.v1_16_R3.ArgumentEntity;
import net.minecraft.server.v1_16_R3.CommandListenerWrapper;
import net.minecraft.server.v1_16_R3.EntityPlayer;

public class DamageCommand {
	public static void register(CommandDispatcher<CommandListenerWrapper> dispatcher) {
		dispatcher.register(net.minecraft.server.v1_16_R3.CommandDispatcher.a("damage_flycmdpl").requires((permission) -> {
			return permission.hasPermission(2);
		}).then(net.minecraft.server.v1_16_R3.CommandDispatcher.a("targets", ArgumentEntity.d()).then(net.minecraft.server.v1_16_R3.CommandDispatcher.a("double", DoubleArgumentType.doubleArg()).executes((execute) -> {
			return damage(ArgumentEntity.f(execute, "targets"), DoubleArgumentType.getDouble(execute, "double"));
		}))));
	}

	private static int damage(Collection<EntityPlayer> players, double damage) {
		players.forEach(player -> player.getBukkitEntity().damage(damage));
		return players.size();
	}
}
