package servidor;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.Condition;


/**
 * <h1>Gestão dos Leilões</h1>
 * Cada leilão criado pelos clientes, é introduzido
 * num treeMap. É a partir da classe GestorLeiloes
 * que se providencia ferramentas capazes de iniciar,
 * licitar e terminar leilões.
 *
 * @author  Ana Rita, Hélder Sousa, Jorge Cardoso
 * @version 1.0
 * @since   2016
 */
public class  GestorLeiloes {
    private Map<Integer, Leilao> leiloes;
    private  Lock lockLeiloes = new ReentrantLock();
    private Condition OKbid = lockLeiloes.newCondition();
    private Condition OKread = lockLeiloes.newCondition();
    private int idLeilao;

    public GestorLeiloes(){
        leiloes = new TreeMap<>();
        idLeilao=0;
    }

    public synchronized int calcularIdLeilao(){
        idLeilao++;
        return idLeilao;
    }


    /**
     * Retorna o ID do leilão.
     * O ID que a classe retorna é utilizado como chave para
     * encontrar no TreeMap a referência ao leilão criado
     *
     * @param  descricao Descrição do objeto a ser leiloado;
     * @param  nome      Nome do vendedor a criar leilão;
     * @return           ID do leilão
     */
    public int iniciarLeilao(String descricao, String nome) {
        int id = calcularIdLeilao();
        Leilao novo = new Leilao(descricao, nome);
        lockLeiloes.lock();
        leiloes.put(id, novo);
        lockLeiloes.unlock();
        return id;
    }


    /**
     * Retorna uma lista de todos os leilões;
     * No caso do leilão ter sido criado pelo utilizador que
     * está a realizar a chamada ao método, deve aparecer um
     * "*" no final da linha, no caso de ser o licitador
     * com vantagem deve aparecer um "+"
     *
     * @param  nome      Noome do utilizador que chama método
     * @return           STRING com todos os leilões
     */
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




    /**
     * Retorna String com mensagem para licitador
     * No caso do leilão ter sido criado pelo utilizador que
     * está a realizar a chamada ao método, deve aparecer um
     * "*" no final da linha, no caso de ser o licitador
     * com vantagem deve aparecer um "+"
     *
     * @param  nome      Nome/String do utilizador que chama método
     * @param  id        id/int do leilão que o utilizador licita
     * @param  licitacao licitação/int do leilão que o user licita
     * @return           STRING mensagens específicas para o user
     */
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

    /**
     * Retorna referência para objeto leilao;
     * Retorna null no caso do leilão não existir no Map ou já
     * ter sido removido e retorna null se o utilizador não for
     * o autor do leilão referenciado pelo ID
     *
     * @param  nome      Nome/String do utilizador que chama método
     * @param  id        id/int do leilão que utilizador termina
     * @return           Leilao ou null
     */
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


    /**
     * Retorna objecto leilao;
     * No caso de não existir o leilão com o ID referido
     * então retorna null
     *
     * @param  id        id/int do leilão que deve ser retornado
     * @return           Leilao ou null
     */
    public Leilao getLeilao(int id){
        Leilao l = leiloes.get(id);
        return l;
    }
}
