package pg.gda.edu.lsea.network.services.clients;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Scanner;

/**
 * UDP Client that sends and receives datagram to UDP server
 */
public class UDPClient {
    private static final int SERVER_PORT = 7777;

    public static void main(String[] args) throws Exception{
        DatagramSocket datagramSocket = new DatagramSocket();
        InetAddress address = InetAddress.getByName("localhost");
        Scanner scanner = new Scanner(System.in);

        System.out.println("The client has been connected to the server!");

        while(true){
            String message = scanner.nextLine();
            byte[] buffer = message.getBytes();

            DatagramPacket datagramPacket = new DatagramPacket(buffer, buffer.length, address, SERVER_PORT);
            datagramSocket.send(datagramPacket);

            byte[] replyBuffer = new byte[1024];
            DatagramPacket replyPacket = new DatagramPacket(replyBuffer, replyBuffer.length);
            datagramSocket.receive(replyPacket);

            String reply = new String(replyPacket.getData(), 0, replyPacket.getLength());
            System.out.println("Server has received message: " + reply);
        }


    }
}
