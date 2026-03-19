package UdpClockV1;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;
import java.util.Scanner;

public class UDPClient {
    public static void main(String[] args) {

        String servidor = "localhost";
        int porta = 9876;

        try (DatagramSocket socket = new DatagramSocket()) {

            // 5 segundos de atraso
            socket.setSoTimeout(5000);

            Scanner sc = new Scanner(System.in);

            System.out.println("Digite a região (ex: America/Sao_Paulo): ");
            String mensagem = sc.nextLine();

            byte[] msgBytes = mensagem.getBytes();

            InetAddress host = InetAddress.getByName(servidor);

            DatagramPacket request = new DatagramPacket(msgBytes, msgBytes.length, host, porta);

            socket.send(request);

            byte[] buffer = new byte[1024];
            DatagramPacket response = new DatagramPacket(buffer, buffer.length);

            try {
                socket.receive(response);

                String resposta = new String(response.getData(), 0, response.getLength());
                System.out.println("Resposta do servidor: " + resposta);

            } catch (SocketTimeoutException e) {
                System.out.println("Servidor ocupado ou offline");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
