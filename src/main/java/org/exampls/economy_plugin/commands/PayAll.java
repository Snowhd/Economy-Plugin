package org.exampls.economy_plugin.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.exampls.economy_plugin.database.Database;
import org.exampls.economy_plugin.logger.Logger;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;


public class PayAll implements CommandExecutor {

    private final Logger logger = new Logger();


    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {

        if(!sender.hasPermission("server.admin")){
            sender.sendMessage("§9§lARASIA §8• §7Du hast nicht genügend Berechtigungen");
            logger.log(sender.getName() + " tried to use an admin command", 1, "/payall");
            return true;
        }


        if(args.length < 1){
            sender.sendMessage("§9§lARASIA §8• §7Es wurden zuwenig Argumenete angegeben");
            logger.log(sender.getName() + " has passed too few arguments (admin command)", 1, "/payall");

            return true;
        }

        final long balance;

        try{
            balance = Long.parseLong(args[0]);
        } catch (NumberFormatException exception){
          
            sender.sendMessage("§9§lARASIA §8• §7Du musst eine Zahl angeben");
            logger.log(sender.getName() + " has passed wrong arguments (admin command) ", 1, "/payall");

            return true;
        }

        final Database database = new Database();

        Bukkit.getOnlinePlayers().forEach(player -> {
            final long oldBalance = database.getBalance(player.getName(),
                                                  Optional.of(player.getUniqueId().toString()));

            boolean status = database.updateBalance(player.getName(),
                                                    Optional.of(player.getUniqueId().toString()), balance + oldBalance);
            if(status) {

                player.sendMessage("§9§lARASIA §8• §7Du hast " + (balance) + " Coins bekommen");
                player.sendMessage("§9§lARASIA §8• §7Deine neuet Balance ist: " + (balance + oldBalance) + " Coins");
              
                logger.log(sender.getName() + " Paid everyplayer " + args[0] + " Coins", 1, "/payall");


            }
        });
        return true;
    }
}
