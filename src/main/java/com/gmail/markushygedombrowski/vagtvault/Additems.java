package com.gmail.markushygedombrowski.vagtvault;

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
    private HashMap<Player, RareItems> rareItemsHashMap = new HashMap<>();
    private HashMap<Player,Inventory> inventoryHashMap = new HashMap<>();

    public Additems(VagtVaultLoader vagtVaultLoader, HLVagtVault plugin) {
        this.vagtVaultLoader = vagtVaultLoader;
        this.plugin = plugin;
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
        if (slot == ADDITEMS_INDEX) {
            openInventory(player, vagtVault, "§aAdd Normal Items: ");
        } else if (slot == ADDHEAD_INDEX) {
            openInventory(player, vagtVault, "§aAdd Heads: ");
        } else if (slot == ADDRAREITEMS_INDEX) {
            openInventory(player, vagtVault, "§aAdd Rare Items: ");
        } else if (slot == ADDRAREHEADS_INDEX) {
            openInventory(player, vagtVault, "§aAdd Rare Heads: ");
        } else if (slot == BACK_INDEX) {
            player.openInventory(inventoryHashMap.get(player));
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
        int slot = event.getRawSlot();
        ItemStack item = event.getCurrentItem();

        if (item == null) {
            return;
        }
        if (inv.getTitle().contains("§aAdd Heads: ")) {
            event.setCancelled(true);
            event.setResult(InventoryClickEvent.Result.DENY);
            clickedInv.remove(item);
            inv.addItem(item);
            player.sendMessage("§aDu har tilføjet et §eHead");

        } else if (inv.getTitle().contains("§aAdd Normal Items: ")) {
            event.setCancelled(true);
            event.setResult(InventoryClickEvent.Result.DENY);
            clickedInv.remove(item);
            inv.addItem(item);
            player.sendMessage("§aDu har tilføjet et §eItem");

        } else if (inv.getTitle().contains("§aAdd Rare Heads: ")) {
            event.setCancelled(true);
            event.setResult(InventoryClickEvent.Result.DENY);
            clickedInv.remove(item);
            inv.addItem(item);
            player.sendMessage("§aDu har tilføjet et §6Rare Head");

        } else if (inv.getTitle().contains("§aAdd Rare Items: ")) {
            event.setCancelled(true);
            event.setResult(InventoryClickEvent.Result.DENY);
            VagtVault vagtVault = vagtVaultLoader.getVagtVault(inv.getName().replace("§aAdd Rare Items: ", ""));
            clickedInv.remove(item);
            inv.addItem(item);
            player.sendMessage("§aDu har tilføjet et §9Rare Item");
            RareItems rareItems = new RareItems(0.001, item);
            rareItemsHashMap.put(player, rareItems);
            setChanceGUI(player, vagtVault);


        }

    }

    public void setChanceGUI(Player player, VagtVault vagtVault) {
        Inventory inv = Bukkit.createInventory(player, 9, "§aSet Chance: " + vagtVault.getName());
        RareItems rareItems = rareItemsHashMap.get(player);
        Wool wool = new Wool(DyeColor.GREEN);
        Wool wool1 = new Wool(DyeColor.RED);
        ItemStack setChance = wool.toItemStack(1);
        ItemStack removeChance = wool1.toItemStack(1);
        ItemStack confirm = new ItemStack(Material.EMERALD_BLOCK);

        ItemMeta setChanceMeta = setChance.getItemMeta();
        ItemMeta removeChanceMeta = removeChance.getItemMeta();
        ItemMeta confirmMeta = confirm.getItemMeta();
        setChanceMeta.setDisplayName("§add Chance 0.001%");
        removeChanceMeta.setDisplayName("§cRemove Chance 0.001%");
        confirmMeta.setDisplayName("§aConfirm");
        List<String> lore = new ArrayList<>();
        lore.add("§aChance: " + rareItems.getChance() + "%");
        confirmMeta.setLore(lore);
        setChance.setItemMeta(setChanceMeta);
        removeChance.setItemMeta(removeChanceMeta);
        confirm.setItemMeta(confirmMeta);
        inv.setItem(2, setChance);
        inv.setItem(4, confirm);
        inv.setItem(6, removeChance);
        player.openInventory(inv);
    }


    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        Inventory inv = event.getInventory();
        Player player = (Player) event.getPlayer();
        if (inv.getTitle().contains("§aAdd Heads: ")) {
            if (closeHeads(inv, player)) return;
        }
        if (inv.getTitle().contains("§aAdd Normal Items: ")) {
            if (closeNormalItems(inv, player)) return;
        }

        if (inv.getTitle().contains("§aAdd Rare Heads: ")) {
            closeRareHeads(inv, player);
        }
        if (inv.getTitle().contains("§aAdd Rare Items: ")) {
            back(player, vagtVaultLoader.getVagtVault(inv.getTitle().replace("§aAdd Rare Items: ", "")));
        }
        if (inv.getTitle().contains("§aSet Chance: ")) {
            rareItemsHashMap.remove(player);
        }

    }

    @EventHandler
    public void setChanceGUIListener(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        Inventory inv = event.getInventory();
        int slot = event.getRawSlot();
        ItemStack item = event.getCurrentItem();
        if (item == null) {
            return;
        }
        if (inv.getTitle().contains("§aSet Chance: ")) {
            event.setCancelled(true);
            event.setResult(InventoryClickEvent.Result.DENY);
            VagtVault vagtVault = vagtVaultLoader.getVagtVault(inv.getTitle().replace("§aSet Chance: ", ""));
            RareItems rareItems = rareItemsHashMap.get(player);
            double chance = rareItems.getChance();
            if (slot == 2) {
                rareItems.setChance(chance + 0.001);
            } else if (slot == 6) {
                rareItems.setChance(chance - 0.001);
            } else if (slot == 4) {
                List<RareItems> rareItemsList = new ArrayList<>();
                rareItemsList.add(rareItems);
                vagtVault.setRareItems(rareItemsList);
                player.sendMessage("§aDu har tilføjet et §9Rare Item med en chance på " + chance + "%");
                rareItemsHashMap.remove(player);
                back(player, vagtVault);
                return;
            }
            ItemStack confirm = inv.getItem(4);
            ItemMeta meta = confirm.getItemMeta();
            List<String> lore = new ArrayList<>();
            lore.add("§aChance: " + rareItems.getChance() + "%");
            meta.setLore(lore);
            confirm.setItemMeta(meta);
            inv.setItem(4, confirm);
            player.updateInventory();


        }
    }

    private boolean closeHeads(Inventory inv, Player player) {
        VagtVault vagtVault = vagtVaultLoader.getVagtVault(inv.getName().replace("§aAdd Heads: ", ""));
        if (vagtVault == null) {
            player.sendMessage("§cvagtVault is null when adding heads");
            return true;
        }
        List<ItemStack> heads = new ArrayList<>();
        for (ItemStack item : inv.getContents()) {
            if (item == null) {
                continue;
            }
            if (item.getType().toString().contains("SKULL")) {
                heads.add(item);
            }

        }
        vagtVault.setHeads(heads);
        back(player, vagtVault);
        return false;
    }

    private boolean closeNormalItems(Inventory inv, Player player) {
        VagtVault vagtVault = vagtVaultLoader.getVagtVault(inv.getName().replace("§aAdd Normal Items: ", ""));
        if (vagtVault == null) {
            player.sendMessage("§cvagtVault is null when adding items");
            return true;
        }
        List<ItemStack> items = new ArrayList<>();
        for (ItemStack item : inv.getContents()) {
            if (item == null) {
                continue;
            }
            if (!item.getType().toString().contains("SKULL")) {
                items.add(item);
            }

        }
        vagtVault.setItems(items);
        back(player, vagtVault);
        return false;
    }

    private void closeRareHeads(Inventory inv, Player player) {
        VagtVault vagtVault = vagtVaultLoader.getVagtVault(inv.getName().replace("§aAdd Rare Heads: ", ""));
        if (vagtVault == null) {
            player.sendMessage("§cvagtVault is null when adding rare heads");
            return;
        }
        List<ItemStack> rareHeads = new ArrayList<>();
        for (ItemStack item : inv.getContents()) {
            if (item == null) {
                continue;
            }
            if (item.getType().toString().contains("SKULL")) {
                rareHeads.add(item);
            }

        }
        vagtVault.setRareHeads(rareHeads);
        back(player, vagtVault);
    }

    private void back(Player player, VagtVault vagtVault) {
        Bukkit.getScheduler().runTaskLater(plugin, () -> create(player, vagtVault,inventoryHashMap.get(player)), 1L);
    }
}
