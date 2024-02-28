package com.github.liert;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public final class Develop extends JavaPlugin {
    public static Player p;
    public boolean flag = false;
    private static Develop develop;

    @Override
    public void onEnable() {
        super.onEnable();
        Develop.develop = this;
        this.saveDefaultConfig();
        this.flag = this.getConfig().getBoolean("AutoListener");
        if (this.flag) {
            int port = this.getConfig().getInt("Port");
            Server server = new Server(port);
            server.connect();
        }
        Bukkit.getPluginCommand("develop").setExecutor(new Commands());
        Bukkit.getConsoleSender().sendMessage(Manage.format("Develop", "加载成功."));
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
