package br.com.speedy.wsipapp.repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import br.com.speedy.wsipapp.model.Fornecedor;
import br.com.speedy.wsipapp.util.Filtro;

public class FornecedoresRepository {
	
	private EntityManager manager;
	
	public FornecedoresRepository(){}
	
	
	public FornecedoresRepository(EntityManager manager){
		this.manager = manager;
	}
	
	
	public List<Fornecedor> todos() {
		TypedQuery<Fornecedor> query = manager.createQuery("select new Fornecedor(f.id, f.nome, f.cpf, f.cnpj) from Fornecedor f order by f.nome", Fornecedor.class);
		return query.getResultList();
	}

	public List<Fornecedor> getFornecedoresFiltro(Filtro filtro) {
		TypedQuery<Fornecedor> query = manager.createQuery("select new Fornecedor(f.id, f.nome, f.cpf, f.cnpj) from Fornecedor f where ((f.cpf is not null and f.cpf is not empty and f.cpf != '') or (f.cnpj is not null and f.cnpj is not empty and f.cnpj != '')) and lower(f.nome) like '" + filtro.getNome().toLowerCase() + "%' order by f.nome", Fornecedor.class);
		return query.getResultList();
	}

}
