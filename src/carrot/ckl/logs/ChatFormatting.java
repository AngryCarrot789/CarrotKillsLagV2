package carrot.ckl.logs;

import org.bukkit.ChatColor;
import org.bukkit.Location;

public class ChatFormatting {
    private static String HasSubCommandsMessage = ChatColor.DARK_AQUA + "(This command holds sub commands)";
    public static String Apostrophise(String content){
        return Surround("'", content, "'");
    }

    public static String Bracketise(String content){
        return Surround("[", content, "]");
    }

    public static String Surround(String left, String content, String right){
        return left + content + right;
    }

    public static double Round(double value, int places) {
        if (places < 0) {
            places = 0;
        }

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }

    public static String TitliseContent(String content, int maxWidth) {
        int totalTitleChars = maxWidth - content.length() - 2;
        String repeatedChars = Repeat('-', totalTitleChars / 2);
        return repeatedChars + content + repeatedChars;
    }

    public static String Repeat(char character, int times) {
        StringBuilder stringBuilder = new StringBuilder(times);
        for(int i = 0; i < times; i++) {
            stringBuilder.append(character);
        }
        return stringBuilder.toString();
    }

    public static String Spacify(String value) {
        return Surround(" ", value, " ");
    }

    public static String ColouredXYZ(Location location) {
        return ColouredXYZ(location.getX(), location.getY(), location.getZ());
    }

    public static String ColouredXYZ(double x, double y, double z) {
        return "" + ChatColor.RED + x + ", " + ChatColor.GREEN + y + ", " + ChatColor.BLUE + z;
    }

    public static String ColouredXZ(double x, double z) {
        return "" + ChatColor.RED + x + ", " + ChatColor.BLUE + z;
    }

    public static String FormatCommand(String commandName, String params, String description) {
        return ChatColor.AQUA + "/ckl " +
               ChatColor.DARK_AQUA + commandName + " " +
               ChatColor.BLUE + params + '\n' +
               ChatColor.GOLD + description;
    }

    public static String FormatMainCommand(String commandName, String description) {
        return ChatColor.AQUA + "/ckl " +
               ChatColor.DARK_AQUA + commandName + " " +
               HasSubCommandsMessage + '\n' +
               ChatColor.GOLD + description;
    }
}
