package chatapp;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashSet;

/**
 *
 * @author Darag
 */
public class Server extends Thread {

    Socket socket;
    private DataInputStream in;
    private DataOutputStream out;
    private static ArrayList<DataOutputStream> sendMessages = new ArrayList<>(); // contains out of client 1 , contains client 2 
    private static ArrayList<String> names = new ArrayList<String>();

    private String name;

    // client 1 connection 
    // client 

    public Server(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());
            name = in.readUTF();
            String clientsConnected = "";
            for (String s : names) {
                clientsConnected += s + " is active \n";
            }
            if (clientsConnected.equals("")) {
                clientsConnected += "Lucky You , You're the first to connect";
            }
            out.writeUTF(clientsConnected);

            broadcast(name + " has Arrived");

            names.add(name);

            sendMessages.add(out);

            while (true) {
                String input = in.readUTF();
                broadcast(name + ": " + input);
            }

        } catch (IOException ex) {
            System.out.println("Client " + name + " Disconnected");
        } finally {
            // This client is going down!  Remove its name and its print
            // writer from the sets, and close its socket.
            if (name != null) {
                names.remove(name);
            }
            if (out != null) {
                sendMessages.remove(out);
            }
            try {
                socket.close();
                broadcast("Client " + name + " Disconnected");
            } catch (IOException e) {
            }
        }
    }

    private void broadcast(String message) {
        try {
            for (DataOutputStream send : sendMessages) {
                send.writeUTF(message);
            }
        } catch (Exception ex) {
            System.out.println("error");
        }
    }

    public static void main(String[] args) {
        try {
            ServerSocket server = new ServerSocket(9090);
            while (true) {
                try {
                    Socket socket = server.accept();// clinent 1 connect 
                    Server thread = new Server(socket);
                    thread.start();

                } catch (IOException ex) {
                    System.out.println("Error" + ex.getMessage());
                }
            }
        } catch (IOException ex) {
            System.out.println("Fatal Error" + ex.getMessage());
        }
    }
}
