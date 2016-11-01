package br.com.speedy.wsipapp.model;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * Created by henrique on 05/05/2015.
 */
@Entity
@Table(name = "romaneio")
public class Romaneio implements AbstractEntity{

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private Long id;

    @Column
    private String lote;

    @Column
    private Integer qtdeEmbalagens;

    @ManyToOne
    @JoinColumn(name = "produto_id")
    private Produto produto;

    @ManyToOne
    @JoinColumn(name = "tipopeixe_id")
    private TipoPeixe tipoPeixe;

    @ManyToOne
    @JoinColumn(name = "tamanhopeixe_id")
    private TamanhoPeixe tamanhoPeixe;

    @ManyToOne
    @JoinColumn(name = "camara_id")
    private Camara camara;

    @ManyToOne
    @JoinColumn(name = "posicaocamara_id")
    private PosicaoCamara posicaoCamara;

    @Column
    private BigDecimal peso;

    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLote() {
        return lote;
    }

    public void setLote(String lote) {
        this.lote = lote;
    }

    public Integer getQtdeEmbalagens() {
        return qtdeEmbalagens;
    }

    public void setQtdeEmbalagens(Integer qtdeEmbalagens) {
        this.qtdeEmbalagens = qtdeEmbalagens;
    }

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    public TipoPeixe getTipoPeixe() {
        return tipoPeixe;
    }

    public void setTipoPeixe(TipoPeixe tipoPeixe) {
        this.tipoPeixe = tipoPeixe;
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

    public BigDecimal getPeso() {
        return peso;
    }

    public void setPeso(BigDecimal peso) {
        this.peso = peso;
    }

    public TamanhoPeixe getTamanhoPeixe() {
        return tamanhoPeixe;
    }

    public void setTamanhoPeixe(TamanhoPeixe tamanhoPeixe) {
        this.tamanhoPeixe = tamanhoPeixe;
    }
}
