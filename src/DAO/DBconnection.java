package DAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBconnection {
	private static final String URL="jdbc:mysql://localhost:3306/gestionemploye";
	private static final String user="root";
    private static final String password="";
    static Connection conn= null; //possibilité d'erreur
    public static Connection getConnexion() {
    	
    	    	
    	try {
    		Class.forName("com.mysql.cj.jdbc.Driver");
    		conn = DriverManager.getConnection(URL, user, password);
    	}catch (ClassNotFoundException |SQLException e) {
    		e.printStackTrace();
    		throw new RuntimeException("ERREUR DE CONN");
    	}
		
    return conn ;
}
}
