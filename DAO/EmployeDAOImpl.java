package DAO;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import Model.Employe;
import Model.Employe.POSTE;
import Model.Employe.ROLE;

public class EmployeDAOImpl implements GenericDAOI<Employe>, DataImportExport<Employe>{
private Connection conn;

	
	public EmployeDAOImpl(){
		conn = DBconnection.getConnexion();
	}


	@Override
	public Employe findById(int employeeId) {
		String sql = "SELECT id_empl, nom, prenom, tel, salaire, email, r.nom_role AS role, p.nom_poste AS poste "
		           + "FROM employee "
		           + "LEFT JOIN poste p ON employee.poste = p.id_poste "
		           + "LEFT JOIN role r ON employee.role = r.id_role "
		           + "WHERE id_empl = ?";
		
		try(PreparedStatement stmt = conn.prepareStatement(sql)){
			stmt.setInt(1, employeeId);
			ResultSet rs = stmt.executeQuery();
			
			if(rs.next()) {
				return new Employe(
						rs.getInt("id_empl"),
						rs.getString("nom"),
						rs.getString("prenom"),
						rs.getInt("tel"),
						rs.getInt("salaire"),
						rs.getString("email"),
						ROLE.valueOf(rs.getString("nom_role").toUpperCase()),
						POSTE.valueOf(rs.getString("nom_poste").toUpperCase())
						);	
				
			} 
		}catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}	
	
	
	 

	@Override
	public void add(Employe employee) {
		
	int idPoste = -1;
	 int idRole = -1;
		String sql = "SELECT id_poste FROM poste where nom_poste = ?";
		
		try(PreparedStatement stmt = conn.prepareStatement(sql)) {
			
			stmt.setString(1, employee.getPoste().toString());
			try (ResultSet rs = stmt.executeQuery()) {
	            if (rs.next()) {
	                idPoste = rs.getInt("id_poste");
	            } else {
	                throw new SQLException("Poste not found: " + employee.getPoste().toString());
	            }
	        }
			
		} catch (SQLException e) {
			e.printStackTrace();		
		}
		
		String sql2 = "SELECT id_role FROM role where nom_role = ?";
		
		try(PreparedStatement stmt = conn.prepareStatement(sql2)) {
			
			stmt.setString(1, employee.getRole().toString());
			try (ResultSet rs = stmt.executeQuery()) {
	            if (rs.next()) {
	                idRole = rs.getInt("id_role");
	            } else {
	                throw new SQLException("Role not found: " + employee.getRole().toString());
	            }
	        }
			
		} catch (SQLException e) {
			e.printStackTrace();		
		}
			
		String sql3 = "Insert Into employee (nom, prenom, tel, salaire, email, poste, role ) values (?,?,?,?,?,?,?)";
		
		try(PreparedStatement stmt = conn.prepareStatement(sql3)) {
			
			stmt.setString(1, employee.getNom());
			stmt.setString(2, employee.getPrenom());
			stmt.setInt(3, employee.gettel());
			stmt.setInt(4, employee.getSalaire());
			stmt.setString(5, employee.getEmail());
			stmt.setInt(6, idPoste);
			stmt.setInt(7, idRole);
			stmt.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();		
		}
	}
	
	
	// getRoles
		public ArrayList<String> getRoles() {
		    ArrayList<String> roles = new ArrayList<>();
		    String sql = "SELECT nom_role FROM role"; // Assurez-vous que 'nom_role' est correct
		    try (PreparedStatement stmt = conn.prepareStatement(sql)) {
		        ResultSet rs = stmt.executeQuery();
		        while (rs.next()) {
		            roles.add(rs.getString("nom_role")); // Correspondance avec le SELECT
		        }
		    } catch (SQLException e) {
		        e.printStackTrace();
		    }
		    return roles;
		}

		
		
		// getPostes
		public ArrayList<String> getPostes() {
		    ArrayList<String> postes = new ArrayList<>();
		    String sql = "SELECT nom_poste FROM poste"; // Assurez-vous que 'nom_poste' est correct
		    try (PreparedStatement stmt = conn.prepareStatement(sql)) {
		        ResultSet rs = stmt.executeQuery();
		        while (rs.next()) {
		            postes.add(rs.getString("nom_poste")); // Correspondance avec le SELECT
		        }
		    } catch (SQLException e) {
		        e.printStackTrace();
		    }
		    return postes;
		}
		
		
		public Object[][] getEmployes() {
		    ArrayList<Employe> employes = new ArrayList<>();
		    String sql = "SELECT e.id_empl, e.nom, e.prenom, e.tel, e.salaire, e.email, " +
		                 "r.nom_role AS role_name, p.nom_poste AS poste_name " +
		                 "FROM employee e " +
		                 "JOIN poste p ON e.poste = p.id_poste " +
		                 "JOIN role r ON e.role = r.id_role";

		    try (PreparedStatement stmt = conn.prepareStatement(sql);
		         ResultSet rs = stmt.executeQuery()) {

		        while (rs.next()) {
		            try {
		                Employe employe = new Employe(
		                    rs.getInt("id_empl"),
		                    rs.getString("nom"),
		                    rs.getString("prenom"),
		                    rs.getInt("tel"),
		                    rs.getInt("salaire"),
		                    rs.getString("email"),
		                    ROLE.valueOf(rs.getString("role_name").toUpperCase()),
		                    POSTE.valueOf(rs.getString("poste_name").toUpperCase())
		                );
		                employes.add(employe);
		            } catch (IllegalArgumentException e) {
		                System.err.println("Invalid role or poste in database: " + e.getMessage());
		            }
		        }

		    } catch (SQLException e) {
		        e.printStackTrace(); // Replace with logger in production
		    }

		    // Convert List to Object[][]
		    Object[][] data = new Object[employes.size()][8];
		    for (int i = 0; i < employes.size(); i++) {
		        Employe emp = employes.get(i);
		        data[i][0] = emp.getidEmpl();      // ID
		        data[i][1] = emp.getNom();        // Nom
		        data[i][2] = emp.getPrenom();     // Prénom
		        data[i][3] = emp.gettel();        // Téléphone
		        data[i][4] = emp.getSalaire();    // Salaire
		        data[i][5] = emp.getEmail();      // Email
		        data[i][6] = emp.getRole().toString(); // Role
		        data[i][7] = emp.getPoste().toString(); // Poste
		    }

		    return data;
		}

		
		@Override
		public void update(Employe employee, int emp_id) {
		    String sql = "UPDATE employee SET nom = ?, prenom = ?, tel = ?, salaire = ?, email = ?, poste = ?, role = ? WHERE id_empl = ?";

		    try (PreparedStatement stmt = conn.prepareStatement(sql)) {
		        // Fetch IDs for poste and role
		        int idPoste = getPosteId(employee.getPoste().toString());
		        int idRole = getRoleId(employee.getRole().toString());

		        stmt.setString(1, employee.getNom());
		        stmt.setString(2, employee.getPrenom());
		        stmt.setInt(3, employee.gettel());
		        stmt.setInt(4, employee.getSalaire());
		        stmt.setString(5, employee.getEmail());
		        stmt.setInt(6, idPoste);
		        stmt.setInt(7, idRole);
		        stmt.setInt(8, emp_id);

		        stmt.executeUpdate();
		    } catch (SQLException e) {
		        e.printStackTrace();
		    }
		}

		private int getPosteId(String posteName) {
		    String sql = "SELECT id_poste FROM poste WHERE nom_poste = ?";
		    try (PreparedStatement stmt = conn.prepareStatement(sql)) {
		        stmt.setString(1, posteName);
		        ResultSet rs = stmt.executeQuery();
		        if (rs.next()) {
		            return rs.getInt("id_poste");
		        }
		    } catch (SQLException e) {
		        e.printStackTrace();
		    }
		    return -1;
		}

		private int getRoleId(String roleName) {
		    String sql = "SELECT id_role FROM role WHERE nom_role = ?";
		    try (PreparedStatement stmt = conn.prepareStatement(sql)) {
		        stmt.setString(1, roleName);
		        ResultSet rs = stmt.executeQuery();
		        if (rs.next()) {
		            return rs.getInt("id_role");
		        }
		    } catch (SQLException e) {
		        e.printStackTrace();
		    }
		    return -1;
		}

		@Override
		public void delete(int employeeId) {
		    String sql = "DELETE FROM employee WHERE id_empl = ?";
		    try (PreparedStatement stmt = conn.prepareStatement(sql)) {
		        stmt.setInt(1, employeeId);
		        int rowsAffected = stmt.executeUpdate();
		        if (rowsAffected > 0) {
		            System.out.println("Employee with ID " + employeeId + " deleted successfully.");
		        } else {
		            System.out.println("No employee found with ID " + employeeId);
		        }
		    } catch (SQLException e) {
		        e.printStackTrace();
		    }
		}
		
		
		// Get employee name by ID
	    public String getEmployeeNameById(int empId) {
	        String query = "SELECT CONCAT(nom, ' ', prenom) AS nom_prenom FROM employee WHERE id_empl = ?";
	        try (PreparedStatement stmt = conn.prepareStatement(query)) {
	            stmt.setInt(1, empId);
	            ResultSet rs = stmt.executeQuery();
	            if (rs.next()) {
	                return rs.getString("nom_prenom");
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	        return null;
	    }
	    
	    
	    
	    @Override
	    public void importData(String fileName) throws IOException {
			String sql = "Insert into employee (nom, prenom, email, tel, role, poste, salaire) values (?,?,?,?,?,?,?)";
			try(BufferedReader reader = new BufferedReader(new FileReader(fileName)); PreparedStatement pstmt = conn.prepareStatement(sql)) {
				
				String line = reader.readLine();
				while((line = reader.readLine()) != null) {
					String[] data = line.split(",");
					if(data.length == 7) {
						pstmt.setString(1, data[0].trim());
						pstmt.setString(2, data[1].trim());
						pstmt.setString(3, data[2].trim());
						pstmt.setString(4, data[3].trim());
						pstmt.setString(5, data[4].trim());
						pstmt.setString(6, data[5].trim());
						pstmt.setString(7, data[6].trim());
						pstmt.addBatch();
					

					}
				}
				pstmt.executeBatch();
				System.out.println("Employees imported successfully!");			
			} catch (IOException | SQLException e) {
				e.printStackTrace();	
			}
		}
	    	
	    
	@Override
	public void exportData(String fileName, List<Employe> data) throws IOException {
		try(BufferedWriter write= new BufferedWriter(new FileWriter(fileName))){
			write.write("First Name,Last Name,Email,Phone,Role,Poste,Salaire");
			write.newLine();
			for(Employe employe :data) {
				String line = String.format("%s,%s,%s,%s,%s,%s,%d",
						employe.getPrenom(),
						employe.getNom(),
						employe.getEmail(),
						employe.gettel(),
						employe.getRole(),
						employe.getPoste(),
						employe.getSalaire()
						);
				write.write(line);
				write.newLine();
			}
			
			
		}
		
	}
	
	@Override
	public List<Employe> findAll() {
		 List<Employe> employes = new ArrayList<>();
		    String sql = "SELECT e.nom, e.prenom, e.tel, e.salaire, e.email, " +
		                 "r.nom_role AS role_name, p.nom_poste AS poste_name " +
		                 "FROM employee e " +
		                 "JOIN poste p ON e.poste = p.id_poste " +
		                 "JOIN role r ON e.role = r.id_role";

		    try (PreparedStatement stmt = conn.prepareStatement(sql);
		         ResultSet rs = stmt.executeQuery()) {

		        while (rs.next()) {
		            try {
		                Employe employe = new Employe(
		                    rs.getString("nom"),
		                    rs.getString("prenom"),
		                    rs.getInt("tel"),
		                    rs.getInt("salaire"),
		                    rs.getString("email"),
		                    ROLE.valueOf(rs.getString("role_name").toUpperCase()),
		                    POSTE.valueOf(rs.getString("poste_name").toUpperCase())
		                );
		                employes.add(employe);
		            } catch (IllegalArgumentException e) {
		                System.err.println("Invalid role or poste in database: " + e.getMessage());
		            }
		        }

		    } catch (SQLException e) {
		        e.printStackTrace(); 
		    }
		return employes;
	}






	
}

