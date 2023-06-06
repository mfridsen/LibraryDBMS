package edu.groupeighteen.librarydbms.view.optionpanes;

import edu.groupeighteen.librarydbms.view.gui.GUI;

import javax.swing.*;

/**
 * @author Mattias Fridsén
 * @project LibraryDBMS
 * @package view.GUI.entities
 * @contact matfir-1@student.ltu.se
 * @date 5/17/2023
 * <p>
 * Represents an abstract OptionPane GUI. This class extends from JOptionPane and
 * provides a structure for creating specialized option panes.
 * The class maintains a reference to the previous GUI that this OptionPane was invoked from.
 */
public abstract class OptionPane extends JOptionPane
{
    // The GUI from which this OptionPane was invoked
    protected final GUI previousGUI;

    /**
     * Constructs a new OptionPane instance linked to a specific previous GUI.
     *
     * @param previousGUI The GUI from which this OptionPane is invoked.
     */
    public OptionPane(GUI previousGUI)
    {
        this.previousGUI = previousGUI;
    }

    /**
     * Returns the previous GUI from which this OptionPane was invoked.
     *
     * @return The previous GUI.
     */
    public GUI getPreviousGUI()
    {
        return previousGUI;
    }
}