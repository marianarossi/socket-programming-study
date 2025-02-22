## Tutorial: Implementação do Jogo da Velha com Sockets TCP em Java

---

## Visão Geral

Neste projeto, implementamos um jogo da velha utilizando comunicação em rede por meio de sockets TCP. A ideia é que o servidor atue como árbitro, gerenciando o estado do tabuleiro e a lógica do jogo, enquanto dois clientes se conectam ao servidor para jogar. Cada cliente representa um jogador (Player 1 com o símbolo **X** e Player 2 com o símbolo **O**).  
O servidor envia atualizações do tabuleiro e mensagens de controle (por exemplo, "Sua vez", "Vencedor", "Empate") para os clientes, que por sua vez enviam suas jogadas (um número de 1 a 9 representando a posição no tabuleiro).

---

## Estrutura dos Arquivos

O projeto é composto por dois arquivos principais:

1. `TicTacToeServer.java` – Código do servidor.
2. `TicTacToeClient.java` – Código do cliente.

---

## Detalhes da Implementação

### 1. Código do Servidor (`TicTacToeServer.java`)

**Funcionamento:**

- **Criação do Socket do Servidor:**  
  O servidor utiliza a classe `ServerSocket` para escutar conexões na porta 2025.

- **Aceitação de Conexões:**  
  O servidor aguarda e aceita duas conexões, uma para cada jogador. Assim que um cliente se conecta, ele é designado como Player 1 (símbolo **X**) e o próximo como Player 2 (símbolo **O**).

- **Inicialização do Tabuleiro:**  
  O tabuleiro é representado por um array de caracteres com nove posições, inicialmente preenchidas pelos números 1 a 9 (para facilitar a visualização das posições).

- **Loop do Jogo:**  
  Em cada iteração:
  - O servidor envia o estado atual do tabuleiro para ambos os clientes.
  - Identifica de quem é a vez (começando com o Player 1).
  - Envia uma mensagem de `YOUR_TURN` (sua vez) para o jogador ativo e `OPPONENT_TURN` para o outro.
  - Recebe a jogada do jogador ativo e valida se a jogada é válida (posição entre 1 e 9 e célula não ocupada).
  - Atualiza o tabuleiro com a jogada válida.
  - Verifica se houve vitória ou empate. Se sim, envia a mensagem final (por exemplo, `WIN`, `LOSE` ou `DRAW`) e encerra o jogo.
  - Caso contrário, alterna o turno para o outro jogador e repete o processo.

- **Fechamento das Conexões:**  
  Ao final do jogo, o servidor fecha todos os fluxos (input/output) e as conexões dos sockets, além de encerrar o `ServerSocket`.

---

### 2. Código do Cliente (`TicTacToeClient.java`)

**Funcionamento:**

- **Conexão com o Servidor:**  
  O cliente se conecta ao servidor na porta 2025. Se os dois clientes estiverem em máquinas diferentes, é necessário alterar o IP de conexão (consulte a seção abaixo sobre "Como rodar o cliente em um computador diferente").

- **Recepção de Mensagens:**  
  Assim que a conexão for estabelecida, o cliente recebe uma mensagem de boas-vindas e, durante o jogo, o servidor envia mensagens com o estado do tabuleiro, indica quando é a vez do jogador e mensagens de controle (por exemplo, `YOUR_TURN`, `OPPONENT_TURN`, `WIN`, `LOSE`, `DRAW`).

- **Entrada da Jogada:**  
  Quando o cliente recebe a mensagem `YOUR_TURN`, ele solicita que o usuário digite sua jogada (um número de 1 a 9) e envia essa jogada para o servidor.

- **Loop de Comunicação:**  
  O cliente fica num loop lendo as mensagens do servidor e respondendo conforme necessário. Quando o jogo termina (vitória, derrota ou empate), o loop é encerrado.

- **Fechamento da Conexão:**  
  Após o término do jogo, os streams e o socket são fechados.

---

## Passo a Passo: Compilação e Execução

### Passo 1: Preparação dos Arquivos

- **Salve os arquivos:**
  - `TicTacToeServer.java` – Código do servidor.
  - `TicTacToeClient.java` – Código do cliente.
- Certifique-se de que ambos os arquivos estejam na mesma pasta.

### Passo 2: Compilação

1. Abra um terminal (ou prompt de comando) e navegue até a pasta onde os arquivos foram salvos.
2. Compile os arquivos Java executando:
   ```bash
   javac TicTacToeServer.java TicTacToeClient.java
Isso irá gerar os arquivos `.class` correspondentes.

## Passo 3: Execução do Servidor

1. **Inicie o Servidor:**
   - No terminal, execute:
     ```bash
     java TicTacToeServer
     ```
   - Você verá mensagens como “Tic Tac Toe Server está rodando na porta 2025” e “Aguardando Player 1…”, indicando que o servidor está aguardando conexões.

## Passo 4: Execução dos Clientes

1. **Em duas janelas de terminal diferentes (ou em máquinas distintas):**
   - Abra uma janela e execute:
     ```bash
     java TicTacToeClient
     ```
     Esse cliente será designado como Player 1 (símbolo **X**).
   - Abra outra janela e execute:
     ```bash
     java TicTacToeClient
     ```
     Esse cliente será designado como Player 2 (símbolo **O**).

## Passo 5: Jogo em Ação

1. **Interação dos Clientes:**
   - Cada cliente exibirá o estado atual do tabuleiro e mensagens informando se é a vez do jogador ou se o oponente está jogando.
   - Quando receber a mensagem “YOUR_TURN” (Sua vez), o cliente solicitará que o usuário digite um número entre 1 e 9 para marcar sua jogada.
   - O servidor atualiza o tabuleiro com a jogada válida e envia o novo estado para ambos os clientes.
   - O processo se repete até que um jogador vença ou o jogo termine em empate.

2. **Finalização:**
   - Ao final do jogo, o servidor envia mensagens “WIN” (vencedor), “LOSE” (perdedor) ou “DRAW” (empate) e encerra a conexão.
   - Os clientes exibem a mensagem final e fecham a conexão.

## Como Rodar o Cliente em um Computador Diferente

1. **Obtenha o IP do Servidor:**
   - No computador que está rodando o servidor, verifique o endereço IP da máquina (por exemplo, `192.168.1.100`).

2. **Alterar o Código do Cliente:**
   - No arquivo `TicTacToeClient.java`, altere a linha que cria o socket:
     ```java
     Socket socket = new Socket("127.0.0.1", 2025);
     ```
     para:
     ```java
     Socket socket = new Socket("192.168.1.100", 2025);
     ```
     substituindo `"192.168.1.100"` pelo endereço IP real do servidor.

3. **Recompile o Cliente:**
   - Após a alteração, compile novamente:
     ```bash
     javac TicTacToeClient.java
     ```

4. **Execute o Cliente na Máquina Diferente:**
   - No terminal do computador cliente, execute:
     ```bash
     java TicTacToeClient
     ```
     O cliente se conectará ao servidor usando o IP especificado.

5. **Verifique Conectividade:**
   - Certifique-se de que não existam bloqueios de firewall ou problemas de rede que impeçam a conexão na porta 2025.
