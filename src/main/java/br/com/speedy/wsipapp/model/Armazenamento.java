package br.com.speedy.wsipapp.model;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.*;

import br.com.speedy.wsipapp.enumerated.StatusArmazenamento;
import br.com.speedy.wsipapp.enumerated.TipoArmazenamento;
import br.com.speedy.wsipapp.enumerated.TipoDestinoRetirada;

@Entity
@Table(name = "armazenamento")
public class Armazenamento implements AbstractEntity{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(unique = true, nullable = false)
	private Long id;

	@Column
	private BigDecimal peso;

	@Column
	private BigDecimal pesoEmbalagem;

	@Column
	private Integer qtdeEmbalagem;

	@Column
	private Date data;

	@Column
	private Date dataSalvo;

	@Enumerated(EnumType.STRING)
	private StatusArmazenamento status;

	@ManyToOne
	@JoinColumn(name="tipopeixe_id")
	private TipoPeixe tipoPeixe;

	@ManyToOne
	@JoinColumn(name="tamanhopeixe_id")
	private TamanhoPeixe tamanhoPeixe;

	@ManyToOne
	@JoinColumn(name="embalagem_id")
	private Embalagem embalagem;

	@ManyToOne
	@JoinColumn(name="camara_id")
	private Camara camara;

	@ManyToOne
	@JoinColumn(name="posicaocamara_id")
	private PosicaoCamara posicaoCamara;

	@ManyToOne
	@JoinColumn(name="peixe_id")
	private Peixe peixe;

	@ManyToOne
	@JoinColumn(name="usuario_id")
	private Usuario usuario;

	@Transient
	private String observacao;

	public Armazenamento(){

	}

	public Date getDataSalvo() {
		return dataSalvo;
	}

	public void setDataSalvo(Date dataSalvo) {
		this.dataSalvo = dataSalvo;
	}

	public Armazenamento(Long id){
		this.id = id;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public BigDecimal getPeso() {
		return peso;
	}

	public void setPeso(BigDecimal peso) {
		this.peso = peso;
	}

	public Peixe getPeixe() {
		return peixe;
	}

	public void setPeixe(Peixe peixe) {
		this.peixe = peixe;
	}

	public BigDecimal getPesoEmbalagem() {
		return pesoEmbalagem;
	}

	public void setPesoEmbalagem(BigDecimal pesoEmbalagem) {
		this.pesoEmbalagem = pesoEmbalagem;
	}

	public Integer getQtdeEmbalagem() {
		return qtdeEmbalagem;
	}

	public void setQtdeEmbalagem(Integer qtdeEmbalagem) {
		this.qtdeEmbalagem = qtdeEmbalagem;
	}

	public TipoPeixe getTipoPeixe() {
		return tipoPeixe;
	}

	public void setTipoPeixe(TipoPeixe tipoPeixe) {
		this.tipoPeixe = tipoPeixe;
	}

	public TamanhoPeixe getTamanhoPeixe() {
		return tamanhoPeixe;
	}

	public void setTamanhoPeixe(TamanhoPeixe tamanhoPeixe) {
		this.tamanhoPeixe = tamanhoPeixe;
	}

	public Embalagem getEmbalagem() {
		return embalagem;
	}

	public void setEmbalagem(Embalagem embalagem) {
		this.embalagem = embalagem;
	}

	public Camara getCamara() {
		return camara;
	}

	public void setCamara(Camara camara) {
		this.camara = camara;
	}

	public PosicaoCamara getPosicaoCamara() {
		return posicaoCamara;
	}

	public void setPosicaoCamara(PosicaoCamara posicaoCamara) {
		this.posicaoCamara = posicaoCamara;
	}

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public StatusArmazenamento getStatus() {
		return status;
	}

	public void setStatus(StatusArmazenamento status) {
		this.status = status;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public String getObservacao() {
		return observacao;
	}

	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}
}
