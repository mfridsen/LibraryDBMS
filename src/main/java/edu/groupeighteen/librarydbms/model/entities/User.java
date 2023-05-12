package edu.groupeighteen.librarydbms.model.entities;

/**
 * @author Mattias Fridsén
 * @project LibraryDBMS
 * @package edu.groupeighteen.librarydbms.model
 * @contact matfir-1@student.ltu.se
 * @date 4/5/2023
 *
 * This class represents a User of the application.
 *
 * Invariants, enforced by setters:
 *      UserIDs have to be positive integers.
 *      Usernames cannot be null or empty. //TODO-future add min and max length
 *      Passwords cannot be null or empty. //TODO-future add min and max length
 */
public class User {

    //TODO-future add more fields and methods
    //TODO-comment everything

    private int userID; //Primary key
    private String username;
    private String password;

    public User(String username, String password) {
        setUsername(username);
        setPassword(password);
    }

    /*********************************** Getters and Setters are self-explanatory. ************************************/
    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        if (userID <= 0) {
            throw new IllegalArgumentException("UserID must be greater than zero.");
        }
        this.userID = userID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        if (username == null || username.isEmpty()) {
            throw new IllegalArgumentException("Username cannot be null or empty.");
        }
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        if (password == null || password.isEmpty()) {
            throw new IllegalArgumentException("Password cannot be null or empty.");
        }
        this.password = password;
    }
}