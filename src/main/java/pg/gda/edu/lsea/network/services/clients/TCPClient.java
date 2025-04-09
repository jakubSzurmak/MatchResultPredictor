package pg.gda.edu.lsea.network.services.clients;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * TCP Client that establishes and manages connections to TCP Server over TCP/IP connection
 */
public class TCPClient {
    private final static int SERVER_PORT = 6666;

    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("localhost", SERVER_PORT);

        BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter output = new PrintWriter(socket.getOutputStream(), true);
        BufferedReader serverInput = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        System.out.println("Connected to the server");

        String userInput;
        while ((userInput = input.readLine()) != null) {
            output.println(userInput);
            System.out.println("Received: " + serverInput.readLine());
        }

        socket.close();
    }
}
