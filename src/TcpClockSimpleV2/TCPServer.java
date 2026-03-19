package TcpClockSimpleV2;

import java.net.*;
import java.io.*;
import java.time.ZonedDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class TCPServer {

    public static void main(String[] args) {

        int porta = 5800;

        try (ServerSocket serverSocket = new ServerSocket(porta)) {

            System.out.println("Servidor TCP rodando na porta " + porta);

            while (true) {
                System.out.println("Aguardando cliente...");

                // 🔥 BLOQUEIA até cliente conectar
                Socket clientSocket = serverSocket.accept();

                System.out.println("Cliente conectado: " + clientSocket.getInetAddress());

                try (
                        BufferedReader in = new BufferedReader(
                                new InputStreamReader(clientSocket.getInputStream())
                        );
                        BufferedWriter out = new BufferedWriter(
                                new OutputStreamWriter(clientSocket.getOutputStream())
                        )
                ) {

                    // Lê região enviada
                    String regiao = in.readLine();
                    System.out.println("Recebido: " + regiao);

                    String resposta;

                    try {
                        ZonedDateTime agora = ZonedDateTime.now(ZoneId.of(regiao));
                        resposta = agora.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
                    } catch (Exception e) {
                        resposta = "Região inválida!";
                    }

                    // Envia resposta
                    out.write(resposta + "\n");
                    out.flush();

                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    // 🔴 Fecha conexão com cliente
                    clientSocket.close();
                    System.out.println("Conexão encerrada.\n");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}