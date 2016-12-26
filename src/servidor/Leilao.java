package servidor;

/**
 * Created by Ana Rita on 26/12/2016.
 */
public class Leilao {
    private String descricao;
    private String nomeVendedor;
    private String nomeCompradorAtual;
    private int licitacaoAtual;

    public Leilao(String descricao, String nomeVendedor, String nomeCompradorAtual, int licitacaoAtual) {
        this.descricao = descricao;
        this.nomeVendedor = nomeVendedor;
        this.nomeCompradorAtual = nomeCompradorAtual;
        this.licitacaoAtual = licitacaoAtual;
    }

    public Leilao(String descricao, String nomeVendedor) {
        this.descricao = descricao;
        this.nomeVendedor = nomeVendedor;
        licitacaoAtual = 0;
    }



    public String getDescricao() {
        return descricao;
    }

    public String getNomeVendedor() {
        return nomeVendedor;
    }

    public String getNomeCompradorAtual() {
        return nomeCompradorAtual;
    }

    public int getLicitacaoAtual() {
        return licitacaoAtual;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public void setNomeVendedor(String nomeVendedor) {
        this.nomeVendedor = nomeVendedor;
    }

    public void setNomeCompradorAtual(String nomeCompradorAtual) {
        this.nomeCompradorAtual = nomeCompradorAtual;
    }

    public void setLicitacaoAtual(int licitacaoAtual) {
        this.licitacaoAtual = licitacaoAtual;
    }
}
