package com.gmail.markushygedombrowski.vagtvault.edit;

import com.gmail.markushygedombrowski.HLVagtVault;
import com.gmail.markushygedombrowski.config.VagtVault;
import com.gmail.markushygedombrowski.config.VagtVaultLoader;
import com.gmail.markushygedombrowski.items.RareItems;
import me.arcaniax.hdb.api.HeadDatabaseAPI;
import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.Wool;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Additems implements Listener {
    private final int ADDITEMS_INDEX = 1;
    private final int ADDHEAD_INDEX = 7;
    private final int ADDRAREITEMS_INDEX = 3;
    private final int ADDRAREHEADS_INDEX = 5;
    private final int BACK_INDEX = 4;
    private VagtVaultLoader vagtVaultLoader;
    private HeadDatabaseAPI api = new HeadDatabaseAPI();
    private HLVagtVault plugin;
    private EditRareItem editRareItem;
    private HashMap<Player, RareItems> rareItemsHashMap = new HashMap<>();
    private HashMap<Player, Inventory> inventoryHashMap = new HashMap<>();
    private static final String ADD_NORMAL_ITEMS_TITLE = "§aAdd Normal Items: ";
    private static final String ADD_HEADS_TITLE = "§aAdd Heads: ";
    private static final String ADD_RARE_ITEMS_TITLE = "§aAdd Rare Items: ";
    private static final String ADD_RARE_HEADS_TITLE = "§aAdd Rare Heads: ";
    private static final String SET_CHANCE_TITLE = "§aSet Chance: ";

    public Additems(VagtVaultLoader vagtVaultLoader, HLVagtVault plugin, EditRareItem editRareItem) {
        this.vagtVaultLoader = vagtVaultLoader;
        this.plugin = plugin;
        this.editRareItem = editRareItem;
    }

    public void create(Player player, VagtVault vagtVault, Inventory backInv) {
        Inventory inv = Bukkit.createInventory(player, 9, "§aAdd Items: " + vagtVault.getName());
        inventoryHashMap.put(player, backInv);
        inv.setItem(BACK_INDEX, createItem("9866", "§cTilbage"));
        inv.setItem(ADDITEMS_INDEX, createItem("54200", "§aTilføj Items"));
        inv.setItem(ADDHEAD_INDEX, createItem("38139", "§aTilføj §eHeads"));
        inv.setItem(ADDRAREITEMS_INDEX, createItem("50099", "§aTilføj §9Rare Items"));
        inv.setItem(ADDRAREHEADS_INDEX, createItem("7888", "§aTilføj §6Rare Heads"));
        player.openInventory(inv);
    }

    private ItemStack createItem(String headId, String displayName) {
        ItemStack item = api.getItemHead(headId);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(displayName);
        item.setItemMeta(meta);
        return item;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        Inventory inv = event.getInventory();
        int slot = event.getRawSlot();
        ItemStack item = event.getCurrentItem();
        if (item == null || !inv.getTitle().contains("§aAdd Items: ")) {
            return;
        }
        event.setCancelled(true);
        event.setResult(InventoryClickEvent.Result.DENY);
        VagtVault vagtVault = vagtVaultLoader.getVagtVault(inv.getName().replace("§aAdd Items: ", ""));
        switch (slot) {
            case ADDITEMS_INDEX:
                openInventory(player, vagtVault, ADD_NORMAL_ITEMS_TITLE);
                break;
            case ADDHEAD_INDEX:
                openInventory(player, vagtVault, ADD_HEADS_TITLE);
                break;
            case ADDRAREITEMS_INDEX:
                openInventory(player, vagtVault, ADD_RARE_ITEMS_TITLE);
                break;
            case ADDRAREHEADS_INDEX:
                openInventory(player, vagtVault, ADD_RARE_HEADS_TITLE);
                break;
            case BACK_INDEX:
                player.openInventory(inventoryHashMap.get(player));
                break;
        }
    }

    public void openInventory(Player player, VagtVault vagtVault, String title) {
        Inventory inv = Bukkit.createInventory(player, 9, title + vagtVault.getName());
        player.openInventory(inv);
    }


    @EventHandler
    public void onInventoryClickAddItems(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        Inventory inv = event.getView().getTopInventory();
        Inventory clickedInv = event.getClickedInventory();
        ItemStack item = event.getCurrentItem();

        if (item == null) return;

        if (inv.getTitle().contains("§aAdd Heads: ")) {
            handleAddItems(event, player, inv, clickedInv, item, "heads");
        } else if (inv.getTitle().contains("§aAdd Normal Items: ")) {
            handleAddItems(event, player, inv, clickedInv, item, "items");
        } else if (inv.getTitle().contains("§aAdd Rare Heads: ")) {
            handleAddItems(event, player, inv, clickedInv, item, "Rare heads");
        } else if (inv.getTitle().contains("§aAdd Rare Items: ")) {
            handleAddItems(event, player, inv, clickedInv, item, "rareitems");
        }
    }

    private void handleAddItems(InventoryClickEvent event, Player player, Inventory inv, Inventory clickedInv,ItemStack item, String type) {
        event.setCancelled(true);
        if((item.getType().toString().contains("SKULL") && !type.contains("heads")) || (!item.getType().toString().contains("SKULL") && type.contains("heads"))) {
            player.sendMessage("§cDu kan kun tilføje " + type);
            return;
        }
        if(item.getType() == Material.AIR) {
            player.sendMessage("§cDu kan kun tilføje " + type);
            return;
        }
        if(type.equals("rareitems")) {
            handleAddRareItems(player, inv, clickedInv, item);
            return;
        }
        player.getInventory().remove(item);
        inv.addItem(item);
        player.sendMessage("§aDu har tilføjet et §e" + type);
    }

    private void handleAddRareItems(Player player, Inventory inv, Inventory clickedInv, ItemStack item) {
        VagtVault vagtVault = vagtVaultLoader.getVagtVault(inv.getName().replace("§aAdd Rare Items: ", ""));
        clickedInv.remove(item);
        inv.addItem(item);
        player.sendMessage("§aDu har tilføjet et §9Rare Item");
        RareItems rareItems = new RareItems(0.001, item);
        rareItemsHashMap.put(player, rareItems);
        editRareItem.setChanceGUI(player, vagtVault, rareItems, inv);
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        Inventory inv = event.getInventory();
        Player player = (Player) event.getPlayer();
        String title = inv.getTitle();

        if (title.startsWith(ADD_HEADS_TITLE)) {
            System.out.println("heads");
            handleNormalItemsClose(inv, player, "heads");
        } else if (title.startsWith(ADD_NORMAL_ITEMS_TITLE)) {
            System.out.println("items");
            handleNormalItemsClose(inv, player, "items");
        } else if (title.startsWith(ADD_RARE_HEADS_TITLE)) {
            System.out.println("rareHeads");
            handleNormalItemsClose(inv, player,"rareHeads");
        } else if (title.startsWith(ADD_RARE_ITEMS_TITLE)) {
            System.out.println("rareItems");
            handleRareItemsClose(inv, player);
        } else if (title.startsWith(SET_CHANCE_TITLE)) {
            rareItemsHashMap.remove(player);
        }

    }

    public void handleRareItemsClose(Inventory inv, Player player) {
        if (rareItemsHashMap.containsKey(player)) {
            return;
        }
        back(player, vagtVaultLoader.getVagtVault(inv.getTitle().replace("§aAdd Rare Items: ", "")));
    }



    private void handleNormalItemsClose(Inventory inv, Player player, String type) {
        String title = inv.getTitle();
        String vvName = title.substring(title.indexOf(":") + 1).trim();
        VagtVault vagtVault = vagtVaultLoader.getVagtVault(vvName);
        if (vagtVault == null) {
            player.sendMessage("§cvagtVault is null when adding items or heads");
            return;
        }
        List<ItemStack> items = new ArrayList<>();
        for (ItemStack item : inv.getContents()) {
            if (item == null) {
                continue;
            }
            if (item.getType() == Material.AIR) {
                continue;
            }
            if (item.getType().toString().contains("SKULL")) {
                if (type.equals("heads") || type.equals("rareHeads")) {
                    items.add(item);
                    continue;
                }
                continue;
            }
            items.add(item);

        }
        switch (type) {
            case "heads":
                vagtVault.addHeads(items);
                break;
            case "rareHeads":
                vagtVault.addRareHeads(items);
                break;
            case "items":
                vagtVault.addItems(items);
                break;
        }
        vagtVaultLoader.save(vagtVault);
        back(player, vagtVault);
    }

    private void back(Player player, VagtVault vagtVault) {
        Bukkit.getScheduler().runTaskLater(plugin, () -> create(player, vagtVault, inventoryHashMap.get(player)), 1L);
    }
}
