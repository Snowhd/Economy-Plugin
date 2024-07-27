package org.exampls.economy_plugin.database;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.exampls.economy_plugin.EconomyPlugin;
import org.exampls.economy_plugin.logger.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.Optional;

public class Database {

    private final String connectionUrl;
    private final String databaseUsername;
    private final String password;
    private final EconomyPlugin plugin;
    private final Logger logger = new Logger();

    //constructor für die login daten
    public Database() {
    this.plugin = EconomyPlugin.getInstance();

    this.connectionUrl = plugin.ConnectionUrl();
    ArrayList<String> loginData = plugin.LoginData();

    this.databaseUsername = loginData.get(0);
    this.password = loginData.get(1);
}



    //User wird der Datenbank hinzugefügt
    public boolean createUser(@NonNull String username, @NonNull String uuid) {
    try (Connection con = DriverManager.getConnection(connectionUrl, databaseUsername, password)) {

        PreparedStatement testStatement = con.prepareStatement("SELECT * FROM user WHERE uuid = ?");
        testStatement.setString(1, uuid);
        ResultSet resultSetTest = testStatement.executeQuery();

        if(resultSetTest.next()) {

            //username Namens Wechsel Check
            String currentUsername = resultSetTest.getString("username");
            if (!currentUsername.equals(username)) {
                PreparedStatement updateStament = con.prepareStatement("UPDATE user SET username = ? WHERE uuid = ?");
                updateStament.setString(1, username);
                updateStament.setString(2, uuid);
            }
            return true;
        }

        PreparedStatement insertStatement = con.prepareStatement("INSERT INTO user (uuid, username, balance, togglepay) VALUES (?, ?, 0, false)");
        insertStatement.setString(1, uuid);
        insertStatement.setString(2, username);
        int result = insertStatement.executeUpdate();

        if(result > 0){
            new Logger().log("Database Error: SQL Statment: " + insertStatement.toString(), 2, "createUser");
            return true;
        }
        logger.log("couldn't find value", 0, "Database: createUser line 61");
        return false;

    } catch (SQLException e) {
         logger.log(e, 2);
        return false;
    }
}



    public boolean setBalance(String uuid, long balance) throws Exception {

        try (Connection con = DriverManager.getConnection(connectionUrl, databaseUsername, password)) {

            PreparedStatement statement = con.prepareStatement("UPDATE user SET balance = ? WHERE uuid = ?");
            statement.setLong(1, balance);
            statement.setString(2, uuid);

            return statement.executeUpdate() > 0;

        } catch (Exception e) {

            logger.log(e,2);
            return false;
        }
    }

    //funktion, um das Geld von ein benutzer zu bekommen
    public long getBalance(@NonNull String username, Optional<String> uuid) {
    if (uuid.isPresent()) {
        try (Connection con = DriverManager.getConnection(connectionUrl, databaseUsername, password)) {
            PreparedStatement statement = con.prepareStatement("SELECT balance FROM user WHERE uuid = ?");
            statement.setString(1, uuid.get());

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getLong("balance");
            }

            throw new SQLException("User Not found");

        } catch (SQLException e) {
            logger.log(e, 2);
            return -1;
        }
    } else {
        try (Connection con = DriverManager.getConnection(connectionUrl, databaseUsername, password)) {
            PreparedStatement statement = con.prepareStatement("SELECT balance FROM user WHERE username = ?");
            statement.setString(1, username);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getLong("balance");
            }

            throw new SQLException("User Not found");

        } catch (SQLException e) {
             logger.log(e,2);
            return -1;
        }
    }
}



    //funktion zum update des geldes
    public boolean updateBalance(@NonNull String username, Optional<String> uuid, long balance) {
    if (uuid.isPresent()) {
        try (Connection con = DriverManager.getConnection(connectionUrl, databaseUsername, password)) {

            PreparedStatement statement = con.prepareStatement("UPDATE user SET balance = ? WHERE uuid = ?");
            statement.setLong(1, balance);
            statement.setString(2, uuid.get());

            int rowsUpdated = statement.executeUpdate();

            return rowsUpdated > 0;

        } catch (SQLException e) {
            logger.log(e,2);
            return false;
        }
    } else {
        try (Connection con = DriverManager.getConnection(connectionUrl, databaseUsername, password)) {

            PreparedStatement statement = con.prepareStatement("UPDATE user SET balance = ? WHERE username = ?");
            statement.setLong(1, balance);
            statement.setString(2, username);

            int rowsUpdated = statement.executeUpdate();

            return rowsUpdated > 0;

        } catch (SQLException e) {
             logger.log(e,2);
            return false;
        }
    }
}



    //Funktion zum switchen des TogglePayStatuses
    public boolean updateTogglePayStatus(@NonNull String username, @NonNull Optional<String> uuid){
    try(Connection con = DriverManager.getConnection(connectionUrl, databaseUsername, password)) {
        boolean status;
        if(uuid.isPresent()){
            PreparedStatement getStatus = con.prepareStatement("SELECT togglepay FROM user WHERE uuid = ?");
            getStatus.setString(1, uuid.get());
            ResultSet rs = getStatus.executeQuery();
            if (rs.next()) {
                status = rs.getBoolean("togglepay");
            } else {
                logger.log("couldn't find value", 0, "Database, updateTogglePayStatus line 177");
                return false;
            }

            PreparedStatement statement = con.prepareStatement("UPDATE user SET togglepay = ? WHERE uuid = ?");
            statement.setBoolean(1, !status);
            statement.setString(2, uuid.get());
            statement.executeUpdate();

            return !status;
        }

        PreparedStatement getStatus = con.prepareStatement("SELECT togglepay FROM user WHERE username = ?");
        getStatus.setString(1, username);
        ResultSet rs = getStatus.executeQuery();
        if (rs.next()) {
            status = rs.getBoolean("togglepay");
        } else {
            logger.log("couldn't find value", 0, "Database, updateTogglePayStatus line 195");
            return false;
        }

        PreparedStatement statement = con.prepareStatement("UPDATE user SET togglepay = ? WHERE username = ?");
        statement.setBoolean(1, !status);
        statement.setString(2, username);
        statement.executeUpdate();

        return !status;

    } catch (SQLException e) {
        logger.log(e, 2);
        return false;
    }
}


    //Funktion um den ToggleStatus zu sehen
    public boolean getTogglePayStatus(@NonNull String username, @NonNull Optional<String> uuid) {

        try(Connection con = DriverManager.getConnection(connectionUrl, databaseUsername, password)) {

            if(uuid.isPresent()) {

                PreparedStatement getStatus = con.prepareStatement("SELECT togglepay FROM user WHERE uuid = ?");
                getStatus.setString(1, uuid.get());
                ResultSet rs = getStatus.executeQuery();

            if(rs.next()) {
                return rs.getBoolean("togglepay");
            }else{
                logger.log("couldn't find value", 0, "Database, updateTogglePayStatus line 227");
                return false;
            }

            }

            PreparedStatement getStatus = con.prepareStatement("SELECT togglepay FROM user WHERE username = ?");
            getStatus.setString(1, username);
            ResultSet rs = getStatus.executeQuery();

            if(rs.next()) {
                return rs.getBoolean("togglepay");
            }
            logger.log("couldn't find value", 0, "Database, updateTogglePayStatus line 240");
            return false;


        } catch (SQLException e) {
            logger.log(e,2);
            return false;
        }

    }
}
