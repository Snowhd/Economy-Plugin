package org.exampls.economy_plugin.api;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.exampls.economy_plugin.database.Database;


import java.util.Optional;
import java.util.UUID;

public interface IEconomyApi {

     Database database = new Database();

    default long getBalance(UUID uuid) {
        Player player = Bukkit.getPlayer(uuid);
        if (player == null) return 0;
        return database.getBalance(player.getName(), Optional.of(uuid.toString()));
    }

    default void setBalance(UUID uuid, long balance) throws Exception {
        database.setBalance(uuid.toString(), balance);
    }

    default void deposit(UUID uuid, long balance) throws Exception {

        database.setBalance(uuid.toString(), getBalance(uuid) + balance);
    }

    default void withdraw(UUID uuid, long balance) throws Exception{
        database.setBalance(uuid.toString(), getBalance(uuid) - balance);
    }
}