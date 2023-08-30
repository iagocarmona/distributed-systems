/**
 * TCPClient: Cliente para conexao TCP
 * Descrição: Envia uma informacao ao servidor e finaliza a conexao
 */

import java.net.*;
import java.io.*;

public class client {
	public static void main (String args[]) {
            /* args[0]: mensagem  e args[1]: ip do servidor */
            
	    Socket s = null;
            try{
                int serverPort = 7896;   /* especifica a porta */
                s = new Socket(args[1], serverPort);  /* conecta com o servidor */  
                
                /* cria objetos de leitura e escrita */
                DataInputStream in = new DataInputStream( s.getInputStream());
                DataOutputStream out =new DataOutputStream( s.getOutputStream());
                
                out.writeUTF(args[0]);      	// envia uma string para o servidor
		
		System.out.println ("Informação enviada.");
		in.close(); out.close(); s.close();   //finaliza a conexao
            
	    } catch (UnknownHostException e){
		System.out.println("Socket:"+e.getMessage());
            } catch (EOFException e){
		System.out.println("EOF:"+e.getMessage());
            } catch (IOException e){
		System.out.println("leitura:"+e.getMessage());
            } //catch
     } //main
} //class