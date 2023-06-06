package edu.groupeighteen.librarydbms.view;

import edu.groupeighteen.librarydbms.LibraryManager;

/**
 * @author Jesper Truedsson
 * @project LibraryDBMS
 * @date 2023-05-12
 * Unit Test for the HomeScreenGUI class.
 */
public class HomeScreenGUITest
{
    public static void main(String[] args)
    {
        LibraryManager.setup();
        new HomeScreenGUI(null);
    }
}