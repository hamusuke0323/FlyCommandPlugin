package com.hamusuke.plugin.flycommandplugin.settings;

import java.util.Collection;
import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;

import com.google.common.collect.ImmutableList;
import com.hamusuke.plugin.flycommandplugin.FlyCommandPlugin;
import com.hamusuke.plugin.flycommandplugin.chat.Chat;
import com.hamusuke.plugin.flycommandplugin.chat.LanguageManager;
import com.hamusuke.plugin.flycommandplugin.util.Util;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import net.minecraft.commands.CommandDispatcher;
import net.minecraft.commands.CommandListenerWrapper;
import net.minecraft.commands.arguments.ArgumentChatComponent;
import net.minecraft.commands.arguments.ArgumentEntity;
import net.minecraft.network.chat.*;
import net.minecraft.server.level.EntityPlayer;
import org.apache.commons.lang.ArrayUtils;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.Nullable;

public class FlyCommandPluginSettings {
	public static final BiConsumer<GeneralSetting<Boolean>, ArgumentBuilder<CommandListenerWrapper, ?>> BOOL = (setting, argumentBuilder) -> {
		argumentBuilder.then(net.minecraft.commands.CommandDispatcher.a(setting.getType().getPrefix()).then(net.minecraft.commands.CommandDispatcher.a(setting.getDisplayName()).executes((execute) -> {
			sendMessage(execute.getSource(), "command.flycommandpluginsetting.get.normal", setting, false, setting.get());
			return 1;
		}).then(net.minecraft.commands.CommandDispatcher.a("value", BoolArgumentType.bool()).executes((execute) -> {
			boolean bl = BoolArgumentType.getBool(execute, "value");
			setting.set(bl);
			sendMessage(execute.getSource(), "command.flycommandpluginsetting.set.normal", setting, true, bl);
			return 1;
		}))));
	};

	public static final BiConsumer<PlayerSetting<Boolean>, ArgumentBuilder<CommandListenerWrapper, ?>> BOOL_PLAYER = (setting, argumentBuilder) -> {
		argumentBuilder.then(net.minecraft.commands.CommandDispatcher.a(AbstractFlyCommandPluginSetting.Type.PLAYER.getPrefix()).then(net.minecraft.commands.CommandDispatcher.a(setting.getDisplayName()).then(net.minecraft.commands.CommandDispatcher.a("targets", ArgumentEntity.d()).executes((e) -> {
			Collection<EntityPlayer> players = ArgumentEntity.f(e, "targets");

			for (EntityPlayer entityPlayer : players) {
				sendMessage(e.getSource(), "command.flycommandpluginsetting.get.player.normal", entityPlayer.getScoreboardDisplayName(), setting, false, setting.get(entityPlayer.getUniqueID()));
			}

			return players.size();
		}).then(net.minecraft.commands.CommandDispatcher.a("boolean", BoolArgumentType.bool()).executes((e) -> {
			Collection<EntityPlayer> players = ArgumentEntity.f(e, "targets");
			boolean bl = BoolArgumentType.getBool(e, "boolean");

			for (EntityPlayer entityPlayer : players) {
				setting.set(entityPlayer.getUniqueID(), bl);
				sendMessage(e.getSource(), "command.flycommandpluginsetting.set.player.normal", entityPlayer.getScoreboardDisplayName(), setting, true, bl);
			}

			return players.size();
		})))));

		argumentBuilder.then(net.minecraft.commands.CommandDispatcher.a(AbstractFlyCommandPluginSetting.Type.OFFLINE_PLAYER.getPrefix()).then(net.minecraft.commands.CommandDispatcher.a(setting.getDisplayName()).then(net.minecraft.commands.CommandDispatcher.a("target", StringArgumentType.word()).suggests(Util.suggestionProvider).executes((e) -> {
			OfflinePlayer offlinePlayer = Util.getOfflinePlayer(StringArgumentType.getString(e, "target"));
			UUID uuid = offlinePlayer.getUniqueId();
			if (!FlyCommandPlugin.getFlyCommandPluginConfig().contains(uuid.toString())) {
				e.getSource().sendFailureMessage(new ChatMessage("argument.entity.notfound.player"));
				return 0;
			}

			sendMessage(e.getSource(), "command.flycommandpluginsetting.get.player.normal", Util.toChatComponent(offlinePlayer.getName(), uuid), setting, false, setting.get(uuid));
			return 1;
		}).then(net.minecraft.commands.CommandDispatcher.a("boolean", BoolArgumentType.bool()).executes((e) -> {
			OfflinePlayer offlinePlayer = Util.getOfflinePlayer(StringArgumentType.getString(e, "target"));
			boolean bl = BoolArgumentType.getBool(e, "boolean");
			UUID uuid = offlinePlayer.getUniqueId();
			if (!FlyCommandPlugin.getFlyCommandPluginConfig().contains(uuid.toString())) {
				e.getSource().sendFailureMessage(new ChatMessage("argument.entity.notfound.player"));
				return 0;
			}

			setting.set(uuid, bl);
			sendMessage(e.getSource(), "command.flycommandpluginsetting.set.player.normal", Util.toChatComponent(offlinePlayer.getName(), uuid), setting, true, bl);
			return 1;
		})))));
	};

	public static final BiFunction<FileConfiguration, GeneralSetting<Boolean>, Boolean> BOOL_FUNC = (config, setting) -> config.getBoolean(setting.getSettingName());
	public static final ImmutableList<AbstractFlyCommandPluginSetting<?>> ALL_SETTINGS = ImmutableList.<AbstractFlyCommandPluginSetting<?>>builder().addAll(ServerSettings.SERVER_SETTINGS_REGISTRY).addAll(PlayerSettings.PLAYER_SETTINGS_REGISTRY).build();

	public static void saveConfig(JavaPlugin javaPlugin, FileConfiguration config) {
		ALL_SETTINGS.forEach(setting -> {
			if (!config.contains(setting.getSettingName())) {
				config.set(setting.getSettingName(), setting.getDefaultValue());
			}
		});

		javaPlugin.saveConfig();
	}

	public static void savePlayerConfig(JavaPlugin javaPlugin, FileConfiguration config, UUID uuid) {
		PlayerSettings.PLAYER_SETTINGS_REGISTRY.forEach(playerSetting -> {
			if (!config.contains(uuid + "." + playerSetting.getSettingName())) {
				config.set(uuid + "." + playerSetting.getSettingName(), playerSetting.getDefaultSettingValue());
			}
		});

		javaPlugin.saveConfig();
	}

	private static void sendMessage(CommandListenerWrapper commandListenerWrapper, String translationKey, AbstractFlyCommandPluginSetting<?> setting, boolean sendToAdmin, Object... objects) {
		sendMessage(commandListenerWrapper, translationKey, null, setting, sendToAdmin, objects);
	}

	private static void sendMessage(CommandListenerWrapper commandListenerWrapper, String translationKey, @Nullable IChatBaseComponent targetOrNull, AbstractFlyCommandPluginSetting<?> setting, boolean sendToAdmin, Object... objects) {
		Object[] settingSummary = new Object[]{LanguageManager.getTranslatedText(commandListenerWrapper, "plugin.str"), new ChatComponentText(setting.getDisplayName()).format(mod -> {
			return mod.setChatClickable(new ChatClickable(ChatClickable.EnumClickAction.d, setting.getCommand())).setChatHoverable(new ChatHoverable(ChatHoverable.EnumHoverAction.a, new ChatComponentText(LanguageManager.getTranslatedText(commandListenerWrapper, setting.getDescription()))));
		})};

		if (targetOrNull != null) {
			settingSummary = ArrayUtils.addAll(new Object[]{targetOrNull}, settingSummary);
		}

		Chat.sendMessage(commandListenerWrapper, translationKey, sendToAdmin, ArrayUtils.addAll(settingSummary, objects));
	}

	public static class ServerSettings {
		public static final GeneralSetting<Boolean> CANCEL_BLOCK_DESTRUCTION_DUE_TO_EXPLOSION = new GeneralSetting<>("CancelBlockDestructionDueToExplosion", AbstractFlyCommandPluginSetting.Type.SERVER, true, BOOL, BOOL_FUNC);
		public static final GeneralSetting<Boolean> CANCEL_BREAK_HANGING = new GeneralSetting<>("CancelBreakHanging", AbstractFlyCommandPluginSetting.Type.SERVER, false, BOOL, BOOL_FUNC);
		public static final GeneralSetting<Boolean> CANCEL_EXPLOSION = new GeneralSetting<>("CancelExplosion", AbstractFlyCommandPluginSetting.Type.SERVER, true, BOOL, BOOL_FUNC);
		public static final GeneralSetting<Boolean> NOTIFY_ENTITY_DIED = new GeneralSetting<>("NotifyEntityDied", AbstractFlyCommandPluginSetting.Type.SERVER, false, BOOL, BOOL_FUNC);

		public static final ImmutableList<GeneralSetting<?>> SERVER_SETTINGS_REGISTRY = ImmutableList.of(CANCEL_BLOCK_DESTRUCTION_DUE_TO_EXPLOSION, CANCEL_BREAK_HANGING, CANCEL_EXPLOSION, NOTIFY_ENTITY_DIED);

		public static void readAll(FileConfiguration config) {
			SERVER_SETTINGS_REGISTRY.forEach(generalSetting -> generalSetting.read(config));
		}
	}

	public static class PlayerSettings {
		public static final PlayerSetting<Boolean> CAN_BREAK_BLOCKS = new PlayerSetting<>("CanBreakBlocks", true, BOOL, BOOL_FUNC, BOOL_PLAYER);
		public static final PlayerSetting<Boolean> CAN_CHANGE_FOOD_LEVEL = new PlayerSetting<>("CanChangeFoodLevel", true, BOOL, BOOL_FUNC, BOOL_PLAYER);
		public static final PlayerSetting<Boolean> CAN_DROP_ITEM = new PlayerSetting<>("CanDropItem", true, BOOL, BOOL_FUNC, BOOL_PLAYER);
		public static final PlayerSetting<Boolean> CAN_PICK_CREATIVE_ITEM = new PlayerSetting<>("CanPickCreativeItem", true, BOOL, BOOL_FUNC, BOOL_PLAYER);
		public static final PlayerSetting<Boolean> DISABLE_DAMAGE = new PlayerSetting<>("DisableDamage", false, BOOL, BOOL_FUNC, BOOL_PLAYER);
		public static final PlayerSetting<Boolean> ENABLE_SNOWBALL_DAMAGE = new PlayerSetting<>("EnableSnowballDamage", false, BOOL, BOOL_FUNC, BOOL_PLAYER);
		public static final PlayerSetting<Boolean> INSTANTLY_BREAK_BLOCK = new PlayerSetting<>("InstantlyBreakBlock", false, BOOL, BOOL_FUNC, BOOL_PLAYER);

		public static final PlayerSetting<Vector> KNOCKBACK_VECTOR = new PlayerSetting<>("KnockbackVector", new Vector(0.6D, 0.5D, 0.6D), (general, argumentBuilder) -> {
			argumentBuilder.then(net.minecraft.commands.CommandDispatcher.a(general.getType().getPrefix()).then(CommandDispatcher.a(general.getDisplayName()).executes((e) -> {
				Vector vec = general.get();
				sendMessage(e.getSource(), "command.flycommandpluginsetting.get.vec3", general, false, vec.getX(), vec.getY(), vec.getZ());
				return 1;
			}).then(net.minecraft.commands.CommandDispatcher.a("vector_x", DoubleArgumentType.doubleArg()).then(net.minecraft.commands.CommandDispatcher.a("vector_y", DoubleArgumentType.doubleArg()).then(net.minecraft.commands.CommandDispatcher.a("vector_z", DoubleArgumentType.doubleArg()).executes((e) -> {
				Vector vec = new Vector(DoubleArgumentType.getDouble(e, "vector_x"), DoubleArgumentType.getDouble(e, "vector_y"), DoubleArgumentType.getDouble(e, "vector_z"));
				general.set(vec);
				sendMessage(e.getSource(), "command.flycommandpluginsetting.set.vec3", general, true, vec.getX(), vec.getY(), vec.getZ());
				return 1;
			}))))));
		}, (config, setting) -> config.getVector(setting.getSettingName()), (setting, argumentBuilder) -> {
			argumentBuilder.then(net.minecraft.commands.CommandDispatcher.a(AbstractFlyCommandPluginSetting.Type.PLAYER.getPrefix()).then(net.minecraft.commands.CommandDispatcher.a(setting.getDisplayName()).then(net.minecraft.commands.CommandDispatcher.a("targets", ArgumentEntity.d()).executes((e) -> {
				Collection<EntityPlayer> players = ArgumentEntity.f(e, "targets");
				for (EntityPlayer entityPlayer : players) {
					UUID uuid = entityPlayer.getUniqueID();
					Vector vec = setting.get(uuid);
					sendMessage(e.getSource(), "command.flycommandpluginsetting.get.player.vec3", entityPlayer.getScoreboardDisplayName(), setting, false, vec.getX(), vec.getY(), vec.getZ());
				}
				return players.size();
			}).then(net.minecraft.commands.CommandDispatcher.a("vector_x", DoubleArgumentType.doubleArg()).then(net.minecraft.commands.CommandDispatcher.a("vector_y", DoubleArgumentType.doubleArg()).then(net.minecraft.commands.CommandDispatcher.a("vector_z", DoubleArgumentType.doubleArg()).executes((e) -> {
				Collection<EntityPlayer> players = ArgumentEntity.f(e, "targets");
				Vector vec = new Vector(DoubleArgumentType.getDouble(e, "vector_x"), DoubleArgumentType.getDouble(e, "vector_y"), DoubleArgumentType.getDouble(e, "vector_z"));
				for (EntityPlayer entityPlayer : players) {
					UUID uuid = entityPlayer.getUniqueID();
					setting.set(uuid, vec);
					sendMessage(e.getSource(), "command.flycommandpluginsetting.set.player.vec3", entityPlayer.getScoreboardDisplayName(), setting, true, vec.getX(), vec.getY(), vec.getZ());
				}
				return players.size();
			})))))));

			argumentBuilder.then(net.minecraft.commands.CommandDispatcher.a(AbstractFlyCommandPluginSetting.Type.OFFLINE_PLAYER.getPrefix()).then(net.minecraft.commands.CommandDispatcher.a(setting.getDisplayName()).then(net.minecraft.commands.CommandDispatcher.a("target", StringArgumentType.word()).suggests(Util.suggestionProvider).executes((e) -> {
				OfflinePlayer offlinePlayer = Util.getOfflinePlayer(StringArgumentType.getString(e, "target"));
				UUID uuid = offlinePlayer.getUniqueId();
				if (!FlyCommandPlugin.getFlyCommandPluginConfig().contains(uuid.toString())) {
					e.getSource().sendFailureMessage(new ChatMessage("argument.entity.notfound.player"));
					return 0;
				}

				Vector vec = setting.get(uuid);
				sendMessage(e.getSource(), "command.flycommandpluginsetting.get.player.vec3", Util.toChatComponent(offlinePlayer.getName(), uuid), setting, false, vec.getX(), vec.getY(), vec.getZ());
				return 1;
			}).then(net.minecraft.commands.CommandDispatcher.a("vector_x", DoubleArgumentType.doubleArg()).then(net.minecraft.commands.CommandDispatcher.a("vector_y", DoubleArgumentType.doubleArg()).then(net.minecraft.commands.CommandDispatcher.a("vector_z", DoubleArgumentType.doubleArg()).executes((e) -> {
				OfflinePlayer offlinePlayer = Util.getOfflinePlayer(StringArgumentType.getString(e, "target"));
				UUID uuid = offlinePlayer.getUniqueId();
				if (!FlyCommandPlugin.getFlyCommandPluginConfig().contains(uuid.toString())) {
					e.getSource().sendFailureMessage(new ChatMessage("argument.entity.notfound.player"));
					return 0;
				}

				Vector vec = new Vector(DoubleArgumentType.getDouble(e, "vector_x"), DoubleArgumentType.getDouble(e, "vector_y"), DoubleArgumentType.getDouble(e, "vector_z"));
				sendMessage(e.getSource(), "command.flycommandpluginsetting.set.player.vec3", Util.toChatComponent(offlinePlayer.getName(), uuid), setting, true, vec.getX(), vec.getY(), vec.getZ());
				return 1;
			})))))));
		});

		public static final PlayerSetting<String> JOIN_MESSAGE = new PlayerSetting<>("JoinMessage", "multiplayer.player.joined", (general, argumentBuilder) -> {
			argumentBuilder.then(net.minecraft.commands.CommandDispatcher.a(general.getType().getPrefix()).then(net.minecraft.commands.CommandDispatcher.a(general.getDisplayName()).executes((e) -> {
				String msg = general.get();
				if (msg.equalsIgnoreCase("null")) {
					sendMessage(e.getSource(), "command.flycommandpluginsetting.get.emp", general, false);
				} else {
					sendMessage(e.getSource(), "command.flycommandpluginsetting.get.str", general, false, msg);
				}

				return 1;
			}).then(net.minecraft.commands.CommandDispatcher.a("null").executes((e) -> {
				general.set("null");
				sendMessage(e.getSource(), "command.flycommandpluginsetting.set.emp", general, true);
				return 1;
			})).then(net.minecraft.commands.CommandDispatcher.a("byText").then(net.minecraft.commands.CommandDispatcher.a("text", StringArgumentType.greedyString()).executes((e) -> {
				String text = StringArgumentType.getString(e, "text");
				general.set(text);
				PlayerSettings.IS_JOIN_MESSAGE_JSON_ELEMENT.setDefaultSettingValue(false);
				sendMessage(e.getSource(), "command.flycommandpluginsetting.set.str", general, true, text);
				return 1;
			}))).then(net.minecraft.commands.CommandDispatcher.a("byJson").then(net.minecraft.commands.CommandDispatcher.a("JsonElement", ArgumentChatComponent.a()).executes((e) -> {
				IChatBaseComponent iChatBaseComponent = ArgumentChatComponent.a(e, "JsonElement");
				String json = IChatBaseComponent.ChatSerializer.b(iChatBaseComponent).toString();
				general.set(json);
				PlayerSettings.IS_JOIN_MESSAGE_JSON_ELEMENT.setDefaultSettingValue(true);
				sendMessage(e.getSource(), "command.flycommandpluginsetting.set.str", general, true, iChatBaseComponent);
				return 1;
			})))));
		}, (config, general) -> config.getString(general.getSettingName()), (setting, argumentBuilder) -> {
			argumentBuilder.then(net.minecraft.commands.CommandDispatcher.a(AbstractFlyCommandPluginSetting.Type.PLAYER.getPrefix()).then(net.minecraft.commands.CommandDispatcher.a(setting.getDisplayName()).then(net.minecraft.commands.CommandDispatcher.a("targets", ArgumentEntity.d()).executes((e) -> {
				Collection<EntityPlayer> players = ArgumentEntity.f(e, "targets");
				for (EntityPlayer entityPlayer : players) {
					UUID uuid = entityPlayer.getUniqueID();
					String msg = setting.get(uuid);
					if (msg.equalsIgnoreCase("null")) {
						sendMessage(e.getSource(), "command.flycommandpluginsetting.get.player.emp", entityPlayer.getScoreboardDisplayName(), setting, false);
					} else {
						sendMessage(e.getSource(), "command.flycommandpluginsetting.get.player.str", entityPlayer.getScoreboardDisplayName(), setting, false, msg);
					}
				}
				return players.size();
			}).then(net.minecraft.commands.CommandDispatcher.a("null").executes((e) -> {
				Collection<EntityPlayer> players = ArgumentEntity.f(e, "targets");
				for (EntityPlayer entityPlayer : players) {
					UUID uuid = entityPlayer.getUniqueID();
					setting.set(uuid, "null");
					sendMessage(e.getSource(), "command.flycommandpluginsetting.set.player.emp", entityPlayer.getScoreboardDisplayName(), setting, true);
				}
				return players.size();
			})).then(net.minecraft.commands.CommandDispatcher.a("byText").then(net.minecraft.commands.CommandDispatcher.a("text", StringArgumentType.greedyString()).executes((e) -> {
				Collection<EntityPlayer> players = ArgumentEntity.f(e, "targets");
				String text = StringArgumentType.getString(e, "text");
				for (EntityPlayer entityPlayer : players) {
					UUID uuid = entityPlayer.getUniqueID();
					setting.set(uuid, text);
					PlayerSettings.IS_JOIN_MESSAGE_JSON_ELEMENT.set(uuid, false);
					sendMessage(e.getSource(), "command.flycommandpluginsetting.set.player.str", entityPlayer.getScoreboardDisplayName(), setting, true, text);
				}
				return players.size();
			}))).then(net.minecraft.commands.CommandDispatcher.a("byJson").then(net.minecraft.commands.CommandDispatcher.a("JsonElement", ArgumentChatComponent.a()).executes((e) -> {
				Collection<EntityPlayer> players = ArgumentEntity.f(e, "targets");
				IChatBaseComponent iChatBaseComponent = ArgumentChatComponent.a(e, "JsonElement");
				for (EntityPlayer entityPlayer : players) {
					UUID uuid = entityPlayer.getUniqueID();
					setting.set(uuid, IChatBaseComponent.ChatSerializer.b(iChatBaseComponent).toString());
					PlayerSettings.IS_JOIN_MESSAGE_JSON_ELEMENT.set(uuid, true);
					sendMessage(e.getSource(), "command.flycommandpluginsetting.set.player.str", entityPlayer.getScoreboardDisplayName(), setting, true, iChatBaseComponent);
				}
				return players.size();
			}))))));

			argumentBuilder.then(net.minecraft.commands.CommandDispatcher.a(AbstractFlyCommandPluginSetting.Type.OFFLINE_PLAYER.getPrefix()).then(net.minecraft.commands.CommandDispatcher.a(setting.getDisplayName()).then(net.minecraft.commands.CommandDispatcher.a("target", StringArgumentType.word()).suggests(Util.suggestionProvider).executes((e) -> {
				OfflinePlayer offlinePlayer = Util.getOfflinePlayer(StringArgumentType.getString(e, "target"));
				UUID uuid = offlinePlayer.getUniqueId();
				if (!FlyCommandPlugin.getFlyCommandPluginConfig().contains(uuid.toString())) {
					e.getSource().sendFailureMessage(new ChatMessage("argument.entity.notfound.player"));
					return 0;
				}

				String msg = setting.get(uuid);
				if (msg.equalsIgnoreCase("null")) {
					sendMessage(e.getSource(), "command.flycommandpluginsetting.get.player.emp", Util.toChatComponent(offlinePlayer.getName(), uuid), setting, false);
				} else {
					sendMessage(e.getSource(), "command.flycommandpluginsetting.get.player.str", Util.toChatComponent(offlinePlayer.getName(), uuid), setting, false, msg);
				}
				return 1;
			}).then(net.minecraft.commands.CommandDispatcher.a("null").executes((e) -> {
				OfflinePlayer offlinePlayer = Util.getOfflinePlayer(StringArgumentType.getString(e, "target"));
				UUID uuid = offlinePlayer.getUniqueId();
				if (!FlyCommandPlugin.getFlyCommandPluginConfig().contains(uuid.toString())) {
					e.getSource().sendFailureMessage(new ChatMessage("argument.entity.notfound.player"));
					return 0;
				}
				setting.set(uuid, "null");
				sendMessage(e.getSource(), "command.flycommandpluginsetting.set.player.emp", Util.toChatComponent(offlinePlayer.getName(), uuid), setting, true);
				return 1;
			})).then(net.minecraft.commands.CommandDispatcher.a("byText").then(net.minecraft.commands.CommandDispatcher.a("text", StringArgumentType.greedyString()).executes((e) -> {
				OfflinePlayer offlinePlayer = Util.getOfflinePlayer(StringArgumentType.getString(e, "target"));
				UUID uuid = offlinePlayer.getUniqueId();
				if (!FlyCommandPlugin.getFlyCommandPluginConfig().contains(uuid.toString())) {
					e.getSource().sendFailureMessage(new ChatMessage("argument.entity.notfound.player"));
					return 0;
				}

				String text = StringArgumentType.getString(e, "text");
				setting.set(uuid, text);
				PlayerSettings.IS_JOIN_MESSAGE_JSON_ELEMENT.set(uuid, false);
				sendMessage(e.getSource(), "command.flycommandpluginsetting.set.player.str", Util.toChatComponent(offlinePlayer.getName(), uuid), setting, true, text);
				return 1;
			}))).then(net.minecraft.commands.CommandDispatcher.a("byJson").then(net.minecraft.commands.CommandDispatcher.a("JsonElement", ArgumentChatComponent.a()).executes((e) -> {
				OfflinePlayer offlinePlayer = Util.getOfflinePlayer(StringArgumentType.getString(e, "target"));
				UUID uuid = offlinePlayer.getUniqueId();
				if (!FlyCommandPlugin.getFlyCommandPluginConfig().contains(uuid.toString())) {
					e.getSource().sendFailureMessage(new ChatMessage("argument.entity.notfound.player"));
					return 0;
				}

				IChatBaseComponent iChatBaseComponent = ArgumentChatComponent.a(e, "JsonElement");
				setting.set(uuid, IChatBaseComponent.ChatSerializer.b(iChatBaseComponent).toString());
				PlayerSettings.IS_JOIN_MESSAGE_JSON_ELEMENT.set(uuid, true);
				sendMessage(e.getSource(), "command.flycommandpluginsetting.set.player.str", Util.toChatComponent(offlinePlayer.getName(), uuid), setting, true, iChatBaseComponent);
				return 1;
			}))))));
		});

		public static final PlayerSetting<String> QUIT_MESSAGE = new PlayerSetting<>("QuitMessage", "multiplayer.player.left", (general, argumentBuilder) -> {
			argumentBuilder.then(net.minecraft.commands.CommandDispatcher.a(general.getType().getPrefix()).then(net.minecraft.commands.CommandDispatcher.a(general.getDisplayName()).executes((e) -> {
				String msg = general.get();
				if (msg.equalsIgnoreCase("null")) {
					sendMessage(e.getSource(), "command.flycommandpluginsetting.get.emp", general, false);
				} else {
					sendMessage(e.getSource(), "command.flycommandpluginsetting.get.str", general, false, msg);
				}

				return 1;
			}).then(net.minecraft.commands.CommandDispatcher.a("null").executes((e) -> {
				general.set("null");
				sendMessage(e.getSource(), "command.flycommandpluginsetting.set.emp", general, true);
				return 1;
			})).then(net.minecraft.commands.CommandDispatcher.a("byText").then(net.minecraft.commands.CommandDispatcher.a("text", StringArgumentType.greedyString()).executes((e) -> {
				String text = StringArgumentType.getString(e, "text");
				general.set(text);
				PlayerSettings.IS_QUIT_MESSAGE_JSON_ELEMENT.setDefaultSettingValue(false);
				sendMessage(e.getSource(), "command.flycommandpluginsetting.set.str", general, true, text);
				return 1;
			}))).then(net.minecraft.commands.CommandDispatcher.a("byJson").then(net.minecraft.commands.CommandDispatcher.a("JsonElement", ArgumentChatComponent.a()).executes((e) -> {
				IChatBaseComponent iChatBaseComponent = ArgumentChatComponent.a(e, "JsonElement");
				String json = IChatBaseComponent.ChatSerializer.b(iChatBaseComponent).toString();
				general.set(json);
				PlayerSettings.IS_QUIT_MESSAGE_JSON_ELEMENT.setDefaultSettingValue(true);
				sendMessage(e.getSource(), "command.flycommandpluginsetting.set.str", general, true, iChatBaseComponent);
				return 1;
			})))));
		}, (config, general) -> config.getString(general.getSettingName()), (setting, argumentBuilder) -> {
			argumentBuilder.then(net.minecraft.commands.CommandDispatcher.a(AbstractFlyCommandPluginSetting.Type.PLAYER.getPrefix()).then(net.minecraft.commands.CommandDispatcher.a(setting.getDisplayName()).then(net.minecraft.commands.CommandDispatcher.a("targets", ArgumentEntity.d()).executes((e) -> {
				Collection<EntityPlayer> players = ArgumentEntity.f(e, "targets");
				for (EntityPlayer entityPlayer : players) {
					UUID uuid = entityPlayer.getUniqueID();
					String msg = setting.get(uuid);
					if (msg.equalsIgnoreCase("null")) {
						sendMessage(e.getSource(), "command.flycommandpluginsetting.get.player.emp", entityPlayer.getScoreboardDisplayName(), setting, false);
					} else {
						sendMessage(e.getSource(), "command.flycommandpluginsetting.get.player.str", entityPlayer.getScoreboardDisplayName(), setting, false, msg);
					}
				}
				return players.size();
			}).then(net.minecraft.commands.CommandDispatcher.a("null").executes((e) -> {
				Collection<EntityPlayer> players = ArgumentEntity.f(e, "targets");
				for (EntityPlayer entityPlayer : players) {
					UUID uuid = entityPlayer.getUniqueID();
					setting.set(uuid, "null");
					sendMessage(e.getSource(), "command.flycommandpluginsetting.set.player.emp", entityPlayer.getScoreboardDisplayName(), setting, true);
				}
				return players.size();
			})).then(net.minecraft.commands.CommandDispatcher.a("byText").then(net.minecraft.commands.CommandDispatcher.a("text", StringArgumentType.greedyString()).executes((e) -> {
				Collection<EntityPlayer> players = ArgumentEntity.f(e, "targets");
				String text = StringArgumentType.getString(e, "text");
				for (EntityPlayer entityPlayer : players) {
					UUID uuid = entityPlayer.getUniqueID();
					setting.set(uuid, text);
					PlayerSettings.IS_QUIT_MESSAGE_JSON_ELEMENT.set(uuid, false);
					sendMessage(e.getSource(), "command.flycommandpluginsetting.set.player.str", entityPlayer.getScoreboardDisplayName(), setting, true, text);
				}
				return players.size();
			}))).then(net.minecraft.commands.CommandDispatcher.a("byJson").then(net.minecraft.commands.CommandDispatcher.a("JsonElement", ArgumentChatComponent.a()).executes((e) -> {
				Collection<EntityPlayer> players = ArgumentEntity.f(e, "targets");
				IChatBaseComponent iChatBaseComponent = ArgumentChatComponent.a(e, "JsonElement");
				for (EntityPlayer entityPlayer : players) {
					UUID uuid = entityPlayer.getUniqueID();
					setting.set(uuid, IChatBaseComponent.ChatSerializer.b(iChatBaseComponent).toString());
					PlayerSettings.IS_QUIT_MESSAGE_JSON_ELEMENT.set(uuid, true);
					sendMessage(e.getSource(), "command.flycommandpluginsetting.set.player.str", entityPlayer.getScoreboardDisplayName(), setting, true, iChatBaseComponent);
				}
				return players.size();
			}))))));

			argumentBuilder.then(net.minecraft.commands.CommandDispatcher.a(AbstractFlyCommandPluginSetting.Type.OFFLINE_PLAYER.getPrefix()).then(net.minecraft.commands.CommandDispatcher.a(setting.getDisplayName()).then(net.minecraft.commands.CommandDispatcher.a("target", StringArgumentType.word()).suggests(Util.suggestionProvider).executes((e) -> {
				OfflinePlayer offlinePlayer = Util.getOfflinePlayer(StringArgumentType.getString(e, "target"));
				UUID uuid = offlinePlayer.getUniqueId();
				if (!FlyCommandPlugin.getFlyCommandPluginConfig().contains(uuid.toString())) {
					e.getSource().sendFailureMessage(new ChatMessage("argument.entity.notfound.player"));
					return 0;
				}

				String msg = setting.get(uuid);
				if (msg.equalsIgnoreCase("null")) {
					sendMessage(e.getSource(), "command.flycommandpluginsetting.get.player.emp", Util.toChatComponent(offlinePlayer.getName(), uuid), setting, false);
				} else {
					sendMessage(e.getSource(), "command.flycommandpluginsetting.get.player.str", Util.toChatComponent(offlinePlayer.getName(), uuid), setting, false, msg);
				}
				return 1;
			}).then(net.minecraft.commands.CommandDispatcher.a("null").executes((e) -> {
				OfflinePlayer offlinePlayer = Util.getOfflinePlayer(StringArgumentType.getString(e, "target"));
				UUID uuid = offlinePlayer.getUniqueId();
				if (!FlyCommandPlugin.getFlyCommandPluginConfig().contains(uuid.toString())) {
					e.getSource().sendFailureMessage(new ChatMessage("argument.entity.notfound.player"));
					return 0;
				}
				setting.set(uuid, "null");
				sendMessage(e.getSource(), "command.flycommandpluginsetting.set.player.emp", Util.toChatComponent(offlinePlayer.getName(), uuid), setting, true);
				return 1;
			})).then(net.minecraft.commands.CommandDispatcher.a("byText").then(net.minecraft.commands.CommandDispatcher.a("text", StringArgumentType.greedyString()).executes((e) -> {
				OfflinePlayer offlinePlayer = Util.getOfflinePlayer(StringArgumentType.getString(e, "target"));
				UUID uuid = offlinePlayer.getUniqueId();
				if (!FlyCommandPlugin.getFlyCommandPluginConfig().contains(uuid.toString())) {
					e.getSource().sendFailureMessage(new ChatMessage("argument.entity.notfound.player"));
					return 0;
				}

				String text = StringArgumentType.getString(e, "text");
				setting.set(uuid, text);
				PlayerSettings.IS_QUIT_MESSAGE_JSON_ELEMENT.set(uuid, false);
				sendMessage(e.getSource(), "command.flycommandpluginsetting.set.player.str", Util.toChatComponent(offlinePlayer.getName(), uuid), setting, true, text);
				return 1;
			}))).then(net.minecraft.commands.CommandDispatcher.a("byJson").then(net.minecraft.commands.CommandDispatcher.a("JsonElement", ArgumentChatComponent.a()).executes((e) -> {
				OfflinePlayer offlinePlayer = Util.getOfflinePlayer(StringArgumentType.getString(e, "target"));
				UUID uuid = offlinePlayer.getUniqueId();
				if (!FlyCommandPlugin.getFlyCommandPluginConfig().contains(uuid.toString())) {
					e.getSource().sendFailureMessage(new ChatMessage("argument.entity.notfound.player"));
					return 0;
				}

				IChatBaseComponent iChatBaseComponent = ArgumentChatComponent.a(e, "JsonElement");
				setting.set(uuid, IChatBaseComponent.ChatSerializer.b(iChatBaseComponent).toString());
				PlayerSettings.IS_QUIT_MESSAGE_JSON_ELEMENT.set(uuid, true);
				sendMessage(e.getSource(), "command.flycommandpluginsetting.set.player.str", Util.toChatComponent(offlinePlayer.getName(), uuid), setting, true, iChatBaseComponent);
				return 1;
			}))))));
		});

		public static final PlayerSetting<Boolean> UNBREAKABLE = new PlayerSetting<>("Unbreakable", false, BOOL, BOOL_FUNC, BOOL_PLAYER);
		public static final PlayerSetting<Boolean> IS_JOIN_MESSAGE_JSON_ELEMENT = new PlayerSetting<>("IsJoinMessageJsonElement", false, BOOL_FUNC);
		public static final PlayerSetting<Boolean> IS_QUIT_MESSAGE_JSON_ELEMENT = new PlayerSetting<>("IsQuitMessageJsonElement", false, BOOL_FUNC);

		public static final ImmutableList<PlayerSetting<?>> PLAYER_SETTINGS_REGISTRY = ImmutableList.of(CAN_BREAK_BLOCKS, CAN_CHANGE_FOOD_LEVEL, CAN_DROP_ITEM, CAN_PICK_CREATIVE_ITEM, DISABLE_DAMAGE, ENABLE_SNOWBALL_DAMAGE, INSTANTLY_BREAK_BLOCK, KNOCKBACK_VECTOR, JOIN_MESSAGE, QUIT_MESSAGE, UNBREAKABLE, IS_JOIN_MESSAGE_JSON_ELEMENT, IS_QUIT_MESSAGE_JSON_ELEMENT);

		public static void readAll(FileConfiguration config) {
			PLAYER_SETTINGS_REGISTRY.forEach(playerSetting -> playerSetting.readDefaultSetting(config));
		}
	}
}
