package Model;

import java.io.File;
import java.io.IOException;
import java.util.List;

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
	 
	 public void ImportData(String filePath) throws IOException {
			dao.importData(filePath);
		
	 }
	 
	 public void exportData(String fileName, List<Employe> data) throws IOException {
		 dao.exportData(fileName, data);
	 }
		 
	 
	 private boolean checkFileExists(File file) {
		 
		 if(!file.exists()) {
			 
			 throw new IllegalArgumentException ("le ficher n'existe pas: " +file.getPath());
		 }
		 return true;
	 }
	 
	 private boolean checkIsFile(File file) {
		 if(!file.isFile()) {
		 throw new IllegalArgumentException("le chemin specifie n'est pas un ficher: " +file.getPath());
		 }
		 return true;
	 }
	 
	 private boolean checkIsReadable(File file) {
		 if(!file.canRead()) {
		 throw new IllegalArgumentException("le fichier n'est pas lisibble: " +file.getPath());
		 }
		 return true;
	 }
	 
	 public List<Employe> findAll() {
		 return dao.findAll();
	 }
}
	

