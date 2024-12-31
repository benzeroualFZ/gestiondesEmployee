package View;

import java.awt.*;

import javax.swing.*;

public class MainView extends JFrame {
    private JTabbedPane tabbedPane;
   

    public MainView(EmployeeView employeeView, HolidayView holidayView) {
    	
        
        setTitle("Gestion des Employés et Congés");
        setSize(800, 600); 

        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Employés", employeeView);
        tabbedPane.addTab("Congés", holidayView);

        add(tabbedPane, BorderLayout.CENTER);

        setVisible(true);
    }
    // This method configures the views based on the role
    public void configureForRole(String role) {
        // Configure the EmployeeView based on role
        // You can call configureForRole on EmployeeView
        // Same for HolidayView
        EmployeeView employeeView = (EmployeeView) tabbedPane.getComponentAt(0);
        HolidayView holidayView = (HolidayView) tabbedPane.getComponentAt(1);

        employeeView.configureForRole(role);
        holidayView.configureForRole(role);
    }
}
