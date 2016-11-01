package br.com.speedy.wsipapp.model;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "configuracoes")
public class Configuracoes implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column
	private BigDecimal pesoCacapa;
	
	@Column
	private BigDecimal valorMinimoDebitoAdiantamento;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public BigDecimal getPesoCacapa() {
		return pesoCacapa;
	}

	public void setPesoCacapa(BigDecimal pesoCacapa) {
		this.pesoCacapa = pesoCacapa;
	}

	public BigDecimal getValorMinimoDebitoAdiantamento() {
		return valorMinimoDebitoAdiantamento;
	}

	public void setValorMinimoDebitoAdiantamento(
			BigDecimal valorMinimoDebitoAdiantamento) {
		this.valorMinimoDebitoAdiantamento = valorMinimoDebitoAdiantamento;
	}

}
