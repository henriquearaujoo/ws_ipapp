package br.com.speedy.wsipapp.repository;


import br.com.speedy.wsipapp.enumerated.StatusPedido;
import br.com.speedy.wsipapp.model.Pedido;
import br.com.speedy.wsipapp.model.Produto;
import br.com.speedy.wsipapp.util.Filtro;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;


public class ProdutoRepository {


	private EntityManager manager;

	public ProdutoRepository(){}


	public ProdutoRepository(EntityManager manager){
		this.manager = manager;
	}


	public List<Produto> getProdutosPorPedido(Filtro filtro) {
		TypedQuery<Produto> query = manager.createQuery("select new Produto(p.id, p.peso, p.peixe.id, p.peixe.descricao, p.tipoPeixe.id, p.tipoPeixe.descricao, p.tamanhoPeixe.id, p.tamanhoPeixe.descricao) from Produto p where p.pedido.id = :id order by p.id asc", Produto.class);
		query.setParameter("id", filtro.getId());
		return query.getResultList();
	}
	
}
