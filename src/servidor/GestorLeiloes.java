package servidor;

import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by Ana Rita on 26/12/2016.
 */
public class GestorLeiloes {
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
            if(e.getValue().getNomeVendedor().equals(nome))
                sb.append("*");
            if(e.getValue().getNomeCompradorAtual().equals(nome))
                sb.append("+");
            sb.append("\n");
        }

        return sb.toString();
    }

    public String licitarLeilao(String nome, int id, int licitacao){
        Leilao l = leiloes.get(id);
        if(l != null){
            if(licitacao > l.getLicitacaoAtual()){
                l.setLicitacaoAtual(licitacao);
                l.setNomeCompradorAtual(nome);
                return "Sucesso";
            }
            return "Licitação menor que licitação atual";
        }
        else
            return "Leilão Inexistente";
    }
}
