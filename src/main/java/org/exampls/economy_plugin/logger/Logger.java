package org.exampls.economy_plugin.logger;

import org.exampls.economy_plugin.EconomyPlugin;
import org.jetbrains.annotations.NotNull;

public class Logger {

    private EconomyPlugin plugin;

    public Logger() {
        this.plugin = EconomyPlugin.getInstance();
    }


    public void log(@NotNull Exception exception, @NotNull int level) {

        switch(level) {

            case 0:
                infoLog(exception);
                break;
            case 1:
                warningLog(exception);
                break;
            case 2:
                errorLog(exception);
                break;
            default:
                debugLog(exception);

     }
    }

    public void log(@NotNull String message, @NotNull int level, @NotNull String location) {
    switch(level) {

            case 0:
                infoLog(message, location);
                break;
            case 1:
                warningLog(message, location);
                break;
            case 2:
                errorLog(message, location);
                break;
            default:
                debugLog(message, location);

     }
    }


    private void errorLog(@NotNull String message, @NotNull String location) {
        plugin.getLogger().info("§4[ERROR] Arasia-Economy-Plugin");
        plugin.getLogger().info("§4[LOCATION]: " + location);
        plugin.getLogger().info("§4[MESSAGE]: " + message);
    }

    private void warningLog(@NotNull String message, @NotNull String location) {
        plugin.getLogger().info("§e[WARN] Arsia-Economy-Plugin");
        plugin.getLogger().info("§e[Location]: " + location);
        plugin.getLogger().info("§e[Message]: " + message);
    }

    private void infoLog(@NotNull String message, @NotNull String location) {
        plugin.getLogger().info("§7[INFO] Arsia-Economy-Plugin");
        plugin.getLogger().info("§7[Location]: " + location);
        plugin.getLogger().info("§7[Message]: " + message);
    }

    private void debugLog(@NotNull String message, @NotNull String location) {
        plugin.getLogger().info("§9[INFO] Arsia-Economy-Plugin");
        plugin.getLogger().info("§9[Location]: " + location);
        plugin.getLogger().info("§9[Message]: " + message);
    }

    private void errorLog(@NotNull Exception exception) {
        plugin.getLogger().info("§4[ERROR] Arasia-Economy-Plugin");
        plugin.getLogger().info("§4[MESSAGE]: " + exception.getMessage());
    }

    private void warningLog(@NotNull Exception exception) {
        plugin.getLogger().info("§e[WARN] Arasia-Economy-Plugin");
        plugin.getLogger().info("§e[MESSAGE]: " + exception.getMessage());
    }

    private void infoLog(@NotNull Exception exception) {
        plugin.getLogger().info("§7[INFO] Arasia-Economy-Plugin");
        plugin.getLogger().info("§7[MESSAGE]: " + exception.getMessage());
    }

    private void debugLog(@NotNull Exception exception) {
        plugin.getLogger().info("§9[ERROR] Arasia-Economy-Plugin");
        plugin.getLogger().info("§9[MESSAGE]: " + exception.getMessage());
    }
}