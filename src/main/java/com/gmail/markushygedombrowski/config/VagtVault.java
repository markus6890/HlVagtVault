package com.gmail.markushygedombrowski.config;

import com.gmail.markushygedombrowski.items.RareItems;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class VagtVault {
    private String name;
    private Location location;
    private int resetTime;
    private int failresetTime;
    private int standStillTime;
    private List<ItemStack> items;
    private List<ItemStack> heads;
    private List<RareItems> rareItems;
    private List<ItemStack> rareHeads;
    private int headChance;



    private int rareHeadChance;


    public VagtVault(String name, Location location, int resetTime, int failresetTime, int standStillTime, List<ItemStack> items, List<ItemStack> heads, List<RareItems> rareItems, List<ItemStack> rareHeads, int headChance, int rareHeadChance) {
        this.name = name;
        this.location = location;
        this.resetTime = resetTime;
        this.failresetTime = failresetTime;
        this.standStillTime = standStillTime;
        this.items = items;
        this.heads = heads;
        this.rareItems = rareItems;
        this.rareHeads = rareHeads;
        this.headChance = headChance;
        this.rareHeadChance = rareHeadChance;
    }
    public VagtVault(String name,Location location,int resetTime,int failresetTime,int standStillTime){
        this.name = name;
        this.location = location;
        this.resetTime = resetTime;
        this.failresetTime = failresetTime;
        this.standStillTime = standStillTime;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public int getResetTime() {
        return resetTime;
    }

    public void setResetTime(int resetTime) {
        this.resetTime = resetTime;
    }

    public List<ItemStack> getItems() {
        return items;
    }

    public void setItems(List<ItemStack> items) {
        this.items = items;
    }

    public List<ItemStack> getHeads() {
        return heads;
    }

    public void setHeads(List<ItemStack> heads) {
        this.heads = heads;
    }

    public List<RareItems> getRareItems() {
        return rareItems;
    }

    public void setRareItems(List<RareItems> rareItems) {
        this.rareItems = rareItems;
    }

    public List<ItemStack> getRareHeads() {
        return rareHeads;
    }

    public void setRareHeads(List<ItemStack> rareHeads) {
        this.rareHeads = rareHeads;
    }

    public int getHeadChance() {
        return headChance;
    }

    public void setHeadChance(int headChance) {
        this.headChance = headChance;
    }

    public int getRareHeadChance() {
        return rareHeadChance;
    }

    public void setRareHeadChance(int rareHeadChance) {
        this.rareHeadChance = rareHeadChance;
    }

    public int getFailresetTime() {
        return failresetTime;
    }

    public void setFailresetTime(int failresetTime) {
        this.failresetTime = failresetTime;
    }

    public boolean canBeCreated() {
        return name != null && location != null && items != null;
    }

    public int getStandStillTime() {
        return standStillTime;
    }

    public void setStandStillTime(int standStillTime) {
        this.standStillTime = standStillTime;
    }

    public void fixNullLists() {
        if(heads == null) {
            heads = new ArrayList<>();
        }
        if(items == null) {
            items = new ArrayList<>();
        }
        if(rareItems == null) {
            rareItems = new ArrayList<>();
        }
        if(rareHeads == null) {
            rareHeads = new ArrayList<>();
        }

    }
}
