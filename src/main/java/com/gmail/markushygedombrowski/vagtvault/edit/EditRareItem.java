package com.gmail.markushygedombrowski.vagtvault.edit;

import com.gmail.markushygedombrowski.HLVagtVault;
import com.gmail.markushygedombrowski.config.VagtVault;
import com.gmail.markushygedombrowski.config.VagtVaultLoader;
import com.gmail.markushygedombrowski.items.RareItems;
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

public class EditRareItem implements Listener {

    private VagtVaultLoader vagtVaultLoader;
    private HLVagtVault plugin;
    private HashMap<Player, RareItems> rareItemsHashMap = new HashMap<>();
    private HashMap<Player, Inventory> inventoryHashMap = new HashMap<>();
    public EditRareItem(VagtVaultLoader vagtVaultLoader, HLVagtVault plugin) {
        this.vagtVaultLoader = vagtVaultLoader;
        this.plugin = plugin;
    }

    public void setChanceGUI(Player player, VagtVault vagtVault,RareItems rareItems, Inventory backInv) {
        Inventory inv = Bukkit.createInventory(player, 9, "§aSet Chance: " + vagtVault.getName());

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
        inventoryHashMap.put(player, backInv);
        player.openInventory(inv);
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
                vagtVault.addRareItem(rareItems);
                player.sendMessage("§aDu har tilføjet et §9Rare Item med en chance på " + chance + "%");
                rareItemsHashMap.remove(player);
                back(player);
                vagtVaultLoader.save(vagtVault);
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




    private void back(Player player) {
        Bukkit.getScheduler().runTaskLater(plugin, () -> player.openInventory(inventoryHashMap.get(player)), 1L);
    }
}
