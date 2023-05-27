package edu.groupeighteen.librarydbms.view.entities.publisher;

import edu.groupeighteen.librarydbms.LibraryManager;
import edu.groupeighteen.librarydbms.control.entities.PublisherHandler;
import edu.groupeighteen.librarydbms.control.entities.UserHandler;
import edu.groupeighteen.librarydbms.model.entities.Publisher;
import edu.groupeighteen.librarydbms.model.exceptions.user.InvalidPasswordException;
import edu.groupeighteen.librarydbms.model.exceptions.user.NullUserException;
import edu.groupeighteen.librarydbms.view.gui.GUI;

import javax.swing.*;
import java.util.Arrays;

/**
 * @author Johan Lund
 * @project LibraryDBMS
 * @date 2023-05-26
 */
public class PublisherDeleteGUI extends GUI {

    //TODO-comment
    private final Publisher PublisherToDelete;
    private JPasswordField passwordField;

    public PublisherDeleteGUI(GUI previousGUI, Publisher PublisherToDelete) {
        super(previousGUI, "PublisherDeleteGUI");
        this.PublisherToDelete = PublisherToDelete;
        setupPanels();
        displayGUI();
    }

    @Override
    protected JButton[] setupButtons() {
        JButton confirmButton = new JButton("Confirm Delete");
        confirmButton.addActionListener(e -> {
            //TODO-prio you shouldn't be able to access this GUI at all without being logged in (and staff)
            if (LibraryManager.getCurrentUser() != null) {
                try {
                    if (UserHandler.validate(LibraryManager.getCurrentUser(),
                            Arrays.toString(passwordField.getPassword()))) {
                        PublisherHandler.deletePublisher(PublisherToDelete);
                        //dispose();
                        //TODO-prio return to some other GUI, probably the LoginGUI
                    }
                } catch (NullUserException | InvalidPasswordException nullUserException) {
                    nullUserException.printStackTrace();
                }
            }
        });
        return new JButton[]{confirmButton};
    }

    @Override
    protected void setupPanels() {
        JPanel passwordPanel = new JPanel();
        JLabel passwordLabel = new JLabel("Enter Password:");
        passwordField = new JPasswordField();
        passwordField.setColumns(10);
        passwordPanel.add(passwordLabel);
        passwordPanel.add(passwordField);
        GUIPanel.add(passwordPanel);
    }
}
