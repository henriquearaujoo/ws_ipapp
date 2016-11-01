package br.com.speedy.wsipapp.repository;


import br.com.speedy.wsipapp.model.Barco;
import br.com.speedy.wsipapp.model.Fornecedor;
import br.com.speedy.wsipapp.model.TipoPeixe;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;


public class TipoPeixeRepository {


	private EntityManager manager;

	public TipoPeixeRepository(){}


	public TipoPeixeRepository(EntityManager manager){
		this.manager = manager;
	}


	public List<TipoPeixe> getTiposPeixe() {
		TypedQuery<TipoPeixe> query = manager.createQuery("select tp from TipoPeixe tp order by tp.descricao asc", TipoPeixe.class);
		return query.getResultList();
	}
	
}
