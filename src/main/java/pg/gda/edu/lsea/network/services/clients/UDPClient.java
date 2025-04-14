package pg.gda.edu.lsea.network.services.clients;

import pg.gda.edu.lsea.dataHandlers.utils.Serializer;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.List;
import java.util.Scanner;

/**
 * UDP Client that sends and receives datagrams to UDP server
 */
public class UDPClient {
    private static final int SERVER_PORT = 7777;
    private static final String SERVER_HOST = "localhost";
    private static final int BUFFER_SIZE = 16384;

    // Available commands
    private static final String CMD_TEAMS = "GET_TEAMS";
    private static final String CMD_CORRELATION = "GET_CORRELATION";
    private static final String CMD_PREDICTION = "GET_PREDICTION";

    public static void main(String[] args) {
        try (
                DatagramSocket datagramSocket = new DatagramSocket();
                Scanner scanner = new Scanner(System.in)
        ) {
            InetAddress serverAddress = InetAddress.getByName(SERVER_HOST);
            System.out.println("UDP Client connected to the server at " + SERVER_HOST + ":" + SERVER_PORT);

            boolean running = true;
            while (running) {
                // Display available options
                System.out.println("\nMENU:");
                System.out.println("1. Get team list");
                System.out.println("2. Get correlation between statistics");
                System.out.println("3. Get prediction of the match");
                System.out.println("4. Exit");
                System.out.print("Choose option: ");

                String choice = scanner.nextLine();
                String request = "";

                switch (choice) {
                    case "1":
                        //Get available teams list command
                        request = CMD_TEAMS;
                        break;

                    case "2":
                        //Get correlation between statistics
                        request = CMD_CORRELATION;
                        break;

                    case "3":
                        //Get prediction winner for hometeam|awayteam format
                        System.out.print("Enter home team name: ");
                        String homeTeam = scanner.nextLine();
                        System.out.print("Enter away team name: ");
                        String awayTeam = scanner.nextLine();
                        request = CMD_PREDICTION + "|" + homeTeam + "|" + awayTeam;
                        break;

                    case "4":
                        //Exit the program
                        running = false;
                        System.out.println("Exiting...");
                        continue;

                    default:
                        //Invalid command
                        System.out.println("Invalid choice. Try again.");
                        continue;
                }

                // Send request to server
                byte[] sendData = request.getBytes();
                DatagramPacket sendPacket = new DatagramPacket(
                        sendData, sendData.length, serverAddress, SERVER_PORT
                );
                datagramSocket.send(sendPacket);
                //Confirmation of sending the request to server
                System.out.println("Request sent: " + request);

                // Receive response from server
                byte[] receiveData = new byte[BUFFER_SIZE];
                DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                datagramSocket.receive(receivePacket);

                // Deserialize and display response from server
                try {
                    byte[] serializedData = new byte[receivePacket.getLength()];
                    System.arraycopy(receivePacket.getData(), 0, serializedData, 0, receivePacket.getLength());

                    List<String> result = Serializer.getDeserializedForm(serializedData);

                    for (String line : result) {
                        System.out.println(line);
                    }
                } catch (Exception e) {
                    System.err.println("Error processing response: " + e.getMessage());
                }
            }

            //End the connection
            System.out.println("Client terminated.");

        } catch (IOException e) {
            //Exception error
            System.err.println("Client error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}