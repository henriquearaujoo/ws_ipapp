package br.com.speedy.wsipapp.model;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by henrique on 2015-04-08.
 */
@Entity
@Table(name="ultimologin")
@XmlRootElement
public class UltimoLogin implements AbstractEntity{

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Column
    private Date data;

    @OneToOne
    private Usuario usuario;

    @Transient
    private String dataFormatada;

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

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public String getDataFormatada() {
        if (data != null)
            dataFormatada = new SimpleDateFormat("hh:mm:ss   dd/MM/yyyy").format(data);
        return dataFormatada;
    }

    public void setDataFormatada(String dataFormatada) {
        this.dataFormatada = dataFormatada;
    }
}
