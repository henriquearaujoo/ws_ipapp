package br.com.speedy.wsipapp.model;


import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlRootElement;

import br.com.speedy.wsipapp.enumerated.StatusCompra;


@Entity
@Table(name="compra")
@XmlRootElement
public class Compra implements AbstractEntity {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(unique=true, nullable=false)
	private Long id;
	@Temporal(TemporalType.TIMESTAMP)
	private Date dataCompra;
	@Column
	private BigDecimal valorTotal;
	@Column(columnDefinition="TEXT")
	private String observacao;
	@Column(columnDefinition="TEXT")
	private String observacaoVerificacao;
	@Column
	private Boolean status;
	@Column
	private Boolean pause;
	@ManyToOne
	@JoinColumn(name="fornecedor_id")
	private Fornecedor fornecedor;
	@ManyToOne
	@JoinColumn(name="barco_id")
	private Barco barco;
	@OneToMany(mappedBy="compra" ,fetch=FetchType.LAZY)
	private List<Lote> lotes;
	@Column
	private String codigo;
	@Column
	private Boolean autorizado;
	@Enumerated(EnumType.STRING)
	private StatusCompra statusCompra;
	
	@Transient
	private String dataCompraString;

	@Transient
	private Long idUsuarioCompra;
	
	public Compra(){
		
		
	}
	
	public Compra(Long id, Date dataCompra, String codigo, BigDecimal valorTotal, Long idFornecedor, String nomeFornecedor, Long idBarco, String nomeBarco, String observacao){
		this.id = id;
		this.dataCompra = dataCompra;
		this.dataCompraString = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(this.dataCompra);
		this.codigo = codigo;
		this.valorTotal = valorTotal;
		this.fornecedor = new Fornecedor();
		this.fornecedor.setId(idFornecedor);
		this.fornecedor.setNome(nomeFornecedor);
		this.barco = new Barco();
		this.barco.setId(idBarco);
		this.barco.setNome(nomeBarco);
		this.observacao = observacao;
		
	}

	public Compra(Long id, Date dataCompra, String codigo, BigDecimal valorTotal, Long idFornecedor, String nomeFornecedor, Long idBarco, String nomeBarco, String observacao, String statusCompra){
		this.id = id;
		this.dataCompra = dataCompra;
		this.dataCompraString = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(this.dataCompra);
		this.codigo = codigo;
		this.valorTotal = valorTotal;
		this.fornecedor = new Fornecedor();
		this.fornecedor.setId(idFornecedor);
		this.fornecedor.setNome(nomeFornecedor);
		this.barco = new Barco();
		this.barco.setId(idBarco);
		this.barco.setNome(nomeBarco);
		this.observacao = observacao;
		this.statusCompra = StatusCompra.valueOf(statusCompra);
	}
	
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public Date getDataCompra() {
		return dataCompra;
	}

	public void setDataCompra(Date dataCompra) {
		this.dataCompra = dataCompra;
	}

	public String getObservacao() {
		return observacao;
	}

	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}
	
	
	public Fornecedor getFornecedor() {
		return fornecedor;
	}

	public void setFornecedor(Fornecedor fornecedor) {
		this.fornecedor = fornecedor;
	}

	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}


	public BigDecimal getValorTotal() {
		return valorTotal;
	}


	public void setValorTotal(BigDecimal valorTotal) {
		this.valorTotal = valorTotal;
	}


	public List<Lote> getLotes() {
		return lotes;
	}


	public void setLotes(List<Lote> lotes) {
		this.lotes = lotes;
	}


	public Boolean getPause() {
		return pause;
	}


	public void setPause(Boolean pause) {
		this.pause = pause;
	}


	public String getCodigo() {
		return codigo;
	}


	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public String getDataCompraString() {
		return dataCompraString;
	}

	public void setDataCompraString(String dataCompraString) {
		this.dataCompraString = dataCompraString;
	}

	public Barco getBarco() {
		return barco;
	}

	public void setBarco(Barco barco) {
		this.barco = barco;
	}

	public Boolean getAutorizado() {
		return autorizado;
	}

	public void setAutorizado(Boolean autorizado) {
		this.autorizado = autorizado;
	}

	public StatusCompra getStatusCompra() {
		return statusCompra;
	}

	public void setStatusCompra(StatusCompra statusCompra) {
		this.statusCompra = statusCompra;
	}

	public String getObservacaoVerificacao() {
		return observacaoVerificacao;
	}

	public void setObservacaoVerificacao(String observacaoVerificacao) {
		this.observacaoVerificacao = observacaoVerificacao;
	}

	public Long getIdUsuarioCompra() {
		return idUsuarioCompra;
	}

	public void setIdUsuarioCompra(Long idUsuarioCompra) {
		this.idUsuarioCompra = idUsuarioCompra;
	}
}
