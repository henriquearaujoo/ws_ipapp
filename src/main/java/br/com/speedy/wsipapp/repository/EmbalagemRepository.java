package br.com.speedy.wsipapp.repository;


import br.com.speedy.wsipapp.model.Barco;
import br.com.speedy.wsipapp.model.Embalagem;
import br.com.speedy.wsipapp.model.Fornecedor;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;


public class EmbalagemRepository {


	private EntityManager manager;

	public EmbalagemRepository(){}


	public EmbalagemRepository(EntityManager manager){
		this.manager = manager;
	}


	public List<Embalagem> getEmbalagens() {
		TypedQuery<Embalagem> query = manager.createQuery("select e from Embalagem e order by e.descricao asc", Embalagem.class);
		return query.getResultList();
	}
	
}
