package AT01SocketsTCP.Q02.server;

/**
 * Descrição: Servidor para conexao TCP com Threads Descricao: Recebe uma
 * conexao, cria uma thread, recebe uma mensagem e finaliza a conexao.
 * 
 * Autor: Iago Ortega Carmona
 * 
 * Data de criação: 05/09/2023
 * Data última atualização: 15/09/2023
 */

import java.net.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.*;

public class server {
    public static void main (String args[]) {
        try {
            int serverPort = 7896; // porta do servidor
            try (ServerSocket listenSocket = new ServerSocket(serverPort)) {
                while (true) {
                    System.out.println("Servidor aguardando conexão ...");
                    Socket clientSocket = listenSocket.accept();
                    
                    System.out.println("Cliente conectado ... Criando thread ...");
                    new Connection(clientSocket);
                }
            }

        } catch (IOException e) {
            System.out.println("Listen socket: " + e.getMessage());
        }
    }


    /**
     * Descrição: Thread responsavel pela comunicacao
     * Descricao: Recebe um socket, cria os objetos de leitura e escrita,
     * aguarda msgs clientes e responde com a msg + :OK
     * 
     * Autor: Iago Ortega Carmona
     * 
     * Data de criação: 05/09/2023
     * Data última atualização: 15/09/2023
     */
    static class Connection extends Thread {
        DataInputStream in;
        DataOutputStream out;
        Socket clientSocket;

        String currentPath;

        private static final Logger logger = Logger.getLogger("tcp");

        public Connection(Socket aClientSocket) {
            try {
                FileHandler fileHandler = new FileHandler("./AT01SocketsTCP/Q02/tcp.log");
                fileHandler.setFormatter(new SimpleFormatter());
                logger.addHandler(fileHandler);

                clientSocket = aClientSocket;
                in = new DataInputStream(clientSocket.getInputStream());
                out = new DataOutputStream(clientSocket.getOutputStream());
                this.start();  /* inicializa a thread */

                logger.info("Cliente se conectou");

			    this.currentPath = System.getProperty("user.dir") + "/AT01SocketsTCP/Q02/files";
            } catch (IOException e) {
                System.out.println("Connection: " + e.getMessage());
            }
        }

        public static String getCommandByByte(byte commandId) {
            switch (commandId) {
                case 1:
                    return "ADDFILE";
                case 2:
                    return "DELETE";
                case 3:
                    return "GETFILELIST";
                case 4:
                    return "GETFILE";
                default:
                    return "UNKNOWN";
            }
        }

        public ByteBuffer createResponseHeader(byte messageType, byte commandId, byte statusCode) {
            ByteBuffer header = ByteBuffer.allocate(3);
            header.order(ByteOrder.BIG_ENDIAN);
    
            header.put(0, messageType);
            header.put(1, commandId);
            header.put(2, statusCode);
            logger.info("ResponseHeader criado");
    
            return header;
        }

        public void run() {
            try {
                int headerSize = 259;

                while (true) {
                    logger.info("Criando e configurando header");
                    byte[] bytes = new byte[headerSize];
                    this.in.read(bytes);
                    ByteBuffer header = ByteBuffer.wrap(bytes);
                    header.order(ByteOrder.BIG_ENDIAN);

                    byte commandId = header.get(1);
                    byte filenameSize = header.get(2);
                    byte[] byteFilename = Arrays.copyOfRange(bytes, 3, filenameSize + 3);
                    String filename = new String(byteFilename);

                    String command = getCommandByByte(commandId);

                    byte statusCode = 2;
				    byte[] responseContent = null;
				    List<String> getFilesListResponseContent = null;
                    
                    if (command.equalsIgnoreCase("ADDFILE")) {
                        String path = this.currentPath + "/" + filename;
                        File file = new File(path);
                
                        if (file.createNewFile()) {
                            FileWriter writer = new FileWriter(path, true);
                            logger.info("Criando arquivo");
                            BufferedWriter buffer = new BufferedWriter(writer);
                            logger.info("Escrevendo conteúdo no arquivo");
                            buffer.write("Teste de conteúdo");
                            buffer.flush();
                            buffer.close();
                            logger.info("Arquivo criado com sucesso");
                            statusCode = 1;
                        } else {
                            logger.warning("Erro ao criar arquivo");
                            statusCode = 2;
                        }
                    } else if (command.equalsIgnoreCase("DELETE")) {
                        String path = this.currentPath + "/" + filename;
                        File file = new File(path);
                
                        if (file.exists()) {
                            if (file.delete()) {
                                logger.info("Arquivo deletado com sucesso");
                                statusCode = 1;
                            }
                        }
                
                        logger.warning("Erro ao deletar arquivo");
                        statusCode = 2;
                    } else if (command.equalsIgnoreCase("GETFILELIST")){
                        System.out.println(this.currentPath);
                        File dir = new File(this.currentPath);
                        File[] arrayFiles = dir.listFiles();

                        List<String> fileList = new ArrayList<String>();

                        logger.info("Obtendo arquivos do diretório");
                        for (File f : arrayFiles) {
                            if (f.isFile()) {
                                fileList.add(f.getName());
                            }
                        }

                        getFilesListResponseContent = fileList;
                    } else if (command.equalsIgnoreCase("GETFILE")) {
                        byte[] response = null;
                        try {
                            String path = this.currentPath + "/" + filename;
                            File file = new File(path);
                
                            logger.info("Obtendo conteúdo do arquivo");
                            FileInputStream inputStream = new FileInputStream(file);
                            response = inputStream.readAllBytes();
                            logger.info("Fechando Stream de dados");
                            inputStream.close();
                
                            responseContent = response;
                        } catch (Exception e) {
                            logger.warning("Erro ao obter arquivo: " + e.getMessage());
                            responseContent = response;
                        }
                    } else {
                        out.writeUTF("Comando inválido.");
                    }

                    // Enviado cabeçalho de resposta com tamanho fixo
                    logger.info("Enviando header de resposta de tamanho fixo");
                    byte responseCode = 2;
                    ByteBuffer buffer = this.createResponseHeader(responseCode, commandId, statusCode);
                    bytes = buffer.array();
                    int size = buffer.limit();
                    out.write(bytes, 0, size);
                    out.flush();

                    // Enviado conteudos do arquivos
                    logger.info("Enviando conteúdo");
                    switch (command) {
                        case "GETFILESLIST": 
                            logger.info("Iniciando envio da resposta do comando GETFILELIST");
                            int listOfFilesSize;
                            if (getFilesListResponseContent == null)
                                listOfFilesSize = 0;
                            else
                                listOfFilesSize = getFilesListResponseContent.size();

                            logger.info("Criando e adicionando dados no buffer");
                            buffer = ByteBuffer.allocate(2);
                            buffer.put((byte) ((listOfFilesSize >> 8) & 0xFF)); // INSERINDO BYTE MAIS SIGNIFICATIVO
                            buffer.put((byte) (listOfFilesSize & 0xFF)); // INSERINDO BYTE MENOS SIGNIFICATIVO

                            bytes = buffer.array();
                            size = buffer.limit();

                            out.write(bytes, 0, size);
                            out.flush();

                            for (String fileName : getFilesListResponseContent) {
                                byte[] filenameInBytes = fileName.getBytes();
                                byte filenameLength = (byte) fileName.length();

                                out.write(filenameLength);
                                out.flush();

                                logger.info("Enviando nomes dos arquivos byte a byte");
                                for (int i = 0; i < filenameLength; i++) {
                                    logger.info("Enviou byte: " + filenameInBytes[i]);
                                    out.write(filenameInBytes[i]);
                                    out.flush();
                                }
                            }

                            break;
                        case "GETFILE":
                            logger.info("Iniciando envio da resposta do comando GETFILE");
                            int sizeResponseContent;
                            if (responseContent == null)
                                sizeResponseContent = 0;
                            else
                                sizeResponseContent = responseContent.length;

                            logger.info("Criando e adicionando dados no buffer");
                            buffer = ByteBuffer.allocate(4);
                            buffer.order(ByteOrder.BIG_ENDIAN);
                            buffer.putInt(sizeResponseContent);
                            bytes = buffer.array();
                            size = buffer.limit();
                            out.write(bytes, 0, size);
                            out.flush();

                            logger.info("Enviando conteúdo do arquivo byte a byte");
                            for (int i = 0; i < sizeResponseContent; i++) {
                                logger.info("Enviou byte: " + responseContent[i]);
                                out.write(responseContent[i]);
                                out.flush();
                            }

                            break;

                    }
				}
            } catch (EOFException e) {
                logger.info("End Of File Exception: " + e.getMessage());
			    System.out.println("EOF: " + e.getMessage());
            } catch (IOException e) {
                logger.info("I/O Exception: " + e.getMessage());
                System.out.println("Erro de leitura: " + e.getMessage());
            } finally {
                try {
                    logger.info("Fechando conexão");
                    in.close();
                    out.close();
                    clientSocket.close();
                } catch (IOException e) {
                    logger.info("I/O Exception ao fechar conexão: " + e.getMessage());
                    System.err.println("IOE: " + e);
                }
            }

            logger.info("Comunicação finalizada.");
            System.out.println("Comunicação finalizada.");
        }
    }
}
