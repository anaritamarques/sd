package servidor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Created by Ana Rita on 26/12/2016.
 */
public class GestorCliente implements Runnable {
    private String nome;
    public GestorUtilizadores utilizadores;
    public GestorLeiloes leiloes;
    public BufferedReader reader;
    public PrintWriter writer;

    public GestorCliente(Socket cliente, GestorLeiloes leiloes, GestorUtilizadores utilizadores) throws IOException {
        nome="";
        reader = new BufferedReader((new InputStreamReader(cliente.getInputStream())));
        writer = new PrintWriter(cliente.getOutputStream(), true);
        this.leiloes = leiloes;
        this.utilizadores = utilizadores;
    }

    public void interpretarPedido(String pedido) {
        String[] args = pedido.split(" ");
        switch (args[0]) {
            case "iniciar":
                int numero = leiloes.iniciarLeilao(args[1], nome);
                writer.println(numero);
                break;
            case "listar":
                String lista = leiloes.listarLeiloes(nome);
                String[] linhas = lista.split("\n");
                for (String l : linhas) {
                    writer.println(l);
                }
                break;
            case "licitar":
                String resultado = leiloes.licitarLeilao(nome, Integer.parseInt(args[1]), Integer.parseInt(args[2]));
                writer.println(resultado);
                break;
            case "finalizar":
                break;
            default:
                writer.println("Comando inexistente");
                break;
        }
    }

    @Override
    public void run() {

        try {
            String[] info = reader.readLine().split("//");
            if (!utilizadores.registaUtilizador(info[0], info[1]))
                writer.println("Erro");
            else {
                nome=info[0];
                writer.println("Sucesso");

                while (true) {
                    try {
                        String pedido = reader.readLine();
                        interpretarPedido(pedido);
                    } catch (IOException e) {
                        System.out.println("Falha na receção do pedido");
                    }
                }
            }
        } catch(IOException e){
            System.out.println("Erro a aceitar cliente");
        }
        return;
    }
}
