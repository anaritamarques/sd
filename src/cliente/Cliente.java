package cliente;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

/**
 * Created by Ana Rita on 26/12/2016...
 */
public class Cliente {
    private String nome;
    private BufferedReader reader;
    private PrintWriter writer;
    private int id;

    public Cliente(String nome, BufferedReader reader, PrintWriter writer, int id) {
        this.nome = nome;
        this.reader = reader;
        this.writer = writer;
        this.id=id;
    }

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
        String resposta="";
        switch(args[0]){
            case "iniciar":
                String[] desc = comando.split("\"");
                if(desc.length!=2)
                    System.out.println("Número de argumentos inválido");
                else
                    writer.println(comando);
                resposta = reader.readLine();
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
                else
                    writer.println(comando);
                resposta = reader.readLine();
                break;
            case "finalizar":
                if(args.length!=2)
                    System.out.println("Número de argumentos inválido");
                else
                    writer.println(comando);
                resposta = reader.readLine();
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
            int id=Integer.parseInt(resposta);
            servidorM = new Socket("localhost", 55556+id);
            (new Thread(new GestorMensagem(nome, servidorM))).start();
            (new Cliente(nome, reader, writer, id)).correr();
        } catch (IOException e) {
            System.out.println("Falha na ligação ao servidor");
        }
    }
}
