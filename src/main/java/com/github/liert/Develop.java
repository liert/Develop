package com.github.liert;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.lang.reflect.Field;
import java.util.List;

public final class Develop extends JavaPlugin {
    public static Player p;
    public boolean flag = false;
    private static Develop develop;

    @Override
    public void onEnable() {
        super.onEnable();
        Develop.develop = this;
        this.saveDefaultConfig();
        Manage manage = new Manage();
        manage.initPlugin();
        this.flag = this.getConfig().getBoolean("AutoListener");
        if (this.flag) {
            int port = this.getConfig().getInt("Port");
            Server server = new Server(port);
            server.connect();
        }
        Bukkit.getPluginCommand("develop").setExecutor(new Commands());
        Bukkit.getConsoleSender().sendMessage(Manage.format("Develop", "加载成功."));
        PluginManager pluginManager = Bukkit.getPluginManager();
        try {
            Field pluginsField = Bukkit.getPluginManager().getClass().getDeclaredField("plugins");
            pluginsField.setAccessible(true);
            List plugins = (List)pluginsField.get(pluginManager);
//            Manage.sendMessage(Manage.format("Develop", plugins.toString()));
            Bukkit.getConsoleSender().sendMessage(plugins.toString());
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onDisable() {
        super.onDisable();
        Server.close();
    }
    public static Develop getInstance() {
        return Develop.develop;
    }
}
