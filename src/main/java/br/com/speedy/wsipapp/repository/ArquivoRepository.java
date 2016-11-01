package br.com.speedy.wsipapp.repository;


import br.com.speedy.wsipapp.model.Arquivo;
import br.com.speedy.wsipapp.model.Barco;
import br.com.speedy.wsipapp.model.Fornecedor;
import br.com.speedy.wsipapp.model.Peixe;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;


public class ArquivoRepository {


	private EntityManager manager;

	public ArquivoRepository(){}


	public ArquivoRepository(EntityManager manager){
		this.manager = manager;
	}


	public List<Arquivo> getArquivoPorPeixe(Peixe peixe) {
		TypedQuery<Arquivo> query = manager.createQuery("select new Arquivo(a.id, a.nome, a.tipo) from Arquivo a where a.peixe.id = :id", Arquivo.class);
		query.setParameter("id", peixe.getId());
		return query.getResultList();
	}
	
}
