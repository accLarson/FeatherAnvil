package com.zerek.featheranvil;

import com.zerek.featheranvil.listeners.PrepareAnvilListener;
import org.bukkit.plugin.java.JavaPlugin;

public final class FeatherAnvil extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        this.saveDefaultConfig();
        this.getServer().getPluginManager().registerEvents(new PrepareAnvilListener(this),this);

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
