/**
* Teste de carga comparativo entre UDP, TCP V2 e TCP V3 com tabela final.
 * @author Rodrigo Simão Guimarães
 *
 * @since 2026-03-19
 */
package testes;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class LoadTestComparativo {

    private static final int TOTAL_REQUISICOES = 1000;
    private static final String HOST = "localhost";
    private static final int THREADS_POOL = 20;

    public static void main(String[] args) {
        List<ResultadoTeste> resultados = new ArrayList<>();

        resultados.add(executarTcp("TCP V2", 5800, TOTAL_REQUISICOES));
        resultados.add(executarTcp("TCP V3", 3000, TOTAL_REQUISICOES));
        resultados.add(executarUdp("UDP", 9876, TOTAL_REQUISICOES));

        imprimirTabela(resultados);
    }

    private static ResultadoTeste executarTcp(String modelo, int porta, int totalRequisicoes) {

        ExecutorService pool = Executors.newFixedThreadPool(THREADS_POOL);

        AtomicInteger sucesso = new AtomicInteger();
        AtomicInteger erros = new AtomicInteger();

        CountDownLatch latch = new CountDownLatch(totalRequisicoes);

        long inicio = System.currentTimeMillis();

        for (int i = 0; i < totalRequisicoes; i++) {
            pool.execute(() -> {
                try (
                        Socket socket = new Socket(HOST, porta);
                        BufferedReader in = new BufferedReader(
                                new InputStreamReader(socket.getInputStream())
                        );
                        BufferedWriter out = new BufferedWriter(
                                new OutputStreamWriter(socket.getOutputStream())
                        )
                ) {
                    out.write(Timezones.aleatorio() + "\n");
                    out.flush();

                    String resposta = in.readLine();

                    if (resposta != null) {
                        sucesso.incrementAndGet();
                    } else {
                        erros.incrementAndGet();
                    }

                } catch (Exception e) {
                    erros.incrementAndGet();
                } finally {
                    latch.countDown();
                }
            });
        }

        try {
            latch.await(); // espera TODAS terminar
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        pool.shutdown();

        long fim = System.currentTimeMillis();

        int perdidos = totalRequisicoes - (sucesso.get() + erros.get());

        return new ResultadoTeste(modelo, totalRequisicoes, sucesso.get(), erros.get(), perdidos, fim - inicio);
    }

    private static ResultadoTeste executarUdp(String modelo, int porta, int totalRequisicoes) {

        AtomicInteger sucesso = new AtomicInteger();
        AtomicInteger erros = new AtomicInteger();

        long inicio = System.currentTimeMillis();

        for (int i = 0; i < totalRequisicoes; i++) {
            try (DatagramSocket socket = new DatagramSocket()) {

                socket.setSoTimeout(2000);

                byte[] msg = Timezones.aleatorio().getBytes();
                InetAddress host = InetAddress.getByName(HOST);

                DatagramPacket req = new DatagramPacket(msg, msg.length, host, porta);
                socket.send(req);

                byte[] buffer = new byte[1024];
                DatagramPacket resp = new DatagramPacket(buffer, buffer.length);

                socket.receive(resp);

                sucesso.incrementAndGet();

            } catch (Exception e) {
                erros.incrementAndGet();
            }
        }

        long fim = System.currentTimeMillis();

        int perdidos = totalRequisicoes - (sucesso.get() + erros.get());

        return new ResultadoTeste(modelo, totalRequisicoes, sucesso.get(), erros.get(), perdidos, fim - inicio);
    }

    private static void imprimirTabela(List<ResultadoTeste> resultados) {
        System.out.println();
        System.out.println("+---------+------+---------+-------+----------+------------------+----------------+");
        System.out.println("| Modelo  | Req. | Sucesso | Erros | Perdidos | Tempo total (ms) | Media (ms/req) |");
        System.out.println("+---------+------+---------+-------+----------+------------------+----------------+");

        for (ResultadoTeste r : resultados) {
            System.out.printf(
                    Locale.US,
                    "| %-7s | %4d | %7d | %5d | %8d | %16d | %14.2f |%n",
                    r.modelo,
                    r.totalRequisicoes,
                    r.sucesso,
                    r.erros,
                    r.perdidos,
                    r.tempoTotalMs,
                    r.mediaMsPorRequisicao()
            );
        }

        System.out.println("+---------+------+---------+-------+----------+------------------+----------------+");
    }

    private static class ResultadoTeste {

        private final String modelo;
        private final int totalRequisicoes;
        private final int sucesso;
        private final int erros;
        private final int perdidos;
        private final long tempoTotalMs;

        private ResultadoTeste(String modelo, int totalRequisicoes, int sucesso, int erros, int perdidos, long tempoTotalMs) {
            this.modelo = modelo;
            this.totalRequisicoes = totalRequisicoes;
            this.sucesso = sucesso;
            this.erros = erros;
            this.perdidos = perdidos;
            this.tempoTotalMs = tempoTotalMs;
        }

        private double mediaMsPorRequisicao() {
            if (totalRequisicoes == 0) return 0.0;
            return (double) tempoTotalMs / totalRequisicoes;
        }
    }
}