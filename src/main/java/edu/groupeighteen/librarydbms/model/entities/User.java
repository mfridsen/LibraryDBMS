package edu.groupeighteen.librarydbms.model.entities;

/**
 * @author Mattias Fridsén
 * @project LibraryDBMS
 * @package edu.groupeighteen.librarydbms.model
 * @contact matfir-1@student.ltu.se
 * @date 4/5/2023
 */
public class User {

    //TODO-future add more fields and methods
    //TODO-comment everything

    private int userID; //Primary key
    private String username;
    private String password;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    /*********************************** Getters and Setters are self-explanatory. ************************************/
    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}