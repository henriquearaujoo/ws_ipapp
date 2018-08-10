package br.com.speedy.wsipapp.enumerated;

import javax.enterprise.inject.Instance;

public enum StatusArmazenamento {

	
	ENVIADO("ENVIADO"),
	AUTORIZADO("AUTORIZADO"),
	RETORNADO("RETORNADO"),
	TUNEL("TUNEL"),
	SALVO("SALVO");
	
	
	private String status;
	
	private StatusArmazenamento(String status) {
		this.status = status;
	}

	public String getStatus() {
		return status;
	}

}
