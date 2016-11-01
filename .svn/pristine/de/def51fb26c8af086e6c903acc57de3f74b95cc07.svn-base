package br.com.speedy.wsipapp.repository;


import br.com.speedy.wsipapp.model.Impressao;
import br.com.speedy.wsipapp.model.Impressora;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;


public class ImpressoraRepository {


	private EntityManager manager;

	public ImpressoraRepository(){}


	public ImpressoraRepository(EntityManager manager){
		this.manager = manager;
	}


	public List<Impressora> getImpressoras() {
		TypedQuery<Impressora> query = manager.createQuery("from Impressora i where i.ativo = true order by id desc", Impressora.class);
		return query.getResultList();
	}
	
}
