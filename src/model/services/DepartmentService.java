package model.services;

import java.util.List;

import model.dao.DaoFactory;
import model.dao.DepartmentDao;
import model.entities.Department;

public class DepartmentService {

	public List<Department> findAll() {
		
		DepartmentDao departmentDao = DaoFactory.createDepartmentDao();
		return departmentDao.findAll();
	}
}