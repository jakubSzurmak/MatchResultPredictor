package pg.gda.edu.lsea.network.services.servers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * TCP Server that accepts and handles clients connection and communication over TCP/IP
 */
public class TCPServer {
    private static final int SERVER_PORT = 6666;
    private static final String WELCOME_MESSAGE = "TCP Server has started. Waiting for connection with the client";
    private static final String CLIENT_ACCEPTED_MESSAGE = "Client has connected";

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(SERVER_PORT);
        System.out.println(WELCOME_MESSAGE);

        Socket socket = serverSocket.accept();
        System.out.println(CLIENT_ACCEPTED_MESSAGE);

        BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter output = new PrintWriter(socket.getOutputStream(), true);

        String line;
        while ((line = input.readLine()) != null) {
            System.out.println("Received: " + line);
            output.println(line);
        }

        socket.close();
        serverSocket.close();
    }
}
