import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class ServerChat {
    ArrayList clientOutputStream;
    private int PORT = 4242;

    public static void main(String args[]){
        new ServerChat().go();
    }

    private void go(){
        clientOutputStream = new ArrayList();
        try{
            ServerSocket serverSocket = new ServerSocket(PORT);
            while (true){
                Socket clientSocket = serverSocket.accept();
                PrintWriter writer = new PrintWriter(clientSocket.getOutputStream());
                clientOutputStream.add(writer);

                new Thread(new ClientHandler(clientSocket)).start();
                System.out.println("connection established");
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    private void tell(String message){
        Iterator it = clientOutputStream.iterator();
        while(it.hasNext()){
            try{
                PrintWriter writer = (PrintWriter) it.next();
                writer.println(message);
                writer.flush();
            } catch(Exception ex){
                ex.printStackTrace();
            }
        }
    }

    private class ClientHandler implements Runnable{
        BufferedReader reader;
        Socket socket;

        public ClientHandler(Socket socket){
            try{
                this.socket = socket;
                this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            }
            catch (Exception ex){
                ex.printStackTrace();
            }
        }

        @Override
        public void run() {
            String message;
            try{
                while ((message = reader.readLine()) != null){
                    System.out.println("[read] "+message);
                    tell(message);
                }
            } catch (Exception ex){
                ex.printStackTrace();
            }
        }
    }
}
