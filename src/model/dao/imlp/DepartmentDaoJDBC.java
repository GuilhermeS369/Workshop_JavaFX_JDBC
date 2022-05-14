package model.dao.imlp;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import db.DB;
import db.DbException;
import db.DbIntegrityException;
import model.dao.DepartmentDao;
import model.entities.Department;

public class DepartmentDaoJDBC implements DepartmentDao {
	
	private Connection conn;
	// POR AQUI PASSA OQ VEM DA FACTORY Q É A CONEXÃO
	public DepartmentDaoJDBC(Connection conn) {
		this.conn = conn;
	}
	@Override
	public void insert(Department obj) {
		PreparedStatement st = null; 
		
		try {
			st = conn.prepareStatement("INSERT INTO department "
					+ "(Name) VALUES (?)", 
					Statement.RETURN_GENERATED_KEYS);
			st.setString(1, obj.getName());
			
			
		int rowsAffected = st.executeUpdate();	
			ResultSet rs = st.getGeneratedKeys();
					
			if(rowsAffected > 0) {
				if(rs.next()) { //ESSE 1 É O NUMERO DA PRIMEIRA COLUNA
					int id = rs.getInt(1);
					obj.setId(id);
				}
			DB.closeResultSet(rs);
			}	
				
		}
		catch(SQLException e)
		{
			throw new DbException (e.getMessage());
		}
		finally {
			DB.closeStatement(st);
		}
		
		
	}
	@Override
	public void update(Department obj) {
		PreparedStatement st = null;
		
		try {
			st = conn.prepareStatement("UPDATE department "
					+ "SET name = ? "
					+ "WHERE Id = ?");
			st.setString(1, obj.getName());
			st.setInt(2, obj.getId());
			
			st.executeUpdate();
		
		}
		
		catch(SQLException e)
		{
			throw new DbException (e.getMessage());
		}
		finally {
			DB.closeStatement(st);
		}
		
	}
	@Override
	public void deleteById(Integer id) {
		PreparedStatement st = null;
		
		try {
			st = conn.prepareStatement("DELETE from department "
					+" WHERE Id = ?");
			st.setInt(1, id);
			
			int rows = st.executeUpdate();
			if(rows == 0) {
				throw new DbException("NENHUM USAURIO ENCONTRADO COM ESTE ID");
			}
		}
		catch (SQLException e) {
			throw new DbIntegrityException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
		}
		
	}
	@Override
	public Department findById(Integer id) {
		PreparedStatement st = null;
		ResultSet rs = null;
		
		try {
			st = conn.prepareStatement("SELECT department.* FROM department  WHERE Id = ?");
			st.setInt(1, id);
			
			rs = st.executeQuery();
			
						
			if(rs.next()) {
				Department dep = instantiateDepartment(rs);
				return dep;
			}
			
			
		}
		catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeResultSet(rs);
			DB.closeStatement(st);
		}
				
		return null;
	}
	@Override
	public List<Department> findAll() {
		PreparedStatement st = null;
		ResultSet rs = null;
		List<Department> depList = new ArrayList<>();
		try {
				
		st = conn.prepareStatement("SELECT department.* FROM department"); // ORDER BY Name");
		
		rs = st.executeQuery();
		
		while (rs.next()) {
		Department dep = new Department();	
			dep.setId(rs.getInt("Id"));
			dep.setName(rs.getString("Name"));
		depList.add(dep);
		}
		return depList;
		
		
		}
		catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeResultSet(rs);
			DB.closeStatement(st);
		}
		
	}
	
	
	//FUNÇÃO Q INICIA UM TIPO DEPARTAMENTO E O RETORNA JA COM OS DADOS DO BANCO
	private Department instantiateDepartment(ResultSet rs) throws SQLException {
		Department dep = new Department();
		dep.setId(rs.getInt("Id"));
		dep.setName(rs.getString("Name"));
		return dep;
		
	}


}
