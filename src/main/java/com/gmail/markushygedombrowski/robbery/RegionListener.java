package com.gmail.markushygedombrowski.robbery;

import com.gmail.markushygedombrowski.config.VagtVault;
import com.gmail.markushygedombrowski.utils.Utils;
import com.mewin.WGRegionEvents.events.RegionEnterEvent;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class RegionListener implements Listener {

    @EventHandler
    public void onRegionEnter(RegionEnterEvent event) {
        Player p = event.getPlayer();
        if (p.hasMetadata("goToSafePlace")) {
            VagtVault vagtVault = (VagtVault) p.getMetadata("goToSafePlace").get(0).value();
            ProtectedRegion region = event.getRegion();
            if (region.getId().contains("a-") || region.getId().contains("b-") || region.getId().contains("c-") ||
                    region.getId().contains("spawn")){
                if (region.getMembers().contains(p.getUniqueId()) || region.getId().contains("spawn")) {
                    p.sendMessage("§7[§9§lVagt Vault§7] Du er kommet i sikkerhed med Vagt Vaulten!");
                    Bukkit.broadcast("§7[§9§lVagt Vault§7] " + p.getName() + " er kommet i sikkerhed med Vagt Vaulten!", "vagt.vault");
                    p.removeMetadata("goToSafePlace", p.getMetadata("goToSafePlace").get(0).getOwningPlugin());
                }
            }
        }
    }
}
