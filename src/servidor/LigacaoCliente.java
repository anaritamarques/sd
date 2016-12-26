package servidor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Created by Ana Rita on 26/12/2016.
 */
public class LigacaoCliente implements Runnable {
    public GestorUtilizadores utilizadores;
    public GestorLeiloes leiloes;
    public Socket cliente;

    public LigacaoCliente(Socket cliente, GestorLeiloes leiloes, GestorUtilizadores utilizadores) {
        this.cliente = cliente;
        this.leiloes = leiloes;
        this.utilizadores = utilizadores;
    }

    @Override
    public void run() {

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(cliente.getInputStream()));
            PrintWriter writer = new PrintWriter(cliente.getOutputStream(), true);
            String[] info = reader.readLine().split("//");
            if(!utilizadores.registaUtilizador(info[0], info[1]))
                writer.println("Erro");
            else {
                writer.println("Sucesso");

                new Thread(new ThreadCliente(reader, writer, leiloes, info[0])).start();
            }
        } catch (IOException e) {
            System.out.println("Erro a aceitar cliente");
        }

        return;

    }
}
