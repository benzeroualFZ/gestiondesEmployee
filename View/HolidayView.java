 package View;

import java.awt.*;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import Controller.HolidayController;
import DAO.DBconnection;
import DAO.EmployeDAOImpl;
import DAO.HolidayDAOImpl;
import Model.Holiday;
import Model.HolidayModel;

public class HolidayView extends JPanel{
	private HolidayDAOImpl dao;
	private Connection conn;
	public JButton ajouter = new JButton("Ajouter");
	public JButton modifier = new JButton("Modifier");
	public JButton supprimer = new JButton("Supprimer");
	public JButton exporter = new JButton("Export");
	JTextField DDebut = new JTextField("yyyy-mm-dd");
	JTextField DFin = new JTextField("yyyy-mm-dd");
	public JTable tab=new JTable();
	private JComboBox<String> employee ;
	private JComboBox<String> typeHoliday ;
	
	
	public HolidayView() {
		this.dao=new HolidayDAOImpl();
		conn = DBconnection.getConnexion();
	
		setLayout(new BorderLayout());
		
		JPanel data= new JPanel();
		data.setLayout(new BorderLayout());
		
		JPanel champ = new JPanel();
		champ.setLayout(new GridLayout(4,2));
		
		champ.add(new JLabel("Nom de l'employe:"));
		ArrayList<String> empl = dao.getEmploye();
		employee = new JComboBox<>(empl.toArray(new String[0]));
		champ.add(employee);
		
		champ.add(new JLabel("Type:"));
		ArrayList<String> types = dao.getType();
		typeHoliday = new JComboBox<>(types.toArray(new String[0]));		
		champ.add(typeHoliday);
		champ.add(new JLabel("Date de debut:"));
		champ.add(DDebut);
		champ.add(new JLabel("Date de congé:"));
		champ.add(DFin);
		
		data.add(champ, BorderLayout.CENTER);
		add(data, BorderLayout.CENTER);
		String[] tableColumns = {"ID", "Employe", "Date de Debut", "Date de fin", "Type"};
		tab = new JTable(new Object[0][6], tableColumns);
		
	    tab.getSelectionModel().addListSelectionListener(event -> {
            if (!event.getValueIsAdjusting() && tab.getSelectedRow() != -1) {
                int selectedRow = tab.getSelectedRow();
                DDebut.setText(tab.getValueAt(selectedRow, 2).toString());  
                DFin.setText(tab.getValueAt(selectedRow, 3).toString());  
                employee.setSelectedItem(tab.getValueAt(selectedRow, 1).toString());  
                typeHoliday.setSelectedItem(tab.getValueAt(selectedRow, 4).toString());  
            }
        });
		
		JScrollPane scroll = new JScrollPane(tab);	
		scroll.setPreferredSize(new Dimension(550, 200)); // Limit table size
		JPanel table = new JPanel();
		table.add(scroll);
		data.add(table, BorderLayout.SOUTH);
		
		JPanel button= new JPanel();
		button.setLayout(new FlowLayout());
		button.add(ajouter);
		button.add(modifier);
		button.add(supprimer);
		button.add(exporter);
		add(button, BorderLayout.SOUTH);
		 refreshTable(); 
		
		
		
	}
	
	public void refreshTable() {
	    // Fetch holiday data from DAO
	    ArrayList<Holiday> holidays = dao.getAllHolidays();  // Assuming this method returns a list of holidays

	    // Create an Object[][] to populate the JTable
	    Object[][] holidayData = new Object[holidays.size()][5];
	    
	    // Populate the data array with holiday details
	 // Populate the data array with holiday details
	    for (int i = 0; i < holidays.size(); i++) {
	        Holiday holiday = holidays.get(i);
	        holidayData[i][0] = holiday.getId();  // Assuming Holiday has an ID getter
	        holidayData[i][1] = holiday.getEmployeeName();  // Get employee name dynamically
	        holidayData[i][2] = holiday.getDDebut();  // Assuming Holiday has a getter for start date
	        holidayData[i][3] = holiday.getDFin();  // Assuming Holiday has a getter for end date
	        holidayData[i][4] = holiday.getType();  // Assuming Holiday has a getter for the holiday type
	    }


	    // Table columns
	    String[] tableColumns = {"ID", "Employe", "Date de Debut", "Date de fin", "Type"};
	    
	    // Update the table model
	    tab.setModel(new DefaultTableModel(holidayData, tableColumns));
	}
	
	public JButton getAddBut() {
	    return ajouter;
	}
	
	public JButton getUpdateBut() {
	    return modifier;
	}
	
	public JButton getDeleteBut() {
	    return supprimer;
	}
	
	public JButton getexportbut() {
		return exporter;
	}
	
	public String gettype() {
		return typeHoliday.getSelectedItem().toString();
	}
	
	public String getEmployee() {
		return employee.getSelectedItem().toString();
	}
	
	public JComboBox<String> getRoleComboBox() {
	    return employee;
	}
	
	 public int getSelectedHolidayId() {
	        int selectedRow = tab.getSelectedRow();
	        if (selectedRow != -1) {
	            // Get the holiday ID from the first column (assuming the ID is in the first column)
	            return (int) tab.getValueAt(selectedRow, 0);
	        }
	        return -1;
	    }
	
	public JComboBox<String> getPosteComboBox() {
	    return typeHoliday;
	}
	
	public JTable getJTable() {
		return tab;
	}
	public void showMessage(String message) {
        JOptionPane.showMessageDialog(this, message);
    }

	public JTextField getDDebut() {
		
		return DDebut;
	}
	
	public JTextField getDFin() {
		
		return DFin;
	}

}
	


