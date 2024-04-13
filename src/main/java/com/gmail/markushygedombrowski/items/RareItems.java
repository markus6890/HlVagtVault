package com.gmail.markushygedombrowski.items;

import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class RareItems implements Cloneable, ConfigurationSerializable {
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

    @Override
    public Map<String, Object> serialize() {
        //serialize the object
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("chance", this.chance);
        result.put("item", this.item);
        return result;
    }
    @Override
    public RareItems clone() {
        try {
            return (RareItems) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new Error(e);
        }
    }

    public static RareItems deserialize(Map<String, Object> args) {
        //deserialize the object
        double chance = (double) args.get("chance");
        ItemStack item = (ItemStack) args.get("item");
        return new RareItems(chance, item);
    }
}
