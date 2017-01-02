package servidor;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * <h1>Gestão dos Utilizadores</h1>
 * Os utilizadores da plataforma são guardados numa estrutura
 * do tipo Map, bem como os writersClientes. Estes últimos
 * servem o propósito de manter a referência p/ sockets
 * criados em Cliente e servem apenas o propósito de
 * receber mensagens por parte do servidor/Gestor
 *
 * @author  Ana Rita, Hélder Sousa, Jorge Cardoso
 * @version 1.0
 * @since   2016
 */
public class GestorUtilizadores {
    private Map<String, String> utilizadores;
    private Map<String, PrintWriter> writersClientes;
    private final Lock lockWritersClientes = new ReentrantLock();

    public GestorUtilizadores(){

        utilizadores = new TreeMap<>();
        writersClientes = new TreeMap<>();
    }

    /**
     * Retorna um Bool
     * True no caso do nome (key) tiver um value igual à pass
     * introduzida pelo utilizador e no caso de o utilizador
     * ainda não existir na estrutura Map. Caso a pass não
     * seja igual (Ou seja o value) ao valor (key) nome
     * Retorna False. Por forma a não permitir multiplas
     * threads de sobreporem pedidos à estrutura o método
     * é considerado uma secção crítica de acesso controlado
     *
     * @param  nome      Nome / key do utilizador que chama método
     * @param  pass      palavra-passe / value do utilizador
     * @return           BOOLEAN
     */
    public synchronized Boolean registaUtilizador (String nome, String pass){
        if(utilizadores.containsKey(nome)) {
            if (utilizadores.get(nome).equals(pass))
                return true;
            else
                return false;
        }
        else
            utilizadores.put(nome, pass);

        return true;
    }

    /**
     * Adiciona writer a Map
     * Por cada cliente que seja registado, deve haver um
     * socket para comunicar com esse mesmo cliente
     *
     * @param  nome     Nome do cliente/utilizador
     * @param  socket   socket específico para comunicar com cliente
     */
    public void adicionarWriter(String nome, Socket socket){
        lockWritersClientes.lock();
        PrintWriter writer = null;
        try {
            writer = new PrintWriter(socket.getOutputStream(), true);
            writersClientes.put(nome, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
        lockWritersClientes.unlock();
    }

    /**
     * O criador de um leilão deve finalizar esse mesmo leilão
     * passando um set de strings que identifica os licitadores
     * em questão e a respectiva mensagem a ser entregue aos
     * licitadores.
     * São criadas threads usando o método Notificador para cada
     * socket (ou seja, uma porta) dos licitadores guardados
     * em writersClientes.
     * Após todas as respetivas threads terem sido criadas e
     * guardadas as suas referências num num arrayList, é
     * feito um ciclo para fazer join e esperar que todos os
     * clientes tenham sido notificados
     *
     * @param  notificacao   Mensagem entregue a todos os licitadores
     * @param  clientes      Licitadores específicos de um leilão
     */
    public void notificarClientes(String notificacao, Set<String> clientes){
        ArrayList<Thread> threads = new ArrayList<>();
        lockWritersClientes.lock();
        Thread t;
        for(String s: clientes){
            t = new Thread(new Notificador(writersClientes.get(s), notificacao));
            t.start();
            threads.add(t);
        }
        for(Thread th: threads){
            try {
                th.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        lockWritersClientes.unlock();
    }
}
