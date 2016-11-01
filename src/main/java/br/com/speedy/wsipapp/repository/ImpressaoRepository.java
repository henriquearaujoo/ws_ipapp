package br.com.speedy.wsipapp.repository;


import br.com.speedy.wsipapp.model.Barco;
import br.com.speedy.wsipapp.model.Fornecedor;
import br.com.speedy.wsipapp.model.Impressao;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;


public class ImpressaoRepository {


	private EntityManager manager;

	public ImpressaoRepository(){}


	public ImpressaoRepository(EntityManager manager){
		this.manager = manager;
	}


	public List<Impressao> getImpressoes() {
		TypedQuery<Impressao> query = manager.createQuery("from Impressao i", Impressao.class);
		return query.getResultList();
	}

	public void salvarImpressao(Impressao impressao){
		try{
			manager.getTransaction().begin();

			manager.persist(impressao);

			manager.getTransaction().commit();
		}catch (Exception e){
			manager.getTransaction().rollback();
			e.printStackTrace();
		}
	}


	public void deletarImpressao(Impressao impressao){
		try{
			manager.getTransaction().begin();

			manager.remove(impressao);

			manager.getTransaction().commit();
		}catch (Exception e){
			manager.getTransaction().rollback();
			e.printStackTrace();
		}
	}
	
}
