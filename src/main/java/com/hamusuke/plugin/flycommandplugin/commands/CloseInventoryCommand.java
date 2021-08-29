package com.hamusuke.plugin.flycommandplugin.commands;

import java.util.Collection;

import org.bukkit.event.inventory.InventoryType;

import com.mojang.brigadier.CommandDispatcher;

import net.minecraft.server.v1_16_R3.ArgumentEntity;
import net.minecraft.server.v1_16_R3.CommandListenerWrapper;
import net.minecraft.server.v1_16_R3.EntityPlayer;

public class CloseInventoryCommand {
	public static void register(CommandDispatcher<CommandListenerWrapper> dispatcher) {
		dispatcher.register(net.minecraft.server.v1_16_R3.CommandDispatcher.a("closeinventory_flycmdpl").requires((permission) -> permission.hasPermission(2)).then(net.minecraft.server.v1_16_R3.CommandDispatcher.a("targets", ArgumentEntity.d()).executes((execute) -> close(ArgumentEntity.f(execute, "targets")))));
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
