package com.gmail.markushygedombrowski.items;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
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
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("chance", this.chance);

        // Convert the ItemStack to a serializable format
        YamlConfiguration config = new YamlConfiguration();
        config.set("item", this.item);
        result.put("item", config.saveToString());

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
        double chance = (double) args.get("chance");

        // Convert the serialized ItemStack back to an ItemStack
        YamlConfiguration config = new YamlConfiguration();
        try {
            config.loadFromString((String) args.get("item"));
        } catch (InvalidConfigurationException e) {
            e.printStackTrace();
        }
        ItemStack item = config.getItemStack("item");

        return new RareItems(chance, item);
    }
}
