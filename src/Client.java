import sercure.AES;
import sercure.RSA;

import java.io.*;
import java.net.Socket;
import java.security.*;
import java.util.Scanner;
import javax.crypto.*;

public class Client {

    private ObjectOutputStream sOutput;
    private ObjectInputStream sInput;

    private Socket socket;
    private String server;
    private int port;
    int i = 0;
    message m;
    SecretKey AESkey;

    Client(String server, int port) {
        this.server = server;
        this.port = port;
    }

    public static void main(String[] args) throws IOException, NoSuchAlgorithmException {

        String serverAddress;

        int portNumber = 8002;
        if (args.length < 1) {
            System.out.println("#############################################################");
            System.out.println("# 															 ");
            System.out.println("# Usage: $ java Client [sever ip]							 ");
            System.out.println("# 															 ");
            System.out.println("# e.g. $ java Client 192.168.1.1																 ");
            System.out.println("# 							 								 ");
            System.out.println("# NO ARGUMENT REQUIRED IF SERVER RUNNING ON LOCALHOST		 ");
            System.out.println("# 															 ");
            System.out.println("#############################################################");

            serverAddress = "localhost";
        } else {
            serverAddress = args[0];
        }
        Client client = new Client(serverAddress, portNumber);
        client.AESkey = AES.generateAESkey();
        client.start();
    }

    void start() throws IOException {
        socket = new Socket(server, port);
        System.out.println("connection accepted " + socket.getInetAddress() + " :" + socket.getPort());


        sInput = new ObjectInputStream(socket.getInputStream());
        sOutput = new ObjectOutputStream(socket.getOutputStream());

        new sendToServer().start();
        new listenFromServer().start();
    }

    class listenFromServer extends Thread {
        public void run() {
            while (true) {
                try {
                    m = (message) sInput.readObject();
                    showMessage(m.getData());
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("connection closed");
                    return;
                }
            }
        }
    }

    class sendToServer extends Thread {
        public void run() {
            while (true) {
                try {

                    if (i == 0) {
                        message toSend = null;

                        byte[] encoded_AES_key = RSA.encryptMessage(AESkey.getEncoded());
                        toSend = new message(encoded_AES_key);
                        sOutput.writeObject(toSend);
                        i = 1;
                    } else {

                        System.out.println("CLIENT: Enter OUTGOING message > ");
                        Scanner sc = new Scanner(System.in);
                        String s = sc.nextLine();

                        byte[] encoded_message = AES.encryptMessage(s, AESkey);
                        message toSend = new message(encoded_message);
                        sOutput.writeObject(toSend);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("No message sent to server");
                    break;
                }
            }
        }
    }

    private void showMessage(byte[] encryptedMessage) {
        byte[] msg = AES.decryptMessage(encryptedMessage, AESkey);
        if (msg != null) {
            System.out.println("CLIENT: INCOMING Message From connection.Server   >> " + new String(msg));
            System.out.println("CLIENT: Enter OUTGOING message > ");
        }
    }

    public void closeSocket() {
        try {
            if (sInput != null) sInput.close();
            if (sOutput != null) sOutput.close();
            if (socket != null) socket.close();
        } catch (IOException ioe) {
            System.out.println("Error in Disconnect methd");
        }
    }
}