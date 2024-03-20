package com.gmail.markushygedombrowski.config;

import com.gmail.markushygedombrowski.HLVagtVault;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.Listener;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class VagtVaultManager {
    public FileConfiguration vagtVaultConfig;
    public File vagtVaultFile;
    private HLVagtVault plugin = HLVagtVault.getPlugin(HLVagtVault.class);

    public void setup() {
        List<File> configList = new ArrayList<>();
        if (!plugin.getDataFolder().exists()) {
            plugin.getDataFolder().mkdir();
        }
        vagtVaultFile = new File(plugin.getDataFolder(), "vagtVault.yml");
        configList.add(vagtVaultFile);
        configList.forEach(file -> {
            if (!file.exists()) {
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.RED + "could not create " + file.getName() + "File");
                }
            }
        });
        vagtVaultConfig = YamlConfiguration.loadConfiguration(vagtVaultFile);
    }
    public void saveVagtVault() {
        try {
            vagtVaultConfig.save(vagtVaultFile);
        } catch (IOException e) {
            Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.RED + "could not save " + vagtVaultFile.getName() + "File");
        }
    }
    public void reloadVagtVault() {
        vagtVaultConfig = YamlConfiguration.loadConfiguration(vagtVaultFile);
    }
    public FileConfiguration getVagtVaultConfig() {
        return vagtVaultConfig;
    }
}
