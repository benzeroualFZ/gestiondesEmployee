package DAO;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import Model.Holiday;

public class HolidayDAOImpl implements GenericDAOI<Holiday> {
    private Connection conn;

    public HolidayDAOImpl() {
        conn = DBconnection.getConnexion();
    }

    // Get all holiday types
    public ArrayList<String> getType() {
        ArrayList<String> typeHoliday = new ArrayList<>();
        String sql = "SELECT type FROM type"; 
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                typeHoliday.add(rs.getString("type"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return typeHoliday;
    }

    // Get all employees
    public ArrayList<String> getEmploye() {
        ArrayList<String> employee = new ArrayList<>();
        String sql = "SELECT CONCAT(nom, ' ', prenom) AS nom_prenom FROM employee";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                employee.add(rs.getString("nom_prenom"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return employee;
    }
    
    public void updateSoldeDisponible(int empId, double newBalance) {
        String sql = "UPDATE employee SET solde = ? WHERE id_empl = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDouble(1, newBalance);
            stmt.setInt(2, empId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Get total allocated leave balance for an employee
    public double getSoldeDisponible(int empId) {
        String query = "SELECT solde FROM employee WHERE id_empl = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, empId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getDouble("solde");
            } else {
                throw new SQLException("Employee with ID " + empId + " not found.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
    
    

    // Get total taken holidays for an employee
    public double getTakenHolidays(int empId) {
        String query = "SELECT SUM(DATEDIFF(DFin, DDebut)) AS totalDays FROM holiday WHERE id_empl = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, empId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getDouble("totalDays");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    // Get remaining leave balance for an employee
    public double getRemainingHolidayBalance(int empId) {
        double totalHolidays = getSoldeDisponible(empId);
        double takenHolidays = getTakenHolidays(empId);
        return totalHolidays - takenHolidays;
    }

    // Check if an employee can take a holiday based on remaining balance
    public boolean canTakeHoliday(int empId, long duration) {
        double remainingBalance = getRemainingHolidayBalance(empId);
        return remainingBalance >= duration;
    }

    @Override
    public void add(Holiday holiday) {
        int idType = -1;
        String sql = "SELECT id_type FROM type WHERE type = ?";
        
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, holiday.getType().toString());
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    idType = rs.getInt("id_type");
                } else {
                    throw new SQLException("Type not found: " + holiday.getType().toString());
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();        
        }
        
        // Get employee ID by full name
        int idEmp = getEmployeeId(holiday.getEmployeeName());
        if (idEmp == -1) {
            throw new IllegalStateException("Employee not found: " + holiday.getEmployeeName());
        }

        // Calculate holiday duration
        long duration = (holiday.getDFin().getTime() - holiday.getDDebut().getTime()) / (1000 * 60 * 60 * 24);

        // Check if employee can take this holiday
        if (!canTakeHoliday(idEmp, duration)) {
            throw new IllegalStateException("Insufficient holiday balance for employee: " + holiday.getEmployeeName());
        }

        String query = "INSERT INTO holiday (DDebut, DFin, type, id_empl) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setDate(1, new java.sql.Date(holiday.getDDebut().getTime()));
            stmt.setDate(2, new java.sql.Date(holiday.getDFin().getTime()));
            stmt.setInt(3, idType);
            stmt.setInt(4, idEmp);
            stmt.executeUpdate();

            // Update employee's remaining holiday balance
            String updateQuery = "UPDATE employee SET solde = solde - ? WHERE id_empl = ?";
            try (PreparedStatement updateStmt = conn.prepareStatement(updateQuery)) {
                updateStmt.setDouble(1, duration);
                updateStmt.setInt(2, idEmp);
                updateStmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Get employee ID from their full name
    public int getEmployeeId(String fullName) {
        String sql = "SELECT id_empl FROM employee WHERE CONCAT(nom, ' ', prenom) = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, fullName);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("id_empl");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    @Override
    public Holiday findById(int id) {
        String query = "SELECT * FROM holiday WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Date startDate = rs.getDate("DDebut");
                Date endDate = rs.getDate("DFin");
                int typeOrdinal = rs.getInt("type");
                int empId = rs.getInt("id_empl");
                Holiday.Type type = Holiday.Type.values()[typeOrdinal];
                return new Holiday(id, startDate, endDate, type, empId);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Holiday> findAll() {
        return getAllHolidays();
    }

    @Override
    public void update(Holiday holiday, int id) {
        String query = "UPDATE holiday SET DDebut = ?, DFin = ?, type = ?, id_empl = ? WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setDate(1, new java.sql.Date(holiday.getDDebut().getTime()));
            stmt.setDate(2, new java.sql.Date(holiday.getDFin().getTime()));
            stmt.setInt(3, holiday.getType().ordinal());
            stmt.setInt(4, holiday.getempId());
            stmt.setInt(5, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(int id) {
        String query = "DELETE FROM holiday WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
//getHolidayById
    public Holiday getHolidayById(int id) {
        String sql = "SELECT id, DDebut, DFin, type, id_empl FROM holiday WHERE id = ?";
        Holiday holiday = null;

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                int holidayId = rs.getInt("id");
                Date DDebut = rs.getDate("DDebut");
                Date DFin = rs.getDate("DFin");
                int typeOrdinal = rs.getInt("type"); // Assume `type` stores an ordinal value
                int empId = rs.getInt("id_empl");

                // Use Holiday.Type.values() to get the type enum directly
                Holiday.Type type = Holiday.Type.values()[typeOrdinal];

                // Create a Holiday instance
                holiday = new Holiday(holidayId, DDebut, DFin, type, empId);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ArrayIndexOutOfBoundsException ex) {
            System.err.println("Invalid type ordinal in database: " + ex.getMessage());
        }

        return holiday;
    }


    // Get all holidays
    public ArrayList<Holiday> getAllHolidays() {
        ArrayList<Holiday> holidays = new ArrayList<>();
        String query = "SELECT * FROM holiday";

        try (PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("id");
                int empId = rs.getInt("id_empl");
                Date startDate = rs.getDate("DDebut");
                Date endDate = rs.getDate("DFin");
                int typeOrdinal = rs.getInt("type");

                try {
                    Holiday.Type type = Holiday.Type.values()[typeOrdinal];
                    Holiday holiday = new Holiday(id, startDate, endDate, type, empId);
                    holidays.add(holiday);
                } catch (ArrayIndexOutOfBoundsException ex) {
                    ex.printStackTrace();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return holidays;
    }
}
