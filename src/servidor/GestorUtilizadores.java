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
 * Created by Ana Rita on 26/12/2016.
 */
public class GestorUtilizadores {
    private Map<String, String> utilizadores;
    private Map<String, PrintWriter> writersClientes;
    private final Lock lockWritersClientes = new ReentrantLock();

    public GestorUtilizadores(){

        utilizadores = new TreeMap<>();
        writersClientes = new TreeMap<>();
    }

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
