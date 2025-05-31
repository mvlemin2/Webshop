package be.webshop.dao;

import be.webshop.connection.DatabaseConnection;
import be.webshop.util.DatabaseConstants;
import be.webshop.util.DatabaseUtils;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO {

    // nieuwe gebruiker registreren in de database
    // true = registratie gelukt // false = registratie niet gelukt
    public boolean register(String username, String password){
        PreparedStatement insertUser = null;
        try {
            //controle of username al bestaat in DB
            if (!checkUser(username)) {
                //DB-connectie met singleton
                Connection connection = DatabaseConnection.getInstance().getConnection();

                //hash password
                String hashedPassword = hashPassword(password);

                //INSERT statement voorbereiden
                insertUser = connection.prepareStatement(
                        "INSERT INTO " + DatabaseConstants.USERS_TABLE + "(username, password)" + " VALUES(?, ?)"
                );

                //parameters voor INSERT statement
                insertUser.setString(1, username);
                insertUser.setString(2, hashedPassword);

                //update DB met nieuwe gebruiker
                insertUser.executeUpdate();
                return true;
            }
        }catch(SQLException | NoSuchAlgorithmException e){
            e.printStackTrace();
        }finally {
            DatabaseUtils.closeQuietly(insertUser);
        }
        return false;
    }

    //Hash password
    private String hashPassword(String password) throws NoSuchAlgorithmException{
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] messageDigest = md.digest(password.getBytes());
        BigInteger bigInt = new BigInteger(1, messageDigest);
        return bigInt.toString(16);
    }

    // controleren of username al bestaat in DB
    // true = username bestaat in DB // false = username bestaat nog niet in DB
    private boolean checkUser(String username){
        PreparedStatement checkUserExists = null;
        ResultSet resultSet = null;
        try{
            Connection connection = DatabaseConnection.getInstance().getConnection();

            checkUserExists = connection.prepareStatement(
                    "SELECT * FROM " + DatabaseConstants.USERS_TABLE + " WHERE USERNAME = ?"
            );
            checkUserExists.setString(1, username);

            resultSet = checkUserExists.executeQuery();

            // controleren of result set leeg is -> indien leeg bestaat gebruiker nog niet
            if(!resultSet.isBeforeFirst()){
                return false;
            }
        }catch(SQLException e){
            e.printStackTrace();
        }finally {
            DatabaseUtils.closeQuietly(resultSet);
            DatabaseUtils.closeQuietly(checkUserExists);
        }
        return true;
    }

    //combinatie username en ww controleren in DB
    private boolean validateLogin(String username, String password){
        PreparedStatement validateUser = null;
        ResultSet resultSet = null;
        try{
            Connection connection = DatabaseConnection.getInstance().getConnection();

            //hash password
            String hashedPassword = hashPassword(password);

            //SELECT
            validateUser = connection.prepareStatement(
                    "SELECT * FROM " + DatabaseConstants.USERS_TABLE + " WHERE USERNAME = ? AND PASSWORD = ?"
            );
            validateUser.setString(1, username);
            validateUser.setString(2, hashedPassword);

            resultSet = validateUser.executeQuery();

            //Indien geen resultaten, login ongeldig
            if(!resultSet.isBeforeFirst()){
                return false;
            }
        }catch(SQLException | NoSuchAlgorithmException e){
            e.printStackTrace();
        }finally {
            DatabaseUtils.closeQuietly(resultSet);
            DatabaseUtils.closeQuietly(validateUser);
        }
        return true;
    }

    public boolean signIn(String username, String password){
        PreparedStatement signIn = null;
        if(validateLogin(username, password)) {
            try {
                Connection connection = DatabaseConnection.getInstance().getConnection();

                signIn = connection.prepareStatement(
                        "UPDATE " + DatabaseConstants.USERS_TABLE + " SET isSignedIn = 1 WHERE USERNAME = ?" );
                signIn.setString(1, username);

                signIn.executeUpdate();
                return true;
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                DatabaseUtils.closeQuietly(signIn);
            }
        }
        return false;
    }

    private boolean checkIsSignedIn(String username){
        PreparedStatement checkIsSignedIn = null;
        ResultSet resultSet = null;
        try {
            Connection connection = DatabaseConnection.getInstance().getConnection();

            checkIsSignedIn = connection.prepareStatement(
                    "SELECT IsSignedIn FROM " + DatabaseConstants.USERS_TABLE + " WHERE USERNAME = ?" );
            checkIsSignedIn.setString(1, username);

            resultSet = checkIsSignedIn.executeQuery();
            while(resultSet.next()){
                return resultSet.getBoolean("isSignedIn");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DatabaseUtils.closeQuietly(resultSet);
            DatabaseUtils.closeQuietly(checkIsSignedIn);
        }
        return false;
    }

    //voor UserService
    public boolean isSignedIn(String username) {
        return checkIsSignedIn(username);
    }

    public boolean signOut(String username){
        PreparedStatement signOut = null;
        if(checkUser(username) && checkIsSignedIn(username)) {
            try {
                Connection connection = DatabaseConnection.getInstance().getConnection();

                signOut = connection.prepareStatement(
                        "UPDATE " + DatabaseConstants.USERS_TABLE + " SET isSignedIn = 0 WHERE USERNAME = ?" );
                signOut.setString(1, username);

                signOut.executeUpdate();
                return true;
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                DatabaseUtils.closeQuietly(signOut);
            }
        }
        return false;
    }
}
