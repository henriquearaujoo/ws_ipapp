package br.com.speedy.wsipapp.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by henrique on 2015-04-13.
 */
@Entity
@Table(name = "observacaoarmazenamento")
public class ObservacaoArmazenamento implements AbstractEntity{

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition="TEXT")
    private String observacao;

    @Column
    private Date data;

    @ManyToOne
    private Retirada retirada;

    @ManyToOne
    private Armazenamento armazenamento;

    @ManyToOne
    private Usuario usuario;

    public ObservacaoArmazenamento(){

    }

    public ObservacaoArmazenamento(Date data, String observacao, String nomeUsuario){
        this.data = data;
        this.observacao = observacao;
        this.usuario = new Usuario();
        this.usuario.setNome(nomeUsuario);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public Retirada getRetirada() {
        return retirada;
    }

    public void setRetirada(Retirada retirada) {
        this.retirada = retirada;
    }

    public Armazenamento getArmazenamento() {
        return armazenamento;
    }

    public void setArmazenamento(Armazenamento armazenamento) {
        this.armazenamento = armazenamento;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
}
