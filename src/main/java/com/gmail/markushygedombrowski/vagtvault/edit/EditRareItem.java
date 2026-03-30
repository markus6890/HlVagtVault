package com.gmail.markushygedombrowski.vagtvault.edit;

import com.gmail.markushygedombrowski.HLVagtVault;
import com.gmail.markushygedombrowski.config.VagtVault;
import com.gmail.markushygedombrowski.config.VagtVaultLoader;
import com.gmail.markushygedombrowski.items.RareItems;
import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.Wool;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class EditRareItem implements Listener {

    private VagtVaultLoader vagtVaultLoader;
    private HLVagtVault plugin;
    private HashMap<Player, RareItems> rareItemsHashMap = new HashMap<>();
    private HashMap<Player, Inventory> inventoryHashMap = new HashMap<>();

    public EditRareItem(VagtVaultLoader vagtVaultLoader, HLVagtVault plugin) {
        this.vagtVaultLoader = vagtVaultLoader;
        this.plugin = plugin;
    }

    public void setChanceGUI(Player player, VagtVault vagtVault, RareItems rareItems, Inventory backInv) {
        Inventory inv = Bukkit.createInventory(player, 9, "§aSet Rare Item Chance: " + vagtVault.getName());
        Wool wool = new Wool(DyeColor.GREEN);
        Wool wool1 = new Wool(DyeColor.RED);
        ItemStack setChance = wool.toItemStack(1);
        ItemStack addChance1 = wool.toItemStack(1);
        ItemStack removeChance = wool1.toItemStack(1);
        ItemStack removeChance1 = wool1.toItemStack(1);
        ItemStack confirm = new ItemStack(Material.EMERALD_BLOCK);

        ItemMeta setChanceMeta = setChance.getItemMeta();
        ItemMeta addChanceMeta = addChance1.getItemMeta();
        ItemMeta removeChanceMeta = removeChance.getItemMeta();
        ItemMeta removeChanceMeta1 = removeChance1.getItemMeta();
        ItemMeta confirmMeta = confirm.getItemMeta();
        setChanceMeta.setDisplayName("§aAdd Chance 0.001%");
        addChanceMeta.setDisplayName("§aAdd Chance 1%");
        removeChanceMeta.setDisplayName("§cRemove Chance 0.001%");
        removeChanceMeta1.setDisplayName("§cRemove Chance 1%");
        confirmMeta.setDisplayName("§aConfirm");
        List<String> lore = new ArrayList<>();
        lore.add("§aChance: " + rareItems.getChance() + "%");
        confirmMeta.setLore(lore);
        setChance.setItemMeta(setChanceMeta);
        addChance1.setItemMeta(addChanceMeta);
        removeChance.setItemMeta(removeChanceMeta);
        removeChance1.setItemMeta(removeChanceMeta1);
        confirm.setItemMeta(confirmMeta);
        inv.setItem(1, addChance1);
        inv.setItem(2, setChance);
        inv.setItem(4, confirm);
        inv.setItem(6, removeChance);
        inv.setItem(7, removeChance1);
        inventoryHashMap.put(player, backInv);
        rareItemsHashMap.put(player, rareItems);
        player.openInventory(inv);
    }

    @EventHandler
    public void setChanceGUIListener(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        Inventory inv = event.getInventory();
        InventoryView view = event.getView();
        int slot = event.getRawSlot();
        ItemStack item = event.getCurrentItem();
        if (item == null) {
            return;
        }
        if (view.getTitle().contains("§aSet Rare Item Chance: ")) {
            event.setCancelled(true);
            event.setResult(InventoryClickEvent.Result.DENY);
            VagtVault vagtVault = vagtVaultLoader.getVagtVault(view.getTitle().replace("§aSet Rare Item Chance: ", ""));
            RareItems rareItems = rareItemsHashMap.get(player);
            double chance = rareItems.getChance();
            if (slot == 1) {
                rareItems.setChance(chance + 1);
                upDateInventory(player, inv, rareItems);
            } else if (slot == 2) {
                rareItems.setChance(chance + 0.001);
            } else if (slot == 6) {
                rareItems.setChance(chance - 0.001);
            } else if (slot == 4) {
                vagtVault.addRareItem(rareItems);
                player.sendMessage("§aDu har tilføjet et §9Rare Item med en chance på " + chance + "%");
                rareItemsHashMap.remove(player);
                back(player);
                vagtVaultLoader.save(vagtVault);
                return;
            } else if (slot == 7) {
                if(chance - 1 < 0) {
                    return;
                }
                rareItems.setChance(chance - 1);
            }


        }
    }
    public void upDateInventory(Player player, Inventory inv, RareItems rareItems) {
        ItemStack confirm = inv.getItem(4);
        assert confirm != null;
        ItemMeta meta = confirm.getItemMeta();
        List<String> lore = new ArrayList<>();
        lore.add("§aChance: " + rareItems.getChance() + "%");
        meta.setLore(lore);
        confirm.setItemMeta(meta);
        inv.setItem(4, confirm);
        player.updateInventory();
    }


    private void back(Player player) {
        Bukkit.getScheduler().runTaskLater(plugin, () -> player.openInventory(inventoryHashMap.get(player)), 1L);
    }


}
