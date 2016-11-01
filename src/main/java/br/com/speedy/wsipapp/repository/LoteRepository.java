package br.com.speedy.wsipapp.repository;


import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import br.com.speedy.wsipapp.model.Compra;
import br.com.speedy.wsipapp.model.Fornecedor;
import br.com.speedy.wsipapp.model.Lote;


public class LoteRepository {
	
	
	private EntityManager manager;
	
	public LoteRepository(){}
	
	
	public LoteRepository(EntityManager manager){
		this.manager = manager;
	}

	public List<Lote> getLotesCompra(String idCompra) {
		TypedQuery<Lote> query = manager.createQuery("select new Lote(l.id, l.peixe.id, l.peixe.descricao, l.peso, l.qtdCaixas, l.valor, l.valorUnitarioPeixe, l.descontokg, l.pesoCacapa, l.desconto, l.acrescimo) from Lote l where l.compra.id = " + idCompra, Lote.class);
		return query.getResultList();
	}

	public List<Lote> getLotesPorCompra(Compra compra){
		TypedQuery<Lote> query = manager.createQuery("from Lote l where l.compra.id = " + compra.getId(), Lote.class);
		return query.getResultList();
	}
	
}
