package be.webshop.dao;

import be.webshop.model.Plant;
import be.webshop.service.UserService;
import be.webshop.connection.DatabaseConnection;
import be.webshop.util.DatabaseConstants;
import be.webshop.util.DatabaseUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class WishlistDAO {
    //private final UserService userService = new UserService();

    public boolean addToWishlist(int product_id, String username){
        PreparedStatement getUserID = null;
        PreparedStatement addToWishlist = null;
        ResultSet resultSet = null;
        ////check if user is logged in
        //if(userService.isSignedIn(username)){
            //check if product is already added to wishlist
            if(!checkInWishlist(product_id, username)){
                //add to wishlist
                try {
                    Connection connection = DatabaseConnection.getInstance().getConnection();

                    getUserID = connection.prepareStatement(
                            "SELECT user_id FROM " + DatabaseConstants.USERS_TABLE + " WHERE username = ?");
                    getUserID.setString(1, username);

                    resultSet = getUserID.executeQuery();

                    int user_id_query = 0;
                    while(resultSet.next()){
                        user_id_query = resultSet.getInt("user_id");
                    }

                    //Controleren of product_id bestaat in database
                    boolean succes = checkProductExists(connection, product_id);
                    if(!succes){
                        return false;
                    }

                    addToWishlist = connection.prepareStatement(
                            "INSERT INTO " + DatabaseConstants.WISHLISTLINES_TABLE + "(product_id, user_id) VALUES(?, ?)");

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
        //}
        return false;
    }

    private boolean checkProductExists(Connection connection, int product_id) throws SQLException {
        PreparedStatement checkProductExists = null;
        ResultSet productResult = null;

        try {
            checkProductExists = connection.prepareStatement(
                    "SELECT 1 FROM " + DatabaseConstants.PLANTS_TABLE + " WHERE product_id = ?");
            checkProductExists.setInt(1, product_id);
            productResult = checkProductExists.executeQuery();

            if (!productResult.next()) {
                //System.out.println("Product met ID " + product_id + " bestaat niet.");
                return false;
            }
        } finally {
            DatabaseUtils.closeQuietly(productResult);
            DatabaseUtils.closeQuietly(checkProductExists);
        }
        return true;
    }

    private boolean checkInWishlist(int product_id, String username){
        PreparedStatement getUserID = null;
        PreparedStatement checkInWishList = null;
        ResultSet rs = null;
        ResultSet resultSet = null;
        try {
            Connection connection = DatabaseConnection.getInstance().getConnection();

            getUserID = connection.prepareStatement(
                    "SELECT user_id FROM " + DatabaseConstants.USERS_TABLE + " WHERE username = ?");
            getUserID.setString(1, username);
            rs = getUserID.executeQuery();

            int user_id_query = 0;
            while(rs.next()){
                user_id_query = rs.getInt("user_id");
            }

            checkInWishList = connection.prepareStatement(
                    "SELECT * FROM " + DatabaseConstants.WISHLISTLINES_TABLE + " WHERE PRODUCT_ID = ? AND USER_ID = ?");
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

    public boolean removeFromWishlist(int product_id, String username){
        PreparedStatement getUserID = null;
        PreparedStatement removeFromWishlist = null;
        ResultSet resultSet = null;
        ////check if user is logged in
        //if(userService.isSignedIn(username)){
            //check if product is already added to wishlist
            if(checkInWishlist(product_id, username)){
                //remove from wishlist
                try {
                    Connection connection = DatabaseConnection.getInstance().getConnection();

                    getUserID = connection.prepareStatement(
                            "SELECT user_id FROM " + DatabaseConstants.USERS_TABLE + " WHERE username = ?");
                    getUserID.setString(1, username);
                    resultSet = getUserID.executeQuery();

                    int user_id_query = 0;
                    while(resultSet.next()){
                        user_id_query = resultSet.getInt("user_id");
                    }

                    removeFromWishlist = connection.prepareStatement(
                            "DELETE FROM " + DatabaseConstants.WISHLISTLINES_TABLE + " WHERE product_id = ? AND user_id = ?");
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
        //}
        return false;
    }

    public boolean removeAllFromWishlist(String username){
        PreparedStatement getUserID = null;
        PreparedStatement removeAllFromWishlist = null;
        ResultSet resultSet = null;
        ////check if user is logged in
        //if(userService.isSignedIn(username)){
            //remove all from wishlist
            try {
                Connection connection = DatabaseConnection.getInstance().getConnection();

                getUserID = connection.prepareStatement(
                        "SELECT user_id FROM " + DatabaseConstants.USERS_TABLE + " WHERE username = ?");
                getUserID.setString(1, username);
                resultSet = getUserID.executeQuery();

                int user_id_query = 0;
                while(resultSet.next()){
                    user_id_query = resultSet.getInt("user_id");
                }

                removeAllFromWishlist = connection.prepareStatement(
                        "DELETE FROM " + DatabaseConstants.WISHLISTLINES_TABLE + " WHERE user_id = ?");
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
        //}
        return false;
    }

//    public boolean displayWishlist(String username) {
//        PreparedStatement getUserID = null;
//        PreparedStatement displayWishlist = null;
//        ResultSet resultSet = null;
//        ResultSet wishlist = null;
//        ////check if user is logged in
//        //if (userService.isSignedIn(username)) {
//            //display wishlist
//            try {
//                Connection connection = DatabaseConnection.getInstance().getConnection();
//
//                getUserID = connection.prepareStatement(
//                        "SELECT user_id FROM " + DatabaseConstants.USERS_TABLE + " WHERE username = ?");
//                getUserID.setString(1, username);
//                resultSet = getUserID.executeQuery();
//
//                int user_id_query = 0;
//                while (resultSet.next()) {
//                    user_id_query = resultSet.getInt("user_id" );
//                }
//
//                displayWishlist = connection.prepareStatement(
//                        "SELECT p.product_id, p.plantName, p.plantNameLatin, p.plantPrice, p.plantCategory, p.plantLocation, p.plantColor " + "FROM " + DatabaseConstants.PLANTS_TABLE + " p " + "JOIN " + DatabaseConstants.WISHLISTLINES_TABLE + " wl ON p.product_id = wl.product_id " + "WHERE wl.user_id = ?");
//                displayWishlist.setInt(1, user_id_query);
//
//                wishlist = displayWishlist.executeQuery();
//                ResultSetMetaData rsmd = wishlist.getMetaData();
//
//                System.out.println("|| Wishlist van " + username + " ||");
//                int columnsNumber = rsmd.getColumnCount();
//                while (wishlist.next()) {
//                    for (int i = 1; i <= columnsNumber; i++) {
//                        //if (i > 1) System.out.print(",  ");
//                        String columnValue = wishlist.getString(i);
//                        System.out.print(rsmd.getColumnName(i) + ": " + columnValue + " | ");
//                    }
//                    System.out.println("");
//                }
//                return true;
//            } catch (SQLException e) {
//                e.printStackTrace();
//            } finally {
//                DatabaseUtils.closeQuietly(resultSet);
//                DatabaseUtils.closeQuietly(getUserID);
//                DatabaseUtils.closeQuietly(wishlist);
//                DatabaseUtils.closeQuietly(displayWishlist);
//            }
//    return false;
//    }

    public List<Plant> displayWishlist(String username) {
        List<Plant> wishlist = new ArrayList<>();
        PreparedStatement getUserID = null;
        PreparedStatement getWishlist = null;
        ResultSet resultSet = null;
        ResultSet wishlistResult = null;

        try {
            Connection connection = DatabaseConnection.getInstance().getConnection();

            getUserID = connection.prepareStatement(
                    "SELECT user_id FROM " + DatabaseConstants.USERS_TABLE + " WHERE username = ?"
            );
            getUserID.setString(1, username);
            resultSet = getUserID.executeQuery();

            int userId = -1;
            if (resultSet.next()) {
                userId = resultSet.getInt("user_id");
            }

            if (userId != -1) {
                getWishlist = connection.prepareStatement(
                        "SELECT p.* FROM " + DatabaseConstants.PLANTS_TABLE + " p " +
                                "JOIN " + DatabaseConstants.WISHLISTLINES_TABLE + " wl ON p.product_id = wl.product_id " +
                                "WHERE wl.user_id = ?"
                );
                getWishlist.setInt(1, userId);
                wishlistResult = getWishlist.executeQuery();

                while (wishlistResult.next()) {
                    Plant plant = new Plant(
                            wishlistResult.getInt("product_id"),
                            wishlistResult.getString("plantName"),
                            wishlistResult.getDouble("plantPrice"),
                            wishlistResult.getString("plantNameLatin"),
                            wishlistResult.getString("plantCategory"),
                            wishlistResult.getString("plantLocation"),
                            wishlistResult.getString("plantColor"),
                            wishlistResult.getString("plantDescription")
                    );
                    wishlist.add(plant);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DatabaseUtils.closeQuietly(resultSet);
            DatabaseUtils.closeQuietly(wishlistResult);
            DatabaseUtils.closeQuietly(getUserID);
            DatabaseUtils.closeQuietly(getWishlist);
        }

        return wishlist;
    }

}
