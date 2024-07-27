package org.exampls.economy_plugin.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.exampls.economy_plugin.database.Database;
import org.exampls.economy_plugin.logger.Logger;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;


public class Pay implements CommandExecutor {
    
    private final Logger logger = new Logger();
    
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {

        if(!(sender instanceof Player)) {
            sender.sendMessage("§9§lARASIA §8• §7Du bist kein Spieler");
            logger.log("Console tried to execute a command", -1, "/pay");

            return true;
        }

        if(args.length < 2) {
            sender.sendMessage("§9§lARASIA §8• §7Du hast zu wenig Argumente angegeben");
            logger.log("Wrong args was given by " + sender.getName(), -1, "/pay");
            return true;
        }

        final Database database = new Database();

        final Player playerOne = (Player) sender;

        final Player playerTwo = Bukkit.getPlayer(args[0]);

        final boolean playerOnePayStatus = database.getTogglePayStatus(sender.getName(),
                                            Optional.of(((Player) sender).getUniqueId().toString()));
        final boolean playerTwoPayStatus = database.getTogglePayStatus(playerTwo.getName(),
                                            Optional.of(playerTwo.getUniqueId().toString()));

        if(playerOne.getUniqueId() == playerTwo.getUniqueId()){

            sender.sendMessage("§9§lARASIA §8• §7Du kannst dir nicht selber Geld überweisen");
            logger.log("Wrong args was given by " + sender.getName(), -1, "/pay");
            return true;
        }
        
        if(playerOnePayStatus || playerTwoPayStatus ) {

            sender.sendMessage("§9§lARASIA §8• §7Du oder der Spieler der das Geld empfängt hat Zahlungen ausgestellt");
            logger.log("Player turned off payments " + sender.getName(), -1, "/pay");

            return true;
        }

        if(playerTwo == null) {

            sender.sendMessage("§9§lARASIA §8• §7Der Spieler " +args[0]+ " wurde nicht gefunden");
            logger.log("Wrong args was given by " + sender.getName(), -1, "/pay");

            return true;
        }

        final long playerOneBalance  = database.getBalance(playerOne.getName(),
                                 Optional.of(playerOne.getUniqueId().toString()));

        final long playerTwoBalance = database.getBalance(playerTwo.getName(),
                                Optional.of(playerTwo.getUniqueId().toString()));

        if(playerOneBalance == -1 || playerTwoBalance == -1){

            sender.sendMessage("§9§lARASIA §8• §7Es gab ein fehler bei der Überweisung, bitte versuch es später erneut");
            logger.log("Database Balance error ", 2, "/pay");

            return true;
        }

        final long newPlayerOneBalance;
        final long newPlayerTwoBalance;

        try{
            newPlayerOneBalance = playerOneBalance - Long.parseLong(args[1]);
            newPlayerTwoBalance = playerTwoBalance + Long.parseLong(args[1]);

            if(newPlayerOneBalance < 0){

                sender.sendMessage("§9§lARASIA §8• §7Du hast nicht so viel geld!");
                logger.log("Wrong args was given by " + sender.getName(), -1, "/pay");

                return true;
            }

        }catch (NumberFormatException ex){

            sender.sendMessage("§9§lARASIA §8• §7diese Zahl ist nicht gültig");
            logger.log("Wrong args was given by " + sender.getName(), -1, "/pay");

            return true;
        }

         final boolean playerOneCheck =  database.updateBalance(playerOne.getName(),
                                   Optional.of(playerOne.getUniqueId().toString()),
                                   newPlayerOneBalance);

        final boolean playerTwoCheck =   database.updateBalance(playerTwo.getName(),
                                   Optional.of(playerTwo.getUniqueId().toString()),
                                   newPlayerTwoBalance);

        if(!playerOneCheck || !playerTwoCheck){

            sender.sendMessage("§9§lARASIA §8• §7es gab ein fatalen fehler");
            logger.log("Error: wrongly updatet the database ", 2, "/pay | balance update");


            database.updateBalance(playerOne.getName(),
                                   Optional.of(playerOne.getUniqueId().toString()),
                                   playerOneBalance);

            database.updateBalance(playerTwo.getName(),
                                   Optional.of(playerTwo.getUniqueId().toString()),
                                   playerTwoBalance);

            return true;
        }

        sender.sendMessage("§9§lARASIA §8• §7Du hast den Spieler " + args[0] + " " + args[1] + " Coins überwiesen");

        playerTwo.sendMessage("§9§lARASIA §8• §7" + sender.getName() + " hat dir " + args[1] + " überwiesen");
        logger.log(sender.getName() + "paid " + playerTwo.getName() + " " + args[1] + " Coins", -1, "/pay");


        return true;
    }
}
