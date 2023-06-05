package edu.groupeighteen.librarydbms.view.entities.author;

import edu.groupeighteen.librarydbms.model.entities.Author;
import edu.groupeighteen.librarydbms.view.buttons.EntityButtonEditor;
import edu.groupeighteen.librarydbms.view.gui.GUI;

import javax.swing.*;

/**
 * @author Johan Lund
 * @project LibraryDBMS
 * @date 2023-05-26
 */
public class AuthorGUIButtonEditor extends EntityButtonEditor
{
    public AuthorGUIButtonEditor(JCheckBox checkBox, Author author, String label, GUI previousGUI)
    {
        super(checkBox, author, label, previousGUI);
    }

    @Override
    public Object getCellEditorValue()
    {
        if (isPushed)
        {
            new AuthorGUI(previousGUI, (Author) entity);
        }
        isPushed = false;
        return label; // Return a new String so the original is not affected
    }

}
