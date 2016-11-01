package br.com.speedy.wsipapp.repository;


import br.com.speedy.wsipapp.enumerated.StatusPedido;
import br.com.speedy.wsipapp.model.*;
import br.com.speedy.wsipapp.util.Filtro;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.Date;
import java.util.List;


public class PedidoRepository {


	private EntityManager manager;

	public PedidoRepository(){}


	public PedidoRepository(EntityManager manager){
		this.manager = manager;
	}

	public Pedido getPedidoPorId(Long id){
		TypedQuery<Pedido> query = manager.createQuery("select new Pedido(p.id, p.data, p.dataEntrega, p.valor, p.codigo, p.vendedor.id, p.vendedor.nome, p.cliente.id, p.cliente.nome, p.transportadora.id, p.transportadora.nome, p.qtdeParcelas, p.placaCarro) from Pedido p where p.id = :id", Pedido.class);
		query.setParameter("id", id);
		return query.getSingleResult();
	}

	public List<Pedido> getPedidosPorDia(Filtro filtro) {
		TypedQuery<Pedido> query = manager.createQuery("select new Pedido(p.id, p.data, p.valor, p.codigo, p.vendedor.id, p.vendedor.nome, p.cliente.id, p.cliente.nome, p.transportadora.id, p.transportadora.nome) from Pedido p where to_char(p.data, 'dd/MM/yyyy') = '" + filtro.getData() + "' and p.status = '" + StatusPedido.AGUARDANDO_EMBARQUE + "' order by p.id asc", Pedido.class);
		return query.getResultList();
	}

	public List<Pedido> getPedidosPorStatus(Filtro filtro) {
		TypedQuery<Pedido> query = manager.createQuery("select new Pedido(p.id, p.data, p.codigo, p.dataEntrega) from Pedido p where p.status = '" + filtro.getStatus() + "' order by p.id desc", Pedido.class);
		return query.getResultList();
	}

	public void salvarPedido(Pedido pedido){
		try{
			manager.getTransaction().begin();

			if (pedido.getId() == null)
				manager.persist(pedido);
			else
				manager.merge(pedido);

			for(Produto produto : pedido.getProdutos()){
				for (Romaneio romaneio : produto.getRomaneios()){
					if (romaneio.getId() == null)
						manager.persist(romaneio);
					else
						manager.merge(romaneio);
				}
			}

			HistoricoPedido historico = new HistoricoPedido();
			historico.setData(new Date());
			Usuario usu = new Usuario();
			usu.setId(pedido.getIdUsuario());
			historico.setUsuario(usu);
			historico.setPedido(pedido);
			historico.setObservacao("Pedido embarcado");

			manager.persist(historico);

			manager.getTransaction().commit();
		}catch (Exception e){
			e.printStackTrace();
			manager.getTransaction().rollback();
		}
	}
	
}
