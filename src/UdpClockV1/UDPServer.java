/**
* Servidor UDP que recebe uma regiao e responde com data/hora formatada.
 * @author Rodrigo Simão Guimarães
 *
 * @since 2026-03-19
 */
package UdpClockV1;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class UDPServer {
    public static void main(String[] args) {
        int porta = 9876;

        try (DatagramSocket socket = new DatagramSocket(porta)) {
            System.out.println("Servidor UDP iniciado na porta " + porta);

            byte[] buffer = new byte[1024];

            while (true) {
                DatagramPacket request = new DatagramPacket(buffer, buffer.length);
                socket.receive(request);

                String regiao = new String(request.getData(), 0, request.getLength());

                System.out.println("Recebido: " + regiao);

                String resposta;

                try {
                    ZonedDateTime agora = ZonedDateTime.now(ZoneId.of(regiao));
                    resposta = agora.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
                } catch (Exception e) {
                    resposta = "Região inválida!";
                }

                byte[] respBytes = resposta.getBytes();

                DatagramPacket reply = new DatagramPacket(
                        respBytes,
                        respBytes.length,
                        request.getAddress(),
                        request.getPort()
                );

                socket.send(reply);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
