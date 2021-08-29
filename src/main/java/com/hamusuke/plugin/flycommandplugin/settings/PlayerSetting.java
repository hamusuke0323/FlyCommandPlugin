package com.hamusuke.plugin.flycommandplugin.settings;

import com.hamusuke.plugin.flycommandplugin.FlyCommandPlugin;
import com.mojang.brigadier.builder.ArgumentBuilder;
import net.minecraft.server.v1_16_R3.CommandListenerWrapper;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;

public class PlayerSetting<T> extends AbstractFlyCommandPluginSetting<T> {
    protected final GeneralSetting<T> defaultSetting;
    protected boolean command = true;
    protected final BiConsumer<PlayerSetting<T>, ArgumentBuilder<CommandListenerWrapper, ?>> commandBuilder;

    public PlayerSetting(String settingName, T defaultValue, BiFunction<FileConfiguration, GeneralSetting<T>, T> defaultSettingReader) {
        this(settingName, defaultValue, (a, b) -> {
        }, defaultSettingReader, (a, b) -> {
        });
        this.command = false;
    }

    public PlayerSetting(String settingName, T defaultValue, BiConsumer<GeneralSetting<T>, ArgumentBuilder<CommandListenerWrapper, ?>> defaultSettingCommandBuilder, BiFunction<FileConfiguration, GeneralSetting<T>, T> defaultSettingReader, BiConsumer<PlayerSetting<T>, ArgumentBuilder<CommandListenerWrapper, ?>> commandBuilder) {
        super(settingName, Type.PLAYER, defaultValue);
        this.defaultSetting = new GeneralSetting<>(settingName, Type.DEFAULT, defaultValue, defaultSettingCommandBuilder, defaultSettingReader);
        this.commandBuilder = commandBuilder;
    }

    public void registerCommand(ArgumentBuilder<CommandListenerWrapper, ?> argumentBuilder) {
        if (this.command) {
            this.defaultSetting.registerCommand(argumentBuilder);
            this.commandBuilder.accept(this, argumentBuilder);
        }
    }

    public void setDefaultSettingValue(T value) {
        this.defaultSetting.set(value);
    }

    public T getDefaultSettingValue() {
        return this.defaultSetting.get();
    }

    public void readDefaultSetting(FileConfiguration config) {
        this.defaultSetting.read(config);
    }

    public void set(UUID uuid, T value) {
        FlyCommandPlugin.getFlyCommandPluginConfig().set(uuid + "." + this.getSettingName(), value);
        FlyCommandPlugin.getInstance().saveConfig();
    }

    public T get(UUID uuid) {
        return (T) FlyCommandPlugin.getFlyCommandPluginConfig().get(uuid.toString() + "." + this.getSettingName());
    }
}
