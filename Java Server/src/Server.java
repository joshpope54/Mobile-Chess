import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TreeMap;

public class Server {
    private static String serverString = "[SERVER] ";
    private static int threadCount;
    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(5560);
            System.out.println(serverString + "Started");
            System.out.println(serverString + "Awaiting first client connection");

            while(true){
                Socket s = null;
                try {
                    s = serverSocket.accept();

                    System.out.println(serverString + "New Client Connection" + s);

                    DataInputStream dataInputStream = new DataInputStream(s.getInputStream());
                    DataOutputStream dataOutputStream = new DataOutputStream(s.getOutputStream());
                    Thread t = new ClientHandler(s, dataInputStream, dataOutputStream);
                    t.start();
                    threadCount++;
                    System.out.println(serverString + "Creating new thread for client connection: THREADID:" + t.getId());
                    System.out.println(serverString + "Player count : " + threadCount);


                }catch (Exception e){
                    s.close();
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

class ClientHandler extends Thread
{
    DateFormat fordate = new SimpleDateFormat("yyyy/MM/dd");
    DateFormat fortime = new SimpleDateFormat("hh:mm:ss");
    final DataInputStream dis;
    final DataOutputStream dos;
    final Socket s;


    // Constructor
    public ClientHandler(Socket s, DataInputStream dis, DataOutputStream dos)
    {
        this.s = s;
        this.dis = dis;
        this.dos = dos;
    }

    @Override
    public void run()
    {
        String received;
        String toreturn;
        while (true)
        {
            try {
                dos.writeUTF("What do you want?[Date | Time]..\n"+
                        "Type Exit to terminate connection.");

                received = dis.readUTF();

                if(received.equals("Exit"))
                {
                    System.out.println("Client " + this.s + " sends exit...");
                    System.out.println("Closing this connection.");
                    this.s.close();
                    System.out.println("Connection closed");
                    break;
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try
        {
            // closing resources
            this.dis.close();
            this.dos.close();

        }catch(IOException e){
            e.printStackTrace();
        }
    }
}