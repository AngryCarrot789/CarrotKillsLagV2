package carrot.ckl.logs;

import carrot.ckl.CarrotKillsLag;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class ChatLogger {
    private CommandSender sender;

    public ChatLogger(CommandSender sender) {
        setSender(sender);
    }

    public void LogPlayer(String text) {
        if (sender != null) {
            sender.sendMessage(text);
        }
    }

    public void LogAny(String text) {
        if (sender == null) {
            LogConsole(text);
        }
        else {
            LogPlayer(text);
        }
    }

    public void LogWarning(String text) {
        LogAny(ChatColor.RED + text);
    }

    public void LogInfo(String text) {
        LogAny(ChatColor.GOLD + text);
    }

    public void LogInfoWithPrefix(String text) {
        LogAny(CarrotKillsLag.PREFIX + " " + ChatColor.GOLD + text);
    }

    public void LogSuccess(String text) {
        LogAny(ChatColor.GREEN + text);
    }

    public static void LogConsole(String text) {
        Bukkit.getConsoleSender().sendMessage(text);
        //System.out.println(text);
    }

    public static void LogPlugin(String text) {
        LogConsole(CarrotKillsLag.PREFIX + " " + ChatColor.GOLD + text);
    }

    public static void LogDebug(String header, String info) {
        LogConsole(CarrotKillsLag.PREFIX + " " + ChatColor.GOLD + header + ": " + info);
    }

    public void setSender(CommandSender sender) {
        this.sender = sender;
    }
}
