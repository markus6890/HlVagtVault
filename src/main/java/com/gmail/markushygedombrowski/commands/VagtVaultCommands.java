package com.gmail.markushygedombrowski.commands;

import com.gmail.markushygedombrowski.vagtvault.Create;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class VagtVaultCommands implements CommandExecutor {
    private Create create;

    public VagtVaultCommands(Create create) {
        this.create = create;
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
            player.sendMessage("§e/vagtvault reload <name>");
            player.sendMessage("§e/vagtvault create <name>");
            player.sendMessage("§e/vagtvault delete <name>");
            player.sendMessage("§e/vagtvault list");
            player.sendMessage("§e/vagtvault info <name>");
            return true;
        }
        if(args[0].equalsIgnoreCase("create")) {
            if (args.length != 2) {
                player.sendMessage("§e/vagtvault create <name>");
                return true;
            }
            create.create(player, args[1]);
        }


        return true;
    }
}
