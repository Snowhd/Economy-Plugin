package org.exampls.economy_plugin.commands.money;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.exampls.economy_plugin.commands.money.subs.Add;
import org.exampls.economy_plugin.commands.money.subs.Remove;
import org.exampls.economy_plugin.commands.money.subs.Set;
import org.exampls.economy_plugin.logger.Logger;
import org.jetbrains.annotations.NotNull;

public class Money implements CommandExecutor {


    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        final Logger log = new Logger();
        if (!sender.hasPermission("server.admin")) {

            sender.sendMessage("§9§lARASIA §8• §7Du hast keine Berechtigung");
            log.log( sender.getName() + " tried to use command without enough privileges", 0, "Class: Money");
            return true;
        }

        if (args.length < 3) {
            sender.sendMessage("§9§lARASIA §8• §7Es wurden nicht genügend Argumente angegeben");

            log.log( sender.getName() + " tried to use it command without enough args", 0, "Class: Money");

            return true;
        }

        final Player player = Bukkit.getPlayer(args[1]);

        final long balance;

        try{

        balance = Long.parseLong(args[2]);

        } catch (NumberFormatException exception) {
            sender.sendMessage("§9§lARASIA §8• §7Du musst eine Zahl eingeben");
            sender.sendMessage("§9§lARASIA §8• §7Usage: /money <add, remove, set> <player> <amount>");
            return true;
        }

        if(player == null) {
            sender.sendMessage("§9§lARASIA §8• §7Der Spieler " + args[1] + " wurde nicht im System gefunden");
            return true;
        }

        //Sub command switch
        switch (args[0]) {
            case "add":
                if(balance <= 0){

                sender.sendMessage("§9§lARASIA §8• §7Dein wert muss größer als 0 sein");
                log.log( sender.getName() + " tried to use it command with wrong args", 0, "Class: Money | case add");

                return true;
                }
                new Add(balance, player, sender).addMoney();
                return true;

            case "remove":
                if(balance <= 0){

                sender.sendMessage("§9§lARASIA §8• §7Dein wert muss größer als 0 sein");

                return true;
                }
                new Remove(balance, player, sender).removeMoney();
                return true;

            case "set":
                if(balance < 0){

                sender.sendMessage("§9§lARASIA §8• §7Der wert darf nicht negativ sein");
                log.log( sender.getName() + " tried to use it command with wrong args", 0, "Class: Money | case set");

                return true;
                }
                new Set(balance, player, sender).setMoney();
                return true;
            default:
                sender.sendMessage("§9§lARASIA §8• §7Es gibt nur die Subcommmands: add, set und remove");
                log.log( sender.getName() + " tried to use it command with wrong args", 1, "Class: Money | case add");

                return true;
        }
    }
}
