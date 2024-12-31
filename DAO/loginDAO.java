package DAO;
import java.sql.*;

import Controller.PasswordUtils;

public class loginDAO  {
    private Connection connection;

    public loginDAO() {
        this.connection = DBconnection.getConnexion();
    }

    public boolean authenticate(String username, String password) {
        String sql = "SELECT password_hash FROM users WHERE username = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String storedHash = rs.getString("password_hash");
                return PasswordUtils.verifyPassword(password, storedHash); 
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


    public String getRole(String username) {
        String sql = "SELECT r.nom_role FROM users u " + 
                     "JOIN role r ON u.role_id = r.id_role " + 
                     "WHERE u.username = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString("nom_role");  
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
