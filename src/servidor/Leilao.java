package servidor;

import java.util.Map;
import java.util.TreeMap;

/**
 * <h1>Leilão</h1>
 * A partir do momento que o Leilão é criado é instanciado
 * um objeto do tipo Leilão, onde é criado o nome , atribuido
 * pelo criador, o nome do vendedor, o nome do comprador atual
 * a licitação em que este decorre e uma estrutura p/ guardar
 * as licitações feitas pelos clientes.
 *
 * @author  Ana Rita,Helder Sousa, Jorge Cardoso
 * @version 1.0
 * @since   2016
 */


public class Leilao {
    private String descricao;
    private String nomeVendedor;
    private String nomeCompradorAtual;
    private int licitacaoAtual;
    private Map<String, Integer> licitacoes;


    public Leilao(String descricao, String nomeVendedor) {
        this.descricao = descricao;
        this.nomeVendedor = nomeVendedor;
        nomeCompradorAtual="sem comprador";
        licitacaoAtual = 0;
        licitacoes = new TreeMap<>();
    }

    public void adicionaLicitacao(String nome, int licitacao){

        licitacoes.put(nome, licitacao);
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

    public Map<String, Integer> getLicitacoes() {
        return licitacoes;
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

    public void setLicitacoes(Map<String, Integer> licitacoes) {
        this.licitacoes = licitacoes;
    }
}
