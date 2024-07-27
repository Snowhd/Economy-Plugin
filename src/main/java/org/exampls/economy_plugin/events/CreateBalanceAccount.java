package org.exampls.economy_plugin.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.exampls.economy_plugin.database.Database;

public class CreateBalanceAccount implements Listener {

    private final Database database;

    public CreateBalanceAccount(Database database) {
        this.database = database;
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void OnPlayerJoin(PlayerJoinEvent event){
        database.createUser(event.getPlayer().getName(), event.getPlayer().getUniqueId().toString());

    }
}