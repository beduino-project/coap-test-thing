package space.beduino.tools;

import java.io.ByteArrayOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;

import jacob.CborEncoder;

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

                            // needed discovery data
                            String prefix = "beduino";
                            String mac = CoapTestThing.MAC_ADDRESS;
                            int port = CoapTestThing.COAP_PORT;

                            // encode discovery data according to cbor
                            ByteArrayOutputStream sendDataStream = new ByteArrayOutputStream();
                            CborEncoder encoder = new CborEncoder(sendDataStream);
                            encoder.writeArrayStart(3);
                            encoder.writeTextString(prefix);
                            encoder.writeTextString(mac);
                            encoder.writeInt(port);
                            encoder.writeBreak();

                            // build data package and send it
                            byte[] sendData = sendDataStream.toByteArray();
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
            out += Integer.toHexString(value & 0xff) + ":";
        }
        return out;
    }

}
