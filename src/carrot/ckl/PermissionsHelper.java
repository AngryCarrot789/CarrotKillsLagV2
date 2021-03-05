package carrot.ckl;

import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import ru.tehkode.permissions.bukkit.PermissionsEx;

public class PermissionsHelper {
    public static boolean HasPermission(Player player, String permission) {
        return PermissionsEx.getPermissionManager().has(player, permission);
    }

    public static boolean HasPermissionOrOp(Player player, String permission) {
        return HasPermission(player, permission) || player.isOp();
    }

    public static boolean IsConsoleOrHasPermsOrOp(CommandSender sender, String permission) {
        if (IsConsole(sender))
            return true;
        return HasPermissionOrOp((Player) sender, permission);
    }

    public static boolean IsConsole(CommandSender sender) {
        return sender instanceof ConsoleCommandSender;
    }
}
