/**
* Cliente TCP da versao V2 para enviar regiao e exibir a resposta.
 * @author Rodrigo Simão Guimarães
 *
 * @since 2026-03-19
 */
package TcpClockSimpleV2;

import java.net.*;
import java.io.*;
import java.util.Scanner;

public class TCPClient {

    public static void main(String[] args) {

        String servidor = "localhost";
        int porta = 5800;

        try (
                Socket socket = new Socket(servidor, porta);
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(socket.getInputStream())
                );
                BufferedWriter out = new BufferedWriter(
                        new OutputStreamWriter(socket.getOutputStream())
                );
                Scanner scanner = new Scanner(System.in)
        ) {

            System.out.print("Digite a região (ex: America/Sao_Paulo): ");
            String regiao = scanner.nextLine();

            // Envia requisição
            out.write(regiao + "\n");
            out.flush();

            // Recebe resposta
            String resposta = in.readLine();

            System.out.println("Resposta do servidor: " + resposta);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
