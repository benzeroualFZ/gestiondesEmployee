package Model;

import java.util.Date;

import DAO.EmployeDAOImpl;

public class Holiday {
	private int ID;
	private Date DDebut;
	private Date DFin;
	private Type type;
	private int emp_id;
	
	
	public Holiday(int ID, Date DDebut, Date DFin, Type type, int emp_id) {
		this.ID=ID;
		this.DDebut=DDebut;
		this.DFin=DFin;
		this.type=type;
		this.emp_id=emp_id;
	}
	
	
	public Holiday( Date DDebut, Date DFin, Type type, int emp_id) {
		this.DDebut=DDebut;
		this.DFin=DFin;
		this.type=type;
		this.emp_id=emp_id;
	}
		
	public enum Type{
		 PAYE,
		 NON_PAYE,
		 MALADIE
	}
	
	public int getempId() {
		return emp_id;
	}
	public Date getDDebut() {
		return DDebut;
	}
	
	public Date getDFin() {
		return DFin;
	}
	
	public Type getType() {
		return type;
	}

	public int getId() {
		return ID;
	}
	public String getEmployeeName() {
	    EmployeDAOImpl empDao = new EmployeDAOImpl();
	    return empDao.getEmployeeNameById(emp_id);
	}

	
}
