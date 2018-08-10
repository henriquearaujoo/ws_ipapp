package br.com.speedy.wsipapp.repository;


import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import br.com.speedy.wsipapp.enumerated.StatusCompra;
import br.com.speedy.wsipapp.model.*;
import br.com.speedy.wsipapp.util.Filtro;


public class CompraRepository {
	
	
	private EntityManager manager;
	
	public CompraRepository(){}
	
	
	public CompraRepository(EntityManager manager){
		this.manager = manager;
	}


	public void salvarCompra(Compra compra, Observacao observacao, Impressao impressao, Rastreabilidade rastreabilidade) {
		manager.getTransaction().begin();
	    try {
	    	if (compra.getId() == null) {
				manager.persist(compra);
			}else{
				manager.merge(compra);
			}

			if (compra.getId() != null)
				removerLotesCompra(compra);

	    	for (Lote lote : compra.getLotes()) {

				lote.setId(null);

				lote.setCompra(compra);
				if (compra.getFornecedor() != null)
					lote.setFornecedor(compra.getFornecedor());

				manager.persist(lote);

	    	 }

			if (!compra.getPause()) {
				Observacao obs = new Observacao();
				obs.setData(compra.getDataCompra());
				obs.setCompra(compra);
				obs.setObservacao("Compra enviada.");
				Usuario usu = new Usuario();
				usu.setId(compra.getIdUsuarioCompra());
				obs.setUsuario(usu);

				manager.persist(obs);
			}
			
			rastreabilidade.setData(new Date());
			manager.persist(rastreabilidade);
	    	
	    	if (observacao != null)
	    		manager.persist(observacao);
	        
	    	manager.getTransaction().commit();
		} catch (Exception e) {
			manager.getTransaction().rollback();
			e.printStackTrace();
		}		
	}
	
	public List<Compra> getComprasSalvas() {
		Query query = manager.createNativeQuery("select c.id as idCompra, to_char(c.datacompra, 'dd/MM/yyyy HH:mm:ss'), c.codigo, c.valortotal, f.id as idFornecedor, f.nome as nomeFornecedor, b.id as idBarco, b.nome as nomeBarco, c.statusCompra " +
													" from compra c " +
													" join fornecedor f on f.id = c.fornecedor_id " +
													" join barco b on b.id = c.barco_id " +
													" where c.pause = true and (statuscompra = 'SALVA' or statuscompra = 'RETORNADO_INICIO') order by c.id desc");
		List<Compra> compras = new ArrayList<Compra>();

		List<Object[]> list = query.getResultList();

		for(Object[] obj : list){
			Compra c = new Compra();
			c.setId(Long.parseLong(obj[0].toString()));
			c.setDataCompraString(obj[1].toString());
			c.setCodigo(obj[2].toString());
			c.setValorTotal(new BigDecimal(obj[3].toString()));
			c.setFornecedor(new Fornecedor());
			c.getFornecedor().setId(Long.parseLong(obj[4].toString()));
			c.getFornecedor().setNome(obj[5].toString());
			c.setBarco(new Barco());
			c.getBarco().setId(Long.parseLong(obj[6].toString()));
			c.getBarco().setNome(obj[7].toString());
			c.setStatusCompra(StatusCompra.valueOf(obj[8].toString()));

			compras.add(c);
		}

		return compras;
	}

	public Compra getCompraPorId(Compra compra){
		String jpql = "select c from Compra c where c.id = :id";
		Query query  = manager.createQuery(jpql);
		query.setParameter("id", compra.getId());
		return (Compra) (query.getResultList().size() > 0 ? query.getResultList().get(0) : null);
	}

	public void descartarCompra(Compra compra){

		try{

			manager.getTransaction().begin();

			/*for(Lote lote : compra.getLotes()){
				manager.remove(lote);
			}

			manager.remove(compra);*/

			manager.merge(compra);

			manager.getTransaction().commit();
		}catch (Exception e){
			manager.getTransaction().rollback();
			throw e;
		}
	}

	public void removerLotesCompra(Compra compra){
		try {
			String sqlDelHistorico = "delete from historico_alteracao_valor where compra_id = " + compra.getId().longValue();
			String sqlDel1 = "delete from compra_lote where compra_id = " + compra.getId().longValue();
			String sqlDel2 = "delete from lote where compra_id = " + compra.getId().longValue();
			Query query = manager.createNativeQuery(sqlDelHistorico);
			query.executeUpdate();
			query = manager.createNativeQuery(sqlDel1);
			query.executeUpdate();
			query = manager.createNativeQuery(sqlDel2);
			query.executeUpdate();
		}catch (Exception e){
			throw e;
		}
	}

	public Integer getNumComprasInconsistentes(Filtro filtro){
		Query query = manager.createNativeQuery("select count(*) from compra where pause = true and status = false and statuscompra = 'RETORNADO_INICIO' ");
		Integer num = Integer.parseInt(query.getSingleResult().toString());
		return num;
	}

	public List<Observacao> getObsCompra(Filtro filtro){
		TypedQuery<Observacao> query = manager.createQuery("select new Observacao(c.data, c.observacao, c.usuario.nome) from Observacao c where c.compra.id = :id order by c.data desc", Observacao.class);
		query.setParameter("id", filtro.getId());
		return query.getResultList();
	}

}
