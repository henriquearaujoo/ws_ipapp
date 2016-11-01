package br.com.speedy.wsipapp.repository;

import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import br.com.speedy.wsipapp.model.Peixe;
import br.com.speedy.wsipapp.util.Filtro;


public class PeixeRepository {
	
	
	private EntityManager manager;
	
	public PeixeRepository(){}
	
	
	public PeixeRepository(EntityManager manager){
		this.manager = manager;
	}
	
	
	public List<Peixe> todos() {
		TypedQuery<Peixe> query = manager.createQuery("from Peixe p order by p.descricao", Peixe.class);
		return query.getResultList();
	}

	public List<Peixe> getPeixesFiltro(Filtro filtro) {
		TypedQuery<Peixe> query = manager.createQuery("select new Peixe(p.id, p.descricao, p.valor) from Peixe p where lower(p.descricao) like '" + filtro.getDescricao().toLowerCase() + "%' and p.tipoCadastro = 'COMPRA' and p.ativo = true order by p.descricao", Peixe.class);
		return query.getResultList();
	}

	public List<Peixe> getPeixesVendaFiltro(Filtro filtro) {
		TypedQuery<Peixe> query = manager.createQuery("select new Peixe(p.id, p.descricao, p.valor) from Peixe p where lower(p.descricao) like '" + filtro.getDescricao().toLowerCase() + "%' and p.tipoCadastro = 'VENDA' and p.ativo = true order by p.descricao", Peixe.class);
		return query.getResultList();
	}
}
