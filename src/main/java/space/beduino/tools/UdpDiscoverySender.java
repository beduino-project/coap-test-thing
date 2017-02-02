package space.beduino.tools;

import java.net.*;

/**
 * Created by stefan on 05.01.17.
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
        }catch (UnknownHostException uhe) {
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
            public void run() {
                DatagramSocket serverSocket = null;
                try {
                    serverSocket = new DatagramSocket();
                    try {
                        while (running) {
                            System.out.println("Sending discovery package");
                            byte[] sendData = "beduino5683".getBytes();
                            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, GROUP, PORT);
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

}
