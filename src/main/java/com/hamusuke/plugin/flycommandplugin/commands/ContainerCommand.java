package com.hamusuke.plugin.flycommandplugin.commands;

import java.util.Iterator;
import java.util.List;

import com.google.common.collect.Lists;
import net.minecraft.commands.CommandListenerWrapper;
import net.minecraft.commands.arguments.coordinates.ArgumentVec3;
import net.minecraft.commands.arguments.item.ArgumentItemStack;
import net.minecraft.network.chat.IChatBaseComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3D;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_17_R1.inventory.CraftItemStack;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

import com.hamusuke.plugin.flycommandplugin.chat.Chat;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;

public class ContainerCommand {
	public static void register(CommandDispatcher<CommandListenerWrapper> dispatcher) {
		LiteralArgumentBuilder<CommandListenerWrapper> literalArgumentBuilder = net.minecraft.commands.CommandDispatcher.a("container_flycmdpl").requires((permission) -> {
			return permission.hasPermission(2);
		});

		literalArgumentBuilder.then(net.minecraft.commands.CommandDispatcher.a("add").then(net.minecraft.commands.CommandDispatcher.a("location", ArgumentVec3.a()).then(net.minecraft.commands.CommandDispatcher.a("item", ArgumentItemStack.a()).executes((execute) -> {
			return add(execute.getSource(), ArgumentVec3.a(execute, "location"), ArgumentItemStack.a(execute, "item").a(1, true));
		}).then(net.minecraft.commands.CommandDispatcher.a("amount", IntegerArgumentType.integer(0)).executes((execute) -> {
			return add(execute.getSource(), ArgumentVec3.a(execute, "location"), ArgumentItemStack.a(execute, "item").a(IntegerArgumentType.getInteger(execute, "amount"), true));
		})))));

		literalArgumentBuilder.then(net.minecraft.commands.CommandDispatcher.a("clear").then(net.minecraft.commands.CommandDispatcher.a("location", ArgumentVec3.a()).executes((execute) -> {
			return clear(execute.getSource(), ArgumentVec3.a(execute, "location"));
		})));

		literalArgumentBuilder.then(net.minecraft.commands.CommandDispatcher.a("get").then(net.minecraft.commands.CommandDispatcher.a("location", ArgumentVec3.a()).executes((execute) -> {
			return get(execute.getSource(), ArgumentVec3.a(execute, "location"));
		})));

		literalArgumentBuilder.then(net.minecraft.commands.CommandDispatcher.a("remove").then(net.minecraft.commands.CommandDispatcher.a("location", ArgumentVec3.a()).then(net.minecraft.commands.CommandDispatcher.a("item", ArgumentItemStack.a()).executes((execute) -> {
			return remove(execute.getSource(), ArgumentVec3.a(execute, "location"), ArgumentItemStack.a(execute, "item").a(1, true));
		}).then(net.minecraft.commands.CommandDispatcher.a("amount", IntegerArgumentType.integer(0)).executes((execute) -> {
			return remove(execute.getSource(), ArgumentVec3.a(execute, "location"), ArgumentItemStack.a(execute, "item").a(IntegerArgumentType.getInteger(execute, "amount"), true));
		})))));

		literalArgumentBuilder.then(net.minecraft.commands.CommandDispatcher.a("set").then(net.minecraft.commands.CommandDispatcher.a("location", ArgumentVec3.a()).then(net.minecraft.commands.CommandDispatcher.a("item", ArgumentItemStack.a()).executes((execute) -> {
			return set(execute.getSource(), ArgumentVec3.a(execute, "location"), ArgumentItemStack.a(execute, "item").a(1, true));
		}).then(net.minecraft.commands.CommandDispatcher.a("amount", IntegerArgumentType.integer(0)).executes((execute) -> {
			return set(execute.getSource(), ArgumentVec3.a(execute, "location"), ArgumentItemStack.a(execute, "item").a(IntegerArgumentType.getInteger(execute, "amount"), true));
		})))));

		dispatcher.register(literalArgumentBuilder);
	}

	private static int add(CommandListenerWrapper commandListenerWrapper, Vec3D vec3, ItemStack itemStackNMS) {
		Location location = new Location(commandListenerWrapper.getWorld().getWorld(), vec3.b, vec3.c, vec3.d);
		Block block = location.getBlock();
		try {
			((InventoryHolder) block.getState()).getInventory().addItem(CraftItemStack.asBukkitCopy(itemStackNMS));
			Chat.sendMessage(commandListenerWrapper, "command.container.add.success", true, location.getBlockX(), location.getBlockY(), location.getBlockZ(), getBlockDisplayName(block.getType()), itemStackNMS.C(), itemStackNMS.getCount());
		} catch (ClassCastException e) {
			Chat.sendFailureMessage(commandListenerWrapper, "command.container.failed", location.getBlockX(), location.getBlockY(), location.getBlockZ(), getBlockDisplayName(block.getType()));
		}
		return 1;
	}

	private static int set(CommandListenerWrapper commandListenerWrapper, Vec3D vec3, ItemStack itemStackNMS) {
		Location location = new Location(commandListenerWrapper.getWorld().getWorld(), vec3.b, vec3.c, vec3.d);
		Block block = location.getBlock();
		try {
			((InventoryHolder) block.getState()).getInventory().clear();
			((InventoryHolder) block.getState()).getInventory().addItem(CraftItemStack.asBukkitCopy(itemStackNMS));
			Chat.sendMessage(commandListenerWrapper, "command.container.set.success", true, location.getBlockX(), location.getBlockY(), location.getBlockZ(), getBlockDisplayName(block.getType()), itemStackNMS.C(), itemStackNMS.getCount());
		} catch (ClassCastException e) {
			Chat.sendFailureMessage(commandListenerWrapper, "command.container.failed", location.getBlockX(), location.getBlockY(), location.getBlockZ(), getBlockDisplayName(block.getType()));
		}
		return 1;
	}

	private static int remove(CommandListenerWrapper commandListenerWrapper, Vec3D vec3, ItemStack itemStackNMS) {
		Location location = new Location(commandListenerWrapper.getWorld().getWorld(), vec3.b, vec3.c, vec3.d);
		Block block = location.getBlock();
		try {
			org.bukkit.inventory.ItemStack bukkitItemStack = CraftItemStack.asBukkitCopy(itemStackNMS);
			Inventory inventory = ((InventoryHolder) block.getState()).getInventory();
			inventory.removeItem(bukkitItemStack);
			Chat.sendMessage(commandListenerWrapper, "command.container.remove.success", true, location.getBlockX(), location.getBlockY(), location.getBlockZ(), getBlockDisplayName(block.getType()), itemStackNMS.C(), itemStackNMS.getCount());
		} catch (ClassCastException e) {
			Chat.sendFailureMessage(commandListenerWrapper, "command.container.failed", location.getBlockX(), location.getBlockY(), location.getBlockZ(), getBlockDisplayName(block.getType()));
		}
		return 1;
	}

	private static int clear(CommandListenerWrapper commandListenerWrapper, Vec3D vec3) {
		Location location = new Location(commandListenerWrapper.getWorld().getWorld(), vec3.b, vec3.c, vec3.d);
		Block block = location.getBlock();
		try {
			Inventory inventory = ((InventoryHolder) block.getState()).getInventory();
			inventory.clear();
			Chat.sendMessage(commandListenerWrapper, "command.container.clear.success", true, location.getBlockX(), location.getBlockY(), location.getBlockZ(), getBlockDisplayName(block.getType()));
		} catch (ClassCastException e) {
			Chat.sendFailureMessage(commandListenerWrapper, "command.container.failed", location.getBlockX(), location.getBlockY(), location.getBlockZ(), getBlockDisplayName(block.getType()));
		}
		return 1;
	}

	private static int get(CommandListenerWrapper commandListenerWrapper, Vec3D vec3) {
		int r = 0;
		Location location = new Location(commandListenerWrapper.getWorld().getWorld(), vec3.b, vec3.c, vec3.d);
		Block block = location.getBlock();
		try {
			Inventory inventory = ((InventoryHolder) block.getState()).getInventory();
			org.bukkit.inventory.ItemStack[] itemStack = inventory.getStorageContents();
			List<org.bukkit.inventory.ItemStack> itemStackList = Lists.newArrayList(itemStack);

			if (itemStackList.isEmpty()) {
				Chat.sendFailureMessage(commandListenerWrapper, "command.container.get.failed", location.getBlockX(), location.getBlockY(), location.getBlockZ(), getBlockDisplayName(block.getType()));
				return 0;
			}

			Chat.sendMessage(commandListenerWrapper, "command.container.get.success.start", false, location.getBlockX(), location.getBlockY(), location.getBlockZ(), getBlockDisplayName(block.getType()));
			for (Iterator<org.bukkit.inventory.ItemStack> iterator = itemStackList.iterator(); iterator.hasNext(); ) {
				org.bukkit.inventory.ItemStack i = iterator.next();
				if (iterator.hasNext()) {
					Chat.sendMessage(commandListenerWrapper, "command.container.get.success.counting", false, CraftItemStack.asNMSCopy(i).C(), CraftItemStack.asNMSCopy(i).getCount());
				} else {
					Chat.sendMessage(commandListenerWrapper, "command.container.get.success.counted", false, CraftItemStack.asNMSCopy(i).C(), CraftItemStack.asNMSCopy(i).getCount());
				}
				++r;
			}
		} catch (ClassCastException e) {
			Chat.sendFailureMessage(commandListenerWrapper, "command.container.failed", location.getBlockX(), location.getBlockY(), location.getBlockZ(), getBlockDisplayName(block.getType()));
		}
		return r;
	}

	private static IChatBaseComponent getBlockDisplayName(Material material) {
		org.bukkit.inventory.ItemStack itemStack = new org.bukkit.inventory.ItemStack(material);
		return CraftItemStack.asNMSCopy(itemStack).G();
	}
}
