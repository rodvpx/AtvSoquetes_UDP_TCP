/**
* Cliente TCP da versao V3 para requisitar horario por regiao.
 * @author Rodrigo Simão Guimarães
 *
 * @since 2026-03-19
 */
package TcpClockMultithreadV3;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Scanner;

public class TCPClientV3 {

    public static void main(String[] args) {

        String servidor = "localhost";
        int porta = 3000;

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
