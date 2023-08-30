/**
 * TCPServer: Servidor para conexao TCP com Threads
 * Descricao: Recebe uma conexao, cria uma thread, recebe uma mensagem e finaliza a conexao
 */

import java.net.*;
import java.io.*;

public class server {
    public static void main (String args[]) {
        try{
            int serverPort = 7896; // porta do servidor
            /* cria um socket e mapeia a porta para aguardar conexão */
            ServerSocket listenSocket = new ServerSocket(serverPort);
            
            while(true) {
                System.out.println ("Servidor aguardando conexão ...");
		
		        /* aguarda conexões */
                Socket clientSocket = listenSocket.accept();
                
                System.out.println ("Cliente conectado ... Criando thread ...");
                /* cria um thread para atender a conexão */
                Connection c = new Connection(clientSocket);
            } //while
            
        } catch(IOException e) {
	    System.out.println("Listen socket:"+e.getMessage());
	} //catch
    } //main
} //class



/**
 * Classe Connection: Thread responsavel pela comunicacao
 * Descricao: Rebebe um socket, cria os objetos de leitura e escrita e aguarda msgs clientes 
 */
class Connection extends Thread {
    DataInputStream in;
    DataOutputStream out;
    Socket clientSocket;
    
    public Connection (Socket aClientSocket) {
        try {
            clientSocket = aClientSocket;
            in = new DataInputStream( clientSocket.getInputStream());
            out =new DataOutputStream( clientSocket.getOutputStream());
            this.start();  /* inicializa a thread */
        } catch(IOException e) {
	    System.out.println("Connection:"+e.getMessage());
	} //catch
    } //construtor
    
    /* metodo executado ao iniciar a thread - start() */
    public void run(){
        try {			                 
            String data = in.readUTF();   /* aguarda o envio de dados */	 
            System.out.println ("Cliente disse: " + data); 

	    in.close(); out.close(); clientSocket.close();  /* finaliza a conexao com o cliente */
	    
        } catch (EOFException e){
	    System.out.println("EOF: "+e.getMessage());
        } catch(IOException e) {
	    System.out.println("leitura: "+e.getMessage());
        } //catch
    } //run
} //class