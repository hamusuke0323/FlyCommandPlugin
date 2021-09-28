package com.hamusuke.plugin.flycommandplugin.commands;

import java.util.Collection;

import net.minecraft.commands.CommandListenerWrapper;
import net.minecraft.commands.arguments.ArgumentEntity;
import net.minecraft.server.level.EntityPlayer;
import org.bukkit.event.inventory.InventoryType;

import com.mojang.brigadier.CommandDispatcher;

public class CloseInventoryCommand {
	public static void register(CommandDispatcher<CommandListenerWrapper> dispatcher) {
		dispatcher.register(net.minecraft.commands.CommandDispatcher.a("closeinventory_flycmdpl").requires((permission) -> permission.hasPermission(2)).then(net.minecraft.commands.CommandDispatcher.a("targets", ArgumentEntity.d()).executes((execute) -> close(ArgumentEntity.f(execute, "targets")))));
	}

	private static int close(Collection<EntityPlayer> players) {
		int i = 0;
		for (EntityPlayer player : players) {
			InventoryType type = player.getBukkitEntity().getOpenInventory().getType();
			if (type == InventoryType.PLAYER || type == InventoryType.CREATIVE) {
				player.getBukkitEntity().getOpenInventory().close();
				i++;
			}
		}
		return i;
	}
}
