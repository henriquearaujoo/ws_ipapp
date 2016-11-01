package br.com.speedy.wsipapp.model;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * Created by henrique on 28/04/2015.
 */
@Entity
@Table(name = "produto")
public class Produto implements AbstractEntity{

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

    @ManyToOne
    private Peixe peixe;

    @ManyToOne
    private TipoPeixe tipoPeixe;

    @ManyToOne
    private TamanhoPeixe tamanhoPeixe;

    @ManyToOne
    private Camara camara;

    @ManyToOne
    private PosicaoCamara posicaoCamara;

    @ManyToOne
    private Pedido pedido;

    @OneToMany(mappedBy="produto", fetch=FetchType.LAZY)
    private List<Romaneio> romaneios;

    public Produto(){

    }

    public Produto(Long id, BigDecimal peso, Long idPeixe, String descricaoPeixe, Long idTipo, String descricaoTipo){
        this.id = id;
        this.peso = peso;
        this.peixe = new Peixe();
        this.peixe.setId(idPeixe);
        this.peixe.setDescricao(descricaoPeixe);
        this.tipoPeixe = new TipoPeixe();
        this.tipoPeixe.setId(idTipo);
        this.tipoPeixe.setDescricao(descricaoTipo);
    }

    public Produto(Long id, BigDecimal peso, Long idPeixe, String descricaoPeixe, Long idTipo, String descricaoTipo, Long idTamanho, String descricaoTamanho){
        this.id = id;
        this.peso = peso;
        this.peixe = new Peixe();
        this.peixe.setId(idPeixe);
        this.peixe.setDescricao(descricaoPeixe);
        this.tipoPeixe = new TipoPeixe();
        this.tipoPeixe.setId(idTipo);
        this.tipoPeixe.setDescricao(descricaoTipo);
        this.tamanhoPeixe = new TamanhoPeixe();
        this.tamanhoPeixe.setId(idTamanho);
        this.tamanhoPeixe.setDescricao(descricaoTamanho);
    }

    public Produto(Long id, BigDecimal peso, Long idPeixe, String descricaoPeixe, Long idTipo, String descricaoTipo, Long idCamara, String descricaoCamara, Long idPosicao, String descricaoPosicao){
        this.id = id;
        this.peso = peso;
        this.peixe = new Peixe();
        this.peixe.setId(idPeixe);
        this.peixe.setDescricao(descricaoPeixe);
        this.tipoPeixe = new TipoPeixe();
        this.tipoPeixe.setId(idTipo);
        this.tipoPeixe.setDescricao(descricaoTipo);
        this.camara = new Camara();
        this.camara.setId(idCamara);
        this.camara.setDescricao(descricaoCamara);
        this.posicaoCamara = new PosicaoCamara();
        this.posicaoCamara.setId(idPosicao);
        this.posicaoCamara.setDescricao(descricaoPosicao);
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

    public TipoPeixe getTipoPeixe() {
        return tipoPeixe;
    }

    public void setTipoPeixe(TipoPeixe tipoPeixe) {
        this.tipoPeixe = tipoPeixe;
    }

    public Pedido getPedido() {
        return pedido;
    }

    public void setPedido(Pedido pedido) {
        this.pedido = pedido;
    }

    public List<Romaneio> getRomaneios() {
        return romaneios;
    }

    public void setRomaneios(List<Romaneio> romaneios) {
        this.romaneios = romaneios;
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

    public TamanhoPeixe getTamanhoPeixe() {
        return tamanhoPeixe;
    }

    public void setTamanhoPeixe(TamanhoPeixe tamanhoPeixe) {
        this.tamanhoPeixe = tamanhoPeixe;
    }
}
