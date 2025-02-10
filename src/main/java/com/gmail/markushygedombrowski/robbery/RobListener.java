package com.gmail.markushygedombrowski.robbery;

import com.gmail.markushygedombrowski.HLVagtVault;
import com.gmail.markushygedombrowski.combat.CombatList;
import com.gmail.markushygedombrowski.config.VagtVault;
import com.gmail.markushygedombrowski.config.VagtVaultLoader;
import com.gmail.markushygedombrowski.config.VagtVaultUtils;
import com.gmail.markushygedombrowski.items.RareItems;
import com.gmail.markushygedombrowski.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;

public class RobListener implements Listener {
    private VagtVaultLoader vagtVaultLoader;
    private HLVagtVault plugin;
    private VagtVaultUtils vagtVaultUtils;
    private CombatList combatList;
    public RobListener(VagtVaultLoader vagtVaultLoader, HLVagtVault plugin, VagtVaultUtils vagtVaultUtils,CombatList combatList) {

        this.vagtVaultLoader = vagtVaultLoader;
        this.plugin = plugin;
        this.vagtVaultUtils = vagtVaultUtils;
        this.combatList = combatList;
    }

    @EventHandler
    public void onBlockInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (event.getClickedBlock() == null) {
            return;
        }
        Block block = event.getClickedBlock();
        Location location = event.getClickedBlock().getLocation();
        if (!vagtVaultLoader.isLocationVagtVault(location)) {
            return;
        }
        event.setCancelled(true);
        VagtVault vagtVault = vagtVaultLoader.getVagtVaultFromLocation(location);
        if (player.hasPermission("vagt.vault")) {
            player.sendMessage("§7[§9Vagt Vault§7] Godt du holder øje med vagt vaulten!");
            return;
        }
        if (vagtVault.isCooldown()) {
            player.sendMessage(vagtVault.getCooldownMessage(player));
            player.sendMessage("§7[§9Vagt Vault§7] Du kan røve vagt vaulten igen om §4" + (VagtVaultUtils.getRemainingTimeInSeconds(vagtVault) / 60) + " §7minutter");
            return;
        }
        if(vagtVault.isNeedItem() && player.getItemInHand().getType() != vagtVault.getRobberyItem().getType()) {
            player.sendMessage("§7[§9Vagt Vault§7] Du skal have §4" + vagtVault.getRobberyItem().getItemMeta().getDisplayName() + " §7for at røve");
            return;
        }
        if(vagtVault.isSendBoardcast()) {
            if(!(vagtVaultUtils.getAmountOfVagt() >= vagtVault.getMinAmountOfVagt())) {
                player.sendMessage("§7[§9Vagt Vault§7] Der skal mindst være §4" + vagtVault.getMinAmountOfVagt() + " §7vagter for at røve denne vagt vault!");
                return;
            }
        }

        takeVagtVault(player, vagtVault);


    }


    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        Location location = player.getLocation();
        if (event.getFrom().getBlockX() != event.getTo().getBlockX() || event.getFrom().getBlockY() != event.getTo().getBlockY() || event.getFrom().getBlockZ() != event.getTo().getBlockZ()) {
            if (player.hasMetadata("vagtVault")) {
                player.sendMessage("§cDu bevægede dig! Du har mistet din chance for at røve vagt vaulten!");
                VagtVault vagtVault = (VagtVault) player.getMetadata("vagtVault").get(0).value();
                player.removeMetadata("vagtVault", plugin);
                VagtVaultUtils.addCooldown(vagtVault, vagtVault.getFailresetTime() * 60);
            }
        }


    }

    public void takeVagtVault(Player player, VagtVault vagtVault) {
        vagtVault.setCooldown(true);
        vagtVault.sendMessages(player);
        player.setMetadata("vagtVault", new FixedMetadataValue(plugin, vagtVault));
        sendActionBarMessageRobbery(player, vagtVault);

    }

    private void sendActionBarMessageRobbery(Player player, VagtVault vagtVault) {
        int[] time = {0};
        new BukkitRunnable() {
            @Override
            public void run() {
                if (player.hasMetadata("vagtVault")) {
                    String message = "§7[§9§lVagt Vault§7] §aDu er igang med at §4§lrøve §6§l" + vagtVault.getName() + " §7du skal stå stille i §4" + (vagtVault.getStandStillTime() - time[0]) + " §7sekunder";
                    Utils.sendActionbar(player, message);
                    time[0]++;
                    if (time[0] >= vagtVault.getStandStillTime()) {
                        getLoot(player, vagtVault);
                        player.sendMessage(vagtVault.getFinishPlayerMessage(player));
                        if (vagtVault.isSendBoardcast()) {
                            boardcastMessage(player, vagtVault);

                        }

                        player.removeMetadata("vagtVault", plugin);
                        VagtVaultUtils.addCooldown(vagtVault, vagtVault.getResetTime() * 60);
                        cancel();
                    }
                } else {
                    cancel();

                }

            }
        }.runTaskTimer(plugin, 20, vagtVault.getStandStillTime() * 2L);


    }

    private void boardcastMessage(Player player, VagtVault vagtVault) {
        Bukkit.broadcastMessage(vagtVault.getFinishBoardcastMessage(player));
        player.sendMessage("§7[§9§lVagt Vault§7] §aDu skal finde til et sikkert sted §7(spawn/en celle)");
        player.setMetadata("goToSafePlace", new FixedMetadataValue(plugin, vagtVault));
        combatList.addPlayerToCombat(player,2400);
        
    }

    public void getLoot(Player player, VagtVault vagtVault) {
        VagtVaultUtils.addItems(vagtVault.getItems(), player);

        getHead(player, vagtVault);
        getRareHead(player, vagtVault);

        getRareItem(player, vagtVault);
    }

    private void getRareItem(Player player, VagtVault vagtVault) {
        if (!vagtVault.getRareItems().isEmpty()) {
            for (RareItems rareItems : vagtVault.getRareItems()) {
                if (Utils.procent(rareItems.getChance())) {
                    player.getInventory().addItem(rareItems.getItem());
                    return;
                }
            }
        }
    }

    private void getHead(Player player, VagtVault vagtVault) {
        if (!vagtVault.getHeads().isEmpty()) {
            if (Utils.procent(vagtVault.getHeadChance())) {
                ItemStack head = vagtVault.getHeads().get(Utils.randomInt(0, vagtVault.getHeads().size() - 1));
                player.getInventory().addItem(head);
            }
        }
    }

    private void getRareHead(Player player, VagtVault vagtVault) {
        if (!vagtVault.getRareHeads().isEmpty()) {
            if (Utils.procent(vagtVault.getRareHeadChance())) {
                ItemStack rareHead = vagtVault.getRareHeads().get(Utils.randomInt(0, vagtVault.getRareHeads().size() - 1));
                player.getInventory().addItem(rareHead);

            }
        }
    }


}
