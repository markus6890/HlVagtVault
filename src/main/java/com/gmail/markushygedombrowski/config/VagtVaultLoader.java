package com.gmail.markushygedombrowski.config;

import com.gmail.markushygedombrowski.items.RareItems;
import com.gmail.markushygedombrowski.items.WrapperItemstack;
import me.arcaniax.hdb.api.HeadDatabaseAPI;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class VagtVaultLoader {
    private HashMap<String, VagtVault> vagtVaults = new HashMap<>();
    private List<Location> locations = new ArrayList<>();
    private FileConfiguration config;
    private VagtVaultManager vagtVaultManager;


    public VagtVaultLoader(FileConfiguration config, VagtVaultManager vagtVaultManager) {
        this.config = config;
        this.vagtVaultManager = vagtVaultManager;
    }

    public void load() {
        FileConfiguration config = vagtVaultManager.getVagtVaultConfig();
        vagtVaults.clear();
        locations.clear();
        for (String key : config.getConfigurationSection("vagtVaults").getKeys(false)) {
            String name = config.getString("vagtVaults." + key + ".name");
            Location location = (Location) config.get("vagtVaults." + key + ".location");
            int resetTime = config.getInt("vagtVaults." + key + ".resetTime");
            int failresetTime = config.getInt("vagtVaults." + key + ".failresetTime");
            int standStillTime = config.getInt("vagtVaults." + key + ".standStillTime");
            List<ItemStack> items = convertWrapperItemstack(((List<WrapperItemstack>) config.get("vagtVaults." + key + ".items")));
            List<ItemStack> heads = ((List<ItemStack>) config.get("vagtVaults." + key + ".heads"));
            List<RareItems> rareItems = ((List<RareItems>) config.get("vagtVaults." + key + ".rareItems"));
            List<ItemStack> rareHeads = ((List<ItemStack>) config.get("vagtVaults." + key + ".rareHeads"));
            double headChance = config.getDouble("vagtVaults." + key + ".headChance");
            double rareHeadChance = config.getDouble("vagtVaults." + key + ".rareHeadChance");
            String boardcastMessage = config.getString("vagtVaults." + key + ".boardcastMessage");
            String playerMessage = config.getString("vagtVaults." + key + ".playerMessage");
            boolean sendBoardcast = config.getBoolean("vagtVaults." + key + ".sendBoardcast");
            String cooldownMessage = config.getString("vagtVaults." + key + ".cooldownMessage");
            String finishBoardcastMessage = config.getString("vagtVaults." + key + ".finishBoardcastMessage");
            String finishPlayerMessage = config.getString("vagtVaults." + key + ".finishPlayerMessage");
            boolean needItem = config.getBoolean("vagtVaults." + key + ".needItem");
            ItemStack item = config.getItemStack("vagtVaults." + key + ".item");
            int minVagtOnline = config.getInt("vagtVaults." + key + ".minVagtOnline");


            VagtVault vagtVault = new VagtVault(name, location, resetTime, failresetTime, standStillTime,
                    items, heads, rareItems, rareHeads, headChance, rareHeadChance, boardcastMessage, playerMessage,
                    sendBoardcast, cooldownMessage, finishBoardcastMessage, finishPlayerMessage, needItem, item, minVagtOnline);
            vagtVaults.put(key, vagtVault);
            locations.add(location);
        }
    }

    public void save(VagtVault vagtVault) {
        config.set("vagtVaults." + vagtVault.getName() + ".name", vagtVault.getName());
        config.set("vagtVaults." + vagtVault.getName() + ".location", vagtVault.getLocation());
        config.set("vagtVaults." + vagtVault.getName() + ".resetTime", vagtVault.getResetTime());
        config.set("vagtVaults." + vagtVault.getName() + ".failresetTime", vagtVault.getFailresetTime());
        config.set("vagtVaults." + vagtVault.getName() + ".standStillTime", vagtVault.getStandStillTime());
        config.set("vagtVaults." + vagtVault.getName() + ".items", convertItemStack(vagtVault.getItems()));
        config.set("vagtVaults." + vagtVault.getName() + ".heads", vagtVault.getHeads());
        config.set("vagtVaults." + vagtVault.getName() + ".rareItems", vagtVault.getRareItems());
        config.set("vagtVaults." + vagtVault.getName() + ".rareHeads", vagtVault.getRareHeads());
        config.set("vagtVaults." + vagtVault.getName() + ".headChance", vagtVault.getHeadChance());
        config.set("vagtVaults." + vagtVault.getName() + ".rareHeadChance", vagtVault.getRareHeadChance());
        config.set("vagtVaults." + vagtVault.getName() + ".boardcastMessage", vagtVault.getBoardcastMessage());
        config.set("vagtVaults." + vagtVault.getName() + ".playerMessage", vagtVault.getPlayerMessage());
        config.set("vagtVaults." + vagtVault.getName() + ".sendBoardcast", vagtVault.isSendBoardcast());
        config.set("vagtVaults." + vagtVault.getName() + ".cooldownMessage", vagtVault.getCooldownMessage());
        config.set("vagtVaults." + vagtVault.getName() + ".finishBoardcastMessage", vagtVault.getFinishBoardcastMessage());
        config.set("vagtVaults." + vagtVault.getName() + ".finishPlayerMessage", vagtVault.getFinishPlayerMessage());
        config.set("vagtVaults." + vagtVault.getName() + ".needItem", vagtVault.isNeedItem());
        config.set("vagtVaults." + vagtVault.getName() + ".item", vagtVault.getRobberyItem());
        config.set("vagtVaults." + vagtVault.getName() + ".minVagtOnline", vagtVault.getMinAmountOfVagt());
        vagtVaults.put(vagtVault.getName(), vagtVault);
        locations.add(vagtVault.getLocation());
        vagtVaultManager.saveVagtVault();
    }
    public void reloadVagtVault() {
        load();
    }
    public List<WrapperItemstack> convertItemStack(List<ItemStack> items) {
        List<WrapperItemstack> wrapperItemstacks = new ArrayList<>();
        for (ItemStack item : items) {
            wrapperItemstacks.add(new WrapperItemstack(item));
        }
        return wrapperItemstacks;
    }

    public List<ItemStack> convertWrapperItemstack(List<WrapperItemstack> wrapperItemstacks) {
        List<ItemStack> items = new ArrayList<>();
        for (WrapperItemstack wrapperItemstack : wrapperItemstacks) {
            items.add(wrapperItemstack.getItem());
        }
        return items;
    }

    public VagtVault getVagtVault(String name) {
        return vagtVaults.get(name);
    }

    public VagtVault getVagtVaultFromLocation(Location location) {
        for (VagtVault vagtVault : vagtVaults.values()) {
            if (vagtVault.getLocation().equals(location)) {
                return vagtVault;
            }
        }
        return null;
    }
    public void saveall() {
        for (VagtVault vagtVault : vagtVaults.values()) {
            save(vagtVault);
        }
    }


    public void removeVagtVault(String name) {
        VagtVault vagtVault = vagtVaults.get(name);
        config.set("vagtVaults." + vagtVault.getName(), null);
        vagtVaults.remove(name);
        locations.remove(vagtVault.getLocation());
    }
    public HashMap<String, VagtVault> getVagtVaults() {
        return vagtVaults;
    }
    public boolean isLocationVagtVault(Location location) {
        return locations.contains(location);
    }
    public ItemStack createItem(String headId, String displayName) {
        HeadDatabaseAPI api = new HeadDatabaseAPI();
        ItemStack item = api.getItemHead(headId);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(displayName);
        item.setItemMeta(meta);
        return item;
    }


}
