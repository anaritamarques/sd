package servidor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by Ana Rita on 26/12/2016.
 */
public class Servidor {
    private GestorUtilizadores utilizadores;
    public GestorLeiloes leiloes;
    private ServerSocket ss;

    private void aceitaClientes() throws IOException {
        while(true){
            Socket cliente = ss.accept();
            BufferedReader reader = new BufferedReader(new InputStreamReader(cliente.getInputStream()));
            PrintWriter writer = new PrintWriter(cliente.getOutputStream());
            String[] info = reader.readLine().split("//");

            if(!utilizadores.registaUtilizador(info[0], info[1]))
                writer.println("Erro");
            else {
                writer.println("Sucesso");

                new Thread(new ThreadCliente(reader, writer, leiloes, info[0])).start();
            }

        }
    }

    public Servidor(int porta){
        utilizadores=new GestorUtilizadores();
        leiloes=new GestorLeiloes();

        try {
            ss= new ServerSocket(porta);
        } catch (IOException e) {
            System.out.println("Erro na criação do socket");
        }
    }

    public static void main (String[] args) {
        Servidor servidor = new Servidor(555555);

        try {
            servidor.aceitaClientes();
        } catch (IOException e) {
            System.out.println("Erro a aceitar cliente");
        }
    }


}
