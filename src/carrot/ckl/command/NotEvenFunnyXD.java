package carrot.ckl.command;

import carrot.ckl.worlds.CoolEffects;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class NotEvenFunnyXD implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (label.equalsIgnoreCase("lolidk")) {
            sender.sendMessage(CoolEffects.RainbowText("ello"));
        }
        return true;
    }
}
