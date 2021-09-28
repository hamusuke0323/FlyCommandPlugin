package com.hamusuke.plugin.flycommandplugin.commands;

import java.util.Collection;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.DoubleArgumentType;

import net.minecraft.commands.CommandListenerWrapper;
import net.minecraft.commands.arguments.ArgumentEntity;
import net.minecraft.server.level.EntityPlayer;

public class DamageCommand {
	public static void register(CommandDispatcher<CommandListenerWrapper> dispatcher) {
		dispatcher.register(net.minecraft.commands.CommandDispatcher.a("damage_flycmdpl").requires((permission) -> {
			return permission.hasPermission(2);
		}).then(net.minecraft.commands.CommandDispatcher.a("targets", ArgumentEntity.d()).then(net.minecraft.commands.CommandDispatcher.a("double", DoubleArgumentType.doubleArg()).executes((execute) -> {
			return damage(ArgumentEntity.f(execute, "targets"), DoubleArgumentType.getDouble(execute, "double"));
		}))));
	}

	private static int damage(Collection<EntityPlayer> players, double damage) {
		players.forEach(player -> player.getBukkitEntity().damage(damage));
		return players.size();
	}
}
