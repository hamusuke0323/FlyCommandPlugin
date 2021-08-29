package com.hamusuke.plugin.flycommandplugin.settings;

import com.mojang.brigadier.builder.ArgumentBuilder;
import net.minecraft.server.v1_16_R3.CommandListenerWrapper;

import java.util.Locale;

public abstract class AbstractFlyCommandPluginSetting<T> {
    protected final String settingName;
    protected final String displayName;
    protected final String command;
    protected final String description;
    protected final Type type;
    protected final T defaultValue;

    public AbstractFlyCommandPluginSetting(String settingName, Type type, T defaultValue) {
        if (settingName.isEmpty()) {
            throw new IllegalArgumentException("settingName can not be null");
        }

        settingName = settingName.trim();
        this.settingName = settingName;
        this.type = type;
        settingName = toLowerCaseFirst(settingName);
        this.displayName = settingName;
        this.command = "/flycommandpluginsetting " + this.type.prefix + " " + settingName + " ";
        this.description = "plugin." + (this.type == Type.OFFLINE_PLAYER ? Type.PLAYER.prefix : this.type.prefix) + "." + settingName.toLowerCase(Locale.ROOT);
        this.defaultValue = defaultValue;
    }

    private static String toLowerCaseFirst(String string) {
        String first = String.valueOf(string.charAt(0)).toLowerCase(Locale.ROOT);
        return string.length() > 1 ? first + string.substring(1) : first;
    }

    public abstract void registerCommand(ArgumentBuilder<CommandListenerWrapper, ?> argumentBuilder);

    public String getSettingName() {
        return this.settingName;
    }

    public String getDisplayName() {
        return this.displayName;
    }

    public String getCommand() {
        return this.command;
    }

    public String getDescription() {
        return this.description;
    }

    public T getDefaultValue() {
        return this.defaultValue;
    }

    public Type getType() {
        return this.type;
    }

    public enum Type {
        SERVER("server"),
        PLAYER("player"),
        OFFLINE_PLAYER("offlineplayer"),
        DEFAULT("default");

        private final String prefix;

        Type(String prefix) {
            this.prefix = prefix;
        }

        public String getPrefix() {
            return this.prefix;
        }
    }
}
