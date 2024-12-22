package Model;

import DAO.EmployeDAOImpl;


public class EmployeModel {
private EmployeDAOImpl dao;
	public EmployeModel(EmployeDAOImpl dao) {
		this.dao=dao;
	}
	public void add(Employe employee) {
	    if (employee == null) {
	        throw new IllegalArgumentException("L'employé ne peut pas être null.");
	    }
	    if (employee.getNom() == null || employee.getNom().isEmpty()) {
	        throw new IllegalArgumentException("Le nom de l'employé est obligatoire.");
	    }
	    dao.add(employee);
	}
	
	public void update(Employe employee, int empId) {
        if (employee == null) {
            throw new IllegalArgumentException("L'employé ne peut pas être null.");
        }
        if (empId <= 0) {
            throw new IllegalArgumentException("L'ID de l'employé est invalide.");
        }
        if (employee.getNom() == null || employee.getNom().isEmpty()) {
            throw new IllegalArgumentException("Le nom de l'employé est obligatoire.");
        }
        dao.update(employee, empId); 
    }
	
	 public void delete(int empId) {
	        if (empId <= 0) {
	            throw new IllegalArgumentException("L'ID de l'employé est invalide.");
	        }
	        dao.delete(empId); 
	    }
	
}
