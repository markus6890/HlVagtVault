package com.gmail.markushygedombrowski.vagtvault.edit;

import com.gmail.markushygedombrowski.HLVagtVault;
import com.gmail.markushygedombrowski.config.VagtVault;
import me.arcaniax.hdb.api.HeadDatabaseAPI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SetTimes implements Listener {
    private final int SET_STAND_STILL_INDEX = 2;
    private final int SET_RESET_TIME_INDEX = 6;
    private final int SET_FAIL_TIME_INDEX = 4;
    private final int BACK_INDEX = 8;
    private HeadDatabaseAPI api = new HeadDatabaseAPI();
    private HashMap<Player, Inventory> inventoryHashMap = new HashMap<>();
    private HashMap<Player,VagtVault> vagtVaultHashMap = new HashMap<>();
    private HLVagtVault plugin;

    public SetTimes(HLVagtVault plugin) {
        this.plugin = plugin;
    }

    public void create(Player player, VagtVault vagtVault, Inventory backInv) {
        Inventory inv = Bukkit.createInventory(player, 9, "§eSæt Tider: " + vagtVault.getName());
        ItemStack standStillItem = createItem("49566", "§3Sæt Så Stille Tid");
        ItemStack resetTimeItem = createItem("32586", "§6Sæt Reset Tid");
        ItemStack failTimeItem = createItem("36123", "§cSæt Fail Tid");
        ItemMeta standStillMeta = standStillItem.getItemMeta();
        ItemMeta resetTimeMeta = resetTimeItem.getItemMeta();
        ItemMeta failTimeMeta = failTimeItem.getItemMeta();
        List<String> lore = new ArrayList<>();
        lore.add("§7Klik her for at sætte tiden");
        lore.add("§aNuværende tid: " + vagtVault.getStandStillTime() + " sekunder");
        standStillMeta.setLore(lore);
        standStillItem.setItemMeta(standStillMeta);
        lore.set(1, "§aNuværende tid: " + vagtVault.getResetTime() + " minutter");
        resetTimeMeta.setLore(lore);
        resetTimeItem.setItemMeta(resetTimeMeta);
        lore.set(1, "§aNuværende tid: " + vagtVault.getFailresetTime() + " minutter");
        failTimeMeta.setLore(lore);
        failTimeItem.setItemMeta(failTimeMeta);

        inv.setItem(SET_STAND_STILL_INDEX, standStillItem);
        inv.setItem(SET_RESET_TIME_INDEX, resetTimeItem);
        inv.setItem(SET_FAIL_TIME_INDEX, failTimeItem);
        inv.setItem(BACK_INDEX, createItem("9866", "§cTilbage"));

        inventoryHashMap.put(player, backInv);
        vagtVaultHashMap.put(player, vagtVault);
        player.openInventory(inv);
    }
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        Inventory inv = event.getInventory();
        int slot = event.getRawSlot();
        ItemStack item = event.getCurrentItem();
        if (item == null || !inv.getTitle().contains("§eSæt Tider: ")) {
            return;
        }
        event.setCancelled(true);
        event.setResult(InventoryClickEvent.Result.DENY);
        String metadataValue;
        String timeFormat;
        if (slot == BACK_INDEX) {
            player.openInventory(inventoryHashMap.get(player));
            return;
        }
        if (slot == SET_STAND_STILL_INDEX) {
            metadataValue = "standStillTime";
            timeFormat = "sekunder";
        } else if (slot == SET_RESET_TIME_INDEX) {
            metadataValue = "resetTime";
            timeFormat = "minutter";
        } else if (slot == SET_FAIL_TIME_INDEX) {
            metadataValue = "failresetTime";
            timeFormat = "minutter";
        } else {
            return;
        }
        player.setMetadata(metadataValue, new FixedMetadataValue(plugin, metadataValue));
        player.sendMessage("§aSkriv tiden i chatten i " + timeFormat);
        player.closeInventory();
    }

    @EventHandler
    public void onChatMessage(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        if (player.hasMetadata("standStillTime")) {
            VagtVault vagtVault = vagtVaultHashMap.get(player);
            vagtVault.setStandStillTime(Integer.parseInt(event.getMessage()));
            player.removeMetadata("standStillTime", plugin);
            create(player, vagtVault, inventoryHashMap.get(player));
        } else if (player.hasMetadata("resetTime")) {
            VagtVault vagtVault = vagtVaultHashMap.get(player);
            vagtVault.setResetTime(Integer.parseInt(event.getMessage()));
            player.removeMetadata("resetTime", plugin);
            create(player, vagtVault, inventoryHashMap.get(player));
        } else if (player.hasMetadata("failresetTime")) {
            VagtVault vagtVault = vagtVaultHashMap.get(player);
            vagtVault.setFailresetTime(Integer.parseInt(event.getMessage()));
            player.removeMetadata("failresetTime", plugin);
            create(player, vagtVault, inventoryHashMap.get(player));
        }
    }

    private ItemStack createItem(String headId, String displayName) {
        ItemStack item = api.getItemHead(headId);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(displayName);
        item.setItemMeta(meta);
        return item;
    }
}
