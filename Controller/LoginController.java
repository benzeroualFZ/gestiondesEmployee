package Controller;

import javax.swing.JOptionPane;
import Model.LoginModel;
import View.MainView;
import View.loginView;
import View.EmployeeView;
import View.HolidayView;

public class LoginController {
    private LoginModel model;
    private loginView view;
    private EmployeeView employeeView;
    private HolidayView holidayView;

    public LoginController(LoginModel model, loginView view, EmployeeView employeeView, HolidayView holidayView) {
        this.model = model;
        this.view = view;
        this.employeeView = employeeView;
        this.holidayView = holidayView;

        this.view.getlogin().addActionListener(e -> loginUser());  // Listen for login button click
    }

    public void loginUser() {
        String username = view.getuser().getText();
        String password = new String(view.getpassword().getPassword());

        // Check authentication
        if (model.authenticate(username, password)) {
            String role = model.getRole(username);  // Get the role of the authenticated user

            if (role != null) {
                // Pass the already initialized views (EmployeeView, HolidayView) to the MainView
                MainView mainView = new MainView(employeeView, holidayView);
                mainView.configureForRole(role); // Configure views based on the role

                // Close the login view
                view.dispose();
                mainView.setVisible(true);  // Show the main view
            } else {
                // If role is not found
                JOptionPane.showMessageDialog(view, "Role not found", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            // If authentication fails
            JOptionPane.showMessageDialog(view, "Invalid credentials", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
