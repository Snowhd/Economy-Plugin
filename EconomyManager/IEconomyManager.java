package org.ArasiaEconomyManager

import java.sql.*;

public interface IEconomyManager {

      default long viewBalanceUsername(String uuid, String connectionUrl,
                                            String databaseUsername, String password) throws Exception {

        try (Connection con = DriverManager.getConnection(connectionUrl, databaseUsername, password)) {

            PreparedStatement preparedStatement = con.prepareStatement("SELECT * FROM user WHERE uuid= ?");
            preparedStatement.setString(1, uuid);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {

                return resultSet.getLong("balance");

            }

            throw new SQLException("No user with the uuid " + uuid + " was found");

        } catch (Exception e) {
            throw new Exception(e);
        }
    }


     default boolean setBalanceString(String uuid, long balance,
                                            String connectionUrl,
                                            String databaseUsername, String password) throws Exception {

        try (Connection con = DriverManager.getConnection(connectionUrl, databaseUsername, password)) {

            PreparedStatement statement = con.prepareStatement("UPDATE user SET balance = ? WHERE uuid = ?");
            statement.setLong(1, balance);
            statement.setString(2, uuid);

            return statement.executeUpdate() > 0;

        } catch (Exception e) {

            throw new Exception(e);

        }
    }

    default long addBalance(String uuid, long balance,
                                   String connectionUrl,
                                   String databaseUsername, String password) throws Exception {

        try (Connection con = DriverManager.getConnection(connectionUrl, databaseUsername, password)) {

            PreparedStatement preparedStatementGetBalance = con.prepareStatement("SELECT * FROM user WHERE uuid= ?");
            preparedStatementGetBalance.setString(1, uuid);

            ResultSet resultSetOne = preparedStatementGetBalance.executeQuery();

            if (!resultSetOne.next()) {
                throw new SQLException("No user with the uuid " + uuid + " was found");
            }

            long oldBalance = resultSetOne.getLong("balance");

            long newBalance = oldBalance + balance;

            PreparedStatement preparedStatementSetBalance = con.prepareStatement("UPDATE user SET balance = ? WHERE uuid = ?");
            preparedStatementSetBalance.setString(1, String.valueOf(newBalance));
            preparedStatementSetBalance.setString(2, uuid);

            if (preparedStatementSetBalance.executeUpdate() > 0) {
                throw new SQLException("Error while executing the sql statment");
            }

            return newBalance;

        } catch (Exception e) {
            throw new Exception(e);
        }

    }
    default long removeBalance(String uuid, long balance,
                                         String connectionUrl,
                                         String databaseUsername, String password) throws Exception {

        try (Connection con = DriverManager.getConnection(connectionUrl, databaseUsername, password)) {

            PreparedStatement preparedStatementGetBalance = con.prepareStatement("SELECT * FROM user WHERE uuid= ?");
            preparedStatementGetBalance.setString(1, uuid);

            ResultSet resultSetOne = preparedStatementGetBalance.executeQuery();

            if (!resultSetOne.next()) {
                throw new SQLException("No user with the uuid " + uuid + " was found");
            }

            long oldBalance = resultSetOne.getLong("balance");

            long newBalance = oldBalance - balance;

            if(newBalance < 0){
                newBalance = 0;
            }


            PreparedStatement preparedStatementSetBalance = con.prepareStatement("UPDATE user SET balance = ? WHERE uuid = ?");
            preparedStatementSetBalance.setString(1, String.valueOf(newBalance));
            preparedStatementSetBalance.setString(2, uuid);

            if (preparedStatementSetBalance.executeUpdate() > 0) {
                throw new SQLException("Error while executing the sql statment");
            }

            return newBalance;

        } catch (Exception e) {
            throw new Exception(e);
        }

    }

}
