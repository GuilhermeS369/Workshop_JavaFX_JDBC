package model.services;

import java.util.List;

import model.dao.DaoFactory;
import model.dao.DepartmentDao;
import model.entities.Department;

public class DepartmentService {
	// INSTANCIA DA FABRICA DE DAO
	DepartmentDao departmentDao = DaoFactory.createDepartmentDao();
	// METODO PARA TRAZER TUDO
	public List<Department> findAll() {
		return departmentDao.findAll();
	}
	// METODO Q DECIDE ENTRE SALVAR E CRIAR
	public void saveOrUpdate(Department obj) {
		if(obj.getId() == null) {
			departmentDao.insert(obj);
		}
		else {
			departmentDao.update(obj);
		}
	}
	// METODO PARA REMOVER O OBJETO
	public void remove (Department obj) {
		departmentDao.deleteById(obj.getId());
	}
}