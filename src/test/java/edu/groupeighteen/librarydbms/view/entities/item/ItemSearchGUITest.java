package edu.groupeighteen.librarydbms.view.entities.item;

import edu.groupeighteen.librarydbms.LibraryManager;

/**
 * @author Jesper Truedsson
 * @project LibraryDBMS
 * @date 2023-05-10
 * Unit Test for the ItemSearchGUI class.
 */
public class ItemSearchGUITest
{
    public static void main(String[] args)
    {
        LibraryManager.setup();
        new ItemSearchGUI(null);
    }
}