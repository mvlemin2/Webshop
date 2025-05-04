package be.webshop.connection;

import java.sql.*;

public class MyJDBC {
    public static final String DB_URL = "jdbc:mysql://dt5.ehb.be:3306/2425PROGPROJWT01";
    public static final String DB_USERNAME = "2425PROGPROJWT01";
    public static final String DB_PASSWORD = "yDHw4Xu";
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
        try {
            //controle of username al bestaat in DB
            if (!checkUser(username)) {
                //DB-connectie
                Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);

                //INSERT
                PreparedStatement insertUser = connection.prepareStatement(
                        "INSERT INTO " + DB_USERS_TABLE_NAME + "(username, password)" + " VALUES(?, ?)"
                );

                //parameters voor INSERT
                insertUser.setString(1, username);
                insertUser.setString(2, password);

                //update DB met nieuwe gebruiker
                insertUser.executeUpdate();
                return true;
            }
        }catch(SQLException e){
            e.printStackTrace();
            }
        return false;
    }

    // controleren of username al bestaat in DB
    // true = username bestaat in DB // false = username bestaat nog niet in DB
    private static boolean checkUser(String username){
        try{
            Connection connection = DriverManager.getConnection(DB_URL,DB_USERNAME,DB_PASSWORD);

            PreparedStatement checkUserExists = connection.prepareStatement(
                    "SELECT * FROM " + DB_USERS_TABLE_NAME + " WHERE USERNAME = ?"
            );
            checkUserExists.setString(1, username);

            ResultSet resultSet = checkUserExists.executeQuery();

            // controleren of result set leeg is -> indien leeg bestaat gebruiker nog niet
            if(!resultSet.isBeforeFirst()){
                return false;
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return true;
    }

    //combinatie username en ww controleren in DB
    public static boolean validateLogin(String username, String password){
        try{
            Connection connection = DriverManager.getConnection(DB_URL,DB_USERNAME,DB_PASSWORD);

            //SELECT
            PreparedStatement validateUser = connection.prepareStatement(
                    "SELECT * FROM " + DB_USERS_TABLE_NAME + " WHERE USERNAME = ? AND PASSWORD = ?"
            );
            validateUser.setString(1, username);
            validateUser.setString(2, password);

            ResultSet resultSet = validateUser.executeQuery();

            if(!resultSet.isBeforeFirst()){
                return false;
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return true;
    }

    public static boolean signIn(String username, String password){
        if(validateLogin(username, password)) {
            try {
                Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);

                PreparedStatement signIn = connection.prepareStatement(
                        "UPDATE " + DB_USERS_TABLE_NAME + " SET isSignedIn = 1 WHERE USERNAME = ?" );
                signIn.setString(1, username);

                signIn.executeUpdate();
                return true;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    private static boolean checkIsSignedIn(String username){
        try {
            Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);

            PreparedStatement checkIsSignedIn = connection.prepareStatement(
                    "SELECT IsSignedIn FROM " + DB_USERS_TABLE_NAME + " WHERE USERNAME = ?" );
            checkIsSignedIn.setString(1, username);

            ResultSet resultSet = checkIsSignedIn.executeQuery();
            //return resultSet.getBoolean("isSignedIn");
            while(resultSet.next()){
                return resultSet.getBoolean("isSignedIn");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean signOut(String username){
        if(checkUser(username) == true && checkIsSignedIn(username) == true) {
            try {
                Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);

                PreparedStatement signOut = connection.prepareStatement(
                        "UPDATE " + DB_USERS_TABLE_NAME + " SET isSignedIn = 0 WHERE USERNAME = ?" );
                signOut.setString(1, username);

                signOut.executeUpdate();
                return true;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public static boolean addToWishlist(int product_id, String username){
        //check if user is logged in
        if(checkIsSignedIn(username)){
            //check if product is already added to wishlist
            if(!checkInWishlist(product_id, username)){
                //add to wishlist
                try {
                    Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);

                    PreparedStatement getUserID = connection.prepareStatement(
                            "SELECT user_id FROM " + DB_USERS_TABLE_NAME + " WHERE username = " + "\'" + username + "\'");
                    ResultSet rs = getUserID.executeQuery();
                    int user_id_query = 0;
                    while(rs.next()){
                        user_id_query = rs.getInt("user_id");
                    }

                    PreparedStatement addToWishlist = connection.prepareStatement(
                            "INSERT INTO " + DB_WISHLISTLINES_TABLE_NAME + "(product_id, user_id) VALUES(?, ?)");

                    addToWishlist.setInt(1, product_id);
                    addToWishlist.setInt(2, user_id_query);

                    addToWishlist.executeUpdate();
                    return true;
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

    private static boolean checkInWishlist(int product_id, String username){
        try {
            Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);

            PreparedStatement getUserID = connection.prepareStatement(
                    "SELECT user_id FROM " + DB_USERS_TABLE_NAME + " WHERE username = " + "\'" + username + "\'");
            ResultSet rs = getUserID.executeQuery();
            int user_id_query = 0;
            while(rs.next()){
                user_id_query = rs.getInt("user_id");
            }

            PreparedStatement checkInWishList = connection.prepareStatement(
                    "SELECT * FROM " + DB_WISHLISTLINES_TABLE_NAME + " WHERE PRODUCT_ID = " + product_id + " AND USER_ID = " + user_id_query);

            ResultSet resultSet = checkInWishList.executeQuery();
            // controleren of result set leeg is -> indien leeg bestaat entry nog niet
            if(!resultSet.isBeforeFirst()){
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }

    public static boolean removeFromWishlist(int product_id, String username){
        //check if user is logged in
        if(checkIsSignedIn(username)){
            //check if product is already added to wishlist
            if(checkInWishlist(product_id, username)){
                //remove from wishlist
                try {
                    Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);

                    PreparedStatement getUserID = connection.prepareStatement(
                            "SELECT user_id FROM " + DB_USERS_TABLE_NAME + " WHERE username = " + "\'" + username + "\'");
                    ResultSet rs = getUserID.executeQuery();
                    int user_id_query = 0;
                    while(rs.next()){
                        user_id_query = rs.getInt("user_id");
                    }

                    PreparedStatement removeFromWishlist = connection.prepareStatement(
                            "DELETE FROM " + DB_WISHLISTLINES_TABLE_NAME + " WHERE product_id = " + product_id + " AND user_id = " + user_id_query);

                    removeFromWishlist.executeUpdate();
                    return true;
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

    public static boolean removeAllFromWishlist(String username){
        //check if user is logged in
        if(checkIsSignedIn(username)){
                //remove all from wishlist
                try {
                    Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);

                    PreparedStatement getUserID = connection.prepareStatement(
                            "SELECT user_id FROM " + DB_USERS_TABLE_NAME + " WHERE username = " + "\'" + username + "\'");
                    ResultSet rs = getUserID.executeQuery();
                    int user_id_query = 0;
                    while(rs.next()){
                        user_id_query = rs.getInt("user_id");
                    }

                    PreparedStatement removeAllFromWishlist = connection.prepareStatement(
                            "DELETE FROM " + DB_WISHLISTLINES_TABLE_NAME + " WHERE user_id = " + user_id_query);

                    removeAllFromWishlist.executeUpdate();
                    return true;
                } catch (SQLException e) {
                    e.printStackTrace();
                }
        }
        return false;
    }

    public static void displayWishlist(String username) {
        //check if user is logged in
        if (checkIsSignedIn(username)) {
            //display wishlist
            try {
                Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);

                PreparedStatement getUserID = connection.prepareStatement(
                        "SELECT user_id FROM " + DB_USERS_TABLE_NAME + " WHERE username = " + "\'" + username + "\'" );
                ResultSet rs = getUserID.executeQuery();
                int user_id_query = 0;
                while (rs.next()) {
                    user_id_query = rs.getInt("user_id" );
                }

                PreparedStatement displayWishlist = connection.prepareStatement(
                        "SELECT plantName, plantNameLatin, plantPrice, plantCategory, plantLocation, plantColor, plantDescription FROM " + DB_PLANTS_TABLE_NAME + " p JOIN " + DB_WISHLISTLINES_TABLE_NAME + " wl ON(p.product_id = wl.product_id) WHERE user_id = " + user_id_query);

                ResultSet wishlist = displayWishlist.executeQuery();
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
        ////System.out.println(MyJDBC.checkUser("testuser1"));

        //check register test
        ////System.out.println(MyJDBC.register("username","password"));

        //check validate login test
        ////System.out.println(MyJDBC.validateLogin("username","password"));
        ////niet gelukt omdat dit binary werd opgeslagen in de DB => verder uitwerken

        //check signIn test
        ////System.out.println(MyJDBC.signIn("username","password"));

        //check isSignedIn
        ////System.out.println(MyJDBC.checkIsSignedIn("username123"));

        //check SignOut
//        System.out.println(MyJDBC.checkIsSignedIn("username"));
//        System.out.println(MyJDBC.signOut("username"));
//        System.out.println(MyJDBC.checkIsSignedIn("username"));

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
        MyJDBC.displayWishlist("username123");
    }
}
