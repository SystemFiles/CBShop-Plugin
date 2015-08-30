package net.cubebeaters;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.inventivetalent.update.spigot.SpigotUpdater;

/**
 *
 * This class that extends plugin will allow players to purchase all the Items
 * they want with cash in a nice clean GUI
 *
 * @author Systemx86 (Bananna)
 * @version 1.1.3
 */
public class CBShop extends JavaPlugin implements Listener {

    private String MSG_PREFIX = ChatColor.BLUE + "[" + ChatColor.GOLD + "Shop" + ChatColor.BLUE + "] " + ChatColor.WHITE;
    private int resourceID;
    private Economy econ;

    // Below Create all Inventory Menu's
    private final Inventory mainMenu = Bukkit.createInventory(null, 18, MSG_PREFIX + " - Main Menu");
    private final Inventory armorMenu = Bukkit.createInventory(null, 27, MSG_PREFIX + " - Armor");
    private final Inventory weaponMenu = Bukkit.createInventory(null, 27, MSG_PREFIX + " - Weapons");
    private final Inventory toolMenu = Bukkit.createInventory(null, 27, MSG_PREFIX + " - Tools");
    private final Inventory oreMenu = Bukkit.createInventory(null, 27, MSG_PREFIX + " - Mining Produce");
    private final Inventory farmMenu = Bukkit.createInventory(null, 27, MSG_PREFIX + " - Farming Produce");

    /**
     * Initializes the plugin.
     */
    @Override
    public void onEnable() {
        getLogger().info("Creating/Loading Configuration.");
        this.saveDefaultConfig();
        resourceID = this.getConfig().getInt("Resource-ID");
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

        try { // This will only work if plugin is on spigot.
            getLogger().log(Level.INFO, "{0}Checking for new versions of CBShop", MSG_PREFIX);
            SpigotUpdater su = new SpigotUpdater(this, resourceID);
            getLogger().log(Level.INFO, "{0}Done", ChatColor.GREEN);
        } catch (IOException ioe) {
            getLogger().log(Level.INFO, "{0}\n\nIOException was thrown while attempting to load SpigotUpdater", ioe.getMessage());
        }
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
     * This Event is triggered when a player clickes an inventory Item and is
     * used to control the GUI shop menu's
     *
     * @param event The Event that is triggered when an item is clicked within'
     * a players inventory
     */
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Player p = (Player) event.getWhoClicked(); // The Player who clicked.
        Inventory inv = event.getInventory(); // The Inventory that was used.
        ItemStack clicked = event.getCurrentItem(); // The item that was clicked.
        ClickType clickType = event.getClick();

        if (inv.getName().equals(mainMenu.getName())) {
            if (null != clicked.getType()) {
                switch (clicked.getType()) {
                    case IRON_CHESTPLATE:
                        event.setCancelled(true);
                        createArmorMenu();
                        p.openInventory(armorMenu);
                        break;
                    case IRON_SWORD:
                        event.setCancelled(true);
                        createWeaponMenu();
                        p.openInventory(weaponMenu);
                        break;
                    case GOLD_SPADE:
                        event.setCancelled(true);
                        createToolsMenu();
                        p.openInventory(toolMenu);
                        break;
                    case COAL:
                        event.setCancelled(true);
                        createOreMenu();
                        p.openInventory(oreMenu);
                        break;
                    case WHEAT:
                        event.setCancelled(true);
                        createFarmMenu();
                        p.openInventory(farmMenu);
                }
            }
        } else if (inv.getName().equals(armorMenu.getName())) {
            if (null != clicked.getType()) {
                switch (clicked.getType()) {
                    case LEATHER_HELMET:
                        itemTransaction(p, clicked, "Items.Armor.Leather.head", clickType);
                        break;
                    case LEATHER_CHESTPLATE:
                        itemTransaction(p, clicked, "Items.Armor.Leather.chest", clickType);
                        break;
                    case LEATHER_LEGGINGS:
                        itemTransaction(p, clicked, "Items.Armor.Leather.legs", clickType);
                        break;
                    case LEATHER_BOOTS:
                        itemTransaction(p, clicked, "Items.Armor.Leather.feet", clickType);
                        break;
                    case CHAINMAIL_HELMET:
                        itemTransaction(p, clicked, "Items.Armor.Chain.head", clickType);
                        break;
                    case CHAINMAIL_CHESTPLATE:
                        itemTransaction(p, clicked, "Items.Armor.Chain.chest", clickType);
                        break;
                    case CHAINMAIL_LEGGINGS:
                        itemTransaction(p, clicked, "Items.Armor.Chain.legs", clickType);
                        break;
                    case CHAINMAIL_BOOTS:
                        itemTransaction(p, clicked, "Items.Armor.Chain.feet", clickType);
                        break;
                    case IRON_HELMET:
                        itemTransaction(p, clicked, "Items.Armor.Iron.head", clickType);
                        break;
                    case IRON_CHESTPLATE:
                        itemTransaction(p, clicked, "Items.Armor.Iron.chest", clickType);
                        break;
                    case IRON_LEGGINGS:
                        itemTransaction(p, clicked, "Items.Armor.Iron.legs", clickType);
                        break;
                    case IRON_BOOTS:
                        itemTransaction(p, clicked, "Items.Armor.Iron.feet", clickType);
                        break;
                    case GOLD_HELMET:
                        itemTransaction(p, clicked, "Items.Armor.Gold.head", clickType);
                        break;
                    case GOLD_CHESTPLATE:
                        itemTransaction(p, clicked, "Items.Armor.Gold.chest", clickType);
                        break;
                    case GOLD_LEGGINGS:
                        itemTransaction(p, clicked, "Items.Armor.Gold.legs", clickType);
                        break;
                    case GOLD_BOOTS:
                        itemTransaction(p, clicked, "Items.Armor.Gold.feet", clickType);
                        break;
                    case DIAMOND_HELMET:
                        itemTransaction(p, clicked, "Items.Armor.Diamond.head", clickType);
                        break;
                    case DIAMOND_CHESTPLATE:
                        itemTransaction(p, clicked, "Items.Armor.Diamond.chest", clickType);
                        break;
                    case DIAMOND_LEGGINGS:
                        itemTransaction(p, clicked, "Items.Armor.Diamond.legs", clickType);
                        break;
                    case DIAMOND_BOOTS:
                        itemTransaction(p, clicked, "Items.Armor.Diamond.feet", clickType);
                        break;
                    case ENDER_CHEST:
                        p.closeInventory();
                        break;
                }
            }
        } else if (inv.getName().equals(weaponMenu.getName())) {
            if (null != clicked.getType()) {
                switch (clicked.getType()) {
                    case WOOD_SWORD:
                        itemTransaction(p, clicked, "Items.Weapons.Swords.Wood", clickType);
                        break;
                    case STONE_SWORD:
                        itemTransaction(p, clicked, "Items.Weapons.Swords.Stone", clickType);
                        break;
                    case IRON_SWORD:
                        itemTransaction(p, clicked, "Items.Weapons.Swords.Iron", clickType);
                        break;
                    case GOLD_SWORD:
                        itemTransaction(p, clicked, "Items.Weapons.Swords.Gold", clickType);
                        break;
                    case DIAMOND_SWORD:
                        itemTransaction(p, clicked, "Items.Weapons.Swords.Diamond", clickType);
                        break;
                    case BOW:
                        itemTransaction(p, clicked, "Items.Weapons.Archery.Bow", clickType);
                        break;
                    case ARROW:
                        itemTransaction(p, clicked, "Items.Weapons.Archery.Arrow", clickType);
                        break;
                    case ENDER_CHEST:
                        p.closeInventory();
                        break;
                }
            }
        } else if (inv.getName().equals(toolMenu.getName())) {
            if (null != clicked.getType()) {
                switch (clicked.getType()) {
                    case WOOD_PICKAXE:
                        itemTransaction(p,clicked,"Items.Tools.Pickaxe.Wood",clickType);
                        break;
                    case STONE_PICKAXE:
                        itemTransaction(p,clicked,"Items.Tools.Pickaxe.Stone",clickType);
                        break;
                    case IRON_PICKAXE:
                        itemTransaction(p,clicked,"Items.Tools.Pickaxe.Iron",clickType);
                        break;
                    case GOLD_PICKAXE:
                        itemTransaction(p,clicked,"Items.Tools.Pickaxe.Gold",clickType);
                        break;
                    case DIAMOND_PICKAXE:
                        itemTransaction(p,clicked,"Items.Tools.Pickaxe.Diamond",clickType);
                        break;
                    case WOOD_AXE:
                        itemTransaction(p,clicked,"Items.Tools.Axe.Wood",clickType);
                        break;
                    case STONE_AXE:
                        itemTransaction(p,clicked,"Items.Tools.Axe.Stone",clickType);
                        break;
                    case IRON_AXE:
                        itemTransaction(p,clicked,"Items.Tools.Axe.Iron",clickType);
                        break;
                    case GOLD_AXE:
                        itemTransaction(p,clicked,"Items.Tools.Axe.Gold",clickType);
                        break;
                    case DIAMOND_AXE:
                        itemTransaction(p,clicked,"Items.Tools.Axe.Diamond",clickType);
                        break;
                    case WOOD_SPADE:
                        itemTransaction(p,clicked,"Items.Tools.Shovel.Wood",clickType);
                        break;
                    case STONE_SPADE:
                        itemTransaction(p,clicked,"Items.Tools.Shovel.Stone",clickType);
                        break;
                    case IRON_SPADE:
                        itemTransaction(p,clicked,"Items.Tools.Shovel.Iron",clickType);
                        break;
                    case GOLD_SPADE:
                        itemTransaction(p,clicked,"Items.Tools.Shovel.Gold",clickType);
                        break;
                    case DIAMOND_SPADE:
                        itemTransaction(p,clicked,"Items.Tools.Shovel.Diamond",clickType);
                        break;
                    case ENDER_CHEST:
                        p.closeInventory();
                }
            }
        } else if (inv.getName().equals(oreMenu.getName())) {
            if (null != clicked.getType()) {
                switch (clicked.getType()) {
                    case COAL:
                        itemTransaction(p,clicked,"Items.Mining.Ore.Coal",clickType);
                        break;
                    case IRON_ORE:
                        itemTransaction(p,clicked,"Items.Mining.Ore.Iron",clickType);
                        break;
                    case GOLD_ORE:
                        itemTransaction(p,clicked,"Items.Mining.Ore.Gold",clickType);
                        break;
                    case DIAMOND:
                        itemTransaction(p,clicked,"Items.Mining.Ore.Diamond",clickType);
                        break;
                    case REDSTONE:
                        itemTransaction(p,clicked,"Items.Mining.Ore.Redstone",clickType);
                        break;
                    case EMERALD:
                        itemTransaction(p,clicked,"Items.Mining.Ore.Emerald",clickType);
                        break;
                    case LAPIS_ORE:
                        itemTransaction(p,clicked,"Items.Mining.Ingots.Lapis",clickType);
                        break;
                    case IRON_INGOT:
                        itemTransaction(p,clicked,"Items.Mining.Ingots.Iron",clickType);
                        break;
                    case GOLD_INGOT:
                        itemTransaction(p,clicked,"Items.Mining.Ingots.Gold",clickType);
                        break;
                    case ENDER_CHEST:
                        p.closeInventory();
                        break;
                }
            }               
        } else if (inv.getName().equals(farmMenu.getName())) {
            if (null != clicked.getType()) {
                switch (clicked.getType()) {
                    case SEEDS:
                        itemTransaction(p,clicked,"Items.Farming.Seeds.Wheat",clickType);
                        break;
                    case MELON_SEEDS:
                        itemTransaction(p,clicked,"Items.Farming.Seeds.Melon",clickType);
                        break;
                    case PUMPKIN_SEEDS:
                        itemTransaction(p,clicked,"Items.Farming.Seeds.Pumpkin",clickType);
                        break;
                    case COCOA:
                        itemTransaction(p,clicked,"Items.Farming.Beans.Cocoa",clickType);
                        break;
                    case CARROT:
                        itemTransaction(p,clicked,"Items.Farming.Plants.Carrot",clickType);
                        break;
                    case POTATO:
                        itemTransaction(p,clicked,"Items.Farming.Plants.Potato",clickType);
                        break;
                    case MELON:
                        itemTransaction(p,clicked,"Items.Farming.Blocks.Melon",clickType);
                        break;
                    case PUMPKIN:
                        itemTransaction(p,clicked,"Items.Farming.Blocks.Pumpkin",clickType);
                        break;
                    case CACTUS:
                        itemTransaction(p,clicked,"Items.Farming.Blocks.Cactus",clickType);
                        break;
                    case SUGAR_CANE:
                        itemTransaction(p,clicked,"Items.Farming.Blocks.SugarCane",clickType);
                        break;
                    case ENDER_CHEST:
                        p.closeInventory();
                        break;
                }
            }          
        }
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
        // Create Armor menu Item
        createMenuItem(Material.IRON_CHESTPLATE, mainMenu, 0, ChatColor.GOLD + "Armor Shop", ChatColor.GREEN + "Click to open the Armor shop");
        // Create Weapons menu Item
        createMenuItem(Material.IRON_SWORD, mainMenu, 1, ChatColor.GOLD + "Armor Shop", ChatColor.GREEN + "Click to open the Weapon Shop");
        // Create Tools menu Item
        createMenuItem(Material.GOLD_SPADE, mainMenu, 2, ChatColor.GOLD + "Tools Shop", ChatColor.GREEN + "Click to open the Tools Shop");
        // Create Ore menu Item
        createMenuItem(Material.COAL, mainMenu, 3, ChatColor.GOLD + "Ore Shop", ChatColor.GREEN + "Click to open the Ore Shop");
        // Create Farm menu Item
        createMenuItem(Material.WHEAT, mainMenu, 4, ChatColor.GOLD + "Farm Shop", ChatColor.GREEN + "Click to open the Farm Shop");
        // Create Exit Button
        createMenuItem(Material.ENDER_CHEST, mainMenu, 17, ChatColor.BLUE + "Exit Shop", ChatColor.GREEN + "Click to exit the CBShop");
    }

    /**
     * Creates all the contents of the Armor Shop
     */
    public void createArmorMenu() {
        // Create Leather Armor Set.
        createPurchaseItem(Material.LEATHER_HELMET, armorMenu, this.getConfig().getInt("Items.Armor.Leather.head.slot"), 1, this.getConfig().getInt("Items.Armor.Leather.head.cost"));
        createPurchaseItem(Material.LEATHER_CHESTPLATE, armorMenu, this.getConfig().getInt("Items.Armor.Leather.chest.slot"), 1, this.getConfig().getInt("Items.Armor.Leather.chest.cost"));
        createPurchaseItem(Material.LEATHER_LEGGINGS, armorMenu, this.getConfig().getInt("Items.Armor.Leather.legs.slot"), 1, this.getConfig().getInt("Items.Armor.Leather.legs.cost"));
        createPurchaseItem(Material.LEATHER_BOOTS, armorMenu, this.getConfig().getInt("Items.Armor.Leather.feet.slot"), 1, this.getConfig().getInt("Items.Armor.Leather.feet.cost"));
        // Create Chainmail Armor Set.
        createPurchaseItem(Material.CHAINMAIL_HELMET, armorMenu, this.getConfig().getInt("Items.Armor.Chain.head.slot"), 1, this.getConfig().getInt("Items.Armor.Chain.head.cost"));
        createPurchaseItem(Material.CHAINMAIL_HELMET, armorMenu, this.getConfig().getInt("Items.Armor.Chain.chest.slot"), 1, this.getConfig().getInt("Items.Armor.Chain.chest.cost"));
        createPurchaseItem(Material.CHAINMAIL_HELMET, armorMenu, this.getConfig().getInt("Items.Armor.Chain.legs.slot"), 1, this.getConfig().getInt("Items.Armor.Chain.legs.cost"));
        createPurchaseItem(Material.CHAINMAIL_HELMET, armorMenu, this.getConfig().getInt("Items.Armor.Chain.feet.slot"), 1, this.getConfig().getInt("Items.Armor.Chain.feet.cost"));
        // Create Iron Armor Set.
        createPurchaseItem(Material.IRON_HELMET, armorMenu, this.getConfig().getInt("Items.Armor.Iron.head.slot"), 1, this.getConfig().getInt("Items.Armor.Iron.head.cost"));
        createPurchaseItem(Material.IRON_HELMET, armorMenu, this.getConfig().getInt("Items.Armor.Iron.chest.slot"), 1, this.getConfig().getInt("Items.Armor.Iron.chest.cost"));
        createPurchaseItem(Material.IRON_HELMET, armorMenu, this.getConfig().getInt("Items.Armor.Iron.legs.slot"), 1, this.getConfig().getInt("Items.Armor.Iron.legs.cost"));
        createPurchaseItem(Material.IRON_HELMET, armorMenu, this.getConfig().getInt("Items.Armor.Iron.feet.slot"), 1, this.getConfig().getInt("Items.Armor.Iron.feet.cost"));
        // Create Gold Armor Set.
        createPurchaseItem(Material.GOLD_HELMET, armorMenu, this.getConfig().getInt("Items.Armor.Gold.head.slot"), 1, this.getConfig().getInt("Items.Armor.Gold.head.cost"));
        createPurchaseItem(Material.GOLD_HELMET, armorMenu, this.getConfig().getInt("Items.Armor.Gold.chest.slot"), 1, this.getConfig().getInt("Items.Armor.Gold.chest.cost"));
        createPurchaseItem(Material.GOLD_HELMET, armorMenu, this.getConfig().getInt("Items.Armor.Gold.legs.slot"), 1, this.getConfig().getInt("Items.Armor.Gold.legs.cost"));
        createPurchaseItem(Material.GOLD_HELMET, armorMenu, this.getConfig().getInt("Items.Armor.Gold.feet.slot"), 1, this.getConfig().getInt("Items.Armor.Gold.feet.cost"));
        // Create Diamond Armor Set.
        createPurchaseItem(Material.DIAMOND_HELMET, armorMenu, this.getConfig().getInt("Items.Armor.Diamond.head.slot"), 1, this.getConfig().getInt("Items.Armor.Diamond.head.cost"));
        createPurchaseItem(Material.DIAMOND_HELMET, armorMenu, this.getConfig().getInt("Items.Armor.Diamond.chest.slot"), 1, this.getConfig().getInt("Items.Armor.Diamond.chest.cost"));
        createPurchaseItem(Material.DIAMOND_HELMET, armorMenu, this.getConfig().getInt("Items.Armor.Diamond.legs.slot"), 1, this.getConfig().getInt("Items.Armor.Diamond.legs.cost"));
        createPurchaseItem(Material.DIAMOND_HELMET, armorMenu, this.getConfig().getInt("Items.Armor.Diamond.feet.slot"), 1, this.getConfig().getInt("Items.Armor.Diamond.feet.cost"));

        // Create Exit Button
        createMenuItem(Material.ENDER_CHEST, armorMenu, 26, ChatColor.BLUE + "Exit", ChatColor.GRAY + "Click to exit to the main menu.");
    }

    /**
     * Creates all the contents of the Weapons Shop
     */
    public void createWeaponMenu() {
        // Create Swords
        createPurchaseItem(Material.WOOD_SWORD, weaponMenu, this.getConfig().getInt("Items.Weapons.Swords.Wood.slot"), 1, this.getConfig().getInt("Items.Weapons.Swords.Wood.cost"));
        createPurchaseItem(Material.STONE_SWORD, weaponMenu, this.getConfig().getInt("Items.Weapons.Swords.Stone.slot"), 1, this.getConfig().getInt("Items.Weapons.Swords.Stone.cost"));
        createPurchaseItem(Material.IRON_SWORD, weaponMenu, this.getConfig().getInt("Items.Weapons.Swords.Iron.slot"), 1, this.getConfig().getInt("Items.Weapons.Swords.Iron.cost"));
        createPurchaseItem(Material.GOLD_SWORD, weaponMenu, this.getConfig().getInt("Items.Weapons.Swords.Gold.slot"), 1, this.getConfig().getInt("Items.Weapons.Swords.Gold.cost"));
        createPurchaseItem(Material.DIAMOND_SWORD, weaponMenu, this.getConfig().getInt("Items.Weapons.Swords.Diamond.slot"), 1, this.getConfig().getInt("Items.Weapons.Swords.Diamond.cost"));
        // Create Archery
        createPurchaseItem(Material.BOW, weaponMenu, this.getConfig().getInt("Items.Weapons.Archery.Bow.slot"), 1, this.getConfig().getInt("Items.Weapons.Archery.Bow.cost"));
        createPurchaseItem(Material.ARROW, weaponMenu, this.getConfig().getInt("Items.Weapons.Archery.Arrow.slot"), this.getConfig().getInt("Items.Weapons.Archery.Arrow.amount"), this.getConfig().getInt("Items.Weapons.Archery.Arrow.cost"));

        // Create Exit Button
        createMenuItem(Material.ENDER_CHEST, weaponMenu, 26, ChatColor.BLUE + "Exit", ChatColor.GRAY + "Click to exit to the main menu.");
    }

    /**
     * Creates all the contents of the Tools Shop
     */
    public void createToolsMenu() {
        // Create Pickaxes'
        createPurchaseItem(Material.WOOD_PICKAXE, toolMenu, this.getConfig().getInt("Items.Tools.Pickaxe.Wood.slot"), 1, this.getConfig().getInt("Items.Tools.Pickaxe.Wood.cost"));
        createPurchaseItem(Material.STONE_PICKAXE, toolMenu, this.getConfig().getInt("Items.Tools.Pickaxe.Stone.slot"), 1, this.getConfig().getInt("Items.Tools.Pickaxe.Stone.cost"));
        createPurchaseItem(Material.IRON_PICKAXE, toolMenu, this.getConfig().getInt("Items.Tools.Pickaxe.Iron.slot"), 1, this.getConfig().getInt("Items.Tools.Pickaxe.Iron.cost"));
        createPurchaseItem(Material.GOLD_PICKAXE, toolMenu, this.getConfig().getInt("Items.Tools.Pickaxe.Gold.slot"), 1, this.getConfig().getInt("Items.Tools.Pickaxe.Gold.cost"));
        createPurchaseItem(Material.DIAMOND_PICKAXE, toolMenu, this.getConfig().getInt("Items.Tools.Pickaxe.Diamond.slot"), 1, this.getConfig().getInt("Items.Tools.Pickaxe.Diamond.cost"));
        // Create Axes'
        createPurchaseItem(Material.WOOD_AXE, toolMenu, this.getConfig().getInt("Items.Tools.Axe.Wood.slot"), 1, this.getConfig().getInt("Items.Tools.Axe.Wood.cost"));
        createPurchaseItem(Material.STONE_AXE, toolMenu, this.getConfig().getInt("Items.Tools.Axe.Stone.slot"), 1, this.getConfig().getInt("Items.Tools.Axe.Stone.cost"));
        createPurchaseItem(Material.IRON_AXE, toolMenu, this.getConfig().getInt("Items.Tools.Axe.Iron.slot"), 1, this.getConfig().getInt("Items.Tools.Axe.Iron.cost"));
        createPurchaseItem(Material.GOLD_AXE, toolMenu, this.getConfig().getInt("Items.Tools.Axe.Gold.slot"), 1, this.getConfig().getInt("Items.Tools.Axe.Gold.cost"));
        createPurchaseItem(Material.DIAMOND_AXE, toolMenu, this.getConfig().getInt("Items.Tools.Axe.Diamond.slot"), 1, this.getConfig().getInt("Items.Tools.Axe.Diamond.cost"));
        // Create Spades
        createPurchaseItem(Material.WOOD_SPADE, toolMenu, this.getConfig().getInt("Items.Tools.Shovel.Wood.slot"), 1, this.getConfig().getInt("Items.Tools.Shovel.Wood.cost"));
        createPurchaseItem(Material.STONE_SPADE, toolMenu, this.getConfig().getInt("Items.Tools.Shovel.Stone.slot"), 1, this.getConfig().getInt("Items.Tools.Shovel.Stone.cost"));
        createPurchaseItem(Material.IRON_SPADE, toolMenu, this.getConfig().getInt("Items.Tools.Shovel.Iron.slot"), 1, this.getConfig().getInt("Items.Tools.Shovel.Iron.cost"));
        createPurchaseItem(Material.GOLD_SPADE, toolMenu, this.getConfig().getInt("Items.Tools.Shovel.Gold.slot"), 1, this.getConfig().getInt("Items.Tools.Shovel.Gold.cost"));
        createPurchaseItem(Material.DIAMOND_SPADE, toolMenu, this.getConfig().getInt("Items.Tools.Shovel.Diamond.slot"), 1, this.getConfig().getInt("Items.Tools.Shovel.Diamond.cost"));

        // Create Exit Button
        createMenuItem(Material.ENDER_CHEST, toolMenu, 26, ChatColor.BLUE + "Exit", ChatColor.GRAY + "Click to exit to the main menu.");
    }

    /**
     * Creates all the contents of the Ore Shop
     */
    public void createOreMenu() {
        // Create Ore Items
        createPurchaseItem(Material.COAL, oreMenu, this.getConfig().getInt("Items.Mining.Ore.Coal.slot"), this.getConfig().getInt("Items.Mining.Ore.Coal.amount"), this.getConfig().getInt("Items.Mining.Ore.Coal.cost"));
        createPurchaseItem(Material.IRON_ORE, oreMenu, this.getConfig().getInt("Items.Mining.Ore.Iron.slot"), this.getConfig().getInt("Items.Mining.Ore.Iron.amount"), this.getConfig().getInt("Items.Mining.Ore.Iron.cost"));
        createPurchaseItem(Material.GOLD_ORE, oreMenu, this.getConfig().getInt("Items.Mining.Ore.Gold.slot"), this.getConfig().getInt("Items.Mining.Ore.Gold.amount"), this.getConfig().getInt("Items.Mining.Ore.Gold.cost"));
        createPurchaseItem(Material.DIAMOND, oreMenu, this.getConfig().getInt("Items.Mining.Ore.Diamond.slot"), this.getConfig().getInt("Items.Mining.Ore.Diamond.amount"), this.getConfig().getInt("Items.Mining.Ore.Diamond.cost"));
        createPurchaseItem(Material.REDSTONE, oreMenu, this.getConfig().getInt("Items.Mining.Ore.Redstone.slot"), this.getConfig().getInt("Items.Mining.Ore.Redstone.amount"), this.getConfig().getInt("Items.Mining.Ore.Redstone.cost"));
        createPurchaseItem(Material.EMERALD, oreMenu, this.getConfig().getInt("Items.Mining.Ore.Emerald.slot"), this.getConfig().getInt("Items.Mining.Ore.Emerald.amount"), this.getConfig().getInt("Items.Mining.Ore.Emerald.cost"));
        // Create Ingot Items
        createPurchaseItem(Material.LAPIS_ORE, oreMenu, this.getConfig().getInt("Items.Mining.Ingots.lapis.slot"), this.getConfig().getInt("Items.Mining.Ingots.lapis.amount"), this.getConfig().getInt("Items.Mining.Ingots.lapis.cost"));
        createPurchaseItem(Material.IRON_INGOT, oreMenu, this.getConfig().getInt("Items.Mining.Ingots.Iron.slot"), this.getConfig().getInt("Items.Mining.Ingots.Iron.amount"), this.getConfig().getInt("Items.Mining.Ingots.Iron.cost"));
        createPurchaseItem(Material.GOLD_INGOT, oreMenu, this.getConfig().getInt("Items.Mining.Ingots.Gold.slot"), this.getConfig().getInt("Items.Mining.Ingots.Gold.amount"), this.getConfig().getInt("Items.Mining.Ingots.Gold.cost"));
        createPurchaseItem(Material.DIAMOND, oreMenu, this.getConfig().getInt("Items.Mining.Ingots.Diamond.slot"), this.getConfig().getInt("Items.Mining.Ingots.Diamond.amount"), this.getConfig().getInt("Items.Mining.Ingots.Diamond.cost"));
        createPurchaseItem(Material.EMERALD, oreMenu, this.getConfig().getInt("Items.Mining.Ingots.Emerald.slot"), this.getConfig().getInt("Items.Mining.Ingots.Emerald.amount"), this.getConfig().getInt("Items.Mining.Ingots.Emerald.cost"));

        // Create Exit Button
        createMenuItem(Material.ENDER_CHEST, oreMenu, 26, ChatColor.BLUE + "Exit", ChatColor.GRAY + "Click to exit to the main menu.");
    }

    /**
     * Creates all the contents of the Farm Shop
     */
    public void createFarmMenu() {
        // Create Seed Items
        createPurchaseItem(Material.SEEDS, farmMenu, this.getConfig().getInt("Items.Farming.Seeds.Wheat.slot"), this.getConfig().getInt("Items.Farming.Seeds.Wheat.amount"), this.getConfig().getInt("Items.Farming.Seeds.Wheat.cost"));
        createPurchaseItem(Material.MELON_SEEDS, farmMenu, this.getConfig().getInt("Items.Farming.Seeds.Melon.slot"), this.getConfig().getInt("Items.Farming.Seeds.Melon.amount"), this.getConfig().getInt("Items.Farming.Seeds.Melon.cost"));
        createPurchaseItem(Material.PUMPKIN_SEEDS, farmMenu, this.getConfig().getInt("Items.Farming.Seeds.Pumpkin.slot"), this.getConfig().getInt("Items.Farming.Seeds.Pumpkin.amount"), this.getConfig().getInt("Items.Farming.Seeds.Pumpkin.cost"));
        // Create Bean Items
        createPurchaseItem(Material.COCOA, farmMenu, this.getConfig().getInt("Items.Farming.Beans.Cocoa.slot"), this.getConfig().getInt("Items.Farming.Beans.Cocoa.amount"), this.getConfig().getInt("Items.Farming.Beans.Cocoa.cost"));
        // Create Plant Items
        createPurchaseItem(Material.CARROT, farmMenu, this.getConfig().getInt("Items.Farming.Plants.Carrot.slot"), this.getConfig().getInt("Items.Farming.Plants.Carrot.amount"), this.getConfig().getInt("Items.Farming.Plants.Carrot.cost"));
        createPurchaseItem(Material.POTATO, farmMenu, this.getConfig().getInt("Items.Farming.Plants.Potato.slot"), this.getConfig().getInt("Items.Farming.Plants.Potato.amount"), this.getConfig().getInt("Items.Farming.Plants.Potato.cost"));
        // Create Block Items
        createPurchaseItem(Material.MELON_BLOCK, farmMenu, this.getConfig().getInt("Items.Farming.Blocks.Melon.slot"), this.getConfig().getInt("Items.Farming.Blocks.Melon.amount"), this.getConfig().getInt("Items.Farming.Blocks.Melon.cost"));
        createPurchaseItem(Material.PUMPKIN, farmMenu, this.getConfig().getInt("Items.Farming.Blocks.Pumpkin.slot"), this.getConfig().getInt("Items.Farming.Blocks.Pumpkin.amount"), this.getConfig().getInt("Items.Farming.Blocks.Pumpkin.cost"));
        createPurchaseItem(Material.CACTUS, farmMenu, this.getConfig().getInt("Items.Farming.Blocks.Cactus.slot"), this.getConfig().getInt("Items.Farming.Blocks.Cactus.amount"), this.getConfig().getInt("Items.Farming.Blocks.Cactus.cost"));
        createPurchaseItem(Material.SUGAR_CANE, farmMenu, this.getConfig().getInt("Items.Farming.Blocks.SugarCane.slot"), this.getConfig().getInt("Items.Farming.Blocks.SugarCane.amount"), this.getConfig().getInt("Items.Farming.Blocks.SugarCane.cost"));
        // Create Cancel Button
        createMenuItem(Material.ENDER_CHEST, farmMenu, 17, ChatColor.BLUE + "Exit Shop", ChatColor.GREEN + "Click to exit the CBShop");
    }

    /**
     *
     * This method decides how much to charge the player for 'item' and allows
     * players to also sell to the server for resale price if they right click
     * the item.
     *
     * @param p The player selling/buying items
     * @param item The Item that is being sold/bought
     * @param itemPath The Path to the chosen item's properties(in Config.yml)
     * @param leftRight Whether the player left-clicked or right-clicked the
     * item.
     */
    public void itemTransaction(Player p, ItemStack item, String itemPath, ClickType leftRight) {
        double playerBalance = econ.getBalance(p); // The players current balance
        Inventory pInv = p.getInventory(); // The players Inventory
        int amount = 0; // Initialize amount var.
        int cost = this.getConfig().getInt(itemPath + ".cost"); // The cost of item
        int resaleV = this.getConfig().getInt(itemPath + ".resell"); // The Resell cost of item.

        if (itemPath.contains("Armor") || itemPath.contains("Weapons") || itemPath.contains("Tools")) {
            if (!itemPath.contains("Arrow")) {
                amount = 1;
            } else {
                amount = this.getConfig().getInt(itemPath + ".amount");
            }
        } else {
            this.getConfig().getInt(itemPath + ".amount");
        }

        if (leftRight.isLeftClick()) { // Buy Item
            item.setAmount(amount);
            if (playerBalance >= cost) {
                econ.withdrawPlayer(p, cost);
                pInv.addItem(item);
                p.sendMessage(MSG_PREFIX + "Purchased " + item.getItemMeta().getDisplayName() + " for " + cost + " " + econ.currencyNamePlural());
            } else {
                p.sendMessage(MSG_PREFIX + "Sorry, you have insufficient funds.");
                p.sendMessage(MSG_PREFIX + playerBalance);
            }
        } else if (leftRight.isRightClick()) { // Sell Item
            boolean foundStack = false;
            if (pInv.contains(item)) {
                for (ItemStack is : pInv) {
                    if (is.getType() == item.getType()) {
                        if (is.getAmount() > amount) {
                            is.setAmount(is.getAmount() - amount);
                            foundStack = true;
                            econ.depositPlayer(p, resaleV);
                        } else {
                            pInv.remove(is);
                            econ.depositPlayer(p, resaleV); // Logic problem here. //
                        }
                    }
                }
            } else {
                p.sendMessage(ChatColor.RED + "You don't have any of that item to sell");
            }
        } else {
            p.sendMessage(ChatColor.RED + "Unsupported Action (" + leftRight.name() + ").");
        }

    }

    /**
     *
     * Creates a purchasable Item for the shop.
     *
     * @param material The Item material to use.
     * @param inv The inventory to add this item to.
     * @param slot The Slot in the specified inventory which to add the item.
     * @param amount The amount of the item to add.
     * @param cost The cost for 'x' amount of the item.
     */
    public void createPurchaseItem(Material material, Inventory inv, int slot, int amount, int cost) {
        String lore;

        ItemStack item = new ItemStack(material);
        item.setAmount(amount);
        ItemMeta meta = item.getItemMeta();
        lore = meta.getDisplayName() + " Cost: " + cost;
        try {
            List<String> Lore = new ArrayList();
            Lore.add(lore);
            meta.setLore(Lore);
        } catch (NullPointerException ex) {
            getLogger().log(Level.INFO, "Nothing entered for the lore of {0}.", meta.getDisplayName());
        }
        item.setItemMeta(meta);

        inv.setItem(slot, item);
    }

    /**
     *
     * Creates a clickable menu Item for the GUI.
     *
     * @param material The material of the item.
     * @param inv The Inventory to put the Item into.
     * @param Slot The slot in the inventory where the item will be held
     * @param name The display name of the item.
     * @param lore The Lore/Description of the item.
     */
    public void createMenuItem(Material material, Inventory inv, int Slot, String name, String lore) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        List<String> Lore = new ArrayList();
        Lore.add(lore);
        meta.setLore(Lore);
        item.setItemMeta(meta);

        inv.setItem(Slot, item);
    }

}
