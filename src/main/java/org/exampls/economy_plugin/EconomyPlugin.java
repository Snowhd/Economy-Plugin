package org.exampls.economy_plugin;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.exampls.economy_plugin.commands.Pay;
import org.exampls.economy_plugin.commands.PayAll;
import org.exampls.economy_plugin.commands.TogglePay;
import org.exampls.economy_plugin.commands.ViewBalance;
import org.exampls.economy_plugin.commands.money.Money;
import org.exampls.economy_plugin.database.Database;
import org.exampls.economy_plugin.database.setup.SetUp;
import org.exampls.economy_plugin.events.CreateBalanceAccount;

import java.io.File;
import java.util.ArrayList;

public final class EconomyPlugin extends JavaPlugin {

    private FileConfiguration databaseConfig;
    private String ConnectionUrl;
    private static EconomyPlugin instance;


    @Override
    public void onEnable() {
        // Plugin startup logic
        getLogger().info("Start des Economy Plugin's");
        saveDefaultConfig();

        instance = this;

        //Database Setup
        getLogger().info("Die Datenbank wird aufgesetzt");
        loadDatabaseSetUp();
        if(!databaseSetUp(instance)){
            getLogger().info("Kritischer Fehler beim Einrichten der Datenbank");
            //INFO: Only Debug usage
            //getServer().shutdown();
            return;
        }
        getLogger().info("Die Datenbank wurde erfolgreich Aufgesetzt");

        //Command setup

        getLogger().info("Setting up Commands");

        getCommand("money").setExecutor(new Money());
        getCommand("pay").setExecutor(new Pay());
        getCommand("togglepay").setExecutor(new TogglePay());
        getCommand("viewbalance").setExecutor(new ViewBalance());
        getCommand("payall").setExecutor(new PayAll());
        //Listener Setting up

        getLogger().info("Setting up Listener");

        getServer().getPluginManager().registerEvents(new CreateBalanceAccount(new Database()), this);

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }



     //loading the Database connection informationen
    private void loadDatabaseSetUp() {
        File databaseFile = new File("plugins/economy-plugin/database.yml");

        if(!databaseFile.exists()) {
            saveResource("database.yml", false);
        }

        databaseConfig = YamlConfiguration.loadConfiguration(databaseFile);

        ConnectionUrl = String.format("jdbc:mysql://%s:%d/%s?useSSL=false",
                databaseConfig.getString("Host"),
                databaseConfig.getInt("Port"),
                databaseConfig.getString("Database"));
    }



    private boolean databaseSetUp(EconomyPlugin plugin) {
        return new SetUp(plugin).database();
    }


    //getter Method, to get the database.yml variables
    public FileConfiguration getDatabaseConfig()  {
        return databaseConfig;
    }
    //getter method to get the connection url
    public String ConnectionUrl() {
        return ConnectionUrl;
    }

    //login data| 0: username, 1: Password
    public ArrayList<String> LoginData() {


        ArrayList<String> data = new ArrayList<String>();

        data.add(databaseConfig.getString("Username"));
        data.add(databaseConfig.getString("Password"));
        
        return data;
    }

    public static EconomyPlugin getInstance(){

        return instance;
    }

}
