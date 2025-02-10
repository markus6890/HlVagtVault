package com.gmail.markushygedombrowski;

import com.gmail.markushygedombrowski.combat.CombatList;
import com.gmail.markushygedombrowski.commands.VagtVaultCommands;
import com.gmail.markushygedombrowski.config.VagtVaultLoader;
import com.gmail.markushygedombrowski.config.VagtVaultManager;
import com.gmail.markushygedombrowski.config.VagtVaultUtils;
import com.gmail.markushygedombrowski.items.RareItems;
import com.gmail.markushygedombrowski.items.WrapperItemstack;
import com.gmail.markushygedombrowski.robbery.RegionListener;
import com.gmail.markushygedombrowski.robbery.RobListener;
import com.gmail.markushygedombrowski.utils.ListHolder;
import com.gmail.markushygedombrowski.utils.Utils;
import com.gmail.markushygedombrowski.vagtvault.edit.*;
import com.gmail.markushygedombrowski.vagtvault.mainGUIS.Create;
import com.gmail.markushygedombrowski.vagtvault.mainGUIS.EditVault;
import org.bukkit.Bukkit;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.plugin.java.JavaPlugin;

public class HLVagtVault extends JavaPlugin {
    private VagtVaultLoader vagtVaultLoader;
    private VagtVaultManager vagtVaultManager;

    @Override
    public void onEnable() {
        ConfigurationSerialization.registerClass(RareItems.class);
        ConfigurationSerialization.registerClass(WrapperItemstack.class);
        loadConfigs();
        CombatMain combatMain = CombatMain.getInstance();
        CombatList combatList = combatMain.getCombatList();
        HLUtils hlUtils = HLUtils.getInstance();
        ListHolder listHolder = hlUtils.getListHolder();
        VagtVaultUtils vagtVaultUtils = new VagtVaultUtils(listHolder);
        RobListener robListener = new RobListener(vagtVaultLoader, this,vagtVaultUtils,combatList);
        Bukkit.getPluginManager().registerEvents(robListener, this);

        NeedItemToRob needItemToRob = new NeedItemToRob(vagtVaultLoader);
        Bukkit.getPluginManager().registerEvents(needItemToRob, this);
        EditRareItem editRareItem = new EditRareItem(vagtVaultLoader, this);
        Bukkit.getPluginManager().registerEvents(editRareItem, this);
        SetHeadChanceGUI setHeadChanceGUI = new SetHeadChanceGUI(vagtVaultLoader, this);
        Bukkit.getPluginManager().registerEvents(setHeadChanceGUI, this);
        OtherSettings otherSettings = new OtherSettings(vagtVaultLoader,setHeadChanceGUI);
        Bukkit.getPluginManager().registerEvents(otherSettings, this);
        Additems additems = new Additems(vagtVaultLoader, this, editRareItem);
        Bukkit.getPluginManager().registerEvents(additems, this);
        SetTimes setTimes = new SetTimes(this);
        Bukkit.getPluginManager().registerEvents(setTimes, this);
        Create create = new Create(additems, vagtVaultLoader, this, setTimes, otherSettings);
        Bukkit.getPluginManager().registerEvents(create, this);
        EditVault editVault = new EditVault(vagtVaultLoader, additems, setTimes, otherSettings, editRareItem,needItemToRob);
        Bukkit.getPluginManager().registerEvents(editVault, this);
        RegionListener regionListener = new RegionListener();
        Bukkit.getPluginManager().registerEvents(regionListener, this);
        VagtVaultCommands vagtVaultCommands = new VagtVaultCommands(create, vagtVaultLoader, editVault, this);
        getCommand("vagtvault").setExecutor(vagtVaultCommands);
        System.out.println("-----------------------------");
        System.out.println("HLVagtVault enabled");
        System.out.println("-----------------------------");
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
            @Override
            public void run() {
                VagtVaultUtils.coolDownTimer();
            }
        }, 20, 20);
    }
    public void reloadConfigs() {
        reloadConfig();
        loadConfigs();

    }

    public void loadConfigs() {
        vagtVaultManager = new VagtVaultManager();
        vagtVaultManager.setup();
        vagtVaultManager.saveVagtVault();
        vagtVaultManager.reloadVagtVault();
        vagtVaultLoader = new VagtVaultLoader(vagtVaultManager.getVagtVaultConfig(), vagtVaultManager);
        vagtVaultLoader.load();
    }
    @Override
    public void onDisable() {
        vagtVaultLoader.saveall();
        System.out.println("-----------------------------");
        System.out.println("HLVagtVault disabled");
        System.out.println("-----------------------------");
    }
}



