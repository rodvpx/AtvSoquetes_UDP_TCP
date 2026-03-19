# Atividade Prática: Sistema Distribuído de Relógio Mundial (TCP/UDP)

## Resumo do Projeto
Esta atividade prática implementa um sistema cliente-servidor em Java para consultar data/hora por região geográfica (timezone), comparando os protocolos UDP e TCP e o impacto de concorrência no servidor.

Objetivos da atividade:
- Praticar comunicação entre processos via sockets.
- Entender diferenças entre UDP (não orientado à conexão) e TCP (orientado à conexão).
- Implementar servidor TCP concorrente (multithread) para melhorar escalabilidade.
- Receber no cliente uma `String` de região (ex: `America/Sao_Paulo`), processar no servidor com `java.time.ZonedDateTime` e devolver data/hora formatada ou erro para região inválida.

## Instruções de Execução

### 1) Compilar o projeto
No diretório raiz do projeto:

```bash
javac src/UdpClockV1/*.java src/TcpClockSimpleV2/*.java src/TcpClockMultithreadV3/*.java src/testes/*.java
```

### 2) Parte 1 - Comunicação Não Orientada à Conexão (UDP)
Terminal 1 (servidor):

```bash
java -cp src UdpClockV1.UDPServer
```

Terminal 2 (cliente):

```bash
java -cp src UdpClockV1.UDPClient
```

Notas:
- Porta usada: `9876`.
- O cliente usa `setSoTimeout(5000)` (5 segundos). Se não houver resposta, exibe: `Servidor ocupado ou offline`.

### 3) Parte 2 - Comunicação Orientada à Conexão (TCP Single-Thread)
Terminal 1 (servidor):

```bash
java -cp src TcpClockSimpleV2.TCPServer
```

Terminal 2 (cliente):

```bash
java -cp src TcpClockSimpleV2.TCPClient
```

Notas:
- Porta usada: `5800`.
- O servidor atende de forma sequencial: aceita, processa, responde e fecha o socket antes do próximo cliente.

### 4) Parte 3 - Servidor TCP Concorrente (Multithread)
Terminal 1 (servidor):

```bash
java -cp src TcpClockMultithreadV3.TCPServerMulti
```

Terminal 2 (cliente):

```bash
java -cp src TcpClockMultithreadV3.TCPClientV3
```

Notas:
- Porta usada: `3000`.
- O servidor usa pool fixo de 10 threads e delega cada conexão para `ClientHandler`.
- Os logs mostram a thread atendendo cliente (`IP:porta`), conforme exigido.

### 5) Teste de carga comparativo (opcional)
Com os três servidores ativos (`UDPServer`, `TCPServer` e `TCPServerMulti`), execute:

```bash
java -cp src testes.LoadTestComparativo
```

Esse teste dispara múltiplas requisições com timezones aleatórios (`testes.Timezones`) e imprime tabela com sucesso, erros, perdas e tempo.

## Análise Técnica (V2 vs V3)
Quando múltiplos clientes conectam ao mesmo tempo, a diferença principal de performance vem do modelo de atendimento no servidor:

- V2 (single-thread):
  - Existe um único fluxo de atendimento no loop principal.
  - Enquanto um cliente está sendo processado, os demais esperam na fila de conexões do sistema operacional.
  - O tempo total cresce mais rapidamente com aumento de concorrência, elevando latência média por cliente.

- V3 (multithread com pool):
  - O `accept()` libera rapidamente o loop principal, e o trabalho é distribuído para threads do pool.
  - Vários clientes são atendidos em paralelo (até o limite do pool), reduzindo fila e tempo de resposta médio sob carga.
  - Em cenários com muitas conexões simultâneas, tende a apresentar throughput maior e menor tempo total que a V2.

Conclusão:
- A V2 é mais simples e adequada para baixo volume.
- A V3 é mais escalável para acesso concorrente, mantendo melhor desempenho perceptível com múltiplos clientes simultâneos.
