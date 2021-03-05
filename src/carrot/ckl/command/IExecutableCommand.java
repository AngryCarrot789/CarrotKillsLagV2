package carrot.ckl.command;

import carrot.ckl.logs.ChatLogger;
import org.bukkit.command.CommandSender;

/**
 * An interface in which contains an executable command, requiring a sender, logger and arguments
 */
public interface IExecutableCommand {
    /**
     * Executes the command, using the given sender, chat logger, and the command arguments
     * @param sender The command sender (could be a player or console... i hope)
     * @param logger The chat logger
     * @param args The arguments to the specific command. E.g, /maincommand command arg1 arg2, args contains arg1 and arg2
     */
    void Execute(CommandSender sender, ChatLogger logger, String[] args);
}
