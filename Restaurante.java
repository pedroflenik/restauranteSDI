import java.rmi.*;

/*
 * CSV (int,string,float)
 * codigo,produto,valor 
*/

public interface Restaurante extends Remote{

    /* Retorna o número da nova comanda pra um novo cliente (String nome)*/
    public int novaComanda(String nome, int mesa) throws RemoteException;

    /*Consulta as opções  disponíveis  no cardápio
     * formato de retorno: CSV*/

    public String[] consultarCardapio() throws RemoteException;

    public String fazerPedido(int comanda,String[] pedido) throws RemoteException;

    /*Solicita o valor total para o pagamento */
    public float valorComanda(int comanda) throws RemoteException;

    /*Realiza o pagamento e libera*/
    public boolean fecharComanda(int comanda) throws RemoteException;


}