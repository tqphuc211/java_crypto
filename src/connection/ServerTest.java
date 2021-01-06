package connection;

import sercure.AES;
import sercure.RSA;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.GeneralSecurityException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;


public class ServerTest {
    int clientId = 0;
    int port;
    public static Map<String, clientThread> cThread;


    public ServerTest(int port) {
        this.port = port;
    }

    public static void main(String[] args) throws IOException, GeneralSecurityException {
//        RSA rsa = new RSA();
//        rsa.createRSA();
        int port = 8002;
        cThread = new HashMap<>();
        ServerTest server = new ServerTest(port);
        server.start();
    }

    void start() throws IOException {
        ServerSocket serverSocket = new ServerSocket(port);
        System.out.print("Receiver listening on the port " + port + ".");
        try {
            while (true) {
                Socket socket = serverSocket.accept();  // accepting the connection.
                String ip = (((InetSocketAddress) socket.getRemoteSocketAddress()).getAddress()).toString().replace("/", "");
//                System.out.println(">>IP: " + ip);
//                System.out.println(">>Accept client from IP: " + socket.getRemoteSocketAddress().toString());
                clientThread t = new clientThread(socket, ++clientId, ip);
                t.start();
                cThread.put(clientId + "", t);
            }
        } finally {
            serverSocket.close();
        }
    }

    class clientThread extends Thread {
        private ObjectOutputStream sOutput;
        private ObjectInputStream sInput;
        Socket socket;
        int i = 0;
        private int clientId;
        private String clientIp;
        public SecretKey AESKey;

        clientThread(Socket socket, int clientId, String clientIp) throws IOException {
            this.socket = socket;
            this.clientId = clientId;
            this.clientIp = clientIp;
            sOutput = new ObjectOutputStream(socket.getOutputStream());
            sInput = new ObjectInputStream(socket.getInputStream());
        }

        public void run() {
            message m;
            while (true) {
                try {
                    m = (message) sInput.readObject();

                } catch (ClassNotFoundException e) {
//                    System.out.println("Class not found while reading the message object");
                    return;
                } catch (IOException e) {
                    e.printStackTrace();
                    return;
                }

//                if (i == 0) {
//                    if (m.getData() != null) {
//                        byte[] decryptedMessage = RSA.decryptMessage(m.getData());
////                        System.out.println(">>decryptedMessage:" + decryptedMessage);
//                        AESKey = new SecretKeySpec(decryptedMessage, "AES");
//                        System.out.println();
//                        i++;
//                    } else {
//                        System.out.println("Error in decrypting AES key in clientThread.run()");
//                        System.exit(1);
//                    }
//                } else {
//                    if (m.getData() != null) {
//                        System.out.println("receive message");
//                    }
//                }
            }
        }
    }
}