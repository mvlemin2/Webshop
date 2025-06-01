package be.webshop.service;

import be.webshop.dao.WishlistDAO;

public class WishlistService {

    private final WishlistDAO wishlistDAO;
    private final UserService userService;

    public WishlistService() {
        this.wishlistDAO = new WishlistDAO();
        this.userService = new UserService();
    }

    public boolean addToWishlist(int productId, String username) {
        if (userService.isSignedIn(username)) {
            return wishlistDAO.addToWishlist(productId, username);
        }
        return false;
    }

    public boolean removeFromWishlist(int productId, String username) {
        if (userService.isSignedIn(username)) {
            return wishlistDAO.removeFromWishlist(productId, username);
        }
        return false;
    }

    public boolean removeAllFromWishlist(String username) {
        if (userService.isSignedIn(username)) {
            return wishlistDAO.removeAllFromWishlist(username);
        }
        return false;
    }

    public boolean displayWishlist(String username) {
        if (userService.isSignedIn(username)) {
            return wishlistDAO.displayWishlist(username);
        }
        return false;
    }
}
