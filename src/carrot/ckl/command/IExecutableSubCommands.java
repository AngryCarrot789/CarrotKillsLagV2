package carrot.ckl.command;

import carrot.ckl.logs.ChatLogger;

import java.util.SortedMap;

/**
 * An abstract class that should be extended by any class that has sub-commands
 */
public abstract class IExecutableSubCommands implements IExecutableCommand {
    public SortedMap<String, IExecutableCommand> subCommandMap;

    public abstract void displayHelp(ChatLogger logger);
}
