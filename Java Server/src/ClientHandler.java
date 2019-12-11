
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;

public class ClientHandler extends Thread {
    Socket client;
    DataInputStream dataInputStream;
    DataOutputStream dataOutputStream;
    String string;

    public ClientHandler(Socket clientSocket, String type){
        client = clientSocket;
        string = type;
        try {
            dataInputStream = new DataInputStream(client.getInputStream());
            dataOutputStream = new DataOutputStream(client.getOutputStream());
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
                received = dataInputStream.readUTF();
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

        try {
            this.dataInputStream.close();
            this.dataOutputStream.close();
        }catch(IOException e){
            e.printStackTrace();
        }

    }
}
