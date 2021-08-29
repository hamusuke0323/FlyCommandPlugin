package com.hamusuke.plugin.flycommandplugin.util;

import com.google.common.collect.Lists;
import com.google.gson.JsonObject;
import com.hamusuke.plugin.flycommandplugin.FlyCommandPlugin;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import net.minecraft.server.v1_16_R3.*;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;

public class Util {
    private static final Pattern uuid = Pattern.compile("^([-A-Fa-f0-9]+)");

    public static OfflinePlayer getOfflinePlayer(String input) {
        if (uuid.matcher(input).find()) {
            return Bukkit.getOfflinePlayer(UUID.fromString(input));
        } else {
            return Bukkit.getOfflinePlayer(input);
        }
    }

    public static final SuggestionProvider<CommandListenerWrapper> suggestionProvider = (commandContext, suggestionsBuilder) -> {
        for (OfflinePlayer offlinePlayer : getOfflinePlayers()) {
            if (FlyCommandPlugin.getFlyCommandPluginConfig().contains(offlinePlayer.getUniqueId().toString()) && offlinePlayer.getName().startsWith(suggestionsBuilder.getRemaining().toLowerCase())) {
                suggestionsBuilder.suggest(offlinePlayer.getName());
            }
        }
        return suggestionsBuilder.buildFuture();
    };

    public static OfflinePlayer[] getOfflinePlayers() {
        List<OfflinePlayer> offlinePlayers = Lists.newArrayList(Bukkit.getOfflinePlayers());
        Collection<? extends Player> onlinePlayers = Bukkit.getOnlinePlayers();

        offlinePlayers.removeIf(offlinePlayer -> {
            for (Player player : onlinePlayers) {
                return player.equals(offlinePlayer);
            }

            return false;
        });

        return offlinePlayers.toArray(new OfflinePlayer[0]);
    }

    public static IChatBaseComponent toChatComponent(String name, UUID uuid) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("type", IRegistry.ENTITY_TYPE.getKey(EntityTypes.PLAYER).toString());
        jsonObject.addProperty("id", uuid.toString());
        jsonObject.add("name", IChatBaseComponent.ChatSerializer.b(new ChatComponentText(name)));

        return new ChatComponentText(name).format((mod) -> {
            return mod.setChatHoverable(new ChatHoverable(ChatHoverable.EnumHoverAction.SHOW_ENTITY, ChatHoverable.b.a(jsonObject))).setChatClickable(new ChatClickable(ChatClickable.EnumClickAction.SUGGEST_COMMAND, "/tell " + name + " "));
        });
    }
}
