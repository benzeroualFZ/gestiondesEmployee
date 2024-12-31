package View;

import java.awt.*;
import javax.swing.*;
import java.sql.Connection;
import DAO.DBconnection;

public class loginView extends JFrame {

    public Connection conn;
    public JButton loginButton = new JButton("Log in");
    public JTextField userField = new JTextField(20);
    public JPasswordField passwordField = new JPasswordField(20);

    public loginView() {
        conn = DBconnection.getConnexion();
      
        setTitle("Login");
        setSize(350, 200);  
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 20)); 
        
        panel.add(new JLabel("Username:"));
        panel.add(userField);

        panel.add(new JLabel("Password:"));
        panel.add(passwordField);

        
        panel.add(loginButton);

        add(panel, BorderLayout.CENTER);

     
        setVisible(true);
    }
    
    public JButton getlogin() {
    	return loginButton;
    }
    
    public JTextField getuser() {
    	return userField;
    }
    
    public JPasswordField getpassword() {
    	return passwordField;
    }

}
