package space.beduino.tools;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;

/**
 * Created by stefan on 05.01.17.
 * Contributor: Tom
 */
public class UdpDiscoverySender {

    private static final int PORT = 65527;
    private static final String MCAST_ADDR = "FF02::1";
    private static final int SLEEP_DELAY_IN_SECONDS = 10;
    private InetAddress GROUP;
    private Thread thread;
    private boolean running;

    public UdpDiscoverySender() {
        try {
            GROUP = InetAddress.getByName(MCAST_ADDR);
        } catch (UnknownHostException uhe) {
            System.out.println("Error with Multicast Address");
        }
    }

    public void start() {
        running = true;
        thread = sender();
        thread.start();
    }

    public void stop() {
        running = false;
    }

    private Thread sender() {
        return new Thread(new Runnable() {
            @Override
            public void run() {
                DatagramSocket serverSocket = null;
                try {
                    serverSocket = new DatagramSocket();
                    try {
                        while (running) {
                            System.out.println("Sending discovery package");

                            /**
                             * Discovery Data (prefix + mac adress + port) = 15 Byte
                             * Prefix (beduino) = 01100010 01100101 01100100 01110101 01101001 01101110 01101111
                             * Mac Address (00:80:41:ae:fd:7e) = 00000000 10000000 01000001 10101110 11111101 01111110
                             * Port (65527) = 11111111 11110111
                             */

                            byte[] prefixAsByte = "beduino".getBytes();
                            byte[] macAsByte = CoapTestThing.MAC_ADDRESS;
                            byte[] portAsByte = ByteBuffer.allocate(4).putInt(CoapTestThing.COAP_PORT).array();

                            byte[] sendData = new byte[15];
                            System.arraycopy(prefixAsByte, 0, sendData, 0, 7);
                            System.arraycopy(macAsByte, 0, sendData, 7, 6);
                            System.arraycopy(portAsByte, 2, sendData, 13, 2);

                            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, GROUP, PORT);

                            System.out
                                    .println("discovery package contents: " + byteArrayToString(sendPacket.getData()));

                            serverSocket.send(sendPacket);
                            Thread.sleep(SLEEP_DELAY_IN_SECONDS * 1000L);
                        }
                    } catch (Exception e) {
                        System.out.println("ERROR: " + e.getMessage());
                    }
                } catch (Exception e) {
                    System.out.println("ERROR: " + e.getMessage());
                }
            }
        });
    }

    private String byteArrayToString(byte[] array) {
        String out = "";
        for (byte value : array) {
            out += Integer.toHexString(value & 0xff) + " ";
        }
        return out;
    }

}
