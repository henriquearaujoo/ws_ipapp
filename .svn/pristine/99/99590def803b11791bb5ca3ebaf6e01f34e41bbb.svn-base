package br.com.speedy.wsipapp.repository;


import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import br.com.speedy.wsipapp.model.Barco;
import br.com.speedy.wsipapp.model.Compra;
import br.com.speedy.wsipapp.model.Configuracoes;
import br.com.speedy.wsipapp.model.Fornecedor;
import br.com.speedy.wsipapp.model.Lote;


public class ConfiguracoesRepository {
	
	
	private EntityManager manager;
	
	public ConfiguracoesRepository(){}
	
	
	public ConfiguracoesRepository(EntityManager manager){
		this.manager = manager;
	}

	public List<Configuracoes> getConfiguracoes() {
		TypedQuery<Configuracoes> query = manager.createQuery("from Configuracoes", Configuracoes.class);
		return query.getResultList();
	}
	
}
