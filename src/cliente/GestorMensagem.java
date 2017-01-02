package cliente;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Created by Ana Rita on 01/01/2017.
 */
public class GestorMensagem implements Runnable{
    private String nome;
    private Socket servidor;

    public GestorMensagem(String nome, Socket servidorM){

        this.nome=nome;
        servidor=servidorM;
    }

    public void run() {

        try {
            BufferedReader reader = new BufferedReader((new InputStreamReader(servidor.getInputStream())));
            String mensagem = reader.readLine();
            System.out.println(mensagem);
        } catch (IOException e) {
            System.out.println("Erro a receber mensagem");
        }
        return;
    }

}
