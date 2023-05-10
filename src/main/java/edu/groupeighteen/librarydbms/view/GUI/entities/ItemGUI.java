package edu.groupeighteen.librarydbms.view.GUI.entities;

import edu.groupeighteen.librarydbms.model.entities.Item;

/**
 * @author Jesper Truedsson
 * @project LibraryDBMS
 * @date 2023-05-10
 *
 * this class displays all information about a single item object
 */
public class ItemGUI {
    private Item item;

    public ItemGUI(Item item) {
        this.item = item;
    }
}
