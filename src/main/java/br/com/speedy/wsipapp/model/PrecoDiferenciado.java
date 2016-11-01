package br.com.speedy.wsipapp.model;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.math.BigDecimal;

@Entity
@Table(name = "precoDiferenciado")
@XmlRootElement
public class PrecoDiferenciado implements AbstractEntity {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(unique = true, nullable = false)
	private Long id;

	@Column
	private BigDecimal valor;
	@ManyToOne

	@JoinColumn(name = "peixe_id")
	private Peixe peixe;

	@ManyToOne
	@JoinColumn(name="fornecedor_id")
	private Fornecedor fornecedor;

	public PrecoDiferenciado(){

	}

	public PrecoDiferenciado(Long id, String nome){
		this.id = id;
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Fornecedor getFornecedor() {
		return fornecedor;
	}
	public void setFornecedor(Fornecedor fornecedor) {
		this.fornecedor = fornecedor;
	}

	public BigDecimal getValor() {
		return valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}

	public Peixe getPeixe() {
		return peixe;
	}

	public void setPeixe(Peixe peixe) {
		this.peixe = peixe;
	}
}
