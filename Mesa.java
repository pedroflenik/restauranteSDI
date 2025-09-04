import java.rmi.*;
import java.rmi.server.*;
import java.rmi.registry.*;
import java.util.Vector;
import java.util.Map;
import java.util.Scanner;
import java.util.HashMap;
import java.util.Collections;
import java.util.Arrays;
import java.util.Set;

public class Mesa {
    public static void main(String[] args) {

   
    HashMap<Integer,Vector<String>> comandasEpedidos = new HashMap<Integer,Vector<String>>();
    Scanner sc = new Scanner(System.in);
    try {
        // Obtém uma referência para o registro do RMI
        Registry registry = LocateRegistry.getRegistry("localhost",8888);

        // Obtém a stub do servidor
        Restaurante stub= (Restaurante) registry.lookup("restaurante");

        // Chama o método do servidor e imprime a mensagem
        int opcao = -1;
        while(opcao != 6){
            System.out.println("Escolha uma opção:");
            System.out.println("1 - Nova comanda");
            System.out.println("2 - Consultar cardápio");
            System.out.println("3 - Novo pedido");
            System.out.println("4 - Valor comanda");
            System.out.println("5 - Fechar comanda");
            System.out.println("6 - Sair");

            opcao = sc.nextInt();
            sc.nextLine();

            switch (opcao) {
                case 1:
                try{
                    System.out.println("Insira o nome:");
                    String nome = sc.nextLine();
                    System.out.println("Insira o número da mesa:");
                    int nMesa = sc.nextInt();
                    int nNovaComanda = stub.novaComanda(nome, nMesa);
                    comandasEpedidos.put(nNovaComanda, new Vector<String>());
                }catch(Exception ex){
                    System.out.println("ERRO: Falha na criacao de nova comanda");
                    ex.printStackTrace();
                }
                break;
                case 2:
                   
                    try {
                        String[] cardapio = stub.consultarCardapio();
                        System.out.println("Cardapio:");
                        for(String a : cardapio){
                            System.out.println(a);
                        }
                    } catch (Exception ex) {
                        System.out.println("ERRO: Falha na consulta do cardapio");
                        ex.printStackTrace();
                    }
                  
                break;
                case 3:
                try{
                    String[] cardapio = stub.consultarCardapio();
                    Vector<String> pedido = new Vector<>();
                    Vector<Integer> codigos = new Vector<>();
                    int codigo = -1;
                    System.out.println("Monte seu pedido inserindo o código dos pedidos (0 para finalizar pedido)");
                    do{
                        System.out.println("Insira um novo codigo:");
                        codigo = sc.nextInt();
                        sc.nextLine();
                        if(codigo != 0){
                            codigos.add(codigo);
                            pedido.add(cardapio[codigo]);
                        }
                    }while(codigo != 0);
                    System.out.println("Escolha uma dessas comandas para adicionar o pedido:");
                    System.out.println("[");
                  
                    for(Integer i : comandasEpedidos.keySet()){
                        System.out.print(i + " ");
                    }
                    System.out.println("]");
                    int nComanda = sc.nextInt();
                    sc.nextLine();
                    Vector<String> vec = comandasEpedidos.getOrDefault(nComanda, new Vector<>());
                    vec.addAll(pedido);
                    comandasEpedidos.put(nComanda, vec);
                    stub.fazerPedido(nComanda, pedido.toArray(new String[pedido.size()]));
                    } catch (Exception ex) {
                        System.out.println("ERRO: Falha ao fazer novo pedido");
                        ex.printStackTrace();
                    }
                break;
                case 4:
                 try {
                    System.out.println("Escolha uma dessas comandas para vizualizar total pedido");
                    System.out.println("[");
                    for(Integer i : comandasEpedidos.keySet()){
                        System.out.print(i + " ");
                    }
                    System.out.println("]");
                    int nComanda = sc.nextInt();
                    sc.nextLine();
                    float valor = stub.valorComanda(nComanda);
                    System.out.println("Valor total comanda == " + valor);
                } catch (Exception ex) {
                        System.out.println("ERRO: Pegar total pedido");
                        ex.printStackTrace();
                }
                break;
                case 5:
                    System.out.println("Escolha uma dessas comandas para pagar:");
                    System.out.println("[");
                    for(Integer i : comandasEpedidos.keySet()){
                        System.out.print(i + " ");
                    }

                    System.out.println("]");
                    int nComanda = sc.nextInt();
                    comandasEpedidos.remove(nComanda);
                    stub.fecharComanda(nComanda);
                    
                break;
                case 6:
                    System.out.println("Saindo.....");
                    sc.close();
                break;
                default:
                    System.out.println("Opção inválida");
                    break;
            }
        }
    } catch (Exception ex) {
        System.out.println("ERRO: Inicializalcao cliente mesa");
        ex.printStackTrace();
    }
    sc.close();
   }
}
