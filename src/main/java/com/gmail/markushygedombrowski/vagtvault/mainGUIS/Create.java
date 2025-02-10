package com.gmail.markushygedombrowski.vagtvault.mainGUIS;

import com.gmail.markushygedombrowski.HLVagtVault;
import com.gmail.markushygedombrowski.config.VagtVault;
import com.gmail.markushygedombrowski.config.VagtVaultLoader;
import com.gmail.markushygedombrowski.vagtvault.edit.Additems;
import com.gmail.markushygedombrowski.vagtvault.edit.OtherSettings;
import com.gmail.markushygedombrowski.vagtvault.edit.SetTimes;
import me.arcaniax.hdb.api.HeadDatabaseAPI;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.BlockIterator;

public class Create implements Listener {
    private final int ADD_ITEM = 2;
    private final int SET_RESETTIME = 6;
    private final int ANDRE = 4;
    private final int CREATEVAGTVAULT =22;
    private Additems additems;
    private VagtVaultLoader vagtVaultLoader;
    private HeadDatabaseAPI api = new HeadDatabaseAPI();
    private HLVagtVault plugin;
    private SetTimes setTimes;
    private OtherSettings otherSettings;
    public Create(Additems additems, VagtVaultLoader vagtVaultLoader, HLVagtVault plugin, SetTimes setTimes, OtherSettings otherSettings) {
        this.additems = additems;

        this.vagtVaultLoader = vagtVaultLoader;
        this.plugin = plugin;
        this.setTimes = setTimes;
        this.otherSettings = otherSettings;
    }

    public void create(Player player, String name) {
        Inventory inv = Bukkit.createInventory(player, 27, "§eVagtVault: " + name);
        ItemStack addItem = api.getItemHead("1709");
        ItemStack setResetTime = api.getItemHead("2394");
        ItemStack createVagtVault = api.getItemHead("21771");
        ItemStack other = api.getItemHead("38869");

        ItemMeta otherMeta = other.getItemMeta();
        ItemMeta addItemMeta = addItem.getItemMeta();
        ItemMeta setResetTimeMeta = setResetTime.getItemMeta();
        ItemMeta createVagtVaultMeta = createVagtVault.getItemMeta();
        otherMeta.setDisplayName("§aQuick Info/Andre Indstillinger");
        createVagtVaultMeta.setDisplayName("§2Opret Vagt Vault");
        addItemMeta.setDisplayName("§aTilføj Items");
        setResetTimeMeta.setDisplayName("§aSæt Tid");
        other.setItemMeta(otherMeta);
        createVagtVault.setItemMeta(createVagtVaultMeta);
        addItem.setItemMeta(addItemMeta);
        setResetTime.setItemMeta(setResetTimeMeta);
        inv.setItem(ANDRE, other);
        inv.setItem(CREATEVAGTVAULT, createVagtVault);
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
        if (item == null) {
            return;
        }
        if (inv.getTitle().contains("§eVagtVault: ")) {
            event.setCancelled(true);
            event.setResult(InventoryClickEvent.Result.DENY);
            VagtVault vagtVault;
            if (vagtVaultLoader.getVagtVault(inv.getName().replace("§eVagtVault: ", "")) == null) {
                vagtVault = new VagtVault(inv.getName().replace("§eVagtVault: ", ""), getTargetBlock(player, 5).getLocation(), 30, 10, 10);

                vagtVaultLoader.save(vagtVault);
            } else {
                vagtVault = vagtVaultLoader.getVagtVault(inv.getName().replace("§eVagtVault: ", ""));
            }
            if (slot == ADD_ITEM) {
                additems.create(player, vagtVault, inv);
            }
            if (slot == SET_RESETTIME) {
                setTimes.create(player, vagtVault, inv);
            }
            if (slot == CREATEVAGTVAULT) {
                if (vagtVault.canBeCreated()) {
                    player.sendMessage("§aVagtVault oprettet");
                    vagtVault.fixNullLists();
                    vagtVaultLoader.save(vagtVault);
                    player.closeInventory();
                } else {
                    player.sendMessage("§cVagtVault kan ikke oprettes");
                }
            }
            if(slot == ANDRE) {
                otherSettings.create(player,vagtVault,inv);
            }
        }
    }



    public Block getTargetBlock(Player player, int range) {
        BlockIterator iter = new BlockIterator(player, range);
        Block lastBlock = iter.next();
        while (iter.hasNext()) {
            lastBlock = iter.next();
            if (lastBlock.getType() == Material.AIR) {
                continue;
            }
            break;
        }
        return lastBlock;
    }


}
