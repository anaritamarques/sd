package cliente;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

/**
 * Created by Ana Rita on 26/12/2016.
 */
public class Cliente {
    private String nome;
    private BufferedReader reader;
    private PrintWriter writer;

    public Cliente(String nome, BufferedReader reader, PrintWriter writer) {
        this.nome = nome;
        this.reader = reader;
        this.writer = writer;
    }

    public String listarLeiloes() throws IOException {
        StringBuilder sb = new StringBuilder();
        String line;
        writer.println("listar");

        while((line = reader.readLine()) != null)
            sb.append(line).append("\n");

        return sb.toString();
    }

    public void interpretar(String comando) throws IOException {
        String[] args = comando.split(" ");
        StringBuilder sb = new StringBuilder();
        String line;
        switch(args[0]){
            case "iniciar":
                if(args.length!=2)
                    System.out.println("Número de argumentos inválido");
                else
                    writer.println(comando);
                break;
            case "listar":
                if(args.length!=1)
                    System.out.println("Número de argumentos inválido");
                else
                    writer.println(comando);
                break;
            case "licitar":
                if(args.length!=3)
                    System.out.println("Número de argumentos inválido");
                else
                    writer.println(comando);
                break;
            case "finalizar":
                if(args.length!=2)
                    System.out.println("Número de argumentos inválido");
                else
                    writer.println(comando);
                break;
            default:
                System.out.println("Comando inválido");
                break;
        }

        while((line = reader.readLine()) != null)
            sb.append(line).append("\n");

        System.out.print(sb.toString());
    }

    public void correr() throws IOException {
        /*try {
            listarLeiloes();
        } catch (IOException e) {
            System.out.println("Erro a comunicar com o servidor");
        }*/

        System.out.println("Escreva o seu comando");

        Scanner s = new Scanner(System.in);
        while(true) {
            interpretar(s.nextLine());
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
        try {
            servidor = new Socket("localhost", 55555);
            BufferedReader reader = new BufferedReader(new InputStreamReader(servidor.getInputStream()));
            PrintWriter writer = new PrintWriter(servidor.getOutputStream(), true);
            System.out.println("ANTES");
            writer.println(nome+"//"+pass);
            System.out.println("DEPOIS");
            String resposta = reader.readLine();
            System.out.println("DEPOIS2");
            System.out.println(resposta);
            if(resposta.equals("Erro"))
                System.exit(0);

            (new Cliente(nome, reader, writer)).correr();
        } catch (IOException e) {
            System.out.println("Falha na ligação ao servidor");
        }


    }
}
