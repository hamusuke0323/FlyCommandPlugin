package com.hamusuke.plugin.flycommandplugin;

import java.util.UUID;

import com.hamusuke.plugin.flycommandplugin.settings.FlyCommandPluginSettings;
import net.minecraft.EnumChatFormat;
import net.minecraft.SystemUtils;
import net.minecraft.commands.CommandListenerWrapper;
import net.minecraft.network.chat.*;
import net.minecraft.network.protocol.game.PacketPlayOutChat;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.EntityPlayer;
import net.minecraft.util.MathHelper;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.projectile.EntitySnowball;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.craftbukkit.v1_17_R1.command.VanillaCommandWrapper;
import org.bukkit.craftbukkit.v1_17_R1.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_17_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_17_R1.entity.CraftSnowball;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.ExplosionPrimeEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;
import org.bukkit.event.inventory.InventoryCreativeEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

import com.hamusuke.plugin.flycommandplugin.chat.Chat;

public class FlyCommandPluginEvents implements Listener {
	private int field_740211_a_ = 0;

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		FlyCommandPlugin plugin = FlyCommandPlugin.getInstance();
		if (plugin == null) {
			return;
		}

		event.setJoinMessage(null);

		Player eventPlayer = event.getPlayer();
		UUID uniqueId = eventPlayer.getUniqueId();

		FileConfiguration config = plugin.getConfig();

		FlyCommandPluginSettings.savePlayerConfig(plugin, config, uniqueId);
		loadPlayerAbilities(plugin, config, eventPlayer);

		String joinMsg = FlyCommandPluginSettings.PlayerSettings.JOIN_MESSAGE.get(uniqueId);
		boolean isJoinMessageJsonElement = FlyCommandPluginSettings.PlayerSettings.IS_JOIN_MESSAGE_JSON_ELEMENT.get(uniqueId);

		if (joinMsg != null) {
			if (!joinMsg.equalsIgnoreCase("null") && !joinMsg.equalsIgnoreCase("multiplayer.player.joined")) {
				if (!isJoinMessageJsonElement) {
					Bukkit.getConsoleSender().sendMessage(ChatColor.YELLOW + (eventPlayer.getName() + " " + joinMsg));
					PacketPlayOutChat field_290571_a_ = new PacketPlayOutChat(IChatBaseComponent.ChatSerializer.a("[\"\",{\"text\":\"" + eventPlayer.getName() + "\",\"color\":\"yellow\",\"clickEvent\":{\"action\":\"suggest_command\",\"value\":\"/tell " + eventPlayer.getName() + " \"},\"hoverEvent\":{\"action\":\"show_text\",\"value\":[\"\",{\"text\":\"" + eventPlayer.getName() + "\\n\"},{\"translate\":\"gui.entity_tooltip.type\",\"with\":[{\"translate\":\"entity.minecraft.player\"}]},{\"text\":\"\\n\"},{\"text\":\"" + uniqueId + "\"}]}},{\"text\":\" " + joinMsg + "\",\"color\":\"yellow\"}]"), ChatMessageType.b, SystemUtils.b);
					Bukkit.getOnlinePlayers().forEach(player -> ((CraftPlayer) player).getHandle().b.sendPacket(field_290571_a_));
				} else {
					Bukkit.getConsoleSender().sendMessage(IChatBaseComponent.ChatSerializer.a(joinMsg.replace("%PlayerName%", eventPlayer.getName()).replace("%PlayerUUID%", uniqueId.toString())).getText());
					PacketPlayOutChat field_190285_a_ = new PacketPlayOutChat(IChatBaseComponent.ChatSerializer.a(joinMsg.replace("%PlayerName%", eventPlayer.getName()).replace("%PlayerUUID%", uniqueId.toString())), ChatMessageType.b, SystemUtils.b);
					Bukkit.getOnlinePlayers().forEach(player -> ((CraftPlayer) player).getHandle().b.sendPacket(field_190285_a_));
				}
			} else if (joinMsg.equalsIgnoreCase("multiplayer.player.joined")) {
				IChatBaseComponent iChatBaseComponent = new ChatMessage("multiplayer.player.joined", new Object[]{getScoreboardDisplayName(eventPlayer)}).a(EnumChatFormat.o);
				PacketPlayOutChat packet = new PacketPlayOutChat(iChatBaseComponent, ChatMessageType.b, SystemUtils.b);
				Bukkit.getOnlinePlayers().forEach(player -> ((CraftPlayer) player).getHandle().b.sendPacket(packet));
				MinecraftServer.p.info(iChatBaseComponent.getString());
			}
		}
	}

	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event) {
		FlyCommandPlugin plugin = FlyCommandPlugin.getInstance();
		if (plugin == null) {
			return;
		}

		event.setQuitMessage(null);
		Player eventPlayer = event.getPlayer();
		FileConfiguration config = plugin.getConfig();
		savePlayerAbilities(plugin, config, eventPlayer);

		String quitMsg = FlyCommandPluginSettings.PlayerSettings.QUIT_MESSAGE.get(eventPlayer.getUniqueId());
		boolean isQuitMsgJsonElement = FlyCommandPluginSettings.PlayerSettings.IS_QUIT_MESSAGE_JSON_ELEMENT.get(eventPlayer.getUniqueId());

		if (quitMsg != null) {
			if (!quitMsg.equalsIgnoreCase("null") && !quitMsg.equalsIgnoreCase("multiplayer.player.left")) {
				if (!isQuitMsgJsonElement) {
					Bukkit.getConsoleSender().sendMessage(ChatColor.YELLOW + (eventPlayer.getName() + " " + quitMsg));
					PacketPlayOutChat field_385902_a_ = new PacketPlayOutChat(IChatBaseComponent.ChatSerializer.a("[\"\",{\"text\":\"" + eventPlayer.getName() + "\",\"color\":\"yellow\",\"clickEvent\":{\"action\":\"suggest_command\",\"value\":\"/tell " + eventPlayer.getName() + " \"},\"hoverEvent\":{\"action\":\"show_text\",\"value\":[\"\",{\"text\":\"" + eventPlayer.getName() + "\\n\"},{\"translate\":\"gui.entity_tooltip.type\",\"with\":[{\"translate\":\"entity.minecraft.player\"}]},{\"text\":\"\\n\"},{\"text\":\"" + eventPlayer.getUniqueId() + "\"}]}},{\"text\":\" " + quitMsg + "\",\"color\":\"yellow\"}]"), ChatMessageType.b, SystemUtils.b);
					Bukkit.getOnlinePlayers().forEach(player -> ((CraftPlayer) player).getHandle().b.sendPacket(field_385902_a_));
				} else {
					Bukkit.getConsoleSender().sendMessage(IChatBaseComponent.ChatSerializer.a(quitMsg.replace("%PlayerName%", eventPlayer.getName()).replace("%PlayerUUID%", eventPlayer.getUniqueId().toString())).getText());
					PacketPlayOutChat field_085902_a_ = new PacketPlayOutChat(IChatBaseComponent.ChatSerializer.a(quitMsg.replace("%PlayerName%", eventPlayer.getName()).replace("%PlayerUUID%", eventPlayer.getUniqueId().toString())), ChatMessageType.b, SystemUtils.b);
					Bukkit.getOnlinePlayers().forEach(player -> ((CraftPlayer) player).getHandle().b.sendPacket(field_085902_a_));
				}
			} else if (quitMsg.equalsIgnoreCase("multiplayer.player.left")) {
				IChatBaseComponent iChatBaseComponent = new ChatMessage("multiplayer.player.left", new Object[]{getScoreboardDisplayName(eventPlayer)}).a(EnumChatFormat.o);
				PacketPlayOutChat packet = new PacketPlayOutChat(iChatBaseComponent, ChatMessageType.b, SystemUtils.b);
				Bukkit.getOnlinePlayers().forEach(player -> ((CraftPlayer) player).getHandle().b.sendPacket(packet));
				MinecraftServer.p.info(iChatBaseComponent.getString());
			}
		}
	}

	@EventHandler
	public void onEntityDamage(EntityDamageEvent event) {
		if (event.getEntityType() == EntityType.PLAYER) {
			Player player = (Player) event.getEntity();
			boolean bl = FlyCommandPluginSettings.PlayerSettings.ENABLE_SNOWBALL_DAMAGE.get(player.getUniqueId());
			boolean bl2 = FlyCommandPluginSettings.PlayerSettings.DISABLE_DAMAGE.get(player.getUniqueId());

			if (bl && event.getCause() == DamageCause.ENTITY_EXPLOSION) {
				event.setCancelled(true);
			}

			if (bl2) {
				event.setCancelled(true);
			}
		}
	}

	@EventHandler
	public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
		Player player = event.getPlayer();
		if (player.isSneaking()) {
			if (field_740211_a_ == 1) {
				field_740211_a_ = 0;
				return;
			}
			field_740211_a_ = 1;
			org.bukkit.entity.Entity rightClicked = event.getRightClicked();
			Chat.sendMessage(VanillaCommandWrapper.getListener(player), "event.entity.interact", false, ((CraftEntity) rightClicked).getHandle().getScoreboardDisplayName(), func_098311_e(rightClicked.getUniqueId().toString(), rightClicked.getUniqueId().toString(), new ChatMessage("chat.copy.click")).a(EnumChatFormat.o));
		}
	}

	@EventHandler
	public void onProjectileHit(ProjectileHitEvent event) {
		if (event.getHitEntity() instanceof Player player) {
			EntityPlayer entityPlayer = ((CraftPlayer) player).getHandle();
			boolean snowballDamage = FlyCommandPluginSettings.PlayerSettings.ENABLE_SNOWBALL_DAMAGE.get(player.getUniqueId());
			Vector vec = FlyCommandPluginSettings.PlayerSettings.KNOCKBACK_VECTOR.get(player.getUniqueId());
			Projectile projectile = event.getEntity();

			if (snowballDamage && projectile.getType().equals(EntityType.SNOWBALL)) {
				EntitySnowball entitySnowball = ((CraftSnowball) projectile).getHandle();
				double vecX = MathHelper.sin(entitySnowball.getYRot() * (float) Math.PI / 180.0F) * vec.getX();
				double vecZ = MathHelper.cos(entitySnowball.getYRot() * (float) Math.PI / 180.0F) * vec.getZ();

				if (!entityPlayer.getAbilities().a) {
					player.damage(Double.MIN_VALUE);
					player.setVelocity(player.getVelocity().add(new Vector(vecX, vec.getY(), vecZ)));
				} else {
					entityPlayer.getAbilities().a = false;
					entityPlayer.updateAbilities();

					player.damage(Double.MIN_VALUE);
					player.setVelocity(player.getVelocity().add(new Vector(vecX, vec.getY(), vecZ)));

					entityPlayer.getAbilities().e = true;
					entityPlayer.updateAbilities();
				}
			}
		}
	}

	@EventHandler
	public void onPlayerDropItem(PlayerDropItemEvent event) {
		if (!FlyCommandPluginSettings.PlayerSettings.CAN_DROP_ITEM.get(event.getPlayer().getUniqueId())) {
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void onInventoryCreative(InventoryCreativeEvent event) {
		if (!FlyCommandPluginSettings.PlayerSettings.CAN_PICK_CREATIVE_ITEM.get(event.getWhoClicked().getUniqueId())) {
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void onFoodLevelChange(FoodLevelChangeEvent event) {
		if (event.getEntityType() == EntityType.PLAYER) {
			if (!FlyCommandPluginSettings.PlayerSettings.CAN_CHANGE_FOOD_LEVEL.get(event.getEntity().getUniqueId())) {
				event.setCancelled(true);
			}
		}
	}

	@EventHandler
	public void onBlockBreak(BlockBreakEvent event) {
		Player player = event.getPlayer();
		boolean bl = FlyCommandPluginSettings.PlayerSettings.CAN_BREAK_BLOCKS.get(player.getUniqueId());
		if (!bl && player.getGameMode().equals(GameMode.CREATIVE)) {
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void onHangingBreakByEntity(HangingBreakByEntityEvent event) {
		if (FlyCommandPluginSettings.ServerSettings.CANCEL_BREAK_HANGING.get()) {
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void onEntityDeath(EntityDeathEvent event) {
		CommandListenerWrapper commandListenerWrapper = VanillaCommandWrapper.getListener(Bukkit.getServer().getConsoleSender());
		LivingEntity livingEntity = event.getEntity();
		Player killer = livingEntity.getKiller();
		if (FlyCommandPluginSettings.ServerSettings.NOTIFY_ENTITY_DIED.get()) {
			if (killer == null) {
				Chat.sendMessage(commandListenerWrapper, "event.entity.death.hasntkiller", true, ((IChatMutableComponent) getNMSEntity(livingEntity).getScoreboardDisplayName()).format(mod -> mod.setChatClickable(new ChatClickable(ChatClickable.EnumClickAction.d, "/kill @e[type=" + getNMSEntity(livingEntity).getEntityType().i() + "]"))));
			} else {
				Chat.sendMessage(commandListenerWrapper, "event.entity.death.haskiller", true, ((IChatMutableComponent) getNMSEntity(livingEntity).getScoreboardDisplayName()).format(mod -> mod.setChatClickable(new ChatClickable(ChatClickable.EnumClickAction.d, "/kill @e[type=" + getNMSEntity(livingEntity).getEntityType().i() + "]"))), getScoreboardDisplayName(killer));
			}
		}
	}

	@EventHandler
	public void onBlockDamage(BlockDamageEvent event) {
		if (FlyCommandPluginSettings.PlayerSettings.INSTANTLY_BREAK_BLOCK.get(event.getPlayer().getUniqueId())) {
			event.setInstaBreak(true);
		}
	}

	@EventHandler
	public void onPlayerItemDamage(PlayerItemDamageEvent event) {
		if (FlyCommandPluginSettings.PlayerSettings.UNBREAKABLE.get(event.getPlayer().getUniqueId())) {
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void onExplosionPrime(ExplosionPrimeEvent event) {
		if (FlyCommandPluginSettings.ServerSettings.CANCEL_EXPLOSION.get()) {
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void onEntityExplode(EntityExplodeEvent event) {
		if (FlyCommandPluginSettings.ServerSettings.CANCEL_BLOCK_DESTRUCTION_DUE_TO_EXPLOSION.get()) {
			event.setCancelled(true);
		}
	}

	public static void savePlayerAbilities(JavaPlugin p_924181_, FileConfiguration p_920181_, Player p_291821_) {
		EntityPlayer field_908541_a_ = ((CraftPlayer) p_291821_).getHandle();
		p_920181_.set(p_291821_.getUniqueId() + ".AllowFlight", field_908541_a_.getAbilities().c);
		p_920181_.set(p_291821_.getUniqueId() + ".FlySpeed", field_908541_a_.getAbilities().f);
		p_920181_.set(p_291821_.getUniqueId() + ".WalkSpeed", field_908541_a_.getAbilities().g);
		p_920181_.set(p_291821_.getUniqueId() + ".Flying", field_908541_a_.getAbilities().b);
		p_920181_.set(p_291821_.getUniqueId() + ".Invulnerable", field_908541_a_.getAbilities().a);
		p_924181_.saveConfig();
		p_924181_.getLogger().info("saved " + field_908541_a_.getName() + "'s abilities to config file");
	}

	public static void loadPlayerAbilities(JavaPlugin p_398501_, FileConfiguration p_082491_, Player p_083911_) {
		EntityPlayer field_258911_a_ = ((CraftPlayer) p_083911_).getHandle();

		if (p_082491_.contains(p_083911_.getUniqueId() + ".AllowFlight")) {
			field_258911_a_.getAbilities().c = p_082491_.getBoolean(p_083911_.getUniqueId() + ".AllowFlight");
		}
		if (p_082491_.contains(p_083911_.getUniqueId() + ".FlySpeed")) {
			field_258911_a_.getAbilities().f = (float) p_082491_.getDouble(p_083911_.getUniqueId() + ".FlySpeed");
		}
		if (p_082491_.contains(p_083911_.getUniqueId() + ".WalkSpeed")) {
			field_258911_a_.getAbilities().g = (float) p_082491_.getDouble(p_083911_.getUniqueId() + ".WalkSpeed");
		}
		if (p_082491_.contains(p_083911_.getUniqueId() + ".Flying")) {
			field_258911_a_.getAbilities().b = p_082491_.getBoolean(p_083911_.getUniqueId() + ".Flying");
		}
		if (p_082491_.contains(p_083911_.getUniqueId() + ".Invulnerable")) {
			field_258911_a_.getAbilities().a = p_082491_.getBoolean(p_083911_.getUniqueId() + ".Invulnerable");
		}
		field_258911_a_.updateAbilities();
		p_398501_.getLogger().info("Loaded " + field_258911_a_.getName() + "'s abilities from the config file and updated");
	}

	private static IChatMutableComponent func_098311_e(String text, String copyText, IChatBaseComponent hoverText) {
		return new ChatComponentText(text).format((modifier) -> modifier.setChatClickable(new ChatClickable(ChatClickable.EnumClickAction.f, copyText)).setChatHoverable(new ChatHoverable(ChatHoverable.EnumHoverAction.a, hoverText)).setInsertion(text));
	}

	private static IChatBaseComponent getScoreboardDisplayName(Player player) {
		return ((CraftPlayer) player).getHandle().getScoreboardDisplayName();
	}

	private static Entity getNMSEntity(LivingEntity livingEntity) {
		return ((CraftEntity) livingEntity).getHandle();
	}
}
