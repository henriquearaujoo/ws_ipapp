package br.com.speedy.wsipapp.model;

import javax.persistence.*;

/**
 * Created by henrique on 28/04/2015.
 */
@Entity
@Table(name="impressora")
public class Impressora implements AbstractEntity{

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(unique=true, nullable=false)
    private Long id;

    @Column
    private String nome;

    @Column
    private Boolean ativo;

    @Override
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

    public Boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }
}
