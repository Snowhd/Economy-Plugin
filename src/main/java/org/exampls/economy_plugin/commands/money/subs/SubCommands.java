package org.exampls.economy_plugin.commands.money.subs;

import org.bukkit.entity.Player;

public class SubCommands {

    public long calculateBalance(long b1, long b2){
        return b1 + b2;
    }

   public String getUUID(Player player){
        return player.getUniqueId().toString();
    }

}
