package pg.gda.edu.lsea.network.services.clients;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

/**
 * TCP Client that establishes and manages connections to TCP Server over TCP/IP connection
 */
public class TCPClient {
    private final static int SERVER_PORT = 6666;

    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("localhost", SERVER_PORT);

        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        Scanner scanner = new Scanner(System.in);

        String fromServer;
        while ((fromServer = in.readLine()) != null) {
            System.out.println(fromServer);
            if (fromServer.equals("Choose option:")) {
                String userInput = scanner.nextLine();
                out.println(userInput);
            }
            if (fromServer.equals("Choose home team to prediction: ")){
                String userInput = scanner.nextLine();
                out.println(userInput);
            }
            if (fromServer.equals("Choose away team to prediction: ")){
                String userInput = scanner.nextLine();
                out.println(userInput);
            }

        }

        scanner.close();
        in.close();
        out.close();
        socket.close();
    }
}
