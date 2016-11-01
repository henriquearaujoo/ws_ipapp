package br.com.speedy.wsipapp.enumerated;

public enum StatusCompra {

	ENVIADO("Enviado primeiro posto"),
	PAGO("Pago"),
	AUTORIZADO("Autorizado"),
	AGUARDA_AUTORIZACAO("Aguardando autorizacao"),
	RETORNADO("Retorno para correcao"),
	RETORNADO_INICIO("Retorno para o primeiro posto"),
	DESCARTADA("Descartada"),
	SALVA("Salva");

	private String status;
	
	private StatusCompra(String status) {
		this.status = status;
	}

	public String getStatus() {
		return status;
	}

}
