package edu.groupeighteen.librarydbms.view.entities.item;

import edu.groupeighteen.librarydbms.control.entities.ItemHandler;
import edu.groupeighteen.librarydbms.control.entities.ItemHandlerUtils;
import edu.groupeighteen.librarydbms.model.entities.Item;
import edu.groupeighteen.librarydbms.view.gui.GUI;

import javax.swing.*;
import java.awt.*;

/**
 * @author Mattias Fridsén
 * @project LibraryDBMS
 * @package edu.groupeighteen.librarydbms.view.GUI.entities.item
 * @contact matfir-1@student.ltu.se
 * @date 5/15/2023
 * <p>
 * This class extends GUI and is responsible for handling the graphical user interface
 * for creating a new item in the LibraryDBMS system.
 * The class allows a user to input necessary data to create a new item.
 */
public class ItemCreateGUI extends GUI
{
    //TODO-prio look over exception handling
    //TODO-prio RENAME TO FilmCreateGUI AND MAKE A LiteratureCreateGUI
    /**
     * Text field for the title of the item.
     */
    private JTextField titleField;

    /**
     * Text field for the author's ID.
     */
    private JTextField authorIDField;

    /**
     * Text field for the classification ID.
     */
    private JTextField classificationIDField;

    /**
     * Text field for the barcode of the item.
     */
    private JTextField barcodeField;

    /**
     * Text field for the age rating of the item.
     */
    private JTextField ageRatingField;

    /**
     * An array of text fields for easy access and manipulation.
     */
    private JTextField[] fields;

    /**
     * Constructor for ItemCreateGUI. Calls the parent constructor,
     * initializes text fields, sets up panels, and displays the GUI.
     *
     * @param previousGUI the GUI instance that preceded this one.
     */
    public ItemCreateGUI(GUI previousGUI)
    {
        super(previousGUI, "ItemCreateGUI", null);
        setupFields();
        setupPanels();
        displayGUI();
    }

    /**
     * Sets up the text fields for this GUI screen.
     * The text fields are "Title", "Author ID", "Classification ID", "Barcode", and "Age Rating".
     * Each of these fields is placed in a 5x2 GridLayout panel, with the label on the left and the text field on
     * the right. The panel is then added to the north region of the GUIPanel.
     */
    private void setupFields()
    {
        JPanel panel = new JPanel(new GridLayout(5, 2));
        JLabel titleLabel = new JLabel("Title:");
        titleField = new JTextField(10);
        panel.add(titleLabel);
        panel.add(titleField);

        JLabel authorIDLabel = new JLabel("Author ID:");
        authorIDField = new JTextField(10);
        panel.add(authorIDLabel);
        panel.add(authorIDField);

        JLabel classificationIDLabel = new JLabel("Classification ID:");
        classificationIDField = new JTextField(10);
        panel.add(classificationIDLabel);
        panel.add(classificationIDField);

        JLabel barcodeLabel = new JLabel("Barcode:");
        barcodeField = new JTextField(10);
        panel.add(barcodeLabel);
        panel.add(barcodeField);

        JLabel ageRatingLabel = new JLabel("Age Rating:");
        ageRatingField = new JTextField(10);
        panel.add(ageRatingLabel);
        panel.add(ageRatingField);

        fields = new JTextField[]{titleField, authorIDField, classificationIDField, barcodeField, ageRatingField};
        GUIPanel.add(panel, BorderLayout.NORTH);
    }

    /**
     * Overrides the setupButtons method from the GUI class.
     * Sets up the "Reset Fields" and "Create Rental" buttons.
     *
     * @return An array of the two JButtons created in this method.
     */
    @Override
    protected JButton[] setupButtons()
    {
        JButton resetButton = new JButton("Reset Fields");
        resetButton.addActionListener(e ->
        {
            resetFields();
        });

        JButton createButton = new JButton("Create Item");
        createButton.addActionListener(e ->
        {
            try
            {
                Item item = createNewItem();
                ItemHandlerUtils.printItemList(ItemHandler.getAllItems());
                dispose();
                new ItemGUI(this, item);
            }
            catch (Exception ex)
            {
                //DON'T reset fields, that's annoying
                JOptionPane.showMessageDialog(null, ex.getCause().getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        return new JButton[]{resetButton, createButton};
    }

    /**
     * Resets all fields to an empty state.
     */
    private void resetFields()
    {
        for (JTextField field : fields)
            field.setText("");
    }

    /**
     * This method retrieves and validates the data from the text fields.
     * If the data is valid, it creates a new Item.
     *
     * @return Item the newly created item.
     * @throws Exception if the input data is invalid.
     */
    private Item createNewItem()
    throws Exception
    {
        String title = titleField.getText();
        String authorIDStr = authorIDField.getText();
        String classificationIDStr = classificationIDField.getText();
        String barcode = barcodeField.getText();
        String ageRatingStr = ageRatingField.getText();

        // Perform validation
        if (title.isEmpty() || title.length() > 255)
        {
            throw new Exception("Invalid title. Title cannot be empty and must be under 255 characters.");
        }
        if (barcode.isEmpty() || barcode.length() > 13)
        {
            throw new Exception("Invalid barcode. Barcode cannot be empty and must be under 13 characters.");
        }

        int authorID;
        try
        {
            authorID = Integer.parseInt(authorIDStr);
        }
        catch (NumberFormatException e)
        {
            throw new Exception("Invalid author ID. Author ID must be a number.");
        }

        int classificationID;
        try
        {
            classificationID = Integer.parseInt(classificationIDStr);
        }
        catch (NumberFormatException e)
        {
            throw new Exception("Invalid classification ID. Classification ID must be a number.");
        }

        int ageRating;
        try
        {
            ageRating = Integer.parseInt(ageRatingStr);
        }
        catch (NumberFormatException e)
        {
            throw new Exception("Invalid age rating. Age rating must be a number.");
        }

        // If all validation checks pass, create the new item
        return ItemHandler.createNewFilm(title, authorID, classificationID, barcode, ageRating);
    }

    /**
     * Overrides the setupPanels method from the GUI class.
     */
    @Override
    protected void setupPanels()
    {
    }
}