package com.github.liert;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Commands implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String arg, String[] args) {
        if (!(commandSender instanceof Player)) {
            return true;
        }
        Player p = (Player) commandSender;
        if (args.length < 1) {
            p.sendMessage("/develop port 8888");
            p.sendMessage("/develop stop");
            return true;
        }
        if (args[0].equals("port") && args.length == 2) {
            Develop.p = p;
            int port = Integer.parseInt(args[1]);
            if (port > 0 && port < 65535) p.sendMessage("Error Port.");
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
