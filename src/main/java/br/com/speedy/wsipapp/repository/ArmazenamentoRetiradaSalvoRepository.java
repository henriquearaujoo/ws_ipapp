package br.com.speedy.wsipapp.repository;

import br.com.speedy.wsipapp.model.ArmazenamentoRetiradaSalvo;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

/**
 * Created by Henrique on 01/08/2016.
 */
public class ArmazenamentoRetiradaSalvoRepository {

    private EntityManager manager;

    public ArmazenamentoRetiradaSalvoRepository(){}


    public ArmazenamentoRetiradaSalvoRepository(EntityManager manager){
        this.manager = manager;
    }

    public List<ArmazenamentoRetiradaSalvo> ObterSalvos(){
        TypedQuery<ArmazenamentoRetiradaSalvo> query = manager.createQuery("select a from ArmazenamentoRetiradaSalvo a where a.ativo = true", ArmazenamentoRetiradaSalvo.class);
        return query.getResultList();
    }

    public void Salvar(ArmazenamentoRetiradaSalvo salvo){
        try{
            if (salvo.getId() != null)
                manager.merge(salvo);
            else
                manager.persist(salvo);
        }catch (Exception e){
            throw e;
        }
    }
}
