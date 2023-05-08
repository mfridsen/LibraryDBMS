package edu.groupeighteen.librarydbms.model.entities;

/**
 * @author Mattias Fridsén
 * @project LibraryDBMS
 * @package edu.groupeighteen.librarydbms.model
 * @contact matfir-1@student.ltu.se
 * @date 4/5/2023
 */
public class Item {

    //TODO-future add more fields and methods
    //TODO-comment everything

    private int itemID; //Primary key
    private String title;

    public Item(String title) {
        this.title = title;
    }

    /*********************************** Getters and Setters are self-explanatory. ************************************/
    public int getItemID() {
        return itemID;
    }

    public void setItemID(int itemID) {
        this.itemID = itemID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}