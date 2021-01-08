package connection;

import cms.service;
import com.google.gson.JsonObject;
import sercure.AES;
import sercure.RSA;

import java.net.InetSocketAddress;
import java.security.*;

import cms.dao;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;

public class Server {
    int clientId = 0;
    int port;
    public static Map<String, clientThread> cThread;


    public Server(int port) {
        this.port = port;
    }

    public static void main(String[] args) throws IOException, GeneralSecurityException {
//        RSA rsa = new RSA();
//        rsa.createRSA();
        int port = 8002;
        cThread = new HashMap<>();
        dao.getListAccount("offline");
        Server server = new Server(port);
        server.start();
    }

    void start() throws IOException {
        ServerSocket serverSocket = new ServerSocket(port);
        System.out.print("Receiver listening on the port " + port + ".");
        try {
//            new getInput().start();
            while (true) {
                Socket socket = serverSocket.accept();  // accepting the connection.
                String ip = (((InetSocketAddress) socket.getRemoteSocketAddress()).getAddress()).toString().replace("/", "");
                System.out.println(">>IP: " + ip);
                System.out.println(">>Accept client from IP: " + socket.getRemoteSocketAddress().toString());
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

            try {
                sendToClient(new message(RSA.getPublicKey()));
            } catch (Exception ex) {
                System.out.println("Error send public key to client " + clientIp);
            }

            while (true) {
                try {
                    m = (message) sInput.readObject();

                } catch (ClassNotFoundException e) {
                    System.out.println("Class not found while reading the message object");
                    return;
                } catch (IOException e) {
                    e.printStackTrace();
                    return;
                }

                if (i == 0) {
                    if (m.getData() != null) {
                        byte[] decryptedMessage = RSA.decryptMessage(m.getData());
                        System.out.println(">>decryptedMessage:" + decryptedMessage);
                        AESKey = new SecretKeySpec(decryptedMessage, "AES");
                        System.out.println();
                        i++;
                    } else {
                        System.out.println("Error in decrypting AES key in clientThread.run()");
                        System.exit(1);
                    }
                } else {
                    if (m.getData() != null) {
//                        showMessage(clientId, clientIp, m.getData(), AESKey);
                        byte[] msg = AES.decryptMessage(m.getData(), AESKey);
                        JsonObject rs = service.route(msg, clientIp);
                        System.out.println(">>rs> \n" + rs.toString());
                        try {
                            message toSend = null;
                            byte[] encryptedmessage = AES.encryptMessage(rs.toString(), AESKey);
                            toSend = new message(encryptedmessage);
                            sendToClient(toSend);
                        } catch (Exception ex) {
                        }
                    }
                }
            }
        }

        public synchronized void sendToClient(message ms) throws IOException {
            sOutput.writeObject(ms);
            sOutput.reset();
        }
    }

    class getInput extends Thread {

        public void run() {
            while (true) {
                try {
                    System.out.println("Sever: Enter OUTGOING  message : > ");
                    Scanner sc = new Scanner(System.in);
                    String s = sc.nextLine();

                    int p = s.indexOf(':');
                    String id = s.substring(0, p);
                    s = s.substring(p + 1);

                    clientThread t = cThread.get(id);
                    if (t != null) {
                        message toSend = null;
                        byte[] encryptedmessage = AES.encryptMessage(s, t.AESKey);
                        toSend = new message(encryptedmessage);
                        t.sendToClient(toSend);
                    } else {
                        System.out.println("No thread " + clientId + " found!");
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("No message sent to server");
                }
            }
        }
    }

    private void showMessage(int clientId, String clientIp, byte[] encryptedMessage, SecretKey AESKey) {
        try {
            byte[] msg = AES.decryptMessage(encryptedMessage, AESKey);
            System.out.println("Server: INCOMING Message From CLIENT >> " + clientId + "(" + clientIp + "): " + new String(msg));
            System.out.println("Sever: Enter OUTGOING  message : > ");
        } catch (Exception e) {
            e.getCause();
            e.printStackTrace();
            System.out.println("Exception genereated in decryptData method. Exception Name  :" + e.getMessage());
        }
    }
}