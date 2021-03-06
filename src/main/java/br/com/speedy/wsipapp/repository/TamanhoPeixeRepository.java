package br.com.speedy.wsipapp.repository;


import br.com.speedy.wsipapp.model.Barco;
import br.com.speedy.wsipapp.model.Fornecedor;
import br.com.speedy.wsipapp.model.TamanhoPeixe;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;


public class TamanhoPeixeRepository {


	private EntityManager manager;

	public TamanhoPeixeRepository(){}


	public TamanhoPeixeRepository(EntityManager manager){
		this.manager = manager;
	}


	public List<TamanhoPeixe> getTamanhos() {
		TypedQuery<TamanhoPeixe> query = manager.createQuery("select tp from TamanhoPeixe tp where tp.descricao <> 'Sem Tamanho' order by tp.descricao asc", TamanhoPeixe.class);
		return query.getResultList();
	}
	
}
