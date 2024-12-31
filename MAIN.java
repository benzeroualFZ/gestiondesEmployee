
import Controller.LoginController;

import DAO.loginDAO;
import Model.LoginModel;
import View.EmployeeView;
import View.HolidayView;
import View.MainView;
import View.loginView;
import Controller.EmployeController;
import Controller.HolidayController;
import DAO.EmployeDAOImpl;
import DAO.HolidayDAOImpl;
import Model.EmployeModel;
import Model.HolidayModel;

public class MAIN {
    public static void main(String[] args) {
       
        loginView login = new loginView();
        EmployeeView employeeView = new EmployeeView();
        HolidayView holidayView = new HolidayView();
        EmployeDAOImpl daoEmpl = new EmployeDAOImpl();
        HolidayDAOImpl daoHoliday = new HolidayDAOImpl();     
        loginDAO loginDAO = new loginDAO();
        LoginModel loginModel = new LoginModel(loginDAO);
        EmployeModel employeModel = new EmployeModel(daoEmpl);
        new EmployeController(employeModel, employeeView, daoEmpl);
        HolidayModel holidayModel = new HolidayModel(daoHoliday);
        new HolidayController(holidayModel, holidayView);
        LoginController loginController = new LoginController(loginModel, login, employeeView, holidayView);
        login.setVisible(true);
    }
}
