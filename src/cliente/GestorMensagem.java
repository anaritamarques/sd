package cliente;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * <h1>Gestor de Mensagens</h1>
 *
 * Na classe GestorMensagem tem como objetivo guardar a informação
 * referente ao cliente para poder enviar as mensagens para os mesmos.
 *
 * @author  Ana Rita, Hélder Sousa, Jorge Cardoso
 * @version 1.0
 * @since   2016
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
            while(true) {
                BufferedReader reader = new BufferedReader((new InputStreamReader(servidor.getInputStream())));
                String mensagem = reader.readLine();
                System.out.println(mensagem);
            }
        } catch (IOException e) {
            System.out.println("Erro a receber mensagem");
        }
        return;
    }

}
