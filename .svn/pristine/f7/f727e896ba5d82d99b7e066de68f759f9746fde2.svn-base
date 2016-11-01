package br.com.speedy.wsipapp.repository;


import br.com.speedy.wsipapp.model.Barco;
import br.com.speedy.wsipapp.model.Camara;
import br.com.speedy.wsipapp.model.Fornecedor;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;


public class CamaraRepository {


	private EntityManager manager;

	public CamaraRepository(){}


	public CamaraRepository(EntityManager manager){
		this.manager = manager;
	}


	public List<Camara> getCamaras() {
		TypedQuery<Camara> query = manager.createQuery("select new Camara(c.id, c.descricao) from Camara c order by c.descricao asc", Camara.class);
		return query.getResultList();
	}
	
}
