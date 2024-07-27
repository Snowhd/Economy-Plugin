package org.exampls.economy_plugin.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.exampls.economy_plugin.database.Database;
import org.exampls.economy_plugin.logger.Logger;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class ViewBalance implements CommandExecutor {
    final Logger logger = new Logger();

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {


        if(!(sender instanceof Player)){
            sender.sendMessage("§9§lARASIA §8• §7Du bist Kein Spieler");
            logger.log("Console tried to execute player command", 1, "/viewbalance");
            return true;
        }

        final Database database = new Database();


        final long balance = database.getBalance(sender.getName(),
                                                Optional.of(((Player) sender).getUniqueId().toString()));

        if(balance < 0) {

            sender.sendMessage("§9§lARASIA §8• §7Es gab ein fehler, versuche es später erneut");
            logger.log("Database Error, couldn't retrieve the Balance from " + sender.getName(), 2, "/viewbalance");

            return true;
        }

        sender.sendMessage("§9§lARASIA §8• §7Deine Balance beträgt " + String.valueOf(balance) + " Coins");

        return true;
    }
}
