package br.com.speedy.wsipapp.repository;


import br.com.speedy.wsipapp.model.Barco;
import br.com.speedy.wsipapp.model.Fornecedor;
import br.com.speedy.wsipapp.model.UltimoLogin;
import br.com.speedy.wsipapp.model.Usuario;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;


public class UltimoLoginRepository {


	private EntityManager manager;

	public UltimoLoginRepository(){}


	public UltimoLoginRepository(EntityManager manager){
		this.manager = manager;
	}


	public UltimoLogin getUltimoLoginUsuario(Usuario usuario) {
		TypedQuery<UltimoLogin> query = manager.createQuery("from UltimoLogin ul where ul.usuario.id = :id ", UltimoLogin.class);
		query.setParameter("id", usuario.getId());
		List<UltimoLogin> list = query.getResultList();
		return list != null && list.size() > 0 ? list.get(0) : null;
	}

	public Boolean salvarUltimoLogin(UltimoLogin ultimoLogin){
		try {
			manager.getTransaction().begin();
			if(ultimoLogin.getId() != null)
				manager.merge(ultimoLogin);
			else
				manager.persist(ultimoLogin);

			manager.getTransaction().commit();
			return true;
		}catch (Exception e){
			e.printStackTrace();
			manager.getTransaction().rollback();
			return false;
		}
	}
	
}
