package TcpClockMultithreadV3;

import java.net.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TCPServerMulti {

    public static void main(String[] args) {

        int porta = 3000;

        // 🔥 Cria o pool UMA VEZ
        ExecutorService pool = Executors.newFixedThreadPool(10);

        try (ServerSocket serverSocket = new ServerSocket(porta)) {

            System.out.println("Servidor TCP MULTI-THREAD rodando na porta " + porta);

            while (true) {
                System.out.println("Aguardando cliente...");

                Socket clientSocket = serverSocket.accept();

                System.out.println("Novo cliente conectado: " + clientSocket.getInetAddress());

                // 🔥 Usa o pool existente
                pool.execute(new ClientHandler(clientSocket));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}