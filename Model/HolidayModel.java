package Model;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import DAO.DBconnection;
import DAO.HolidayDAOImpl;

public class HolidayModel {
    private HolidayDAOImpl dao;
    private Connection conn;

    public HolidayModel(HolidayDAOImpl dao) {
        this.dao = dao;
        this.conn = DBconnection.getConnexion(); 
    }

    public int getEmployeeId(String fullName) {
        return dao.getEmployeeId(fullName);  
    }

    public Holiday getHolidayById(int holidayId) {
        return dao.getHolidayById(holidayId);
    }
    
    public double getSoldeDisponible(int id) {
        return dao.getSoldeDisponible(id);  
    }

    public void addHoliday(Holiday holiday) {
        dao.add(holiday); 
    }

    public void updateHoliday(Holiday holiday) {
        try {
            dao.update(holiday, holiday.getId());  // Perform the update
        } catch (Exception e) {
            e.printStackTrace();  // Handle any general exceptions
        }
    }

    public void exportData(String fileName, List<Employe> data) throws IOException {
		 dao.exportData(fileName, data);
	 }
		 
    public List<Employe> findEmployeesWithHolidaysDetailed(){
    	return dao.findEmployeesWithHolidaysDetailed();
    }

    public List<Holiday> findAll() {
		 return dao.findAll();
	 }
    
    
    public ArrayList<Holiday> getAllHolidays() {
        return dao.getAllHolidays(); 
    }

    public void deleteHoliday(int holidayId) {
        dao.delete(holidayId);  
    }

	public void updateSoldeDisponible(int empId, double newBalance) {
	 dao.updateSoldeDisponible(empId, newBalance);
		
	}

}
