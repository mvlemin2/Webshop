package be.webshop.connection;

import be.webshop.util.DatabaseUtils;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;

public class MyJDBC {
    ////public static final String DB_URL = "jdbc:mysql://dt5.ehb.be:3306/2425PROGPROJWT01";
    ////public static final String DB_USERNAME = "2425PROGPROJWT01";
    ////public static final String DB_PASSWORD = "yDHw4Xu";
    public static final String DB_USERS_TABLE_NAME = "Users";
    public static final String DB_PLANTS_TABLE_NAME = "Plants";
    public static final String DB_WISHLISTLINES_TABLE_NAME = "Wishlist_lines";

//    public static void connect(){
//        try{
//            Connection connection = DriverManager.getConnection(
//                    DB_URL,DB_USERNAME,DB_PASSWORD
//            );
//        }catch(SQLException e){
//            e.printStackTrace();
//        }
//    }

    // nieuwe gebruiker registreren in de database
    // true = registratie gelukt // false = registratie niet gelukt
    public static boolean register(String username, String password){
        PreparedStatement insertUser = null;
        try {
            //controle of username al bestaat in DB
            if (!checkUser(username)) {
                //DB-connectie met singleton
                Connection connection = DatabaseConnection.getInstance().getConnection();
                ////ipv//// DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);

                    //hash password
                    String hashedPassword = hashPassword(password);

                    //INSERT statement voorbereiden
                    insertUser = connection.prepareStatement(
                            "INSERT INTO " + DB_USERS_TABLE_NAME + "(username, password)" + " VALUES(?, ?)"
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
    private static String hashPassword(String password) throws NoSuchAlgorithmException{
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] messageDigest = md.digest(password.getBytes());
        BigInteger bigInt = new BigInteger(1, messageDigest);
        return bigInt.toString(16);
    }

    // controleren of username al bestaat in DB
    // true = username bestaat in DB // false = username bestaat nog niet in DB
    private static boolean checkUser(String username){
        PreparedStatement checkUserExists = null;
        ResultSet resultSet = null;
        try{
            ////Connection connection = DriverManager.getConnection(DB_URL,DB_USERNAME,DB_PASSWORD);
            Connection connection = DatabaseConnection.getInstance().getConnection();

            checkUserExists = connection.prepareStatement(
                    "SELECT * FROM " + DB_USERS_TABLE_NAME + " WHERE USERNAME = ?"
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
    public static boolean validateLogin(String username, String password){
        PreparedStatement validateUser = null;
        ResultSet resultSet = null;
        try{
            Connection connection = DatabaseConnection.getInstance().getConnection();
            ////Connection connection = DriverManager.getConnection(DB_URL,DB_USERNAME,DB_PASSWORD);

            //hash password
            String hashedPassword = hashPassword(password);

            //SELECT
            validateUser = connection.prepareStatement(
                    "SELECT * FROM " + DB_USERS_TABLE_NAME + " WHERE USERNAME = ? AND PASSWORD = ?"
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

    public static boolean signIn(String username, String password){
        PreparedStatement signIn = null;
        if(validateLogin(username, password)) {
            try {
                Connection connection = DatabaseConnection.getInstance().getConnection();
                ////Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);

                signIn = connection.prepareStatement(
                        "UPDATE " + DB_USERS_TABLE_NAME + " SET isSignedIn = 1 WHERE USERNAME = ?" );
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

    private static boolean checkIsSignedIn(String username){
        PreparedStatement checkIsSignedIn = null;
        ResultSet resultSet = null;
        try {
            ////Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
            Connection connection = DatabaseConnection.getInstance().getConnection();

            checkIsSignedIn = connection.prepareStatement(
                    "SELECT IsSignedIn FROM " + DB_USERS_TABLE_NAME + " WHERE USERNAME = ?" );
            checkIsSignedIn.setString(1, username);

            resultSet = checkIsSignedIn.executeQuery();
            //return resultSet.getBoolean("isSignedIn");
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

    public static boolean signOut(String username){
        PreparedStatement signOut = null;
        if(checkUser(username) == true && checkIsSignedIn(username) == true) {
            try {
                ////Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
                Connection connection = DatabaseConnection.getInstance().getConnection();

                signOut = connection.prepareStatement(
                        "UPDATE " + DB_USERS_TABLE_NAME + " SET isSignedIn = 0 WHERE USERNAME = ?" );
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

    public static boolean addToWishlist(int product_id, String username){
        PreparedStatement getUserID = null;
        PreparedStatement addToWishlist = null;
        ResultSet resultSet = null;
        //check if user is logged in
        if(checkIsSignedIn(username)){
            //check if product is already added to wishlist
            if(!checkInWishlist(product_id, username)){
                //add to wishlist
                try {
                    Connection connection = DatabaseConnection.getInstance().getConnection();
                    ////Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);

                    getUserID = connection.prepareStatement(
                            "SELECT user_id FROM " + DB_USERS_TABLE_NAME + " WHERE username = ?");
                    getUserID.setString(1, username);

                    resultSet = getUserID.executeQuery();

                    int user_id_query = 0;
                    while(resultSet.next()){
                        user_id_query = resultSet.getInt("user_id");
                    }

                    addToWishlist = connection.prepareStatement(
                            "INSERT INTO " + DB_WISHLISTLINES_TABLE_NAME + "(product_id, user_id) VALUES(?, ?)");

                    addToWishlist.setInt(1, product_id);
                    addToWishlist.setInt(2, user_id_query);

                    addToWishlist.executeUpdate();
                    return true;
                } catch (SQLException e) {
                    e.printStackTrace();
                } finally {
                    DatabaseUtils.closeQuietly(resultSet);
                    DatabaseUtils.closeQuietly(getUserID);
                    DatabaseUtils.closeQuietly(addToWishlist);
                }
            }
        }
        return false;
    }

    private static boolean checkInWishlist(int product_id, String username){
        PreparedStatement getUserID = null;
        PreparedStatement checkInWishList = null;
        ResultSet rs = null;
        ResultSet resultSet = null;
        try {
            ////Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
            Connection connection = DatabaseConnection.getInstance().getConnection();

            getUserID = connection.prepareStatement(
                    "SELECT user_id FROM " + DB_USERS_TABLE_NAME + " WHERE username = ?");
            getUserID.setString(1, username);
            rs = getUserID.executeQuery();

            int user_id_query = 0;
            while(rs.next()){
                user_id_query = rs.getInt("user_id");
            }

            checkInWishList = connection.prepareStatement(
                    "SELECT * FROM " + DB_WISHLISTLINES_TABLE_NAME + " WHERE PRODUCT_ID = ? AND USER_ID = ?");
            checkInWishList.setInt(1, product_id);
            checkInWishList.setInt(2, user_id_query);

            resultSet = checkInWishList.executeQuery();
            // controleren of result set leeg is -> indien leeg bestaat entry nog niet
            if(!resultSet.isBeforeFirst()){
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DatabaseUtils.closeQuietly(rs);
            DatabaseUtils.closeQuietly(getUserID);
            DatabaseUtils.closeQuietly(resultSet);
            DatabaseUtils.closeQuietly(checkInWishList);
        }
        return true;
    }

    public static boolean removeFromWishlist(int product_id, String username){
        PreparedStatement getUserID = null;
        PreparedStatement removeFromWishlist = null;
        ResultSet resultSet = null;
        //check if user is logged in
        if(checkIsSignedIn(username)){
            //check if product is already added to wishlist
            if(checkInWishlist(product_id, username)){
                //remove from wishlist
                try {
                    Connection connection = DatabaseConnection.getInstance().getConnection();
                    ////Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);

                    getUserID = connection.prepareStatement(
                            "SELECT user_id FROM " + DB_USERS_TABLE_NAME + " WHERE username = ?");
                    getUserID.setString(1, username);
                    resultSet = getUserID.executeQuery();

                    int user_id_query = 0;
                    while(resultSet.next()){
                        user_id_query = resultSet.getInt("user_id");
                    }

                    removeFromWishlist = connection.prepareStatement(
                            "DELETE FROM " + DB_WISHLISTLINES_TABLE_NAME + " WHERE product_id = ? AND user_id = ?");
                    removeFromWishlist.setInt(1, product_id);
                    removeFromWishlist.setInt(2, user_id_query);

                    removeFromWishlist.executeUpdate();
                    return true;
                } catch (SQLException e) {
                    e.printStackTrace();
                } finally {
                    DatabaseUtils.closeQuietly(resultSet);
                    DatabaseUtils.closeQuietly(getUserID);
                    DatabaseUtils.closeQuietly(removeFromWishlist);
                }
            }
        }
        return false;
    }

    public static boolean removeAllFromWishlist(String username){
        PreparedStatement getUserID = null;
        PreparedStatement removeAllFromWishlist = null;
        ResultSet resultSet = null;
        //check if user is logged in
        if(checkIsSignedIn(username)){
                //remove all from wishlist
                try {
                    Connection connection = DatabaseConnection.getInstance().getConnection();
                    ////Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);

                    getUserID = connection.prepareStatement(
                            "SELECT user_id FROM " + DB_USERS_TABLE_NAME + " WHERE username = ?");
                    getUserID.setString(1, username);
                    resultSet = getUserID.executeQuery();

                    int user_id_query = 0;
                    while(resultSet.next()){
                        user_id_query = resultSet.getInt("user_id");
                    }

                    removeAllFromWishlist = connection.prepareStatement(
                            "DELETE FROM " + DB_WISHLISTLINES_TABLE_NAME + " WHERE user_id = ?");
                    removeAllFromWishlist.setInt(1, user_id_query);

                    removeAllFromWishlist.executeUpdate();
                    return true;
                } catch (SQLException e) {
                    e.printStackTrace();
                } finally {
                    DatabaseUtils.closeQuietly(resultSet);
                    DatabaseUtils.closeQuietly(getUserID);
                    DatabaseUtils.closeQuietly(removeAllFromWishlist);
                }
        }
        return false;
    }

    public static void displayWishlist(String username) {
        PreparedStatement getUserID = null;
        PreparedStatement displayWishlist = null;
        ResultSet resultSet = null;
        ResultSet wishlist = null;
        //check if user is logged in
        if (checkIsSignedIn(username)) {
            //display wishlist
            try {
                Connection connection = DatabaseConnection.getInstance().getConnection();
                ////Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);

                getUserID = connection.prepareStatement(
                        "SELECT user_id FROM " + DB_USERS_TABLE_NAME + " WHERE username = ?");
                getUserID.setString(1, username);
                resultSet = getUserID.executeQuery();

                int user_id_query = 0;
                while (resultSet.next()) {
                    user_id_query = resultSet.getInt("user_id" );
                }

                displayWishlist = connection.prepareStatement(
                        "SELECT plantName, plantNameLatin, plantPrice, plantCategory, plantLocation, plantColor, plantDescription " + "FROM " + DB_PLANTS_TABLE_NAME + " p " + "JOIN " + DB_WISHLISTLINES_TABLE_NAME + " wl ON p.product_id = wl.product_id " + "WHERE wl.user_id = ?");
                displayWishlist.setInt(1, user_id_query);

                wishlist = displayWishlist.executeQuery();
                ResultSetMetaData rsmd = wishlist.getMetaData();

                System.out.println("|| Wishlist van " + username + " ||");
                int columnsNumber = rsmd.getColumnCount();
                while (wishlist.next()) {
                    for (int i = 1; i <= columnsNumber; i++) {
                        //if (i > 1) System.out.print(",  ");
                        String columnValue = wishlist.getString(i);
                        System.out.print(rsmd.getColumnName(i) + ": " + columnValue + " | ");
                    }
                    System.out.println("");
                }

            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                DatabaseUtils.closeQuietly(resultSet);
                DatabaseUtils.closeQuietly(getUserID);
                DatabaseUtils.closeQuietly(wishlist);
                DatabaseUtils.closeQuietly(displayWishlist);
            }
        } else {
            System.err.println("Je bent nog niet ingelogd" );
        }
    }
    public static void main(String[] args) {
//        try{
//            Connection connection = DriverManager.getConnection(
////                    "jdbc:mysql://dt5.ehb.be:3306/2425PROGPROJWT01",
////                    "2425PROGPROJWT01",
////                    "yDHw4Xu"
//                    DB_URL,DB_USERNAME,DB_PASSWORD
//            );
//
//            Statement statement = connection.createStatement();
//            ResultSet resultSet = statement.executeQuery("SELECT * FROM " + DB_PLANTS_TABLE_NAME);
//
//            while(resultSet.next()){
//                System.out.println(resultSet.getString("plantName"));
//                System.out.println(resultSet.getString("plantPrice"));
//            }
//        }catch(SQLException e){
//            e.printStackTrace();
//        }

        //check user test
        //System.out.println(MyJDBC.checkUser("username1234"));

        //check register test
        //System.out.println(MyJDBC.register("usernamehashed4","passwordhashed4"));

        //check validate login test
        ////System.out.println(MyJDBC.validateLogin("username","password"));
        ////niet gelukt omdat dit binary werd opgeslagen in de DB => verder uitwerken

        //check signIn test
        //System.out.println(MyJDBC.signIn("usernamehashed2","passwordhashed2"));

        //check isSignedIn
        //System.out.println(MyJDBC.checkIsSignedIn("usernamehashed2"));

        //check SignOut
//        System.out.println(MyJDBC.checkIsSignedIn("usernamehashed2"));
//        System.out.println(MyJDBC.signOut("usernamehashed2"));
//        System.out.println(MyJDBC.checkIsSignedIn("usernamehashed2"));

        //check addToWishlist test
        //MyJDBC.signIn("username","password");
        //System.out.println(MyJDBC.addToWishlist(2,"username"));

        //check removeFromWishlist test
        //System.out.println(MyJDBC.removeFromWishlist(2,"username"));

        //check removeAllFromWishlist test
//        MyJDBC.signIn("username","password");
//        MyJDBC.signIn("username123","test");
//        System.out.println(MyJDBC.addToWishlist(2,"username"));
//        System.out.println(MyJDBC.addToWishlist(1,"username"));
//        System.out.println(MyJDBC.addToWishlist(3,"username"));
//        System.out.println(MyJDBC.addToWishlist(4,"username123"));
//        System.out.println(MyJDBC.addToWishlist(5,"username123"));
//        System.out.println(MyJDBC.addToWishlist(6,"username123"));
        //System.out.println(MyJDBC.removeAllFromWishlist("username"));

        //test display wishlist
        //MyJDBC.displayWishlist("username123");
    }
}
