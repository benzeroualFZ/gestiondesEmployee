package View;

import javax.swing.*;
import Controller.PasswordUtils;
import DAO.EmployeDAOImpl;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class CreateUserDialog extends JDialog {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton saveButton;
    private JComboBox<String> employeeComboBox;  // ComboBox for employee names
    private EmployeDAOImpl dao;

    public CreateUserDialog() {
        this.dao = new EmployeDAOImpl();

        setTitle("Create Username/Password");
        setSize(300, 250);
        setLocationRelativeTo(null); 
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setModal(true); 
        
        // Fetch employee names from the database
        List<String> employeeNames = dao.getEmployeeNames();

        // Initialize components
        usernameField = new JTextField(20);
        passwordField = new JPasswordField(20);
        saveButton = new JButton("Save");
        employeeComboBox = new JComboBox<>(employeeNames.toArray(new String[0]));  // Populate ComboBox

        setLayout(new FlowLayout());
        add(new JLabel("Select Employee:"));
        add(employeeComboBox);
        add(new JLabel("Username:"));
        add(usernameField);
        add(new JLabel("Password:"));
        add(passwordField);
        add(saveButton);

        // Action listener for the save button
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveUsernameAndPassword();
            }
        });

        setVisible(true);
    }

    // Method to save the username and password
    private void saveUsernameAndPassword() {
        String selectedEmployee = (String) employeeComboBox.getSelectedItem();
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        // Hash the password
        String hashedPassword = PasswordUtils.hashPassword(password);

        // Save in the database using DAO
        dao.saveUserCredentials(selectedEmployee, username, hashedPassword);

        // Show a confirmation message and close the dialog
        JOptionPane.showMessageDialog(this, "Username and Password created successfully!");
        dispose();  // Close the dialog
    }
}
