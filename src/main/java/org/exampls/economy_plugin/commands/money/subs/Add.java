package org.exampls.economy_plugin.commands.money.subs;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.exampls.economy_plugin.database.Database;
import org.exampls.economy_plugin.logger.Logger;

import java.util.Optional;

public class Add extends SubCommands {

    final long balance;
    final Player player;
    final CommandSender sender;
    final Database database;

    public Add(@NonNull long balance, @NonNull Player player, @NonNull CommandSender sender) {
        this.balance = balance;
        this.player = player;
        this.sender = sender;
        this.database = new Database();

    }

    public void addMoney(){

       long beforeBalance = database.getBalance(player.getName(), Optional.of(getUUID(player)));

       boolean status = database.updateBalance(player.getName(),
                                                Optional.of(getUUID(player)),
                                                calculateBalance(beforeBalance, balance));

       if(!status){
           sender.sendMessage("§9§lARASIA §8• §7Es gab ein Fehler bei der Überweisung");
           new Logger().log("Unkown Database Error", 2, "Subcommand /moeny add | status");
           return;
       }

       sender.sendMessage("§9§lARASIA §8• §7Die Balance wurde von "+ player.getName() +" erfolgreich auf " + (beforeBalance + balance) + " Coins erhöht");

       player.sendMessage("§9§lARASIA §8• §7Deine Balance wurde auf " + (beforeBalance + balance) + " Coins erhöht");


       new Logger().log(balance + " was added to "+ player.getName() + " Balance | Command executer: " + sender.getName() , -1, "/money add");

    }


}
