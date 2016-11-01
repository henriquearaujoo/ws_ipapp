package br.com.speedy.wsipapp.repository;


import br.com.speedy.wsipapp.model.Barco;
import br.com.speedy.wsipapp.model.Fornecedor;
import br.com.speedy.wsipapp.model.Peixe;
import br.com.speedy.wsipapp.model.PrecoDiferenciado;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;


public class PrecoDiferenciadoRepository {


	private EntityManager manager;

	public PrecoDiferenciadoRepository(){}


	public PrecoDiferenciadoRepository(EntityManager manager){
		this.manager = manager;
	}

	public PrecoDiferenciado getPrecoDiferenciadoPorFornecedorEPeixe(Fornecedor fornecedor, Peixe peixe){
		String jpql = "select p from PrecoDiferenciado p where p.fornecedor.id = :id and p.peixe.id = :idPeixe order by p.peixe.descricao";
		TypedQuery<PrecoDiferenciado> query = manager.createQuery(jpql, PrecoDiferenciado.class);
		query.setParameter("id", fornecedor.getId().longValue());
		query.setParameter("idPeixe", peixe.getId().longValue());
		return query.getResultList() != null && query.getResultList().size() > 0 ? query.getResultList().get(0) : null;
	}
	
}
