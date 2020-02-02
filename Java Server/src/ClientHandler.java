
import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class ClientHandler extends Thread {
    Socket client;
    Scanner dataInputStream;
    PrintWriter dataOutputStream;
    String string;

    public ClientHandler(Socket clientSocket, String type){
        client = clientSocket;
        string = type;
        try {
            dataInputStream = new Scanner(client.getInputStream());
            dataOutputStream = new PrintWriter(client.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        super.run();
        String received;
        try{
            while(true){
                received = dataInputStream.nextLine();
                System.out.println(received);
            }
        } catch (Exception e) {
            try {
                client.close();
            } catch (IOException e1) {
            }
        }finally {
            System.out.println(string + " Client " + this.client + " sends exit...");
            System.out.println(string + " Closing this connection.");
            System.out.println(string + " Connection closed");
        }

    }
}
