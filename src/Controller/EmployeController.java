package Controller;

import javax.swing.JOptionPane;

import DAO.EmployeDAOImpl;
import Model.Employe;


import Model.Employe.POSTE;
import Model.Employe.ROLE;
import Model.EmployeModel;
import View.EmployeeView;

public class EmployeController {
	
	
	private EmployeModel model;
	private EmployeeView view;
	private EmployeDAOImpl dao;
	
	
	public EmployeController(EmployeModel model, EmployeeView view, EmployeDAOImpl dao) {
		this.dao= dao;
		this.model=model;
		this.view=view;
		this.view.ajouter.addActionListener(e -> addemployee());
		this.view.modifier.addActionListener(e ->updateEmployee());
		this.view.supprimer.addActionListener(e -> deleteemployee());
		this.view.afficher.addActionListener(e -> showEmployes());
	}
	private void addemployee() {
	   
	        // Fetch data from the view
	        String nom = view.getNom().getText();
	        String prenom = view.getPrenom().getText();
	        String telStr = view.gettel().getText();
	        String salaireStr = view.getSalaire().getText();
	        String email = view.getEmail().getText();
	        String roleStr = view.getRole();
	        String posteStr = view.getPoste();


	        
	        int tel = Integer.parseInt(telStr);
	        int salaire = Integer.parseInt(salaireStr);
	        ROLE role = ROLE.valueOf(roleStr.toUpperCase());
	        POSTE poste = POSTE.valueOf(posteStr.toUpperCase());

	        
	        Employe employee = new Employe(nom, prenom, tel, salaire, email, role, poste);
	        model.add(employee);
	        view.refreshTable();

	  
	}
	
	private void updateEmployee() {
	    try {
	        // Get selected employee ID
	        String selectedId = view.getSelectedEmployeeId();
	        if (selectedId == null) {
	            view.showError("Veuillez sélectionner un employé à modifier.");
	            return;
	        }

	        int emp_id = Integer.parseInt(selectedId);

	        // Fetch updated data from view
	        String nom = view.getNom().getText();
	        String prenom = view.getPrenom().getText();
	        String telStr = view.gettel().getText();
	        String salaireStr = view.getSalaire().getText();
	        String email = view.getEmail().getText();
	        String roleStr = view.getRole();
	        String posteStr = view.getPoste();

	        // Convert inputs
	        int tel = Integer.parseInt(telStr);
	        int salaire = Integer.parseInt(salaireStr);
	        ROLE role = ROLE.valueOf(roleStr.toUpperCase());
	        POSTE poste = POSTE.valueOf(posteStr.toUpperCase());

	        // Create updated Employe object
	        Employe updatedEmployee = new Employe(nom, prenom, tel, salaire, email, role, poste);

	        // Update in DAO
	        dao.update(updatedEmployee, emp_id);
	        view.refreshTable();
	    } catch (Exception ex) {
	        view.showError("Erreur lors de la mise à jour : " + ex.getMessage());
	    }
	}

	
	private void deleteemployee() {
	    String selectedIdStr = view.getSelectedEmployeeId();
	    if (selectedIdStr != null) {
	        int selectedId = Integer.parseInt(selectedIdStr);
	        int confirmation = JOptionPane.showConfirmDialog(
	            view,
	            "Êtes-vous sûr de vouloir supprimer cet employé ?",
	            "Confirmation",
	            JOptionPane.YES_NO_OPTION
	        );
	        if (confirmation == JOptionPane.YES_OPTION) {
	            dao.delete(selectedId);
	            view.refreshTable();
	        }
	    } else {
	        view.showError("Veuillez sélectionner un employé à supprimer.");
	    }
	}

	
	private void showEmployes() {
	    try {
	        Object[][] employes = dao.getEmployes();
	        view.refreshTable();  // Passer les données à la vue
	    } catch (Exception ex) {
	        view.showError("Erreur lors de l'affichage des employés : " + ex.getMessage());
	    }
	}


}
