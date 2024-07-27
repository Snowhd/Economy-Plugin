package org.exampls.economy_plugin.commands.money.subs;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.exampls.economy_plugin.database.Database;
import org.exampls.economy_plugin.logger.Logger;

import java.util.Optional;

public class Set extends SubCommands{

    final long balance;
    final Player player;
    final CommandSender sender;
    final Database database;

    public Set(@NonNull long balance, @NonNull Player player, @NonNull CommandSender sender){

        this.balance = balance;
        this.player = player;
        this.sender = sender;

        this.database = new Database();
    }

    public void setMoney(){

        boolean status = database.updateBalance(player.getName(),
                                                Optional.of(getUUID(player)),
                                                balance);

        if(!status){
            sender.sendMessage("§9§lARASIA §8• §7Es gab ein fehler, versuche es später erneut");
             new Logger().log("Unkown Database Error", 2, "Subcommand /moeny add | status");
            return;
        }

        player.sendMessage("§9§lARASIA §8• §7Deine Balance ist auf " + balance + " gesetzt worden");
        sender.sendMessage("§9§lARASIA §8• §7Die Balance von "+ player.getName() + " wurde auf "+ balance + " Coins gesetzt");
       new Logger().log(player.getName() + " Balance was set to " + balance + " | Command executer: " + sender.getName() , 1, "/money add");


    }
}