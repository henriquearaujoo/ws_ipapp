package br.com.speedy.wsipapp.repository;

import javax.persistence.EntityManager;

import br.com.speedy.wsipapp.model.Compra;
import br.com.speedy.wsipapp.model.Lote;
import br.com.speedy.wsipapp.model.Observacao;

public class ObservacaoRepository {
	
	
	private EntityManager manager;
	
	public ObservacaoRepository(){}
	
	
	public ObservacaoRepository(EntityManager manager){
		this.manager = manager;
	}
	
	public void salvarObservacao(Observacao observacao) {
		manager.getTransaction().begin();
	    try {
	    	if (observacao.getId() == null) {
				manager.persist(observacao);
			}else{
				manager.merge(observacao);
			}
	        
	    	manager.getTransaction().commit();
		} catch (Exception e) {
			manager.getTransaction().rollback();
			e.printStackTrace();
		}		
	}
	
}
