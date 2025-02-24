## Implementation of Tic Tac Toe using TCP Sockets in Java

---

## Overview

In this project, we implemented a tic tac toe game using network communication via TCP sockets. The idea is that the server acts as the referee, managing the board state and game logic, while two clients connect to the server to play. Each client represents a player (Player 1 with the **X** symbol and Player 2 with the **O** symbol).  
The server sends board updates and control messages (for example, "Your turn", "Winner", "Draw") to the clients, who in turn send their moves (a number from 1 to 9 representing the board position).

---

## File Structure

The project consists of two main files:

1. `TicTacToeServer.java` – Server code.
2. `TicTacToeClient.java` – Client code.

---

## Implementation Details

### 1. Server Code (`TicTacToeServer.java`)

**How it Works:**

- **Server Socket Creation:**  
  The server uses the `ServerSocket` class to listen for connections on port 2025.

- **Accepting Connections:**  
  The server waits for and accepts two connections, one for each player. Once a client connects, it is designated as Player 1 (symbol **X**) and the next as Player 2 (symbol **O**).

- **Board Initialization:**  
  The board is represented by a character array with nine positions, initially filled with the numbers 1 to 9 (to help visualize the positions).

- **Game Loop:**  
  In each iteration:
  - The server sends the current board state to both clients.
  - It identifies whose turn it is (starting with Player 1).
  - It sends a `YOUR_TURN` message to the active player and an `OPPONENT_TURN` message to the other.
  - It receives the move from the active player and validates whether the move is valid (position between 1 and 9 and cell not already occupied).
  - It updates the board with the valid move.
  - It checks if there is a win or a draw. If so, it sends the final message (for example, `WIN`, `LOSE`, or `DRAW`) and ends the game.
  - Otherwise, it alternates the turn to the other player and repeats the process.

- **Closing Connections:**  
  At the end of the game, the server closes all streams (input/output) and the socket connections, as well as the `ServerSocket`.

---

### 2. Client Code (`TicTacToeClient.java`)

**How it Works:**

- **Connecting to the Server:**  
  The client connects to the server on port 2025. If the two clients are on different machines, you must change the connection IP (see the section below on [How to Run the Client on a Different Computer](#how-to-run-the-client-on-a-different-computer).


- **Receiving Messages:**  
  Once the connection is established, the client receives a welcome message and, during the game, the server sends messages with the board state, indicates when it is the player's turn, and sends control messages (for example, `YOUR_TURN`, `OPPONENT_TURN`, `WIN`, `LOSE`, `DRAW`).

- **Inputting the Move:**  
  When the client receives the `YOUR_TURN` message, it prompts the user to enter their move (a number from 1 to 9) and sends that move to the server.

- **Communication Loop:**  
  The client remains in a loop reading messages from the server and responding as needed. When the game ends (win, loss, or draw), the loop terminates.

- **Closing the Connection:**  
  After the game ends, the streams and the socket are closed.

---

## Step by Step: Compilation and Execution

### Step 1: Preparing the Files

- **Save the files:**
  - `TicTacToeServer.java` – Server code.
  - `TicTacToeClient.java` – Client code.
- Make sure that both files are in the same folder.

### Step 2: Compilation

1. Open a terminal (or command prompt) and navigate to the folder where the files are saved.
2. Compile the Java files by executing:
   ```bash
   javac TicTacToeServer.java TicTacToeClient.java

This will generate the corresponding `.class` files.

## Step 3: Running the Server

1. **Start the Server:**
   - In the terminal, execute:
     ```bash
     java TicTacToeServer
     ```
   - You will see messages such as “Tic Tac Toe Server is running on port 2025” and “Waiting for Player 1…”, indicating that the server is waiting for connections.

## Step 4: Running the Clients

1. **In two different terminal windows (or on different machines):**
   - Open one window and execute:
     ```bash
     java TicTacToeClient
     ```
     This client will be designated as Player 1 (symbol **X**).
   - Open another window and execute:
     ```bash
     java TicTacToeClient
     ```
     This client will be designated as Player 2 (symbol **O**).

## Step 5: Game in Action

1. **Client Interaction:**
   - Each client will display the current state of the board and messages indicating whether it is the player's turn or if the opponent is playing.
   - When receiving the “YOUR_TURN” (Your turn) message, the client will prompt the user to enter a number between 1 and 9 to mark their move.
   - The server updates the board with the valid move and sends the new state to both clients.
   - This process repeats until a player wins or the game ends in a draw.

2. **Finalization:**
   - At the end of the game, the server sends “WIN” (winner), “LOSE” (loser), or “DRAW” (draw) messages and closes the connection.
   - The clients display the final message and close the connection.

## How to Run the Client on a Different Computer

1. **Obtain the Server's IP:**
   - On the computer running the server, check the machine's IP address (for example, `192.168.1.100`).

2. **Modify the Client Code:**
   - In the file `TicTacToeClient.java`, change the line that creates the socket:
     ```java
     Socket socket = new Socket("127.0.0.1", 2025);
     ```
     to:
     ```java
     Socket socket = new Socket("192.168.1.100", 2025);
     ```
     replacing `"192.168.1.100"` with the actual IP address of the server.

3. **Recompile the Client:**
   - After the change, compile again:
     ```bash
     javac TicTacToeClient.java
     ```

4. **Run the Client on a Different Computer:**
   - In the terminal of the client computer, execute:
     ```bash
     java TicTacToeClient
     ```
     The client will connect to the server using the specified IP.

5. **Verify Connectivity:**
   - Make sure there are no firewall restrictions or network issues that might block the connection on port 2025.
