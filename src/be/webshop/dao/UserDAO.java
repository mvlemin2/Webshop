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

    //1. Registreren
    public boolean register(String username, String password){
        PreparedStatement insertUser = null;
        try {
            //controle of username al bestaat in DB
            if (!checkUser(username)) {
                //DB-connectie met singleton
                Connection connection = DatabaseConnection.getInstance().getConnection();

                //Hashing wachtwoord
                String hashedPassword = hashPassword(password);

                //INSERT-statement voorbereiden
                insertUser = connection.prepareStatement(
                        "INSERT INTO " + DatabaseConstants.USERS_TABLE + "(username, password)" + " VALUES(?, ?)"
                );

                //Parameters voor INSERT-statement
                insertUser.setString(1, username);
                insertUser.setString(2, hashedPassword);

                //DB updaten met nieuwe gebruiker
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

    //Hashing wachtwoord
    private String hashPassword(String password) throws NoSuchAlgorithmException{
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] messageDigest = md.digest(password.getBytes());
        BigInteger bigInt = new BigInteger(1, messageDigest);
        return bigInt.toString(16);
    }

    //Controleren of username al bestaat in de database
    public boolean checkUser(String username){
        PreparedStatement checkUserExists = null;
        ResultSet resultSet = null;
        try{
            Connection connection = DatabaseConnection.getInstance().getConnection();

            checkUserExists = connection.prepareStatement(
                    "SELECT * FROM " + DatabaseConstants.USERS_TABLE + " WHERE USERNAME = ?"
            );
            checkUserExists.setString(1, username);

            resultSet = checkUserExists.executeQuery();

            //Indien ResultSet leeg is, bestaat gebruiker nog niet
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

    //Combinatie username en wachtwoord controleren in de database
    private boolean validateLogin(String username, String password){
        PreparedStatement validateUser = null;
        ResultSet resultSet = null;
        try{
            Connection connection = DatabaseConnection.getInstance().getConnection();
            String hashedPassword = hashPassword(password);

            validateUser = connection.prepareStatement(
                    "SELECT * FROM " + DatabaseConstants.USERS_TABLE + " WHERE USERNAME = ? AND PASSWORD = ?"
            );
            validateUser.setString(1, username);
            validateUser.setString(2, hashedPassword);

            resultSet = validateUser.executeQuery();

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

    //2. Aanmelden
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

    //Controleren of gebruiker aangemeld is
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

    //checkIsSignedIn voor UserService
    public boolean isSignedIn(String username) {
        return checkIsSignedIn(username);
    }

    //3. Afmelden
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
