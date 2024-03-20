package com.gmail.markushygedombrowski.vagtvault;

import com.gmail.markushygedombrowski.HLVagtVault;
import com.gmail.markushygedombrowski.config.VagtVault;
import com.gmail.markushygedombrowski.config.VagtVaultLoader;
import me.arcaniax.hdb.api.HeadDatabaseAPI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class Additems implements Listener {
    private final int ADDITEMS_INDEX = 1;
    private final int ADDHEAD_INDEX = 7;
    private final int ADDRAREITEMS_INDEX = 3;
    private final int ADDRAREHEADS_INDEX = 5;
    private VagtVaultLoader vagtVaultLoader;
    private HeadDatabaseAPI api = new HeadDatabaseAPI();
    private HLVagtVault plugin;

    public Additems(VagtVaultLoader vagtVaultLoader, HLVagtVault plugin) {
        this.vagtVaultLoader = vagtVaultLoader;
        this.plugin = plugin;
    }

    public void create(Player player, VagtVault vagtVault) {
        Inventory inv = Bukkit.createInventory(player, 9, "§aAdd Items: " + vagtVault.getName());
        ItemStack addItems = api.getItemHead("54200");
        ItemStack addHeads = api.getItemHead("38139");
        ItemStack addRareItems = api.getItemHead("50099");
        ItemStack addRareHeads = api.getItemHead("7888");
        ItemMeta addItemsMeta = addItems.getItemMeta();
        ItemMeta addHeadsMeta = addHeads.getItemMeta();
        ItemMeta addRareItemsMeta = addRareItems.getItemMeta();
        ItemMeta addRareHeadsMeta = addRareHeads.getItemMeta();
        addItemsMeta.setDisplayName("§aTilføj Items");
        addHeadsMeta.setDisplayName("§aTilføj §eHeads");
        addRareItemsMeta.setDisplayName("§aTilføj §9Rare Items");
        addRareHeadsMeta.setDisplayName("§aTilføj §6Rare Heads");
        addItems.setItemMeta(addItemsMeta);
        addHeads.setItemMeta(addHeadsMeta);
        addRareItems.setItemMeta(addRareItemsMeta);
        addRareHeads.setItemMeta(addRareHeadsMeta);
        inv.setItem(ADDITEMS_INDEX, addItems);
        inv.setItem(ADDHEAD_INDEX, addHeads);
        inv.setItem(ADDRAREITEMS_INDEX, addRareItems);
        inv.setItem(ADDRAREHEADS_INDEX, addRareHeads);
        player.openInventory(inv);

    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        Inventory inv = event.getInventory();
        int slot = event.getRawSlot();
        ItemStack item = event.getCurrentItem();
        if (item == null) {
            return;
        }
        if (inv.getTitle().contains("§aAdd Items: ")) {
            event.setCancelled(true);
            event.setResult(InventoryClickEvent.Result.DENY);
            VagtVault vagtVault = vagtVaultLoader.getVagtVault(inv.getName().replace("§aAdd Items: ", ""));
            if (slot == ADDITEMS_INDEX) {
                additems(player, vagtVault);
                return;
            }
            if (slot == ADDHEAD_INDEX) {
                addheads(player, vagtVault);
                return;
            }
            if (slot == ADDRAREITEMS_INDEX) {
                addrareitems(player, vagtVault);
                return;
            }
            if (slot == ADDRAREHEADS_INDEX) {
                addrareheads(player, vagtVault);
                return;
            }
        }
    }

    public void additems(Player player, VagtVault vagtVault) {
        Inventory inv = Bukkit.createInventory(player, 9, "§aAdd Normal Items: " + vagtVault.getName());
        player.openInventory(inv);
    }

    public void addheads(Player player, VagtVault vagtVault) {
        Inventory inv = Bukkit.createInventory(player, 9, "§aAdd Heads: " + vagtVault.getName());
        player.openInventory(inv);
    }

    public void addrareitems(Player player, VagtVault vagtVault) {
        Inventory inv = Bukkit.createInventory(player, 9, "§aAdd Rare Items: " + vagtVault.getName());
        player.openInventory(inv);
    }

    public void addrareheads(Player player, VagtVault vagtVault) {
        Inventory inv = Bukkit.createInventory(player, 9, "§aAdd Rare Heads: " + vagtVault.getName());
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
            clickedInv.remove(item);
            inv.addItem(item);
            player.sendMessage("§aDu har tilføjet et §9Rare Item");
            player.sendMessage("§aSkriv en procent chance for at få det");
            player.setMetadata("rareItem", new org.bukkit.metadata.FixedMetadataValue(plugin, item));


        }

    }
    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        if (!player.hasMetadata("rareItem")) {
            return;
        }
        event.setCancelled(true);
        String message = event.getMessage();
        if(!message.matches("[0-9]+")) {
            player.sendMessage("§cDu skal skrive et tal");
            return;
        }
        double chance = Double.parseDouble(message);
        ItemStack item = (ItemStack) player.getMetadata("rareItem").get(0).value();
        VagtVault vagtVault = vagtVaultLoader.getVagtVault(player.getOpenInventory().getTitle().replace("§aAdd Rare Items: ", ""));

        player.removeMetadata("rareItem", plugin);
        player.sendMessage("§aDu har tilføjet et §9Rare Item med en chance på " + chance + "%");
        player.closeInventory();

    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        Inventory inv = event.getInventory();
        Player player = (Player) event.getPlayer();
        if (inv.getName().contains("§aAdd Heads: ")) {
            if (closeHeads(inv, player)) return;
        }
        if (inv.getName().contains("§aAdd Normal Items: ")) {
            if (closeNormalItems(inv, player)) return;
        }

        if (inv.getName().contains("§aAdd Rare Heads: ")) {
            closeRareHeads(inv, player);
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
        Bukkit.getScheduler().runTaskLater(plugin, () -> create(player, vagtVault), 1L);
    }
}
