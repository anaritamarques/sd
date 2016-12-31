package servidor;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by Ana Rita on 26/12/2016.
 */
public class  GestorLeiloes {
    private Map<Integer, Leilao> leiloes;
    private final Lock lockLeiloes = new ReentrantLock();
    private int idLeilao;

    public GestorLeiloes(){
        leiloes = new TreeMap<>();
        idLeilao=0;
    }

    public synchronized int calcularIdLeilao(){
        idLeilao++;
        return idLeilao;
    }

    public int iniciarLeilao(String descricao, String nome) {
        int id = calcularIdLeilao();
        Leilao novo = new Leilao(descricao, nome);
        lockLeiloes.lock();
        leiloes.put(id, novo);
        lockLeiloes.unlock();
        return id;
    }

    public String listarLeiloes(String nome) { //será necessário lock?
        StringBuilder sb = new StringBuilder();
        for(Map.Entry<Integer, Leilao> e: leiloes.entrySet()){
            sb.append(e.getKey()).append(" | ");
            sb.append(e.getValue().getDescricao()).append(" | ");
            sb.append(e.getValue().getLicitacaoAtual()).append(" | ");
            if(e.getValue().getNomeVendedor().equals(nome))
                sb.append("*");
            if(e.getValue().getNomeCompradorAtual().equals(nome))
                sb.append("+");
            sb.append("\n");
        }
        sb.append("###\n");
        return sb.toString();
    }

    public String licitarLeilao(String nome, int id, int licitacao){
        lockLeiloes.lock();
        Leilao l = leiloes.get(id);
        String mensagem;
        if(l != null){
            if(!l.getNomeVendedor().equals(nome)) {
                if(licitacao > l.getLicitacaoAtual()){
                    l.setLicitacaoAtual(licitacao);
                    l.setNomeCompradorAtual(nome);
                    l.adicionaLicitacao(nome, licitacao);
                    leiloes.put(id, l);

                    mensagem = "Sucesso";
                }
                else
                    mensagem = "Licitação menor que licitação atual";
            }
            else
                mensagem = "É o vendedor deste leilão";
        }
        else
            mensagem = "Leilão Inexistente";
        lockLeiloes.unlock();
        return mensagem;
    }

    public Leilao finalizarLeilao(String nome, int id) {
        lockLeiloes.lock();
        Leilao l = leiloes.get(id);
        String leilao = Integer.toString(id);
        String mensagem ="";
        if(l != null && l.getNomeVendedor().equals(nome)) {
            leiloes.remove(id);
        }
        lockLeiloes.unlock();
        return l;
    }

    public Leilao getLeilao(int id){
        Leilao l = leiloes.get(id);
        return l;
    }
}
