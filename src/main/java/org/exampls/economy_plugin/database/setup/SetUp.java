package org.exampls.economy_plugin.database.setup;

import org.exampls.economy_plugin.EconomyPlugin;
import org.exampls.economy_plugin.logger.Logger;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.ArrayList;

public class SetUp {

    private final String connectionUrl;
    private final String username;
    private final String password;
    private final EconomyPlugin plugin;
    private final Logger logger;

    public SetUp(EconomyPlugin plugin) {
        this.plugin = plugin;
        this.connectionUrl = plugin.ConnectionUrl();
        ArrayList<String> loginData = plugin.LoginData();
        this.logger = new Logger();
        this.username = loginData.get(0);
        this.password = loginData.get(1);
    }

    public boolean database() {
        try (Connection con = DriverManager.getConnection(connectionUrl, username, password)) {

            InputStreamReader streamReader = new InputStreamReader(plugin.getClass().getResourceAsStream("/DatabaseSetUp.sql"));
            BufferedReader reader = new BufferedReader(streamReader);

            String line;
            while ((line = reader.readLine()) != null) {
                try (Statement statement = con.createStatement()) {
                    statement.execute(line);
                    logger.log("SQL Statments was executed", -1, "Plugin Start: Database SetUp");
                }
            }

            return true;
        } catch (Exception e) {
            logger.log(e, 2);
            return false;
        }
    }

}
