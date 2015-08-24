package net.cubebeaters.cbshop;

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
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

/**
 *
 * This class that extends plugin will allow players to purchase all the Items
 * they want with cash in a nice clean GUI
 *
 * @author Systemx86 (Bananna)
 * @version 1.0
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
        createPurchaseItem(Material.CHAINMAIL_HELMET,armorMenu,this.getConfig().getInt("Items.Armor.Chain.head.slot"),1,this.getConfig().getInt("Items.Armor.Chain.head.cost"));
        createPurchaseItem(Material.CHAINMAIL_HELMET,armorMenu,this.getConfig().getInt("Items.Armor.Chain.chest.slot"),1,this.getConfig().getInt("Items.Armor.Chain.chest.cost"));
        createPurchaseItem(Material.CHAINMAIL_HELMET,armorMenu,this.getConfig().getInt("Items.Armor.Chain.legs.slot"),1,this.getConfig().getInt("Items.Armor.Chain.legs.cost"));
        createPurchaseItem(Material.CHAINMAIL_HELMET,armorMenu,this.getConfig().getInt("Items.Armor.Chain.feet.slot"),1,this.getConfig().getInt("Items.Armor.Chain.feet.cost"));
        // Create Iron Armor Set.
        createPurchaseItem(Material.IRON_HELMET,armorMenu,this.getConfig().getInt("Items.Armor.Iron.head.slot"),1,this.getConfig().getInt("Items.Armor.Iron.head.cost"));
        createPurchaseItem(Material.IRON_HELMET,armorMenu,this.getConfig().getInt("Items.Armor.Iron.chest.slot"),1,this.getConfig().getInt("Items.Armor.Iron.chest.cost"));
        createPurchaseItem(Material.IRON_HELMET,armorMenu,this.getConfig().getInt("Items.Armor.Iron.legs.slot"),1,this.getConfig().getInt("Items.Armor.Iron.legs.cost"));
        createPurchaseItem(Material.IRON_HELMET,armorMenu,this.getConfig().getInt("Items.Armor.Iron.feet.slot"),1,this.getConfig().getInt("Items.Armor.Iron.feet.cost"));
        // Create Gold Armor Set.
        createPurchaseItem(Material.GOLD_HELMET,armorMenu,this.getConfig().getInt("Items.Armor.Gold.head.slot"),1,this.getConfig().getInt("Items.Armor.Gold.head.cost"));
        createPurchaseItem(Material.GOLD_HELMET,armorMenu,this.getConfig().getInt("Items.Armor.Gold.chest.slot"),1,this.getConfig().getInt("Items.Armor.Gold.chest.cost"));
        createPurchaseItem(Material.GOLD_HELMET,armorMenu,this.getConfig().getInt("Items.Armor.Gold.legs.slot"),1,this.getConfig().getInt("Items.Armor.Gold.legs.cost"));
        createPurchaseItem(Material.GOLD_HELMET,armorMenu,this.getConfig().getInt("Items.Armor.Gold.feet.slot"),1,this.getConfig().getInt("Items.Armor.Gold.feet.cost"));
        // Create Diamond Armor Set.
        createPurchaseItem(Material.DIAMOND_HELMET,armorMenu,this.getConfig().getInt("Items.Armor.Diamond.head.slot"),1,this.getConfig().getInt("Items.Armor.Diamond.head.cost"));
        createPurchaseItem(Material.DIAMOND_HELMET,armorMenu,this.getConfig().getInt("Items.Armor.Diamond.chest.slot"),1,this.getConfig().getInt("Items.Armor.Diamond.chest.cost"));
        createPurchaseItem(Material.DIAMOND_HELMET,armorMenu,this.getConfig().getInt("Items.Armor.Diamond.legs.slot"),1,this.getConfig().getInt("Items.Armor.Diamond.legs.cost"));
        createPurchaseItem(Material.DIAMOND_HELMET,armorMenu,this.getConfig().getInt("Items.Armor.Diamond.feet.slot"),1,this.getConfig().getInt("Items.Armor.Diamond.feet.cost"));
    }

    /**
     * Creates all the contents of the Weapons Shop
     */
    public void createWeaponMenu() {
        // TODO
    }

    /**
     * Creates all the contents of the Tools Shop
     */
    public void createToolsMenu() {
        // TODO
    }

    /**
     * Creates all the contents of the Ore Shop
     */
    public void createOreMenu() {
        // TODO
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
