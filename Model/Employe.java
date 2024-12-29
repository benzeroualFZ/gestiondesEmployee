package Model;



public class Employe {
	private int id_empl;
	private String nom;
	private String prenom;
	private String email;
	private int tel;
	private int salaire;
	private ROLE role;
	private POSTE poste;
	public Holiday holiday;
	
	public Employe(int id_empl,String nom,String prenom,int tel,int salaire, String email,ROLE role,POSTE poste){
		this.id_empl = id_empl;
		this.nom = nom;
		this.prenom = prenom;
		this.email = email;
		this.tel = tel;
		this.salaire = salaire;
		this.poste= poste;
		this.role=role;
	     this.holiday = holiday; 
	}
	public Employe(String nom, String prenom, int tel, int salaire, String email, ROLE role, POSTE poste) {
	    this.nom = nom;
	    this.prenom = prenom;
	    this.email = email;
	    this.tel = tel;
	    this.salaire = salaire;
	    this.role = role;
	    this.poste = poste;
	     this.holiday = holiday; 
	}

	public enum ROLE {
		
		ADMIN,
		EMPLOYEE,
		Manager

	}

	public enum POSTE {
		INGENIEUR_ETUDE_ET_DEVELOPPEMENT,
		TEAM_LEADER,
		PILOTE
	}
	
	public int getidEmpl() {
		return id_empl;
	}
	
	public String getNom() {
		return nom;
	}
	
	public String getPrenom() {
		return prenom;
	}
	
	public String getEmail() {
		return email;
	}
	
	public int gettel() {
		return tel;
	}
	
	public int getSalaire() {
		return salaire;
	}
	
	public ROLE getRole() {
		return role;
	}
	
	public POSTE getPoste() {
		return poste;
	}
	
	public Holiday getHoliday() {
		return holiday;
	}
	  
    public void setHoliday(Holiday Holiday) {
        this.holiday = Holiday;
    }
}
