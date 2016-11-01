package br.com.speedy.wsipapp.rn;

import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import br.com.speedy.wsipapp.model.Peixe;
import br.com.speedy.wsipapp.repository.PeixeRepository;

public class PeixeRn {
	
	@Inject
	private PeixeRepository peixes;
	
	
	public PeixeRn(){
		
	}
	
	public PeixeRn(PeixeRepository peixes){
		this.peixes = peixes;
	}
	
	public List<Peixe> todos() {
		List<Peixe> lista = peixes.todos();
		return lista;
	}
	
}
