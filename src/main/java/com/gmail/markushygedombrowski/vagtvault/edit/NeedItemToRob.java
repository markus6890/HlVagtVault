package com.gmail.markushygedombrowski.vagtvault.edit;

import com.gmail.markushygedombrowski.config.VagtVault;
import com.gmail.markushygedombrowski.config.VagtVaultLoader;
import me.arcaniax.hdb.api.HeadDatabaseAPI;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;

public class NeedItemToRob implements Listener {
    private HeadDatabaseAPI api = new HeadDatabaseAPI();
    private VagtVaultLoader vagtVaultLoader;
    private final int NEED_ITEM_INDEX = 2;
    private final int SET_ITEM_INDEX = 6;
    private final int BACK_INDEX = 8;
    private HashMap<Player, Inventory> inventoryHashMap = new HashMap<>();

    public NeedItemToRob(VagtVaultLoader vagtVaultLoader) {
        this.vagtVaultLoader = vagtVaultLoader;
    }

    public void create(Player player, VagtVault vagtVault, Inventory backInv) {
        Inventory inv = Bukkit.createInventory(player, 9, "§eNeed Item To Rob " + vagtVault.getName());

        if (vagtVault.isNeedItem()) {
            inv.setItem(NEED_ITEM_INDEX, createItem("33383", "§aSkal Bruge Item"));
        } else {
            inv.setItem(NEED_ITEM_INDEX, createItem("22835", "§cSkal Bruge Item"));
        }
        inv.setItem(SET_ITEM_INDEX, createItem("3247", "§6Set Item I Hånd"));
        inv.setItem(BACK_INDEX, createItem("9866", "§cBack"));
        inventoryHashMap.put(player, backInv);
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
        if (item == null || !inv.getTitle().contains("§eNeed Item To Rob")) {
            return;
        }
        event.setCancelled(true);
        event.setResult(InventoryClickEvent.Result.DENY);
        if (item.getType() == null || item.getType() == Material.AIR) {
            return;
        }
        VagtVault vagtVault = vagtVaultLoader.getVagtVault(inv.getTitle().replace("§eNeed Item To Rob ", ""));
        if (slot == NEED_ITEM_INDEX) {
            vagtVault.setNeedItem(!vagtVault.isNeedItem());
            vagtVaultLoader.save(vagtVault);
            create(player, vagtVault, inventoryHashMap.get(player));

        } else if (slot == SET_ITEM_INDEX) {
            vagtVault.setRobberyItem(player.getItemInHand());
            player.closeInventory();
            player.sendMessage("§aItemet i din hånd er nu sat som itemet der skal bruges for at røve vven");
        } else if (slot == BACK_INDEX) {
            player.openInventory(inventoryHashMap.get(player));
        }
    }



}
