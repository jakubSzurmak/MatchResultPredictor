package pg.gda.edu.lsea.network.services.servers;

import pg.gda.edu.lsea.dataHandlers.utils.Serializer;
import pg.gda.edu.lsea.network.services.ParseDataService;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

/**
 * UDP Server that receives and processes datagrams from clients for football data analysis purposes
 */
public class UDPServer {
    private static final String WELCOME_MESSAGE = "UDP Server has started. Waiting for connection with the client";
    private static final int SERVER_PORT = 7777;
    private static final int BUFFER_SIZE = 8192;

    // Command identifiers
    private static final String CMD_TEAMS = "GET_TEAMS";
    private static final String CMD_CORRELATION = "GET_CORRELATION";
    private static final String CMD_PREDICTION = "GET_PREDICTION";

    public static void main(String[] args) {
        DatagramSocket datagramSocket = null;
        ParseDataService dataService = null;

        try {
            //Initialize parser and parse data
            dataService = new ParseDataService();

            // Create datagram socket
            datagramSocket = new DatagramSocket(SERVER_PORT);
            byte[] buffer = new byte[BUFFER_SIZE];

            System.out.println(WELCOME_MESSAGE);

            while (true) {
                // Receive request
                DatagramPacket receivePacket = new DatagramPacket(buffer, buffer.length);
                datagramSocket.receive(receivePacket);

                // Get client port
                InetAddress clientAddress = receivePacket.getAddress();
                int clientPort = receivePacket.getPort();

                // Get request from client
                String request = new String(receivePacket.getData(), 0, receivePacket.getLength());
                System.out.println("Received request: " + request);

                // Empty buffer for commands from client
                byte[] responseData = null;

                if (request.startsWith(CMD_TEAMS)) {
                    // Get teams list command
                    responseData = dataService.getTeamsList();
                } else if (request.startsWith(CMD_CORRELATION)) {
                    // Get correlation command
                    responseData = dataService.getCorrelationData();
                } else if (request.startsWith(CMD_PREDICTION)) {
                    // Get prediction command
                    String[] parts = request.split("\\|");
                    if (parts.length == 3) {
                        String homeTeam = parts[1];
                        String awayTeam = parts[2];
                        responseData = dataService.getPredictionData(homeTeam, awayTeam);
                    } else {
                        List<String> error = new ArrayList<>();
                        error.add("Invalid prediction format. Use: GET_PREDICTION|HomeTeam|AwayTeam");
                        responseData = Serializer.getSerializedForm(error);
                    }
                } else {
                    // Invalid input - error
                    List<String> error = new ArrayList<>();
                    error.add("Unknown command. Valid commands: GET_TEAMS, GET_CORRELATION, GET_PREDICTION|HomeTeam|AwayTeam");
                    responseData = Serializer.getSerializedForm(error);
                }

                // Send response to client
                DatagramPacket sendPacket = new DatagramPacket(
                        responseData, responseData.length, clientAddress, clientPort
                );
                datagramSocket.send(sendPacket);
                //Confirmation message of sending response
                System.out.println("Response sent to client " + clientAddress + ":" + clientPort);
            }
        } catch (IOException e) {
            //Error if exception
            System.err.println("Server error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            if (datagramSocket != null && !datagramSocket.isClosed()) {
                datagramSocket.close();
                System.out.println("Server socket closed");
            }
        }
    }
}