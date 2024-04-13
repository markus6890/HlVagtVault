package com.gmail.markushygedombrowski.vagtvault.edit;

import com.gmail.markushygedombrowski.config.VagtVault;
import com.gmail.markushygedombrowski.config.VagtVaultLoader;
import com.gmail.markushygedombrowski.config.VagtVaultManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

public class OtherSettings implements Listener {
    private VagtVaultLoader vagtVaultLoader;
    private final int SET_PLAYER_MESSAGE_INDEX = 2;
    private final int SEND_BOARDCAST_INDEX = 6;
    private final int BACK_INDEX = 8;
    private final int SET_BROADCAST_MESSAGE_INDEX = 4;
    private HashMap<Player, Inventory> inventoryHashMap = new HashMap<>();

    public OtherSettings(VagtVaultLoader vagtVaultLoader) {
        this.vagtVaultLoader = vagtVaultLoader;
    }

    public void create(Player player, VagtVault vagtVault,Inventory backInv) {
        Inventory inv = Bukkit.createInventory(player, 9, "§eAndre Indstillinger: " + vagtVault.getName());
        inv.setItem(SET_PLAYER_MESSAGE_INDEX, vagtVaultLoader.createItem("34470", "§3Se Spiller Besked"));
        inv.setItem(SET_BROADCAST_MESSAGE_INDEX, vagtVaultLoader.createItem("3247", "§6Se Broadcast Besked"));
        inv.setItem(BACK_INDEX, vagtVaultLoader.createItem("9866", "§cTilbage"));
        if(vagtVault.isSendBoardcast()) {
            inv.setItem(SEND_BOARDCAST_INDEX, vagtVaultLoader.createItem("33383", "§3Skal Broadcast Besked Sendes"));
        } else {
            inv.setItem(SEND_BOARDCAST_INDEX, vagtVaultLoader.createItem("22835", "§3Skal Broadcast Besked Sendes"));
        }
        inventoryHashMap.put(player, backInv);
        player.openInventory(inv);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        Inventory inv = event.getInventory();
        int slot = event.getRawSlot();
        ItemStack item = event.getCurrentItem();
        if (item == null || !inv.getTitle().contains("§eAndre Indstillinger: ")){
            return;
        }
        event.setCancelled(true);
        event.setResult(InventoryClickEvent.Result.DENY);
        VagtVault vagtVault = vagtVaultLoader.getVagtVault(inv.getTitle().replace("§eAndre Indstillinger: ", ""));
        if (item.getType() == null || item.getType() == Material.AIR) {
            return;
        }
        if (slot == SET_PLAYER_MESSAGE_INDEX) {
            player.sendMessage(vagtVault.getPlayerMessage(player));
        } else if (slot == SET_BROADCAST_MESSAGE_INDEX) {
            player.sendMessage(vagtVault.getBoardcastMessage());
        } else if (slot == SEND_BOARDCAST_INDEX) {
            vagtVault.setSendBoardcast(!vagtVault.isSendBoardcast());
            vagtVaultLoader.save(vagtVault);
            create(player, vagtVault, inventoryHashMap.get(player));
        } else if (slot == BACK_INDEX) {
            player.openInventory(inventoryHashMap.get(player));
        }

    }
}
