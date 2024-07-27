package org.exampls.economy_plugin.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.exampls.economy_plugin.database.Database;
import org.exampls.economy_plugin.logger.Logger;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class TogglePay implements CommandExecutor {


    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {

        if(!(sender instanceof Player)) {
            sender.sendMessage("§9§lARASIA §8• §7Du bist Kein Spieler");
            return true;
        }

        Database database = new Database();

        boolean status = database.updateTogglePayStatus(sender.getName(),
                                  Optional.of(((Player) sender).getUniqueId().toString()));


        if(status) {
            sender.sendMessage("§9§lARASIA §8• §7Du kannst jetzt keine Zahlungen mehr empfangen");
        } else {
            sender.sendMessage("§9§lARASIA §8• §7Du kannst jetzt Zahlungen empfangen");
        }

        new Logger().log(sender.getName() + " switched toggleplay to " + status, -1, "/togglepay" );

        return true;
    }
}
