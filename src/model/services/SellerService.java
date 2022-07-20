package model.services;

import java.util.List;

import model.dao.DaoFactory;
import model.dao.SellerDao;
import model.entities.Seller;

public class SellerService {
	// INSTANCIA DA FABRICA DE DAO
	SellerDao sellerDao = DaoFactory.createSellerDao();
	// METODO PARA TRAZER TUDO
	public List<Seller> findAll() {
		return sellerDao.findAll();
	}
	// METODO Q DECIDE ENTRE SALVAR E CRIAR
	public void saveOrUpdate(Seller obj) {
		if(obj.getId() == null) {
			sellerDao.insert(obj);
		}
		else {
			sellerDao.update(obj);
		}
	}
	// METODO PARA REMOVER O OBJETO
	public void remove (Seller obj) {
		sellerDao.deleteById(obj.getId());
	}
}