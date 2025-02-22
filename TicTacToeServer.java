import java.io.*;
import java.net.*;

public class TicTacToeServer {

    private char[] board;
    private DataInputStream in1, in2;
    private DataOutputStream out1, out2;
    private Socket player1, player2;

    public TicTacToeServer() {
        board = new char[9];
        // Initialize board with positions 1-9 (for ease of display)
        for (int i = 0; i < 9; i++) {
            board[i] = (char) ('1' + i);
        }
    }

    // Checks if a player with mark 'X' or 'O' has won
    private boolean checkWin(char mark) {
        // rows
        if (board[0] == mark && board[1] == mark && board[2] == mark) return true;
        if (board[3] == mark && board[4] == mark && board[5] == mark) return true;
        if (board[6] == mark && board[7] == mark && board[8] == mark) return true;
        // columns
        if (board[0] == mark && board[3] == mark && board[6] == mark) return true;
        if (board[1] == mark && board[4] == mark && board[7] == mark) return true;
        if (board[2] == mark && board[5] == mark && board[8] == mark) return true;
        // diagonals
        if (board[0] == mark && board[4] == mark && board[8] == mark) return true;
        if (board[2] == mark && board[4] == mark && board[6] == mark) return true;
        return false;
    }

    // Checks if the board is full (draw condition)
    private boolean boardFull() {
        for (char c : board) {
            if (c != 'X' && c != 'O') {
                return false;
            }
        }
        return true;
    }

    // Returns a string representation of the board
    private String boardToString() {
        return board[0] + " | " + board[1] + " | " + board[2] + "\n" +
                "---------\n" +
                board[3] + " | " + board[4] + " | " + board[5] + "\n" +
                "---------\n" +
                board[6] + " | " + board[7] + " | " + board[8];
    }

    public void start() throws Exception {
        ServerSocket serverSocket = new ServerSocket(2025);
        System.out.println("Tic Tac Toe Server is running on port 2025.");

        // Accept connection from Player 1
        System.out.println("Waiting for Player 1...");
        player1 = serverSocket.accept();
        System.out.println("Player 1 connected: " + player1.getInetAddress().getHostName());
        out1 = new DataOutputStream(player1.getOutputStream());
        in1 = new DataInputStream(player1.getInputStream());
        out1.writeUTF("WELCOME X. Waiting for Player 2 to connect...");

        // Accept connection from Player 2
        System.out.println("Waiting for Player 2...");
        player2 = serverSocket.accept();
        System.out.println("Player 2 connected: " + player2.getInetAddress().getHostName());
        out2 = new DataOutputStream(player2.getOutputStream());
        in2 = new DataInputStream(player2.getInputStream());
        out2.writeUTF("WELCOME O. Game is starting...");
        out1.writeUTF("Player 2 has connected. Game is starting...");

        // Game loop – players take turns until someone wins or a draw occurs.
        char currentMark = 'X'; // Player 1 starts
        DataInputStream currentIn;
        DataOutputStream currentOut, otherOut;
        while (true) {
            // Send updated board to both players
            String boardStr = boardToString();
            out1.writeUTF("BOARD\n" + boardStr);
            out2.writeUTF("BOARD\n" + boardStr);

            // Identify current player and send turn messages
            if (currentMark == 'X') {
                currentOut = out1;
                currentIn = in1;
                otherOut = out2;
            } else {
                currentOut = out2;
                currentIn = in2;
                otherOut = out1;
            }
            currentOut.writeUTF("YOUR_TURN");
            otherOut.writeUTF("OPPONENT_TURN");

            // Prompt current player for a move
            currentOut.writeUTF("Enter your move (1-9): ");
            int move = currentIn.readInt();

            // Validate move
            if (move < 1 || move > 9) {
                currentOut.writeUTF("INVALID_MOVE. Try again.");
                continue;
            }
            int index = move - 1;
            if (board[index] == 'X' || board[index] == 'O') {
                currentOut.writeUTF("INVALID_MOVE. Spot already taken. Try again.");
                continue;
            }

            // Update board with valid move
            board[index] = currentMark;

            // Check for win
            if (checkWin(currentMark)) {
                boardStr = boardToString();
                out1.writeUTF("BOARD\n" + boardStr);
                out2.writeUTF("BOARD\n" + boardStr);
                currentOut.writeUTF("WIN");
                otherOut.writeUTF("LOSE");
                break;
            }

            // Check for draw
            if (boardFull()) {
                boardStr = boardToString();
                out1.writeUTF("BOARD\n" + boardStr);
                out2.writeUTF("BOARD\n" + boardStr);
                out1.writeUTF("DRAW");
                out2.writeUTF("DRAW");
                break;
            }

            // Switch turn to the other player
            currentMark = (currentMark == 'X') ? 'O' : 'X';
        }

        // Close all connections and streams
        in1.close();
        out1.close();
        player1.close();
        in2.close();
        out2.close();
        player2.close();
        serverSocket.close();
        System.out.println("Game over. Server shutting down.");
    }

    public static void main(String[] args) {
        try {
            TicTacToeServer server = new TicTacToeServer();
            server.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
