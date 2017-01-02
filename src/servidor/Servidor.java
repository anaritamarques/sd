package servidor;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;


public class Servidor {
    public GestorUtilizadores utilizadores;
    public GestorLeiloes leiloes;
    private ServerSocket ss;
    private int id;

    private void aceitaClientes() throws IOException {
        while(true){
            id++;
            Socket cliente = ss.accept();
            (new Thread(new GestorCliente(cliente, leiloes, utilizadores, id))).start();
        }
    }

    public Servidor(int porta){
        utilizadores=new GestorUtilizadores();
        leiloes=new GestorLeiloes();
        id =0;
        try {
            ss= new ServerSocket(porta);
        } catch (IOException e) {
            System.out.println("Erro na criação do socket");
        }
    }

    public static void main (String[] args) {

        try {
            Servidor servidor = new Servidor(55555);
            servidor.aceitaClientes();
        } catch (IOException e) {
            System.out.println("Erro a aceitar cliente");
        }
    }


}
