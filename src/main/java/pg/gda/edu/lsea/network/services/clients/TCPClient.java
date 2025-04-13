package pg.gda.edu.lsea.network.services.clients;

import pg.gda.edu.lsea.dataHandlers.utils.Serializer;

import java.io.*;
import java.net.Socket;
import java.util.List;
import java.util.Scanner;

/**
 * TCP Client that establishes and manages connections to TCP Server over TCP/IP connection
 * Supports serialized object transfer for football match analysis functionality
 */
public class TCPClient {
    // Initialize Server Port
    private final static int SERVER_PORT = 6666;
    // Initialize Server Host Name
    private final static String SERVER_HOST = "localhost";
    // Initialize Ending message
    private static final String CLIENT_ENDING_MESSAGE = "Connection with the server has been disconnected. Thank you!";


    public static void main(String[] args) {
        try (
                // Establish a socket connection between the server and the client
                Socket socket = new Socket(SERVER_HOST, SERVER_PORT);

                // Set up a buffered reader - receiving output from the server
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                // Set up a print write - sending output to the server
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

                // Set up a input stream - receiving serialized objects from the server
                ObjectInputStream objectIn = new ObjectInputStream(socket.getInputStream());

                // Set up a scanner - reading user input
                Scanner scanner = new Scanner(System.in)
        ) {
            // Display initializing message
            System.out.println("Connected to server at " + SERVER_HOST + ":" + SERVER_PORT);

            String fromServer;

            // Handles receiving output from the server
            while ((fromServer = in.readLine()) != null) {
                System.out.println(fromServer);

                if (fromServer.equals("Choose option:")) {
                    String userInput = scanner.nextLine();
                    out.println(userInput);

                    // Handle serialized responses for server working options
                    if (userInput.equals("1") || userInput.equals("2") || userInput.equals("3")) {

                        if (userInput.equals("3")) {
                            // Handling choosing home team for prediction
                            fromServer = in.readLine();
                            System.out.println(fromServer);
                            userInput = scanner.nextLine();
                            out.println(userInput);

                            // Handling choosing away team for prediction
                            fromServer = in.readLine();
                            System.out.println(fromServer);
                            userInput = scanner.nextLine();
                            out.println(userInput);
                        }

                        // Receive and deserialize response object
                        try {
                            byte[] serializedData = (byte[]) objectIn.readObject();
                            List<String> result = Serializer.getDeserializedForm(serializedData);

                            System.out.println();
                            System.out.println("Server response:");
                            for (String line : result) {
                                System.out.println(line);
                            }
                            System.out.println();
                        } catch (Exception e) {
                            System.err.println("Error receiving serialized data: " + e.getMessage());
                        }
                    }
                } else if (fromServer.equals(CLIENT_ENDING_MESSAGE)) {
                    System.out.println("Disconnecting from server. Thank you!");
                    break;
                }
            }

            // Display ending message
            System.out.println(CLIENT_ENDING_MESSAGE);

        } catch (IOException e) {
            // Handle exception
            System.err.println("Error connecting to server: " + e.getMessage());
        }
    }
}