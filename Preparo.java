import java.rmi.RemoteException;
import java.io.File;
import java.io.FileNotFoundException;
import java.rmi.*;
import java.rmi.server.*;
import java.util.Scanner;
import java.util.Vector;

import javax.print.DocFlavor.STRING;

import java.rmi.registry.*;
import java.util.Vector;
import java.util.Collections;
import java.util.HashMap;
import java.util.Random;

public class Preparo implements Cozinha {
    public Preparo(){};
    HashMap<Integer,Vector<String>> preparoEpedidos = new HashMap<Integer,Vector<String>>();
    HashMap<Integer,Long> preparoEtimestamp = new HashMap<Integer,Long>();
    HashMap<Integer,Integer> preparoEcomanda = new HashMap<Integer,Integer>();
    int prepCounter = 0;

    /*Cadastra um novo preparo e retorna o código */
    public int novoPreparo(int comanda, String[] pedido) throws RemoteException{
        Vector<String> p = new Vector<String>();
        for(String s : pedido){
            p.add(s);
        }
        prepCounter++;

        preparoEpedidos.put(prepCounter, p);
        preparoEcomanda.put(prepCounter,comanda);
        return prepCounter;
    }

    /* Consulta tempo de espera de um preparo (em segundos)
     * Tempo: random, entre 1 e 10 segundos.
     */
    public int tempoPreparo(int preparo) throws RemoteException {
        int tempo = (int)(Math.random() * 10) + 1; // random seconds (1–10)
        long prontoEm = System.currentTimeMillis() + tempo * 1000; // ready time
        preparoEtimestamp.put(preparo, prontoEm); // store for this specific preparo
        return tempo;
    }
    /*Busca o pedido preparado para entrega.
     * Somente pode buscar apos tempo combinado
     * 
     * 
     */
    public String[] pegarPreparo(int preparo) throws RemoteException {
        
        if (!preparoEpedidos.containsKey(preparo) || !preparoEtimestamp.containsKey(preparo)) {
            return new String[0]; 
        }

        long prontoEm = preparoEtimestamp.get(preparo);
        long agora = System.currentTimeMillis();

        if (agora >= prontoEm) {
          
            Vector<String> pedidos = preparoEpedidos.get(preparo);
            String[] resultado = pedidos.toArray(new String[pedidos.size()]);

           
            preparoEpedidos.remove(preparo);
            preparoEtimestamp.remove(preparo);
            preparoEcomanda.remove(preparo);

            return resultado;
        } else {
            // Not ready yet
            return new String[0];
        }
    }

    static void main(String[] args){
        try{
        Preparo server = new Preparo();
        Cozinha stub = (Cozinha) UnicastRemoteObject.exportObject(server, 0);
        // Registra a stub no RMI Registry para que ela seja obtAida pelos clientes
        Registry registry = LocateRegistry.createRegistry(8888);
        //Registry registry = LocateRegistry.getRegistry(9999);
        registry.bind("cozinha", stub);
        System.out.println("Servidor pronto");
        }catch (Exception ex) {
            System.out.println("ERRO: Erro no servidor cozinha");
            ex.printStackTrace();
        }
    }

}
