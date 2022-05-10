package model.services;

import java.util.List;

import model.dao.DaoFactory;
import model.dao.DepartmentDao;
import model.entities.Department;

public class DepartmentService {
	
	DepartmentDao departmentDao = DaoFactory.createDepartmentDao();
	
	public List<Department> findAll() {
		return departmentDao.findAll();
	}
	
	public void saveOrUpdate(Department obj) {
		if(obj.getId() == null) {
			departmentDao.insert(obj);
		}
		else {
			departmentDao.update(obj);
		}
	}
}