package be.webshop.service;

import be.webshop.dao.UserDAO;

public class UserService {
    private final UserDAO userDAO;

    public UserService() {
        this.userDAO = new UserDAO();
    }

    public boolean isSignedIn(String username) {
        return userDAO.isSignedIn(username);
    }

    public boolean register(String username, String password) {
        return userDAO.register(username, password);
    }

    public boolean checkUser(String username) {
        return userDAO.checkUser(username);
    }

    public boolean signIn(String username, String password) {
        return userDAO.signIn(username, password);
    }

    public boolean signOut(String username) {
        return userDAO.signOut(username);
    }
}
