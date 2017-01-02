package servidor;

import java.io.PrintWriter;


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
