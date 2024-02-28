package com.github.liert;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.InvalidDescriptionException;
import org.bukkit.plugin.InvalidPluginException;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

public class Commands implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String arg, String[] args) {
        if (commandSender instanceof Player) {
            Player p = (Player) commandSender;
            if (!p.isOp()) return true;
        }
        if (args.length < 1) {
            Manage.sendMessage(commandSender, "/develop port 8888");
            Manage.sendMessage(commandSender, "/develop stop");
            Manage.sendMessage(commandSender, "/develop unload pluginName");
            Manage.sendMessage(commandSender, "/develop load pluginName");
            return true;
        }
        if (args[0].equals("port") && args.length == 2) {
            if (!(commandSender instanceof Player)) {
                Manage.sendMessage(commandSender, Manage.format("Develop", "非玩家禁止此命令."));
                return true;
            }
            if (Develop.getInstance().getConfig().getBoolean("AutoListener")) {
                Manage.sendMessage(commandSender, Manage.format("Develop", "AutoListener 模式禁止此命令."));
                return true;
            }
            Develop.p = (Player) commandSender;
            int port = Integer.parseInt(args[1]);
            if (port < 0 || port > 65535) {
                Manage.sendMessage(commandSender, Manage.format("Develop","Error Port."));
                return true;
            }
            Server server = new Server(port);
            server.connect();
            return true;
        }
        if (args[0].equals("unload") && args.length == 2) {
            if (!Bukkit.getPluginManager().isPluginEnabled(args[1])) {
                Manage.sendMessage(commandSender, Manage.format("Develop", args[1] + " 未启用."));
                return true;
            }
//            Plugin plugin = Bukkit.getPluginManager().getPlugin(args[1]);
//            PluginManager pluginManager = Bukkit.getPluginManager();
//            try {
//                Field pluginsField = Bukkit.getPluginManager().getClass().getDeclaredField("plugins");
//                pluginsField.setAccessible(true);
//                List plugins = (List)pluginsField.get(pluginManager);
//            } catch (NoSuchFieldException e) {
//                throw new RuntimeException(e);
//            }


//            Field pluginsField = Bukkit.getPluginManager().getClass().getDeclaredField("plugins");
//            plugin.getPluginLoader().disablePlugin();
//            Bukkit.getPluginManager().clearPlugins(plugin);
            Manage.sendMessage(commandSender, Manage.format("Develop", args[1] + " 卸载成功."));
            return true;
        }
        if (args[0].equals("load") && args.length == 2) {
            if (!Manage.isunLoadPlugin(args[1])) {
                Manage.sendMessage(commandSender, Manage.format("Develop", args[1] + " 未找到."));
                return true;
            }
            try {
                Bukkit.getPluginManager().loadPlugin(Manage.getPlugin(args[1]));
                Manage.sendMessage(commandSender, Manage.format("Develop", args[1] + " 重载成功."));
            } catch (InvalidPluginException | InvalidDescriptionException e) {
                Manage.sendMessage(commandSender, Manage.format("Develop", args[1] + " 重载失败."));
                throw new RuntimeException(e);
            }
            return true;
        }
        if (args[0].equals("plugin") && args[1].equals("list")) {
            Manage.sendMessage(commandSender, Manage.format("Develop", Manage.getPlugins()));
            return true;
        }
        if (args[0].equals("stop") && args.length == 1) {
            Server.close();
            return true;
        }
        return true;
    }
}
