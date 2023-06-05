package edu.groupeighteen.librarydbms.view.entities.user;

import edu.groupeighteen.librarydbms.model.entities.User;
import edu.groupeighteen.librarydbms.view.buttons.EntityButtonEditor;
import edu.groupeighteen.librarydbms.view.gui.GUI;

import javax.swing.*;

/**
 * @author Jesper Truedsson
 * @project LibraryDBMS
 * @date 2023-05-19
 */
public class UserGUIButtonEditor extends EntityButtonEditor
{

    public UserGUIButtonEditor(JCheckBox checkBox, User user, String label, GUI previousGUI)
    {
        super(checkBox, user, label, previousGUI);
    }

    @Override
    public Object getCellEditorValue()
    {
        if (isPushed)
        {
            new UserGUI(previousGUI, (User) entity);
        }
        isPushed = false;
        return label; // Return a new String so the original is not affected
    }
}

