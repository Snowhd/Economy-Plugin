package org.exampls.economy_plugin.commands.money.subs;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.exampls.economy_plugin.database.Database;
import org.exampls.economy_plugin.logger.Logger;

import java.util.Optional;


public class Remove extends SubCommands {

    final long balance;
    final Player player;
    final CommandSender sender;
    final Database database;

    public Remove(@NonNull long balance, @NonNull Player player, @NonNull CommandSender sender) {
        this.balance = balance;
        this.player = player;
        this.sender = sender;
        this.database = new Database();

    }

    public void removeMoney(){

        final long currentBalance = database.getBalance(player.getName(), Optional.of(getUUID(player)));

        final long newBalance = currentBalance - balance;

        if(newBalance < 0){
            boolean status = database.updateBalance(player.getName(),
                                                    Optional.of(getUUID(player)),
                                                    0);

            if(!status) {
                sender.sendMessage("§9§lARASIA §8• §7Es gab ein fehler, versuch es später erneut");
                 new Logger().log("Unkown Database Error", 2, "Subcommand /moeny add | status");

                return;
            }

            sender.sendMessage("§9§lARASIA §8• §7Die Balance von "+ player.getName() +" wurde auf 0 Coins reduziert");

            player.sendMessage("§9§lARASIA §8• §7Deine Balance wurde auf 0 Coins reduziert");
            return;

        }

        boolean status = database.updateBalance(player.getName(),
                               Optional.of(getUUID(player)),
                                newBalance);

        if(!status){
            sender.sendMessage("§9§lARASIA §8• §7Es gab ein fehler, versuch es später erneut");
             new Logger().log("Unkown Database Error", 2, "Subcommand /moeny add | status");

            return;
        }


        sender.sendMessage("§9§lARASIA §8• §7Die Balance von "+ player.getName() +" wurde auf " + newBalance + " Coins reduziert");

        player.sendMessage("§9§lARASIA §8• §7Deine Balance wurde auf " + newBalance + " Coins reduziert");

        new Logger().log(balance + " was removed from "+ player.getName() + " Balance | Command executer: " + sender.getName() , -1, "/money remove");

    }
}