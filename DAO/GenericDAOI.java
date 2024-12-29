package DAO;

import java.util.List;

import Model.Employe;
import Model.Employe.POSTE;
import Model.Employe.ROLE;

public interface GenericDAOI<T> {
	
	  T findById(int id);
	  void add(T entity);
	  List<T> findAll();
	  void update(T entity, int id);
	  void delete(int id);
}
