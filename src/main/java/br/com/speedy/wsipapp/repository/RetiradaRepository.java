package br.com.speedy.wsipapp.repository;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import br.com.speedy.wsipapp.enumerated.StatusArmazenamento;
import br.com.speedy.wsipapp.model.Armazenamento;
import br.com.speedy.wsipapp.model.Barco;
import br.com.speedy.wsipapp.model.Compra;
import br.com.speedy.wsipapp.model.Fornecedor;
import br.com.speedy.wsipapp.model.Lote;
import br.com.speedy.wsipapp.model.Peixe;
import br.com.speedy.wsipapp.model.Retirada;
import br.com.speedy.wsipapp.util.Filtro;
import br.com.speedy.wsipapp.util.PeixeDisponivel;


public class RetiradaRepository {
	
	
	private EntityManager manager;
	
	public RetiradaRepository(){}
	
	
	public RetiradaRepository(EntityManager manager){
		this.manager = manager;
	}
	
	public void salvarRetirada(Retirada retirada){
		try {
			manager.getTransaction().begin();
			if(retirada.getId() == null)
				manager.persist(retirada);
			else
				manager.merge(retirada);
			manager.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			manager.getTransaction().rollback();
		}
	}

	public List<Retirada> getInconsistenciasRetiradas(Filtro filtro){
		TypedQuery<Retirada> query = manager.createQuery("select r from Retirada r where r.status = '" + StatusArmazenamento.RETORNADO + "' AND r.usuario.id = :id", Retirada.class);
		query.setParameter("id", filtro.getId());
		return query.getResultList();
	}

	public Integer getNumInconsistenciasRetiradas(Filtro filtro){
		Query query = manager.createNativeQuery("select count(r.id) from retirada r join usuario u on u.id = r.usuario_id " +
				" where r.status = '" + StatusArmazenamento.RETORNADO + "' AND u.id = :id");
		query.setParameter("id", filtro.getId());
		Integer num = Integer.parseInt(query.getSingleResult().toString());
		return num;
	}
}
