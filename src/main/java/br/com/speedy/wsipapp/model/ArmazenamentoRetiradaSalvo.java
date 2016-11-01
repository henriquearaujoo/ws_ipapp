package br.com.speedy.wsipapp.model;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by Henrique on 01/08/2016.
 */
@Entity
@Table(name = "armazenamento_retirada_salvo")
public class ArmazenamentoRetiradaSalvo implements AbstractEntity{
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private Long id;

    @Column
    private Date data;

    @Column
    private Boolean ativo;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public Boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }
}
