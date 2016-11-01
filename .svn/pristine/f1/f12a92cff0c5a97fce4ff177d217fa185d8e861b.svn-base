package br.com.speedy.wsipapp.repository;


import br.com.speedy.wsipapp.model.Barco;
import br.com.speedy.wsipapp.model.Camara;
import br.com.speedy.wsipapp.model.Fornecedor;
import br.com.speedy.wsipapp.model.PosicaoCamara;
import br.com.speedy.wsipapp.util.Filtro;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;


public class PosicaoCamaraRepository {


	private EntityManager manager;

	public PosicaoCamaraRepository(){}


	public PosicaoCamaraRepository(EntityManager manager){
		this.manager = manager;
	}


	public List<PosicaoCamara> getPosicoesPorCamara(Filtro filtro) {
		TypedQuery<PosicaoCamara> query = manager.createQuery("select pc from PosicaoCamara pc where pc.camara.id = :id order by pc.descricao asc", PosicaoCamara.class);
		query.setParameter("id", filtro.getId());
		return query.getResultList();
	}
	
}
