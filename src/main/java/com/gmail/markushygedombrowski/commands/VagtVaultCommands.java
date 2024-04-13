package com.gmail.markushygedombrowski.commands;

import com.gmail.markushygedombrowski.config.VagtVaultLoader;
import com.gmail.markushygedombrowski.vagtvault.mainGUIS.Create;
import com.gmail.markushygedombrowski.vagtvault.mainGUIS.EditVault;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class VagtVaultCommands implements CommandExecutor {
    private Create create;
    private VagtVaultLoader vagtVaultLoader;
    private EditVault editVault;
    public VagtVaultCommands(Create create, VagtVaultLoader vagtVaultLoader, EditVault editVault) {
        this.create = create;
        this.vagtVaultLoader = vagtVaultLoader;
        this.editVault = editVault;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String alias, String[] args) {
        if(!(sender instanceof Player))
            return true;
        Player player = (Player) sender;

        if (!player.hasPermission("vagtvault")){
            player.sendMessage("Du har ikke permission til at bruge denne kommando!");
            return true;
        }

        if(args.length == 0) {
            player.sendMessage("§e/vagtvault list");
            player.sendMessage("§e/vagtvault reload <name>");
            player.sendMessage("§e/vagtvault create <name>");

            player.sendMessage("§e/vagtvault info <name>");
            return true;
        }
        if(args[0].equalsIgnoreCase("create")) {
            if (args.length != 2) {
                player.sendMessage("§e/vagtvault create <name>");
                return true;
            }
            if(vagtVaultLoader.getVagtVault(args[1]) != null) {
                player.sendMessage("§cDenne vagtvault eksistere allerede!");
                return true;
            } else if (vagtVaultLoader.isLocationVagtVault(create.getTargetBlock(player, 5).getLocation())) {
                player.sendMessage("§cDenne lokation er allerede en vagtvault!");
                return true;
            }
            create.create(player, args[1]);
        } else if(args[0].equalsIgnoreCase("list")) {
            player.sendMessage("§eVagtVaults:");
            for (String name : vagtVaultLoader.getVagtVaults().keySet()) {
                player.sendMessage("§7[§aVagt Vaults§7]§e- " + name);
            }
        } else if (args[0].equalsIgnoreCase("info")) {
            if (args.length != 2) {
                player.sendMessage("§e/vagtvault info <name>");
                return true;
            }
            if(vagtVaultLoader.getVagtVault(args[1]) == null) {
                player.sendMessage("§cDenne vagtvault eksistere ikke!");
                return true;
            }
            editVault.create(player, vagtVaultLoader.getVagtVault(args[1]));

        }


        return true;
    }

}
