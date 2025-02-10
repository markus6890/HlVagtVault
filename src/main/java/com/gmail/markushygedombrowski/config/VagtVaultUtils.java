package com.gmail.markushygedombrowski.config;

import com.gmail.markushygedombrowski.utils.ListHolder;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class VagtVaultUtils {
    private static HashMap<VagtVault, Integer> vagtVaultCooldown = new HashMap<>();
    private ListHolder listHolder;

    public VagtVaultUtils(ListHolder listHolder) {
        this.listHolder = listHolder;
    }


    public static void addCooldown(VagtVault vagtVault, int time) {
        vagtVaultCooldown.put(vagtVault, time);
    }

    public static void removeCooldown(VagtVault vagtVault) {
        vagtVaultCooldown.remove(vagtVault);
    }

    public static int getRemainingTimeInSeconds(VagtVault vagtVault) {
        System.out.println(vagtVaultCooldown.get(vagtVault));
        return vagtVaultCooldown.get(vagtVault);
    }
    public static void coolDownTimer() {
        if (vagtVaultCooldown.isEmpty()) {
            return;
        }
        new HashMap<>(vagtVaultCooldown).forEach((vagtVault, time) -> {
            if (time <= 0) {
                vagtVaultCooldown.remove(vagtVault);
                vagtVault.setCooldown(false);
            } else {
                vagtVaultCooldown.put(vagtVault, time - 1);
            }
        });
    }
    public int getAmountOfVagt() {
        return listHolder.getVagtList().size();
    }

    public static void addItems(List<ItemStack> items, Player player) {
        for (ItemStack item : items) {
            player.getInventory().addItem(item);
        }

    }


}
