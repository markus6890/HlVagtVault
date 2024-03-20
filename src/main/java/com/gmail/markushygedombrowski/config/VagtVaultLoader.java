package com.gmail.markushygedombrowski.config;

import com.gmail.markushygedombrowski.items.RareItems;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;

public class VagtVaultLoader {
    private HashMap<String, VagtVault> vagtVaults = new HashMap<>();
    private FileConfiguration config;

    public VagtVaultLoader(FileConfiguration config) {
        this.config = config;
    }

    public void load(FileConfiguration config) {
        for (String key : config.getConfigurationSection("vagtVaults").getKeys(false)) {
            String name = config.getString("vagtVaults." + key + ".name");
            Location location = (Location) config.get("vagtVaults." + key + ".location");
            int resetTime = config.getInt("vagtVaults." + key + ".resetTime");
            int failresetTime = config.getInt("vagtVaults." + key + ".failresetTime");
            List<ItemStack> items = ((List<ItemStack>) config.get("vagtVaults." + key + ".items"));
            List<ItemStack> heads = ((List<ItemStack>) config.get("vagtVaults." + key + ".heads"));
            List<RareItems> rareItems = ((List<RareItems>) config.get("vagtVaults." + key + ".rareItems"));
            List<ItemStack> rareHeads = ((List<ItemStack>) config.get("vagtVaults." + key + ".rareHeads"));
            int headChance = config.getInt("vagtVaults." + key + ".headChance");
            int rareHeadChance = config.getInt("vagtVaults." + key + ".rareHeadChance");
            VagtVault vagtVault = new VagtVault(name, location, resetTime, failresetTime, items, heads, rareItems, rareHeads, headChance, rareHeadChance);
            vagtVaults.put(key, vagtVault);


        }
    }

    public void save(VagtVault vagtVault) {
        config.set("vagtVaults." + vagtVault.getName() + ".name", vagtVault.getName());
        config.set("vagtVaults." + vagtVault.getName() + ".location", vagtVault.getLocation());
        config.set("vagtVaults." + vagtVault.getName() + ".resetTime", vagtVault.getResetTime());
        config.set("vagtVaults." + vagtVault.getName() + ".failresetTime", vagtVault.getFailresetTime());
        config.set("vagtVaults." + vagtVault.getName() + ".items", vagtVault.getItems());
        config.set("vagtVaults." + vagtVault.getName() + ".heads", vagtVault.getHeads());
        config.set("vagtVaults." + vagtVault.getName() + ".rareItems", vagtVault.getRareItems());
        config.set("vagtVaults." + vagtVault.getName() + ".rareHeads", vagtVault.getRareHeads());
        config.set("vagtVaults." + vagtVault.getName() + ".headChance", vagtVault.getHeadChance());
        config.set("vagtVaults." + vagtVault.getName() + ".rareHeadChance", vagtVault.getRareHeadChance());
        vagtVaults.put(vagtVault.getName(), vagtVault);
    }
    public VagtVault getVagtVault(String name) {
        return vagtVaults.get(name);
    }
    public void removeVagtVault(String name) {
        VagtVault vagtVault = vagtVaults.get(name);
        config.set("vagtVaults." + vagtVault.getName(), null);
        vagtVaults.remove(name);
    }
}
