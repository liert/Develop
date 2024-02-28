package com.github.liert;

import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class Manage {
    public static String format(String title, String msg) {
        return "§7[§a" + title + "§7]§a " + msg;
    }
    public static <T> void sendMessage(T p, String msg) {
        if (Develop.getInstance().flag) {
            Bukkit.getConsoleSender().sendMessage(msg);
        } else if (p instanceof Player) {
            ((Player) p).sendMessage(msg);
        } else if (p instanceof ConsoleCommandSender) {
            ((ConsoleCommandSender) p).sendMessage(msg);
        } else {
            Develop.p.sendMessage(msg);
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
        } else {
            Develop.p.chat(msg);
        }
    }
}
