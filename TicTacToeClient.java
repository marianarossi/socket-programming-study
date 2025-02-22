import java.io.*;
import java.net.*;
import java.util.Scanner;

public class TicTacToeClient {

    public void start() throws Exception {
        Socket socket = new Socket("192.168.2.100", 2025);
        DataInputStream in = new DataInputStream(socket.getInputStream());
        DataOutputStream out = new DataOutputStream(socket.getOutputStream());
        Scanner scanner = new Scanner(System.in);

        // Print welcome message and any initial messages from the server
        System.out.println(in.readUTF());

        while (true) {
            String serverMsg = in.readUTF();

            if (serverMsg.startsWith("BOARD")) {
                // Display the board (skip the "BOARD\n" prefix)
                System.out.println(serverMsg.substring(6));
            } else if (serverMsg.equals("YOUR_TURN")) {
                // When it’s this client’s turn, wait for the prompt
                System.out.println("It's your turn.");
                String prompt = in.readUTF(); // e.g., "Enter your move (1-9): "
                System.out.print(prompt);
                int move = scanner.nextInt();
                out.writeInt(move);
            } else if (serverMsg.equals("OPPONENT_TURN")) {
                // Inform the player that it is the opponent’s turn
                System.out.println("Waiting for opponent's move...");
            } else if (serverMsg.equals("INVALID_MOVE. Try again.") ||
                    serverMsg.startsWith("INVALID_MOVE")) {
                // Display any invalid move message and loop back
                System.out.println(serverMsg);
            } else if (serverMsg.equals("WIN")) {
                System.out.println("Congratulations, you win!");
                break;
            } else if (serverMsg.equals("LOSE")) {
                System.out.println("Sorry, you lose.");
                break;
            } else if (serverMsg.equals("DRAW")) {
                System.out.println("Game is a draw.");
                break;
            } else {
                // Print any other messages from the server
                System.out.println(serverMsg);
            }
        }

        // Close all streams and the socket
        scanner.close();
        in.close();
        out.close();
        socket.close();
    }

    public static void main(String[] args) {
        try {
            TicTacToeClient client = new TicTacToeClient();
            client.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
