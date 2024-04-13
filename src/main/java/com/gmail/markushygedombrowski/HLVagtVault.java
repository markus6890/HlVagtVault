package com.gmail.markushygedombrowski;

import com.gmail.markushygedombrowski.commands.VagtVaultCommands;
import com.gmail.markushygedombrowski.config.VagtVaultLoader;
import com.gmail.markushygedombrowski.config.VagtVaultManager;
import com.gmail.markushygedombrowski.vagtvault.edit.Additems;
import com.gmail.markushygedombrowski.vagtvault.edit.EditRareItem;
import com.gmail.markushygedombrowski.vagtvault.edit.OtherSettings;
import com.gmail.markushygedombrowski.vagtvault.mainGUIS.Create;
import com.gmail.markushygedombrowski.vagtvault.edit.SetTimes;
import com.gmail.markushygedombrowski.vagtvault.mainGUIS.EditVault;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class HLVagtVault extends JavaPlugin {

    @Override
    public void onEnable() {
        VagtVaultManager vagtVaultManager = new VagtVaultManager();
        vagtVaultManager.setup();
        vagtVaultManager.saveVagtVault();
        VagtVaultLoader vagtVaultLoader = new VagtVaultLoader(vagtVaultManager.getVagtVaultConfig(), vagtVaultManager);
        EditRareItem editRareItem = new EditRareItem(vagtVaultLoader, this);
        Bukkit.getPluginManager().registerEvents(editRareItem, this);
        OtherSettings otherSettings = new OtherSettings(vagtVaultLoader);
        Bukkit.getPluginManager().registerEvents(otherSettings, this);
        Additems additems = new Additems(vagtVaultLoader, this, editRareItem);
        Bukkit.getPluginManager().registerEvents(additems, this);
        SetTimes setTimes = new SetTimes(this);
        Bukkit.getPluginManager().registerEvents(setTimes, this);
        Create create = new Create(additems, vagtVaultLoader, this, setTimes,otherSettings);
        Bukkit.getPluginManager().registerEvents(create, this);
        EditVault editVault = new EditVault(vagtVaultLoader, additems, setTimes,otherSettings,editRareItem);
        Bukkit.getPluginManager().registerEvents(editVault, this);
        VagtVaultCommands vagtVaultCommands = new VagtVaultCommands(create, vagtVaultLoader,editVault);
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
