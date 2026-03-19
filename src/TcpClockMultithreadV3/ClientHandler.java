/**
* Handler de conexao da V3 que processa uma requisicao TCP por thread.
 * @author Rodrigo Simão Guimarães
 *
 * @since 2026-03-19
 */
package TcpClockMultithreadV3;

import java.net.*;
import java.io.*;
import java.time.ZonedDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class ClientHandler extends Thread {

    private Socket clientSocket;

    public ClientHandler(Socket socket) {
        this.clientSocket = socket;
    }

    @Override
    public void run() {
        String threadName = Thread.currentThread().getName();

        try (
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(clientSocket.getInputStream())
                );
                BufferedWriter out = new BufferedWriter(
                        new OutputStreamWriter(clientSocket.getOutputStream())
                )
        ) {

            String clienteInfo = clientSocket.getInetAddress() + ":" + clientSocket.getPort();
            System.out.println("[" + threadName + "] Atendendo cliente: " + clienteInfo);

            String regiao = in.readLine();
            System.out.println("[" + threadName + "] Recebido: " + regiao);

            String resposta;

            try {
                ZonedDateTime agora = ZonedDateTime.now(ZoneId.of(regiao));
                resposta = agora.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
            } catch (Exception e) {
                resposta = "Região inválida!";
            }

            out.write(resposta + "\n");
            out.flush();

            System.out.println("[" + threadName + "] Finalizado atendimento.\n");

        } catch (Exception e) {
            System.out.println("Erro na thread: " + e.getMessage());
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}