package com.gmail.markushygedombrowski.vagtvault.edit;

import com.gmail.markushygedombrowski.HLVagtVault;
import com.gmail.markushygedombrowski.config.VagtVault;
import com.gmail.markushygedombrowski.config.VagtVaultLoader;
import com.gmail.markushygedombrowski.items.RareItems;
import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.Wool;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SetHeadChanceGUI implements Listener {
    private HashMap<Player, Inventory> inventoryHashMap = new HashMap<>();
    private HashMap<Player, String> headTypeHashMap = new HashMap<>();
    private HashMap<Player, VagtVault> vagtVaultHashMap = new HashMap<>();

    private VagtVaultLoader vagtVaultLoader;
    private HLVagtVault plugin;

    public SetHeadChanceGUI(VagtVaultLoader vagtVaultLoader, HLVagtVault plugin) {
        this.vagtVaultLoader = vagtVaultLoader;
        this.plugin = plugin;
    }

    public void setChanceGUI(Player player, VagtVault vagtVault, Inventory backInv, String headType) {
        Inventory inv = Bukkit.createInventory(player, 9, "§aSet " + headType + " Head Chance: " + vagtVault.getName());
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
        setChanceMeta.setDisplayName("§aAdd Chance 0.01%");
        addChanceMeta.setDisplayName("§aAdd Chance 0.1%");
        removeChanceMeta.setDisplayName("§cRemove Chance 0.01%");
        removeChanceMeta1.setDisplayName("§cRemove Chance 0.1%");
        confirmMeta.setDisplayName("§aConfirm");

        double chance;
        if (headType.equalsIgnoreCase("Rare")) {
            chance = vagtVault.getRareHeadChance();
        } else {
            chance = vagtVault.getHeadChance();
        }
        List<String> lore = new ArrayList<>();
        lore.add("§aChance: " + chance + "%");
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
        headTypeHashMap.put(player, headType);
        vagtVaultHashMap.put(player, vagtVault);
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
        if (inv.getTitle().contains(" Head Chance: ")) {
            event.setCancelled(true);
            event.setResult(InventoryClickEvent.Result.DENY);
            VagtVault vagtVault = vagtVaultHashMap.get(player);
            String headType = headTypeHashMap.get(player);
            double chance;
            double chancechange = 0;
            if (headType.equalsIgnoreCase("Rare")) {
                chance = vagtVault.getRareHeadChance();
            } else {
                chance = vagtVault.getHeadChance();
            }

            if (slot == 1) {
                setChance(vagtVault, headType, chance + 0.1);
            } else if (slot == 2) {
                setChance(vagtVault, headType, chance + 0.01);
            } else if (slot == 6) {
                if (chance - 0.01 < 0) {
                    return;
                }
                setChance(vagtVault, headType, chance - 0.01);
            } else if (slot == 7) {
                if (chance - 0.1 < 0) {
                    return;
                }
                setChance(vagtVault, headType, chance - 0.1);
            }
            if (slot == 4) {
                player.sendMessage("§aDu har ændret på §6" + headType + " Head chance til " + chance + "%");
                headTypeHashMap.remove(player);
                vagtVaultLoader.save(vagtVault);
                vagtVaultHashMap.remove(player);
                back(player);
                return;
            }
            ItemStack confirm = inv.getItem(4);
            ItemMeta meta = confirm.getItemMeta();
            List<String> lore = new ArrayList<>();
            if (headType.equalsIgnoreCase("Rare")) {
                lore.add("§aChance: " + vagtVault.getRareHeadChance() + "%");
            } else {
                lore.add("§aChance: " + vagtVault.getHeadChance() + "%");
            }
            meta.setLore(lore);
            confirm.setItemMeta(meta);
            inv.setItem(4, confirm);
            player.updateInventory();


        }
    }

    private void setChance(VagtVault vagtVault, String headType, double chance) {
        double roundOff = Math.round(chance * 100.0) / 100.0;
        if(headType.equalsIgnoreCase("Rare")) {
            vagtVault.setRareHeadChance(roundOff);
        } else {
            vagtVault.setHeadChance(roundOff);
        }
    }
    private void back(Player player) {
        Bukkit.getScheduler().runTaskLater(plugin, () -> player.openInventory(inventoryHashMap.get(player)), 1L);
    }
}
