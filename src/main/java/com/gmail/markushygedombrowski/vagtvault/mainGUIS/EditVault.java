package com.gmail.markushygedombrowski.vagtvault.mainGUIS;

import com.gmail.markushygedombrowski.config.VagtVault;
import com.gmail.markushygedombrowski.config.VagtVaultLoader;
import com.gmail.markushygedombrowski.items.RareItems;
import com.gmail.markushygedombrowski.vagtvault.edit.Additems;
import com.gmail.markushygedombrowski.vagtvault.edit.EditRareItem;
import com.gmail.markushygedombrowski.vagtvault.edit.OtherSettings;
import com.gmail.markushygedombrowski.vagtvault.edit.SetTimes;
import me.arcaniax.hdb.api.HeadDatabaseAPI;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class EditVault implements Listener {
    private final int ITEM_INDEX = 11;
    private final int TIMES_INDEX = 15;
    private final int QUCIK_INFO_INDEX = 13;
    private final int CLOSE_INDEX = 22;
    private final HeadDatabaseAPI api = new HeadDatabaseAPI();
    private VagtVaultLoader vagtVaultLoader;
    private Additems additems;
    private SetTimes setTimes;
    private OtherSettings otherSettings;
    private EditRareItem editRareItem;
    public EditVault(VagtVaultLoader vagtVaultLoader, Additems additems, SetTimes setTimes, OtherSettings otherSettings, EditRareItem editRareItem) {
        this.vagtVaultLoader = vagtVaultLoader;
        this.additems = additems;
        this.setTimes = setTimes;
        this.otherSettings = otherSettings;
        this.editRareItem = editRareItem;
    }

    public void create(Player player, VagtVault vagtVault) {
        Inventory inv = Bukkit.createInventory(player, 27, "§6Edit VagtVault: " + vagtVault.getName());
        ItemStack item = createItem("1709", "§aItems");
        ItemStack times = createItem("2394", "§aTider");
        ItemStack quickInfo = createItem("38869", "§aQuick Info/Andre Indstillinger");
        ItemStack close = createItem("9357", "§cClose");

        ItemMeta quickInfoMeta = getQuickMeta(vagtVault, quickInfo);
        quickInfo.setItemMeta(quickInfoMeta);

        item.getItemMeta().setLore(Collections.singletonList("Klik her for at ændre items"));
        times.getItemMeta().setLore(Collections.singletonList("Klik her for at ændre tider"));
        close.getItemMeta().setLore(Collections.singletonList("Klik her for at lukke"));

        inv.setItem(ITEM_INDEX, item);
        inv.setItem(TIMES_INDEX, times);
        inv.setItem(QUCIK_INFO_INDEX, quickInfo);
        inv.setItem(CLOSE_INDEX, close);
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
        if (inv.getTitle().contains("Edit VagtVault: ")) {
            event.setCancelled(true);
            event.setResult(InventoryClickEvent.Result.DENY);
            if (item.getType() == null || item.getType() == Material.AIR) {
                return;
            }
            VagtVault vagtVault = vagtVaultLoader.getVagtVault(inv.getTitle().replace("§6Edit VagtVault: ", ""));
            if (slot == ITEM_INDEX) {
                items(player, vagtVault);
            } else if (slot == TIMES_INDEX) {
                setTimes.create(player, vagtVault, inv);
            } else if (slot == CLOSE_INDEX) {
                player.closeInventory();
            } else if(slot == QUCIK_INFO_INDEX) {
                otherSettings.create(player,vagtVault,inv);
            }

        } else if (inv.getTitle().contains("§aEdit Items and §6Heads: ")) {
            if (item.getType() == null || item.getType() == Material.AIR) {
                return;
            }
            event.setCancelled(true);
            event.setResult(InventoryClickEvent.Result.DENY);
            selectEdit(inv, slot, player);
        } else if (inv.getTitle().contains("§5Edit §7")) {
            if (item.getType() == null || item.getType() == Material.AIR) {
                return;
            }
            event.setCancelled(true);
            event.setResult(InventoryClickEvent.Result.DENY);
            editItemIntercaten(inv, slot, player, item,event);

        }
    }

    private void selectEdit(Inventory inv, int slot, Player player) {
        VagtVault vagtVault = vagtVaultLoader.getVagtVault(inv.getTitle().replace("§aEdit Items and §6Heads: ", ""));
        if (slot == 22) {
            create(player, vagtVault);
        } else if (slot == 10) {
            showItemsInVagtVault(player, vagtVault, "Items");
        } else if (slot == 16) {
            showItemsInVagtVault(player, vagtVault, "Heads");
        } else if (slot == 12) {
            showItemsInVagtVault(player, vagtVault, "Rare Items");
        } else if (slot == 14) {
            showItemsInVagtVault(player, vagtVault, "Rare Heads");
        }
    }

    private void editItemIntercaten(Inventory inv, int slot, Player player, ItemStack item, InventoryClickEvent event) {
        String title = inv.getTitle();
        String vvName = title.substring(title.indexOf(":") + 1).trim();
        VagtVault vagtVault = vagtVaultLoader.getVagtVault(vvName);
        if (slot == 45) {
            items(player, vagtVault);
        } else if (slot == 53) {
            additems.create(player, vagtVault, inv);
        } else {
            if (item.getItemMeta().getLore().contains("§7Klik for at §cFjerne")) {
                removeItem(title, inv, vagtVault, item, player);
            } else if (item.getItemMeta().getLore().contains("§bVenstre §7Klik for at §aRedigere")) {
                RareItems rareItems = vagtVault.getRareItem(item);
                if(event.isLeftClick()) {
                    editRareItem.setChanceGUI(player,vagtVault,rareItems,inv);
                } else if(event.isRightClick()) {
                    vagtVault.removeRareItem(rareItems);
                    vagtVaultLoader.save(vagtVault);
                }

            }
        }
    }

    private void removeItem(String title, Inventory inv, VagtVault vagtVault, ItemStack item, Player player) {
        String type = title.substring(title.indexOf("Edit §7") + 6, title.indexOf(":"));
        if (inv.getTitle().contains("Rare Heads")) {
            vagtVault.getRareHeads().remove(item);
        } else if (inv.getTitle().contains("Items")) {
            vagtVault.getItems().remove(item);
        } else if (inv.getTitle().contains("Heads")) {
            vagtVault.getHeads().remove(item);
        }
        vagtVaultLoader.save(vagtVault);
        showItemsInVagtVault(player, vagtVault, type);
    }


    public void items(Player player, VagtVault vagtVault) {
        Inventory inv = Bukkit.createInventory(player, 27, "§aEdit Items and §6Heads: " + vagtVault.getName());
        inv.setItem(22, createItem("9866", "§cTilbage"));
        inv.setItem(10, createItem("54200", "§aEdit Items"));
        inv.setItem(16, createItem("38139", "§aEdit §eHeads"));
        inv.setItem(12, createItem("50099", "§aEdit §9Rare Items"));
        inv.setItem(14, createItem("7888", "§aEdit §6Rare Heads"));
        player.openInventory(inv);
    }

    public void showItemsInVagtVault(Player p, VagtVault vagtVault, String type) {
        Inventory inv = Bukkit.createInventory(p, 54, "§5Edit §7" + type + ": " + vagtVault.getName());
        ItemStack back = createItem("9866", "§cTilbage");
        inv.setItem(45, back);
        ItemStack add = createItem("54200", "§aTilføj");
        add.getItemMeta().setLore(Collections.singletonList("§7Klik for at §aTilføje"));
        inv.setItem(53, add);

        List<ItemStack> items = new ArrayList<>();
        if (type.contains("Rare Items")) {
            setRareItemsInInv(p, vagtVault, inv);
            return;
        } else if (type.contains("Items")) {
            items = vagtVault.getItems();
        } else if (type.contains("Rare Heads")) {
            items = vagtVault.getRareHeads();
        } else if (type.contains("Heads")) {
            items = vagtVault.getHeads();
        }
        setItemsInInv(items, inv);
        p.openInventory(inv);
    }

    private void setItemsInInv(List<ItemStack> items, Inventory inv) {
        items.forEach(i -> {
            ItemMeta meta = i.getItemMeta();
            List<String> lore = new ArrayList<>();
            lore.add("§7Klik for at §cFjerne");
            meta.setLore(lore);
            i.setItemMeta(meta);
            inv.addItem(i);
        });
    }

    private void setRareItemsInInv(Player p, VagtVault vagtVault, Inventory inv) {
        vagtVault.getRareItems().forEach(i -> {
            ItemStack item = i.getItem();
            ItemMeta meta = item.getItemMeta();
            List<String> lore = new ArrayList<>();
            lore.add("§cChance: " + i.getChance() + "%");
            lore.add("§bVenstre §7Klik for at §aRedigere");
            lore.add("§cHøjre §7Klik for at §cFjerne");
            meta.setLore(lore);
            item.setItemMeta(meta);
            inv.addItem(item);
        });
        p.openInventory(inv);
    }


    private ItemMeta getQuickMeta(VagtVault vagtVault, ItemStack quickInfo) {
        ItemMeta quickInfoMeta = quickInfo.getItemMeta();
        List<String> lore = new ArrayList<>();
        lore.add("§a§lHurtig info");
        lore.add("Total amount of items: " + vagtVault.getItems().size());
        lore.add("Total amount of heads: " + vagtVault.getHeads().size());
        lore.add("Total amount of rare items: " + vagtVault.getRareItems().size());
        lore.add("Total amount of rare heads: " + vagtVault.getRareHeads().size());
        lore.add("Head chance: " + vagtVault.getHeadChance());
        lore.add("Rare head chance: " + vagtVault.getRareHeadChance());
        lore.add("Reset time: " + vagtVault.getResetTime() + " minutes");
        lore.add("Fail reset time: " + vagtVault.getFailresetTime() + " minutes");
        lore.add("Stand still time: " + vagtVault.getStandStillTime() + " seconds");
        lore.add("Last reset: " + vagtVault.getLastReset());
        quickInfoMeta.setLore(lore);
        return quickInfoMeta;
    }

    private ItemStack createItem(String headId, String displayName) {
        ItemStack item = api.getItemHead(headId);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(displayName);
        item.setItemMeta(meta);
        return item;
    }
}
