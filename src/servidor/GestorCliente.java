package servidor;

import com.sun.corba.se.spi.activation.Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 * Created by Ana Rita on 26/12/2016. test
 */
public class GestorCliente implements Runnable {
    private String nome;
    public GestorUtilizadores utilizadores;
    public GestorLeiloes leiloes;
    public BufferedReader reader;
    public PrintWriter writer;
    public int id;

    public GestorCliente(Socket cliente, GestorLeiloes leiloes, GestorUtilizadores utilizadores, int id) throws IOException {
        nome="";
        reader = new BufferedReader((new InputStreamReader(cliente.getInputStream())));
        writer = new PrintWriter(cliente.getOutputStream(), true);
        this.leiloes = leiloes;
        this.utilizadores = utilizadores;
        this.id=id;
    }

    public void interpretarPedido(String pedido) {
        String[] args = pedido.split(" ");
        switch (args[0]) {
            case "iniciar":
                String[] desc = pedido.split("\"");
                int numero = leiloes.iniciarLeilao(desc[1], nome);
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
                Leilao l = leiloes.finalizarLeilao(nome, Integer.parseInt(args[1]));
                String mensagem = "O leilão".concat(args[1]).concat("já terminou. O vencedor é ").concat(l.getNomeCompradorAtual()).concat("!");
                HashSet<String> clientes= new HashSet<String>(l.getLicitacoes().keySet());
                clientes.add(l.getNomeVendedor());
                utilizadores.notificarClientes(mensagem, clientes);
                if(l==null)
                    writer.println("Leilão não encontrado");
                else
                    writer.println("Leilão terminado");
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
                ServerSocket sm = new ServerSocket(55556+id);
                writer.println(id);
                Socket clienteM= sm.accept();
                utilizadores.adicionarWriter(nome, clienteM);
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
