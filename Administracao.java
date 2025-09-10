import java.io.File;
import java.io.FileNotFoundException;
import java.rmi.*;
import java.rmi.server.*;
import java.util.Scanner;
import java.util.Vector;

import javax.print.DocFlavor.STRING;

import wsMercado.wsClientMercado;

import java.rmi.registry.*;
import java.util.Vector;
import java.util.Collections;
import java.util.HashMap;

public class Administracao implements Restaurante{

    static Vector<String> cardapio = new Vector<String>(); 
    static HashMap<Integer,Vector<String>> comandasEpedidos = new HashMap<Integer,Vector<String>>();
    static HashMap<Integer,Integer> comandasEmesas = new HashMap<Integer,Integer>();
    static Vector<Integer> preparos = new Vector<Integer>();

    public Administracao(){};

    /* Retorna o número da nova comanda pra um novo cliente (String nome)*/
    public int novaComanda(String nome, int mesa) throws RemoteException{
          int nNovaComanda;

        if (comandasEpedidos.isEmpty()) {
            nNovaComanda = 1;
        } else {
            nNovaComanda = Collections.max(comandasEpedidos.keySet()) + 1;
        }
        comandasEpedidos.put(nNovaComanda,new Vector<String>());
        comandasEmesas.put(nNovaComanda,mesa);
        return nNovaComanda;
    }

    /*Consulta as opções  disponíveis  no cardápio
     * formato de retorno: CSV*/

    public String[] consultarCardapio() throws RemoteException{
        return cardapio.toArray(new String[cardapio.size()]);
    }

    public String fazerPedido(int comanda,String[] pedido) throws RemoteException{
        Vector<String> novoPedido = new Vector<String>();
        for(String a : pedido){
            novoPedido.add(a);
        }
        comandasEpedidos.get(comanda).addAll(novoPedido);
        return "Pedido feito com sucesso";
    }

    /*Solicita o valor total para o pagamento */

    public float valorComanda(int comanda) throws RemoteException {
        Vector<String> pedidos = comandasEpedidos.get(comanda);
        float valorTotal = 0.0f;

        if (pedidos != null) {
            for (String pedido : pedidos) {
                String[] parts = pedido.split(",");
                if (parts.length >= 3) {
                    float preco = Float.parseFloat(parts[2]);
                    valorTotal += preco;
                }
            }
        }

        return valorTotal;
    }

    /*Realiza o pagamento e libera*/
    public boolean fecharComanda(int comanda) throws RemoteException{
        comandasEpedidos.remove(comanda);
        comandasEmesas.remove(comanda);
        return true;
    }


    public static void main(String[] args){
        System.out.println("Lendo arquivo...");
        try {
            File arquivo = new File("menu_restaurante.csv");
            Scanner scanner = new Scanner(arquivo);
            while (scanner.hasNextLine()) {
                String linha = scanner.nextLine();
                cardapio.add(linha);
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("ERRO: Erro na leitura do arquivo csv");
            e.printStackTrace();
        }
        Scanner sc = new Scanner(System.in);
        // for(String a : cardapio){
        //     System.out.println(a);
        // }
        try {
        // Instancia o objeto servidor e a sua stub
        Administracao server = new Administracao();
        Restaurante stub = (Restaurante) UnicastRemoteObject.exportObject(server, 0);
        // Registra a stub no RMI Registry para que ela seja obtAida pelos clientes
        Registry registry = LocateRegistry.createRegistry(8888);
        //Registry registry = LocateRegistry.getRegistry(9999);
        registry.bind("restaurante", stub);
        System.out.println("Servidor pronto");
        }catch (Exception ex) {
            System.out.println("ERRO: Erro no servidor");
            ex.printStackTrace();
        }
        try{
            Registry registry = LocateRegistry.getRegistry("localhost",8889);

        
            Cozinha stub= (Cozinha) registry.lookup("cozinha");
            int opcao = -1;
            while(opcao != 5){
                System.out.println("Escolha uma opção:");
                System.out.println("1 - Novo Preparo");
                System.out.println("2 - Tempo preparo");
                System.out.println("3 - Pegar preparo");
                System.out.println("4 - Compras Mercado");
                System.out.println("5 - Sair");
            
                opcao = sc.nextInt();
                sc.nextLine();
            switch (opcao) {
                case 1:
                    try {
                         System.out.println("Escolha uma dessas comandas para pagar:");
                        System.out.println("[");
                        for(Integer i : comandasEpedidos.keySet()){
                            System.out.print(i + " ");
                        }
                        System.out.println("\n]");
                        int nComanda = sc.nextInt();
                        sc.nextLine();
                        int t = stub.novoPreparo(nComanda, comandasEpedidos.get(nComanda).toArray(new String[comandasEpedidos.size()]));
                        preparos.add(t);
                    } catch (Exception ex) {
                        System.out.println("ERRO: Falha na criação de novo preparo");
                        ex.printStackTrace();
                    }
                break;
                case 2:
                    try{
                        System.out.println("Escolha um dos preparos:\n[");
                        for(int p : preparos){
                            System.out.print(p);
                        }
                        int nPreparo = sc.nextInt();
                        sc.nextLine();
                        System.out.println("\n]");
                        int tempo = stub.tempoPreparo(nPreparo);
                        System.out.println("Tempo: " + tempo);
                    }catch(Exception ex){
                        System.out.println("ERRO: Falha no tempo preparo");
                    }
                break;
                case 3:
                    try {
                        System.out.println("Escolha um dos preparos:\n[");
                        for (int p : preparos) {
                            System.out.print(p + " ");
                        }
                        System.out.println("\n]");
                        int nPreparo = sc.nextInt();
                        sc.nextLine();

                        String[] pedido = stub.pegarPreparo(nPreparo);

                        if (pedido.length == 0) {
                            System.out.println("O preparo ainda não está pronto.");
                        } else {
                            System.out.println("Preparo pronto! Pedido:");
                            for (String item : pedido) {
                                System.out.println("- " + item);
                            }
                            preparos.remove(Integer.valueOf(nPreparo));
                        }
                    } catch (Exception ex) {
                        System.out.println("ERRO: Falha ao pegar preparo");
                        ex.printStackTrace();
                    }
                    break;
                case 4:
                    wsClientMercado.menu();
                break;
                case 5:
                System.out.println("Saindo....");
                break;
                default:
                    System.out.println("Opção inválida");
                break;
            }
            }

        }catch(Exception ex){
            System.out.println("ERRO: Erro cliente administracao");
            ex.printStackTrace();
        }
        sc.close();
    }

}
