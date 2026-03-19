package TcpClockMultithreadV3;

import java.net.*;

public class TCPServerMulti {

    public static void main(String[] args) {

        int porta = 3000;

        try (ServerSocket serverSocket = new ServerSocket(porta)) {

            System.out.println("Servidor TCP MULTI-THREAD rodando na porta " + porta);

            while (true) {
                System.out.println("Aguardando cliente...");

                Socket clientSocket = serverSocket.accept();

                System.out.println("Novo cliente conectado: " + clientSocket.getInetAddress());

                // 🔥 Cria nova thread para cada cliente
                ClientHandler handler = new ClientHandler(clientSocket);
                handler.start(); // inicia a thread
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
