package com.gmail.markushygedombrowski;

import com.gmail.markushygedombrowski.commands.VagtVaultCommands;
import com.gmail.markushygedombrowski.config.VagtVaultLoader;
import com.gmail.markushygedombrowski.config.VagtVaultManager;
import com.gmail.markushygedombrowski.vagtvault.Additems;
import com.gmail.markushygedombrowski.vagtvault.Create;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class HLVagtVault extends JavaPlugin {

    @Override
    public void onEnable() {
        VagtVaultManager vagtVaultManager = new VagtVaultManager();
        vagtVaultManager.setup();
        vagtVaultManager.saveVagtVault();
        VagtVaultLoader vagtVaultLoader = new VagtVaultLoader(vagtVaultManager.getVagtVaultConfig(), vagtVaultManager);
        Additems additems = new Additems(vagtVaultLoader, this);
        Bukkit.getPluginManager().registerEvents(additems, this);
        Create create = new Create(additems, vagtVaultLoader, this);
        Bukkit.getPluginManager().registerEvents(create, this);
        VagtVaultCommands vagtVaultCommands = new VagtVaultCommands(create);
        getCommand("vagtvault").setExecutor(vagtVaultCommands);
        System.out.println("-----------------------------");
        System.out.println("HLVagtVault enabled");
        System.out.println("-----------------------------");
    }

    @Override
    public void onDisable() {
        System.out.println("-----------------------------");
        System.out.println("HLVagtVault disabled");
        System.out.println("-----------------------------");
    }
}
