package br.com.speedy.wsipapp.model;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name="lote")
public class Lote implements AbstractEntity{
	
	public static final BigDecimal PESO_CACAPA = new BigDecimal(2);
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(unique=true, nullable=false)
	private Long id;
	@ManyToOne
	@JoinColumn(name="peixe_id")
	private Peixe peixe;
	@ManyToOne
	@JoinColumn(name="fornecedor_id")
	private Fornecedor fornecedor;
	@ManyToOne
	@JoinColumn(name="compra_id")
	private Compra compra;
	@Column
	private BigDecimal peso;
	@Column
	private BigDecimal valor;
	@Column
	private Integer qtdCaixas;
	@Column
	private BigDecimal valorUnitarioPeixe;
	@Column
	private Integer sequencia;
	@Column
	private BigDecimal descontokg;
	@Column
	private BigDecimal desconto;
	@Column
	private BigDecimal acrescimo;
	@Column
	private BigDecimal pesoCacapa;
	@Column
	private Boolean isPrecoDiferenciado;
	
	public Lote(){
	}
	
	public Lote(Long id, Long idPeixe, String descricaoPeixe, BigDecimal peso, Integer qtdCaixas, BigDecimal valor, BigDecimal valorUnitarioPeixe, BigDecimal descontokg, BigDecimal pesoCacapa){
		this.id = id;
		this.peixe = new Peixe();
		peixe.setId(idPeixe);
		peixe.setDescricao(descricaoPeixe);
		this.peso = peso;
		this.qtdCaixas = qtdCaixas;
		this.valor = valor;
		this.valorUnitarioPeixe = valorUnitarioPeixe;
		this.descontokg = descontokg;
		this.pesoCacapa = pesoCacapa;
		
	}

	public Lote(Long id, Long idPeixe, String descricaoPeixe, BigDecimal peso, Integer qtdCaixas, BigDecimal valor, BigDecimal valorUnitarioPeixe, BigDecimal descontokg, BigDecimal pesoCacapa, BigDecimal desconto, BigDecimal acrescimo){
		this.id = id;
		this.peixe = new Peixe();
		peixe.setId(idPeixe);
		peixe.setDescricao(descricaoPeixe);
		this.peso = peso;
		this.qtdCaixas = qtdCaixas;
		this.valor = valor;
		this.valorUnitarioPeixe = valorUnitarioPeixe;
		this.descontokg = descontokg;
		this.pesoCacapa = pesoCacapa;
		this.desconto = desconto;
		this.acrescimo = acrescimo;

	}

	public BigDecimal getPesoLiquido(){
		return peso.subtract(pesoCacapa.multiply(new BigDecimal(qtdCaixas)));
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public Peixe getPeixe() {
		return peixe;
	}

	public void setPeixe(Peixe peixe) {
		this.peixe = peixe;
	}

	
	public BigDecimal getPeso() {
		return peso;
	}

	public void setPeso(BigDecimal peso) {
		this.peso = peso;
	}
	
	public BigDecimal getValor() {
		return valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}
	
	public Fornecedor getFornecedor() {
		return fornecedor;
	}

	public void setFornecedor(Fornecedor fornecedor) {
		this.fornecedor = fornecedor;
	}
	
	public Integer getQtdCaixas() {
		return qtdCaixas;
	}

	public void setQtdCaixas(Integer qtdCaixas) {
		this.qtdCaixas = qtdCaixas;
	}


	public Compra getCompra() {
		return compra;
	}


	public void setCompra(Compra compra) {
		this.compra = compra;
	}

	public BigDecimal getValorUnitarioPeixe() {
		return valorUnitarioPeixe;
	}


	public void setValorUnitarioPeixe(BigDecimal valorUnitarioPeixe) {
		this.valorUnitarioPeixe = valorUnitarioPeixe;
	}

	public Integer getSequencia() {
		return sequencia;
	}

	public void setSequencia(Integer sequencia) {
		this.sequencia = sequencia;
	}

	public BigDecimal getDescontokg() {
		return descontokg;
	}

	public void setDescontokg(BigDecimal descontokg) {
		this.descontokg = descontokg;
	}

	public BigDecimal getDesconto() {
		return desconto;
	}

	public void setDesconto(BigDecimal desconto) {
		this.desconto = desconto;
	}

	public BigDecimal getAcrescimo() {
		return acrescimo;
	}

	public void setAcrescimo(BigDecimal acrescimo) {
		this.acrescimo = acrescimo;
	}

	public BigDecimal getPesoCacapa() {
		return pesoCacapa;
	}

	public void setPesoCacapa(BigDecimal pesoCacapa) {
		this.pesoCacapa = pesoCacapa;
	}

	public Boolean getIsPrecoDiferenciado() {
		return isPrecoDiferenciado;
	}

	public void setIsPrecoDiferenciado(Boolean isPrecoDiferenciado) {
		this.isPrecoDiferenciado = isPrecoDiferenciado;
	}
}
