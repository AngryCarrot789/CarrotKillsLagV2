package carrot.ckl;

import atomicscience.particle.accelerator.EntityParticle;
import carrot.ckl.command.MainCommandExecutor;
import carrot.ckl.command.NotEvenFunnyXD;
import carrot.ckl.config.ConfigManager;
import carrot.ckl.logs.ChatLogger;
import carrot.ckl.player.tables.PlayerResultsTable;
import carrot.ckl.worlds.CoolEffects;
import net.minecraft.world.WorldServer;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_6_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_6_R3.entity.CraftEntity;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import sun.nio.ch.Net;

import java.util.List;

public class CarrotKillsLag extends JavaPlugin {
    public static final String PREFIX = "§6[§3CKL§6]§r";
    private static CarrotKillsLag instance;
    private PlayerResultsTable resultsTable;

    public void onEnable() {
        instance = this;
        try {
            ConfigManager.initialise();
        }
        catch (Exception e) {

        }
        resultsTable = new PlayerResultsTable();

        getCommand("ckl").setExecutor(new MainCommandExecutor());
        getCommand("lolidk").setExecutor(new NotEvenFunnyXD());

        ChatLogger.LogPlugin("Enabled :)");
    }

    public void onDisable() {
        ChatLogger.LogPlugin("Disabled :(");
    }

    public static CarrotKillsLag getInstance() {
        return instance;
    }
}
