package com.gmail.markushygedombrowski.vagtvault;

import com.gmail.markushygedombrowski.config.VagtVault;
import com.gmail.markushygedombrowski.config.VagtVaultLoader;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class Create implements Listener{
    private final int ADD_ITEM = 2;
    private final int SET_RESETTIME = 6;
    private Additems additems;
    private VagtVaultLoader vagtVaultLoader;

    public Create(Additems additems, VagtVaultLoader vagtVaultLoader) {
        this.additems = additems;

        this.vagtVaultLoader = vagtVaultLoader;
    }

    public void create(Player player, String name) {
        Inventory inv = Bukkit.createInventory(player, 9, "§eVagtVault: " + name);
        ItemStack addItem = new ItemStack(Material.EMERALD);
        ItemStack setResetTime = new ItemStack(Material.WATCH);
        ItemMeta addItemMeta = addItem.getItemMeta();
        ItemMeta setResetTimeMeta = setResetTime.getItemMeta();
        addItemMeta.setDisplayName("§aTilføj item");
        setResetTimeMeta.setDisplayName("§aSæt reset tid");
        addItem.setItemMeta(addItemMeta);
        setResetTime.setItemMeta(setResetTimeMeta);
        inv.setItem(ADD_ITEM, addItem);
        inv.setItem(SET_RESETTIME, setResetTime);
        player.openInventory(inv);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        Inventory inv = event.getInventory();
        int slot = event.getRawSlot();
        ItemStack item = event.getCurrentItem();
        if(item == null) {
            return;
        }
        if(inv.getTitle().contains("§eVagtVault: ")) {
            event.setCancelled(true);
            event.setResult(InventoryClickEvent.Result.DENY);
            VagtVault vagtVault = new VagtVault(inv.getName().replace("§eVagtVault: ", ""), player.getLocation());
            vagtVaultLoader.save(vagtVault);
            if(slot == ADD_ITEM) {
                additems.create(player, vagtVault);
            }
            if(slot == SET_RESETTIME) {
                player.sendMessage("§aSæt reset tid");
            }
        }
    }

}
