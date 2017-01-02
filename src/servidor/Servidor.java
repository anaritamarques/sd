package servidor;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * <h1>Servidor</h1>
 * A partir do momento que o Servidor corre
 * é instanciado um objeto do tipo Servidor
 * sendo criadas duas estruturas p/ guardar
 * os utilizadores e os leilões.O método aceitaClientes
 * corre esperando por ligações à porta 55555 do socket
 * servidor, que quando recebe um pedido de um cliente
 * cria uma nova thread instanciando um objeto do tipo
 * gestorCliente. Mais detalhes são fornecidos abaixo
 *
 * @author  Zara Ali
 * @version 1.0
 * @since   2016
 */
public class Servidor {
    public GestorUtilizadores utilizadores;
    public GestorLeiloes leiloes;
    private ServerSocket ss;
    private int id;

    /**
     * Retorna o ID do leilão.
     * O ID que a classe retorna é utilizado como chave para
     * encontrar no TreeMap a referência ao leilão criado
     *
     * @param  descricao Descrição do objeto a ser leiloado;
     * @param  nome      Nome do vendedor a criar leilão;
     * @return           ID do leilão
     */
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
