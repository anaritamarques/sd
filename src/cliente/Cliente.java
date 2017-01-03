package cliente;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

/**
 * <h1>Cliente</h1>
 * O Cliente deve comunicar com o servidor
 * para tal utiliza-se um socket com a porta 55555
 * sendo que o utilizador deve introduzir uma palavra-passe
 * e um nome de utilizador enviando a informação para o servidor
 * este por sua vez deve verificar a existência ou não de conta e de
 * seguida retornando um boolean terminando o programa ou criando um socket
 * para o servidor de mensagens.
 *
 * @author  Ana Rita, Hélder Sousa, Jorge Cardoso
 * @version 1.0
 * @since   2016
 */

public class Cliente {
    private String nome;
    private BufferedReader reader;
    private PrintWriter writer;



    /**
     * Construtor responsável pela instanciação
     * de objectos do tipo cliente. Recebe parâmetros
     * para comunicar com o servidor.
     *
     * @param  nome      Nome do cliente/ utilizador;
     * @param  reader    apontador p/ socket servidor - cliente;
     * @param  writer    apontador p/ socket cliente - Servidor;
     */
    public Cliente(String nome, BufferedReader reader, PrintWriter writer) {

        this.nome = nome;
        this.reader = reader;
        this.writer = writer;
    }

    /**
     *
     * A função listarLeiloes() tem como objetivo listar todos os leiloes
     * existentes.
     *
     */
    public String listarLeiloes() throws IOException {
        StringBuilder sb = new StringBuilder();
        String line;
        writer.println("listar");

        while(!(line = reader.readLine()).equals("###"))
            sb.append(line).append("\n");

        return sb.toString();
    }

    public void interpretar(String comando) throws IOException {
        String[] args = comando.split(" ");
        int ret = 0;
        String resposta="";
        switch(args[0]){
            case "iniciar":
                String[] desc = comando.split("\"");
                if(desc.length!=2)
                    System.out.println("Número de argumentos inválido");
                else{
                    writer.println(comando);
                    resposta = reader.readLine();
                }
                break;
            case "listar":
                StringBuilder sb = new StringBuilder();
                if(args.length!=1)
                    System.out.println("Número de argumentos inválido");

                else {
                    writer.println(comando);
                    String linha="";
                    while (!(linha = reader.readLine()).equals("###"))
                        sb.append(linha).append("\n");
                    resposta = sb.toString();
                    }
                break;
            case "licitar":
                if(args.length!=3)
                    System.out.println("Número de argumentos inválido");
                else{
                    writer.println(comando);
                    resposta = reader.readLine();
                    }
                break;
            case "finalizar":
                if(args.length!=2)
                    System.out.println("Número de argumentos inválido");
                else{
                    writer.println(comando);
                    resposta = reader.readLine();
                    break;
                    }
                break;
            default:
                resposta="Comando inválido";
            break;
        }

        System.out.print(resposta+"\n");
    }

    public void correr() throws IOException {

        try {
            listarLeiloes(); //aqui a ideia era mostrar os leiloes para o utilizador ver o que pode fazer
        } catch (IOException e) {
            System.out.println("Erro a comunicar com o servidor");
        }
        Scanner s = new Scanner(System.in);
        while(true) {
            System.out.println("Escreva o seu comando");
            String comando = s.nextLine();
            interpretar(comando);
        }
    }


    public static void main(String[] args) {
        Scanner s = new Scanner(System.in);
        String pass;
        System.out.println("Nome:");
        String nome=s.nextLine();
        System.out.println("Password:");
        pass=s.nextLine();
        Socket servidor = null;
        Socket servidorM = null;
        try {
            servidor = new Socket("localhost", 55555);
            BufferedReader reader = new BufferedReader(new InputStreamReader(servidor.getInputStream()));
            PrintWriter writer = new PrintWriter(servidor.getOutputStream(), true);
            writer.println(nome+"//"+pass);
            String resposta = reader.readLine();
            if(resposta.equals("Erro"))
                System.exit(0);
            int id = Integer.parseInt(resposta);
            servidorM = new Socket("localhost", 55556+id);
            (new Thread(new GestorMensagem(nome, servidorM))).start();
            (new Cliente(nome, reader, writer)).correr();
        } catch (IOException e) {
            System.out.println("Falha na ligação ao servidor");
        }
    }
}
