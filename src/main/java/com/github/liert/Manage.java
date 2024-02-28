package com.github.liert;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredListener;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URLClassLoader;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Manage {
    private static final HashMap<String, File> plugins = new HashMap<>();
    public static String format(String title, String msg) {
        return "§7[§a" + title + "§7]§a " + msg;
    }
    public static <T> void sendMessage(T p, String msg) {
        if (Develop.getInstance().flag) {
            Bukkit.getConsoleSender().sendMessage(msg);
        } else if (Server.getStatus()){
            Develop.p.sendMessage(msg);
        } else if (p instanceof Player) {
            ((Player) p).sendMessage(msg);
        } else if (p instanceof ConsoleCommandSender) {
            ((ConsoleCommandSender) p).sendMessage(msg);
        }
    }
    public static void sendMessage(String msg) {
        if (Develop.getInstance().flag) {
            Bukkit.getConsoleSender().sendMessage(msg);
        } else {
            Develop.p.sendMessage(msg);
        }
    }
    public static void exec(String msg) {
        if (Develop.getInstance().flag) {
            Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), msg);
        } else if (Server.getStatus()) {
            Develop.p.chat(msg);
        }
    }
    public void initPlugin() {
        Plugin[] plugins = Bukkit.getPluginManager().getPlugins();
        for (Plugin plugin: plugins) {
            Manage.plugins.put(plugin.getName(), plugin.getDataFolder());
        }
    }
    public static String getPlugins() {
        ArrayList<String> list = new ArrayList<>();
        for(HashMap.Entry<String, File> entry : plugins.entrySet()) {
            list.add(entry.getKey());
        }
        return list.toString();
    }
    public static File getPlugin(String name) {
        return Manage.plugins.get(name);
    }
    public static boolean isunLoadPlugin(String name) {
        return Manage.plugins.containsKey(name);
    }
}
