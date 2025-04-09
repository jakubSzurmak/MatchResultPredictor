package pg.gda.edu.lsea.network.services.servers;

import javax.xml.crypto.Data;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

/**
 * UDP Server that receives and processes datagrams from clients
 */
public class UDPServer {
    private static final String WELCOME_MESSAGE = "UDP Server has started. Waiting for connection with the client";
    private static final int SERVER_PORT = 7777;

    public static void main(String[] args) throws Exception{
        DatagramSocket datagramSocket = new DatagramSocket(SERVER_PORT);
        byte[] buffer = new byte[1024];

        System.out.println(WELCOME_MESSAGE);

        while (true){
            DatagramPacket datagramPacket = new DatagramPacket(buffer, buffer.length);
            datagramSocket.receive(datagramPacket);

            String receivedMessage = new String(datagramPacket.getData(), 0, datagramPacket.getLength());
            System.out.println(receivedMessage);

            byte[] replyData = receivedMessage.getBytes();
            DatagramPacket replyDatagramPacket = new DatagramPacket(replyData, replyData.length,
                    datagramPacket.getAddress(), datagramPacket.getPort());

            datagramSocket.send(replyDatagramPacket);

        }

    }
}
