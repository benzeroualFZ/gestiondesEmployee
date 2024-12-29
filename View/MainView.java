package View;

import java.awt.*;

import javax.swing.*;

public class MainView extends JFrame {
    private JTabbedPane tabbedPane;
   

    public MainView(EmployeeView employeeView, HolidayView holidayView) {
    	
        
        setTitle("Gestion des Employés et Congés");
        setSize(800, 600); 
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Employés", employeeView);
        tabbedPane.addTab("Congés", holidayView);

        add(tabbedPane, BorderLayout.CENTER);

        setVisible(true);
    }
}
