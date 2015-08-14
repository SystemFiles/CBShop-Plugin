package net.cubebeaters.cbshop;

import java.util.logging.Level;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

/**
 *
 * This class that extends plugin will allow players to purchase all the Items
 * they want with cash in a nice clean GUI
 *
 * @author Systemx86 (Bananna)
 * @version 0.5
 */
public class CBShop extends JavaPlugin implements Listener {

    private String MSG_PREFIX = ChatColor.BLUE + "[" + ChatColor.GOLD + "Shop" + ChatColor.BLUE + "] " + ChatColor.WHITE;
    private Economy econ;

    // Below Create all Inventory Menu's
    private final Inventory mainMenu = Bukkit.createInventory(null, 18, MSG_PREFIX + " - Main Menu");
    private final Inventory armorMenu = Bukkit.createInventory(null, 27, MSG_PREFIX + " - Armor");
    private final Inventory weaponMenu = Bukkit.createInventory(null, 27, MSG_PREFIX + " - Weapons");
    private final Inventory toolMenu = Bukkit.createInventory(null, 27, MSG_PREFIX + " - Tools");
    private final Inventory oreMenu = Bukkit.createInventory(null, 27, MSG_PREFIX + " - Mining Produce");

    /**
     * Initializes the plugin.
     */
    @Override
    public void onEnable() {
        getLogger().info("Creating/Loading Configuration.");
        this.saveDefaultConfig();
        Bukkit.getServer().getPluginManager().registerEvents(this, this);
        getLogger().info("Events Registered.");

        getLogger().info("Setting up Economy Hook.");
        if (!setupEconomy()) {
            getLogger().info(String.format("[%s] - Disabled due to no Vault dependency found!", getDescription().getName()));
            getServer().getPluginManager().disablePlugin(this);
        } else {
            getLogger().info("Economy Enabled!");
        }

        getLogger().info("Setting custom prefix...");
        MSG_PREFIX = net.md_5.bungee.api.ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("Messages.CommandPrefix").replace("'", "").replace("'", ""));
        getLogger().info("Set. Done.");
    }

    /**
     * Deactivates the plugin.
     */
    @Override
    public void onDisable() {
        getLogger().log(Level.INFO, "{0}Saving configuration.", net.md_5.bungee.api.ChatColor.YELLOW);
        this.saveConfig(); // Saves the Config.yml file.
        getLogger().log(Level.INFO, "{0}Saved. Stopping...", net.md_5.bungee.api.ChatColor.GREEN);
    }

    /**
     * 
     * This method hooks into the servers default economy plugin.
     * 
     * @return true if there is an Economy plugin present.
     */
    public boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (cmd.getName().equalsIgnoreCase("Shop")) {
                if (p.hasPermission("cbshop.shop")) {
                    createMainMenu();
                    p.openInventory(mainMenu);
                    p.sendMessage(MSG_PREFIX + "Shop Opened.");
                    return true;
                } else {
                    p.sendMessage(ChatColor.RED + "Missing permissions.");
                }
            }
        }
        return true;
    }
    
    /**
     * Creates all the options In the main menu.
     */
    public void createMainMenu() {
        
    }

}
