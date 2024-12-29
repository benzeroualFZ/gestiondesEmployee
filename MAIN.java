import Controller.EmployeController;
import Controller.HolidayController;
import DAO.EmployeDAOImpl;
import DAO.HolidayDAOImpl;
import Model.EmployeModel;
import Model.HolidayModel;
import View.EmployeeView;
import View.HolidayView;
import View.MainView;

public class MAIN {
	public static void main(String[] args) {
		// Employee
		EmployeeView viewEmpl= new EmployeeView();
		EmployeDAOImpl daoEmpl= new EmployeDAOImpl();
		EmployeModel modelEmpl =new EmployeModel(daoEmpl);
		new EmployeController(modelEmpl, viewEmpl, daoEmpl);
		
		// Holiday
		
		HolidayView viewHol = new HolidayView();
		HolidayDAOImpl daoH = new HolidayDAOImpl();
		HolidayModel modelH = new HolidayModel(daoH); 
		new HolidayController(modelH, viewHol);
		
		new MainView(viewEmpl, viewHol);
	}
}
