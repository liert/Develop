package com.github.liert;

import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.entity.Player;
import org.bukkit.plugin.InvalidDescriptionException;
import org.bukkit.plugin.InvalidPluginException;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URLClassLoader;
import java.util.*;

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
    public static String load(String name) {
        try {
            Plugin plugin = Bukkit.getPluginManager().loadPlugin(Manage.getPlugin(name));
            plugin.onLoad();
            Bukkit.getPluginManager().enablePlugin(plugin);
        } catch (InvalidDescriptionException | InvalidPluginException e) {
            e.printStackTrace();
            return Manage.format("Develop", name + " 加载失败.");
        }
        return Manage.format("Develop", name + " 加载成功.");
    }
    public static String unload(Plugin plugin) {
        PluginManager pluginManager = Bukkit.getPluginManager();
        String name = plugin.getName();
        List<?> plugins;
        Map<?, ?> names;
        Map<?, ?> commands;
        SimpleCommandMap commandMap;
        try {
            Field pluginsField = Bukkit.getPluginManager().getClass().getDeclaredField("plugins");
            pluginsField.setAccessible(true);
            plugins = (List<?>) pluginsField.get(pluginManager);
            Field lookupNamesField = Bukkit.getPluginManager().getClass().getDeclaredField("lookupNames");
            lookupNamesField.setAccessible(true);
            names = (Map<?, ?>)lookupNamesField.get(pluginManager);
            Field commandMapField = Bukkit.getPluginManager().getClass().getDeclaredField("commandMap");
            commandMapField.setAccessible(true);
            commandMap = (SimpleCommandMap)commandMapField.get(pluginManager);
            Field knownCommandsField = SimpleCommandMap.class.getDeclaredField("knownCommands");
            knownCommandsField.setAccessible(true);
            commands = (Map<?, ?>)knownCommandsField.get(commandMap);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
            return Manage.format("Develop", name + " 卸载失败.");
        }
        if (plugins != null && plugins.contains(plugin)) {
            plugins.remove(plugin);
        }
        if (names != null && names.containsKey(name)) {
            names.remove(name);
        }
        if (commandMap != null) {
            Iterator<?> it = commands.entrySet().iterator();
            while (it.hasNext()) {
                PluginCommand pluginCommand;
                Map.Entry<?, ?> entry = (Map.Entry<?, ?>) it.next();
                if (!(entry.getValue() instanceof PluginCommand) || (pluginCommand = (PluginCommand)entry.getValue()).getPlugin() != plugin) continue;
                pluginCommand.unregister(commandMap);
                it.remove();
            }
        }
        ClassLoader classLoader = plugin.getClass().getClassLoader();
        if (classLoader instanceof URLClassLoader) {
            try {
                Field pluginField = classLoader.getClass().getDeclaredField("plugin");
                pluginField.setAccessible(true);
                pluginField.set(classLoader, null);
                Field pluginInitField = classLoader.getClass().getDeclaredField("pluginInit");
                pluginInitField.setAccessible(true);
                pluginInitField.set(classLoader, null);
            }
            catch (IllegalAccessException | IllegalArgumentException | NoSuchFieldException | SecurityException e) {
                e.printStackTrace();
            }
            try {
                ((URLClassLoader)classLoader).close();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.gc();
        return Manage.format("Develop", name + " 卸载成功.");
    }
}
