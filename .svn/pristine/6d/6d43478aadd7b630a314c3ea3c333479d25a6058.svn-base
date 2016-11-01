package br.com.speedy.wsipapp.model;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "arquivo")
public class Arquivo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy =  GenerationType.IDENTITY)
	private Long id;
	@Column
	private String nome;
	@Column
	private String tipo;
	@Column
	private byte[] dados;
	@Column
	private Long tamanho;

	@ManyToOne
	private Peixe peixe;
	
	public Arquivo(){
		
	}
	
	public Arquivo(Long id, String nome, String tipo){
		this.id = id;
		this.nome = nome;
		this.tipo = tipo;
	}
	
	public Arquivo(Long id, String nome, String tipo, Long tamanho){
		this.id = id;
		this.nome = nome;
		this.tipo = tipo;
		this.tamanho = tamanho;
	}

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getTipo() {
		return tipo;
	}
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	public byte[] getDados() {
		return dados;
	}
	public void setDados(byte[] dados) {
		this.dados = dados;
	}
	public Long getTamanho() {
		return tamanho;
	}
	public void setTamanho(Long tamanho) {
		this.tamanho = tamanho;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Arquivo other = (Arquivo) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}


	public Peixe getPeixe() {
		return peixe;
	}

	public void setPeixe(Peixe peixe) {
		this.peixe = peixe;
	}

}
