package br.com.speedy.wsipapp.enumerated;

public enum TipoCadastro {

	COMPRA("Compra"),
	VENDA("Venda");

	private String tipo;

	private TipoCadastro(String tipo) {
		this.tipo = tipo;
	}

	public String getTipo() {
		return tipo;
	}

}
