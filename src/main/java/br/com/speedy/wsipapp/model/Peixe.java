package br.com.speedy.wsipapp.model;

import br.com.speedy.wsipapp.enumerated.TipoCadastro;
import com.sun.org.apache.xpath.internal.operations.Bool;

import java.math.BigDecimal;
import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name = "peixe")
@XmlRootElement
public class Peixe implements AbstractEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7543518951292238000L;

	private Long id;

	private String descricao;

	private BigDecimal valor;

	private String urlFoto;

	private TipoCadastro tipoCadastro;

	private Boolean ativo;

	public Peixe() {

	}

	public Peixe(Long id, String descricao) {
		this.id = id;
		this.descricao = descricao;
	}

	public Peixe(Long id, String descricao, BigDecimal valor) {
		this.id = id;
		this.descricao = descricao;
		this.valor = valor;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(unique = true, nullable = false)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column
	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	@Column
	public BigDecimal getValor() {
		return valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}

	@Transient
	public String getUrlFoto() {
		return urlFoto;
	}

	public void setUrlFoto(String urlFoto) {
		this.urlFoto = urlFoto;
	}

	@Enumerated(EnumType.STRING)
	public TipoCadastro getTipoCadastro() {
		return tipoCadastro;
	}

	public void setTipoCadastro(TipoCadastro tipoCadastro) {
		this.tipoCadastro = tipoCadastro;
	}

	@Column
	public Boolean getAtivo() {
		return ativo;
	}

	public void setAtivo(Boolean ativo) {
		this.ativo = ativo;
	}
}

