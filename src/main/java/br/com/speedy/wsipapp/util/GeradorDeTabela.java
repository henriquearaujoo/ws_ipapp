package br.com.speedy.wsipapp.util;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class GeradorDeTabela {

	public static void main(String[] args) {
		EntityManagerFactory factory = Persistence.createEntityManagerFactory("ws_ipapp");
	    factory.close();
	}
	
}
