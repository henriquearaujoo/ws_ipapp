package br.com.speedy.wsipapp.repository;

import br.com.speedy.wsipapp.model.Observacao;
import br.com.speedy.wsipapp.model.ObservacaoArmazenamento;
import br.com.speedy.wsipapp.util.Filtro;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

public class ObservacaoArmazenamentoRepository {


	private EntityManager manager;

	public ObservacaoArmazenamentoRepository(){}


	public ObservacaoArmazenamentoRepository(EntityManager manager){
		this.manager = manager;
	}
	
	public void salvarObservacao(ObservacaoArmazenamento observacao) {
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

	public List<ObservacaoArmazenamento> getObsArmazenamento(Filtro filtro){
		TypedQuery<ObservacaoArmazenamento> query = manager.createQuery("select new ObservacaoArmazenamento(oa.data, oa.observacao, oa.usuario.nome) from ObservacaoArmazenamento oa where oa.armazenamento.id = :id order by oa.data desc", ObservacaoArmazenamento.class);
		query.setParameter("id", filtro.getId());
		return query.getResultList();
	}

	public List<ObservacaoArmazenamento> getObsRetirada(Filtro filtro){
		TypedQuery<ObservacaoArmazenamento> query = manager.createQuery("select new ObservacaoArmazenamento(oa.data, oa.observacao, oa.usuario.nome) from ObservacaoArmazenamento oa where oa.retirada.id = :id order by oa.data desc", ObservacaoArmazenamento.class);
		query.setParameter("id", filtro.getId());
		return query.getResultList();
	}
	
}
