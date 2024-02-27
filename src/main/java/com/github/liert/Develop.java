package com.github.liert;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public final class Develop extends JavaPlugin {
    public static Player p;

    @Override
    public void onEnable() {
        super.onEnable();
        Bukkit.getPluginCommand("develop").setExecutor(new Commands());
        Bukkit.getConsoleSender().sendMessage("Develop 加载成功.");
    }

    @Override
    public void onDisable() {
        super.onDisable();
        Server.close();
    }
}
