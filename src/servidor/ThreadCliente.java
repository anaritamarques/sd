package servidor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by Ana Rita on 26/12/2016.
 */
public class ThreadCliente implements Runnable {
    private BufferedReader reader;
    private PrintWriter writer;
    public GestorLeiloes leiloes;
    private String nome;

    public ThreadCliente(BufferedReader reader, PrintWriter writer, GestorLeiloes leiloes, String nome) {
        this.reader=reader;
        this.writer=writer;
        this.leiloes=leiloes;
        this.nome=nome;
    }

    public void interpretarPedido(String pedido){
        String[] args=pedido.split(" ");
        switch (args[0]){
            case "iniciar":
                int numero=leiloes.iniciarLeilao(args[1]);
                writer.println(numero);
                break;
            case "listar":
                String lista= leiloes.listarLeiloes(nome);
                writer.print(lista);
                break;
            case "licitar":
                String resultado=leiloes.licitarLeilao(args[1], args[2]);
                writer.println(resultado);
                break;
            case "finalizar":
                break;
            default:
                writer.println("Comando inexistente");
        }
    }

    @Override
    public void run() {
        while(true) {
            try {
                String pedido = reader.readLine();
                interpretarPedido(pedido);

            } catch (IOException e) {
                System.out.println("Falha na receção do pedido");
            }
        }
    }
}