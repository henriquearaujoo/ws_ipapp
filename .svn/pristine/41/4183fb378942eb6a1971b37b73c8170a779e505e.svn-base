package br.com.speedy.wsipapp.model;

import javax.persistence.*;

/**
 * Created by henrique on 28/04/2015.
 */
@Entity
@Table(name="impressao")
public class Impressao implements AbstractEntity{

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(unique=true, nullable=false)
    private Long id;

    @Column(columnDefinition = "TEXT")
    private String conteudo;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getConteudo() {
        return conteudo;
    }

    public void setConteudo(String conteudo) {
        this.conteudo = conteudo;
    }
}
