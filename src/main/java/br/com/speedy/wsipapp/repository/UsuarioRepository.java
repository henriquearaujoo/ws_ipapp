package br.com.speedy.wsipapp.repository;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import br.com.speedy.wsipapp.model.Usuario;


public class UsuarioRepository {
	
	

	private EntityManager manager;
	
	public UsuarioRepository(){}
	
	@Inject
	public UsuarioRepository(EntityManager manager){
		this.manager = manager;
	}

	public Usuario getUsuario(String login, String senha, String imei){
		//String jpql = "select u from Usuario u where u.login = :login and u.senha = :senha and u.imei = :imei";
		String jpql = "select u from Usuario u where u.login = :login and u.senha = :senha";
		Query query  = manager.createQuery(jpql);
		query.setParameter("login", login);
		query.setParameter("senha", senha);
		//query.setParameter("imei", imei);
		return (Usuario) (query.getResultList().size() > 0 ? query.getResultList().get(0) : null);
	}

	public Usuario getUsuarioPorId(Usuario usuario){
		String jpql = "select u from Usuario u where u.id = :id";
		Query query  = manager.createQuery(jpql);
		query.setParameter("id", usuario.getId());
		return (Usuario) (query.getResultList().size() > 0 ? query.getResultList().get(0) : null);
	}

	public Boolean salvarUsuario(Usuario usuario){
		try {
			manager.getTransaction().begin();
			if (usuario.getId() != null)
				manager.merge(usuario);
			else
				manager.persist(usuario);
			manager.getTransaction().commit();
			return true;
		}catch (Exception e){
			e.printStackTrace();
			manager.getTransaction().rollback();
			return false;
		}
	}


}
