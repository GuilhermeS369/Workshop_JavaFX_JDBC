package db;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class DB {
	//METODO DE CONEXÃO COM O BANCO DE DADOS DO JDBC:
	private static Connection conn = null;
	//METODO PARA CONECTAR O BANCO DE DADOS
	//O METODO RETORNA UM TIPO CONNECTION 
	public static Connection getConnection() {
		if (conn == null) {
			try {
				Properties props = loadProperties();
				//PEGAR AS PROPRIEDADES DE CONEXÃO
				String url = props.getProperty("dburl");
				conn = DriverManager.getConnection(url, props);
			}
			catch(SQLException e) {
				throw new DbException(e.getMessage());
			}
		}
		return conn;
		//ELE TERA DE TORNAR O OBJETO CONN, MAS SE O CONN ESTIVER NULO
		// ELE FAZ UM LOAD PROPIERTIES QUE É O METODO ABAIXO DE FAZER ALEITURA 
		// DS DADOS E TRAZER PARA NÓS.
		
	}
	
	public static void closeConnection () {
		if (conn != null) {
			try {	
				conn.close();
			}
			catch(SQLException e){
				throw new DbException(e.getMessage());
				
			}
		}
	}

	
	private static Properties loadProperties() {
		try (FileInputStream fs = new FileInputStream("db.properties")){
			Properties props = new Properties();
			props.load(fs);
			//O COMANDO LOAD FAZ A LEITURA DO ARQUIVO APONTADO PELA NOVA INSTANCIA ACIMA
			//GUARDA DENTRO DO PROPS
			return props;
		}
		catch (IOException e) {//PRECISA TRATAR AS EXCESSÕES DESSE ERRO
		throw new DbException(e.getMessage());
		}
	}
	
	public static void closeStatement(Statement st) {
		if (st != null)
			try {
				st.close();
			} catch (SQLException e) {
				throw new DbException (e.getMessage());
			}
	}
	
	public static void closeResultSet(ResultSet rs) {
		if (rs != null)
			try {
				rs.close();
			} catch (SQLException e) {
				throw new DbException (e.getMessage());
			}
	}
	
}
