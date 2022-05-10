package model.dao.imlp;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import db.DB;
import db.DbException;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

public class SellerDaoJDBC implements SellerDao{
	
	private Connection conn;
	// POR AQUI PASSA OQ VEM DA FACTORY Q É A CONEXÃO
	public SellerDaoJDBC(Connection conn) {
		this.conn = conn;
	}

	@Override
	public void insert(Seller obj) {
		PreparedStatement st = null; //RECEBE O COMANDO
			
		try { //PREPARA A QUERY COM RETORNO DE UM NUMERO
			st = conn.prepareStatement(
					"INSERT INTO seller "
					+ "(Name, Email, BirthDate, BaseSalary, DepartmentId) "
					+ "VALUES "
					+ "(?, ?, ?, ?, ?)", 
					Statement.RETURN_GENERATED_KEYS);
			Department dep = obj.getDepartment();	
			// CHAMA O DEPARTAMENTO DO OBJETO
			// A DATA AQUI PRECISA SER DO JAVA.SQL
			st.setString(1, obj.getName());
			st.setString(2, obj.getEmail());
			st.setDate(3, new java.sql.Date(obj.getBirthDate().getTime()));	
			st.setDouble(4, obj.getBaseSalary());
			st.setInt(5, dep.getId());
			//RETORNA AS LINHAS AFETAS
			int rowsAffected = st.executeUpdate();
			
			//SE FOR MAIOR QUE 0, ENTAO ELE PUXA AS CHAVES E ATRIBUI NA LISTA RS
			if(rowsAffected > 0) {
				ResultSet rs = st.getGeneratedKeys();
				//SE HOUVER ALGO NA PROXIMA LINHA, ENTAO ELE SETA O ID DO OBJETO SELLER CO MO PROXIMO ID				
				if(rs.next()){
					int id = rs.getInt(1);
					obj.setId(id);
					
				}

				DB.closeResultSet(rs);
			}// SE NAO, DEU RUIM E PRECISA LANÇAR A EXCEÇÃO
			else {
				throw new DbException("ERRO INESPERADO, NENHUMA LINHA AFETADA");
			}
		}
		catch(SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
			
		}
	}

	@Override
	public void update(Seller obj) {
		PreparedStatement st = null; //RECEBE O COMANDO
		
		try { //PREPARA A QUERY COM RETORNO DE UM NUMERO
			st = conn.prepareStatement(
					"UPDATE seller "
					+"SET Name = ?, Email = ?, BirthDate = ?, BaseSalary = ?, DepartmentId = ? "
					+"WHERE Id = ?", 
					Statement.RETURN_GENERATED_KEYS);
			Department dep = obj.getDepartment();	
			// CHAMA O DEPARTAMENTO DO OBJETO
			// A DATA AQUI PRECISA SER DO JAVA.SQL
			st.setString(1, obj.getName());
			st.setString(2, obj.getEmail());
			st.setDate(3, new java.sql.Date(obj.getBirthDate().getTime()));	
			st.setDouble(4, obj.getBaseSalary());
			st.setInt(5, dep.getId());
			
			st.setInt(6, obj.getId());
			
			st.executeUpdate();
			
			
		}
		catch(SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
			
		}
		
	}

	@Override
	public void deleteById(Integer id) {
		
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement ("DELETE FROM seller "
					+ "WHERE Id = ?");
			st.setInt(1, id);
			int rows = st.executeUpdate();
			if (rows == 0) {
				throw new DbException("Nenhum com este ID usuario encontrado");
			}
		
		
		}
		catch(SQLException e) {
		throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
		}
				
	}

	@Override
	public Seller findById(Integer id) {
		PreparedStatement st = null; //RECEBE O COMANDO
		ResultSet rs = null; //LISTA O RETORNO
		
		try {
			st = conn.prepareStatement(
					"SELECT seller.*,department.Name as DepName "
					+"FROM seller INNER JOIN department "
					+"ON seller.DepartmentId = department.Id "
					+"WHERE seller.Id = ?");
				st.setInt(1, id);
				rs = st.executeQuery();
				if (rs.next()) {
					Department dep = instantiateDepartment(rs);
					Seller obj = instantiateSeller(rs, dep);
					return obj;
			}
				return null;
		}
		catch(SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}
	
	@Override
	public List<Seller> findAll() {
		PreparedStatement st = null; //RECEBE O COMANDO
		ResultSet rs = null; //LISTA O RETORNO
		
		try {
			st = conn.prepareStatement(
					"SELECT seller.*,department.Name as DepName "
					+ "FROM seller INNER JOIN department "
					+ "ON seller.DepartmentId = department.Id ");
					//+ "ORDER BY Name");
			
				rs = st.executeQuery();
				
				List<Seller> list = new ArrayList<>();
				Map<Integer, Department> map = new HashMap<>();
				
				while (rs.next()) {
					//AQUI BUSCO DENTRO DO MEU MAP SE EXISTE O MESMO ID
					//Q ESTA NO DepartmentId, CASO NÃO TENHA, A VARIAVEL DEP VAI SER NULL
					Department dep = map.get(rs.getInt("DepartmentId"));
					// CASO SEJA NULO, ELE INSTANCIA E INSERE NO MAP ID E O DEPARTAMENTO
					if (dep == null) {
					dep = instantiateDepartment(rs);
					map.put(rs.getInt("DepartmentId"), dep);
					}
				
					Seller obj = instantiateSeller(rs, dep);
					list.add(obj);
			}
				return list;
		}
		catch(SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
		
	}
	
	@Override
	public List<Seller> findByDepartment(Department department) {
		PreparedStatement st = null; //RECEBE O COMANDO
		ResultSet rs = null; //LISTA O RETORNO
		
		try {
			st = conn.prepareStatement(
					"SELECT seller.*,department.Name as DepName "
					+ "FROM seller INNER JOIN department "
					+ "ON seller.DepartmentId = department.Id "
					+ "WHERE DepartmentId = ? "
					+ "ORDER BY Name");
			
				st.setInt(1, department.getId());
				
				rs = st.executeQuery();
				
				List<Seller> list = new ArrayList<>();
				Map<Integer, Department> map = new HashMap<>();
				
				while (rs.next()) {
					//AQUI BUSCO DENTRO DO MEU MAP SE EXISTE O MESMO ID
					//Q ESTA NO DepartmentId, CASO NÃO TENHA, A VARIAVEL DEP VAI SER NULL
					Department dep = map.get(rs.getInt("DepartmentId"));
					// CASO SEJA NULO, ELE INSTANCIA E INSERE NO MAP ID E O DEPARTAMENTO
					if (dep == null) {
					dep = instantiateDepartment(rs);
					map.put(rs.getInt("DepartmentId"), dep);
					}
				
					Seller obj = instantiateSeller(rs, dep);
					list.add(obj);
			}
				return list;
		}
		catch(SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
		
	}
	
	//FUNÇÕES Q INICIA UM TIPO SELLER E JA RETORNA COM OS DADOS DO BANCO DE DADOS
	private Seller instantiateSeller(ResultSet rs, Department dep) throws SQLException{
		
		Seller obj = new Seller();
		obj.setId(rs.getInt("Id"));
		obj.setName(rs.getString("Name"));
		obj.setEmail(rs.getString("Email"));
		obj.setBirthDate(rs.getDate("BirthDate"));
		obj.setBaseSalary(rs.getDouble("BaseSalary"));
		obj.setDepartment(dep);
		return obj;
		
	}
	//FUNÇÃO Q INICIA UM TIPO DEPARTAMENTO E O RETORNA JA COM OS DADOS DO BANCO
	private Department instantiateDepartment(ResultSet rs) throws SQLException {
		Department dep = new Department();
		dep.setId(rs.getInt("DepartmentId"));
		dep.setName(rs.getString("DepName"));
		return dep;
		
	}

	

}

	

