package com.github.liert;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Commands implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String arg, String[] args) {
        if (commandSender instanceof Player) {
            Player p = (Player) commandSender;
            if (!p.isOp()) return true;
        }
        if (args.length < 1) {
            Manage.sendMessage(commandSender, "/develop port <number>");
            Manage.sendMessage(commandSender, "/develop stop");
            return true;
        }
        if (args[0].equals("port") && args.length == 2) {
            if (!(commandSender instanceof Player)) {
                Manage.sendMessage(commandSender, Manage.format("Develop", "非玩家禁止此使用命令."));
                return true;
            }
            if (Develop.getInstance().getConfig().getBoolean("AutoListener")) {
                Manage.sendMessage(commandSender, Manage.format("Develop", "AutoListener 模式禁止此使用命令."));
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
        if (args[0].equals("stop") && args.length == 1) {
            Server.close();
            return true;
        }
        return true;
    }
}
