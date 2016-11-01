package br.com.speedy.wsipapp.repository;


import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import br.com.speedy.wsipapp.model.Barco;
import br.com.speedy.wsipapp.model.Compra;
import br.com.speedy.wsipapp.model.Fornecedor;
import br.com.speedy.wsipapp.model.Lote;


public class BarcoRepository {
	
	
	private EntityManager manager;
	
	public BarcoRepository(){}
	
	
	public BarcoRepository(EntityManager manager){
		this.manager = manager;
	}


	public List<Barco> getBarcosPorFornecedor(Fornecedor fornecedor) {
		TypedQuery<Barco> query = manager.createQuery("select new Barco(b.id, b.nome) from Barco b where b.fornecedor.id = :id order by b.nome asc", Barco.class);
		query.setParameter("id", fornecedor.getId());
		return query.getResultList();
	}
	
}
