package br.com.speedy.wsipapp.repository;


import br.com.speedy.wsipapp.model.TipoPeixe;
import br.com.speedy.wsipapp.model.Transportadora;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;


public class TransportadoraRepository {


	private EntityManager manager;

	public TransportadoraRepository(){}


	public TransportadoraRepository(EntityManager manager){
		this.manager = manager;
	}


	public List<Transportadora> getTransportadoras() {
		TypedQuery<Transportadora> query = manager.createQuery("select new Transportadora(t.id, t.nome) from Transportadora t order by t.nome asc", Transportadora.class);
		return query.getResultList();
	}
	
}
