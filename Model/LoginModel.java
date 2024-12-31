package Model;

import DAO.loginDAO;
public class LoginModel {
    private loginDAO loginDAO;

    public LoginModel(loginDAO loginDAO) {
        this.loginDAO = loginDAO;
    }

    public boolean authenticate(String username, String password) {
        return loginDAO.authenticate(username, password);
    }

    public String getRole(String username) {
        return loginDAO.getRole(username);
    }
}
