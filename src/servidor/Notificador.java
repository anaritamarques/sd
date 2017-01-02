package servidor;

import java.io.PrintWriter;

/**
 * <h1>Notificador</h1>
 *
 * A classe Notificador foi criada com o objetivo de comunicar com todos os
 * clientes do servidor.
 *
 * @author  Ana Rita,Helder Sousa, Jorge Cardoso
 * @version 1.0
 * @since   2016
 */
public class Notificador implements Runnable{
    private PrintWriter cliente;
    private String notificacao;

    public Notificador(PrintWriter cliente, String notificacao) {
        this.cliente = cliente;
        this.notificacao = notificacao;
    }

    @Override
    public void run() {
        cliente.println(notificacao);
    }
}
