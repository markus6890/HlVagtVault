package com.gmail.markushygedombrowski.items;

import org.bukkit.inventory.ItemStack;

public class RareItems {
    private double chance;
    private ItemStack item;

    public RareItems(double chance, ItemStack item) {
        this.chance = chance;
        this.item = item;
    }

    public double getChance() {
        return chance;
    }
    public ItemStack getItem() {
        return item;
    }
    public void setChance(double chance) {
        this.chance = chance;
    }

}
