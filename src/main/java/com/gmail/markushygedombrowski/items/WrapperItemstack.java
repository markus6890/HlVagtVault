package com.gmail.markushygedombrowski.items;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.inventory.ItemStack;

import java.util.LinkedHashMap;
import java.util.Map;

public class WrapperItemstack implements Cloneable, ConfigurationSerializable {
    private ItemStack item;

    public WrapperItemstack(ItemStack item) {
        this.item = item;
    }

    public ItemStack getItem() {
        return item;
    }

    public void setItem(ItemStack item) {
        this.item = item;
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> result = new LinkedHashMap<>();

        // Convert the ItemStack to a serializable format
        YamlConfiguration config = new YamlConfiguration();
        config.set("item", this.item);
        result.put("item", config.saveToString());

        // Save the amount of the ItemStack
        result.put("amount", this.item.getAmount());

        return result;
    }

    public static WrapperItemstack deserialize(Map<String, Object> args) {
        // Convert the serialized ItemStack back to an ItemStack
        YamlConfiguration config = new YamlConfiguration();
        try {
            config.loadFromString((String) args.get("item"));
        } catch (InvalidConfigurationException e) {
            e.printStackTrace();
        }
        ItemStack item = config.getItemStack("item");

        // Load the amount of the ItemStack
        if (args.containsKey("amount")) {
            item.setAmount((int) args.get("amount"));
        }

        return new WrapperItemstack(item);
    }
    @Override
    public WrapperItemstack clone() {
        try {
            WrapperItemstack cloned = (WrapperItemstack) super.clone();
            cloned.item = this.item.clone(); // Clone the ItemStack
            return cloned;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError(); // Should never happen
        }
    }
}
