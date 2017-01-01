package servidor;

import java.util.Map;
import java.util.TreeMap;

/**
 * Created by Ana Rita on 26/12/2016.
 */
public class GestorUtilizadores {
    private Map<String, String> utilizadores;

    public GestorUtilizadores(){

        utilizadores = new TreeMap<>();
    }

    public synchronized Boolean registaUtilizador (String nome, String pass){
        if(utilizadores.containsKey(nome)) {
            if (utilizadores.get(nome).equals(pass))
                return true;
            else
                return false;
        }
        else
            utilizadores.put(nome, pass);

        return true;
    }
}
