package com.hamusuke.plugin.flycommandplugin.settings;

import com.hamusuke.plugin.flycommandplugin.FlyCommandPlugin;
import com.mojang.brigadier.builder.ArgumentBuilder;
import net.minecraft.server.v1_16_R3.CommandListenerWrapper;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;

public class GeneralSetting<T> extends AbstractFlyCommandPluginSetting<T> {
    protected final AtomicReference<T> atomicReference = new AtomicReference<>();
    protected final BiConsumer<GeneralSetting<T>, ArgumentBuilder<CommandListenerWrapper, ?>> commandBuilder;
    protected final BiFunction<FileConfiguration, GeneralSetting<T>, T> reader;

    public GeneralSetting(String settingName, Type type, T defaultValue, BiConsumer<GeneralSetting<T>, ArgumentBuilder<CommandListenerWrapper, ?>> biConsumer, BiFunction<FileConfiguration, GeneralSetting<T>, T> reader) {
        super(settingName, type, defaultValue);
        this.atomicReference.set(defaultValue);
        this.commandBuilder = biConsumer;
        this.reader = reader;
    }

    public void registerCommand(ArgumentBuilder<CommandListenerWrapper, ?> argumentBuilder) {
        this.commandBuilder.accept(this, argumentBuilder);
    }

    public void set(T value) {
        this.atomicReference.set(value);
        FlyCommandPlugin.getFlyCommandPluginConfig().set(this.getSettingName(), value);
        FlyCommandPlugin.getInstance().saveConfig();
    }

    public T get() {
        return this.atomicReference.get();
    }

    public void read(FileConfiguration config) {
        this.atomicReference.set(this.reader.apply(config, this));
    }
}
