package model.dao;
import java.util.List;

import model.entities.Department;
import model.entities.Seller;

public interface SellerDao {
	
	// OPERA플O RESPONSAVEL POR INSERIR NO BANCO DE DADOS ESSE OBJETO Q SERA ENVIADO COMO PARAMETO DE ENTRADA
		void insert(Seller obj);
		// OPERA플O ATUALIZAR UM OBJ DENTRO DO BANCO DE DADOS
		void update(Seller obj);
		// OPERA플O QUE DELETARA OBJETOS CONFORME O ID
		void deleteById(Integer id);
		// OBJETO DO TIPO DEPARTAMENTO Q RECEBE UM ID PARA PROCURAR NO BANCO DE DADOS
		Seller findById(Integer id);
		// OPERA플O QUE RETORNA UMA LISTA DE TODOS OS DEPARTAMENTOS
		List<Seller> findAll();
		// OPERA플O QUE RETORNA UMA LISTA DE TODOS OS FUNCIONARIOS DE UM DEPARTAMENTO
		List<Seller> findByDepartment(Department department);
	
	
	
	
	
}
