package View;

import java.awt.*;

import java.sql.*;
import java.util.ArrayList;

import javax.swing.*;


import DAO.DBconnection;
import DAO.EmployeDAOImpl;



public class EmployeeView extends JPanel{
	private EmployeDAOImpl dao;
	private Connection conn;
	public JButton ajouter = new JButton("Ajouter");
	public JButton modifier = new JButton("Modifier");
	public JButton supprimer = new JButton("Supprimer");
	public JButton afficher = new JButton("Afficher");
	public JButton export = new JButton("Export");
	public JButton imp = new JButton("Import");
	JTextField nom = new JTextField(20);
	JTextField prenom = new JTextField(20);
	JTextField email = new JTextField(20);
	JTextField tel = new JTextField(20);
	JTextField salaire = new JTextField(20);
	public JTable tbl=new JTable();
	JComboBox<String> role;
	JComboBox<String> poste;
	
	public EmployeeView() {
		this.dao=new EmployeDAOImpl();
		conn = DBconnection.getConnexion();
		setLayout(new BorderLayout());
		JPanel data= new JPanel();
		data.setLayout(new BorderLayout());
		
		JPanel champ = new JPanel();
		champ.setLayout(new GridLayout(7,2));
		champ.add(new JLabel("Nom:"));
		champ.add(nom);
		champ.add(new JLabel("Prenom:"));
		champ.add(prenom);
		champ.add(new JLabel("Telephone:"));
		champ.add(tel);
		champ.add(new JLabel("salaire:"));
		champ.add(salaire);
		champ.add(new JLabel("Email:"));
		champ.add(email);
		ArrayList<String> roles = dao.getRoles();
		ArrayList<String> postes = dao.getPostes();
		role = new JComboBox<>(roles.toArray(new String[0]));
		poste = new JComboBox<>(postes.toArray(new String[0]));
		champ.add(new JLabel("Role:"));
		champ.add(role);
		champ.add(new JLabel("Poste:"));
		champ.add(poste);
		
		data.add(champ, BorderLayout.CENTER);
		add(data, BorderLayout.CENTER);
		Object[][] employes = dao.getEmployes();
		String[] tableColumns = {"ID", "Nom", "Prénom", "Téléphone", "Salaire", "Email", "Role", "Poste"};
		tbl = new JTable(new Object[0][8], tableColumns);
		
		tbl.getSelectionModel().addListSelectionListener(event -> {
		    if (!event.getValueIsAdjusting() && tbl.getSelectedRow() != -1) {
		        int selectedRow = tbl.getSelectedRow();
		        nom.setText(tbl.getValueAt(selectedRow, 1).toString());
		        prenom.setText(tbl.getValueAt(selectedRow, 2).toString());
		        tel.setText(tbl.getValueAt(selectedRow, 3).toString());
		        salaire.setText(tbl.getValueAt(selectedRow, 4).toString());
		        email.setText(tbl.getValueAt(selectedRow, 5).toString());
		        role.setSelectedItem(tbl.getValueAt(selectedRow, 6).toString());
		        poste.setSelectedItem(tbl.getValueAt(selectedRow, 7).toString());
		    }
		});

		
	    JScrollPane scroll = new JScrollPane(tbl);	
		scroll.setPreferredSize(new Dimension(550, 200)); // Limit table size
		JPanel table = new JPanel();
		table.add(scroll);
		data.add(table, BorderLayout.SOUTH);
		
		JPanel button= new JPanel();
		button.setLayout(new FlowLayout());
		button.add(ajouter);
		button.add(modifier);
		button.add(supprimer);
		button.add(afficher);
		button.add(export);
		button.add(imp);
		
		add(button, BorderLayout.SOUTH);
					
	}
	


		public void refreshTable() {
		    Object[][] employes = dao.getEmployes();  // Récupérer les valeurs des employés
		    String[] tableColumns = {"ID", "Nom", "Prénom", "Téléphone", "Salaire", "Email", "Role", "Poste"};
	
		    // Mettre à jour le modèle de JTable
		    tbl.setModel(new javax.swing.table.DefaultTableModel(employes, tableColumns));
		}
		


		public String getSelectedEmployeeId() {
		    int selectedRow = tbl.getSelectedRow();
		    if (selectedRow != -1) { // Une ligne est sélectionnée
		        return tbl.getValueAt(selectedRow, 0).toString(); // Colonne 0 contient l'ID
		    }
		    return null; // Aucune ligne n'est sélectionnée
		}
		public void showError(String message) {
		    JOptionPane.showMessageDialog(this, message, "Erreur", JOptionPane.ERROR_MESSAGE);
		}
		
		
		
	public JButton getAddButton() {
	    return ajouter;
	}
	
	public JButton getUpdateButton() {
	    return modifier;
	}
	
	public JButton getDeleteButton() {
	    return supprimer;
	}
	
	public JButton getListButton() {
	    return afficher;
	}
	
	public JButton getimportbutton() {
	    return imp;
	}
		
	public JButton getExportButton() {
	    return export;
	}
		
		
		public JTextField getNom() {
			return nom;
		}
		
		public JTextField getPrenom() {
			return prenom;
		}
		
		public JTextField gettel() {
			return tel;
		}
		
		public JTextField getSalaire() {
			return salaire;
		}
		
		public JTextField getEmail() {
			return email;
		}
		
		public String getRole() {
			return role.getSelectedItem().toString();
		}
		
		public String getPoste() {
			return poste.getSelectedItem().toString();
		}
		
		public JComboBox<String> getRoleComboBox() {
		    return role;
		}
		
		public JComboBox<String> getPosteComboBox() {
		    return poste;
		}
		
		public JTable getJTable() {
			return tbl;
		}
		
		
}
