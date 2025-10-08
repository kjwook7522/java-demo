import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class SocketServer {
    public static List<PrintWriter> clientOutputs = new ArrayList<>();
    public static volatile boolean running = true;

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(8080);
        System.out.println("Start Server on 8080 port!");
        System.out.println("Wating for client...");

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            running = false;

            synchronized (clientOutputs) {
                for (PrintWriter out : clientOutputs) {
                    out.close();
                }
            }
        }));

        while (running) {
            Socket clientSocket = serverSocket.accept();
            System.out.println("New Connection is created: " + clientSocket.getLocalAddress());
            new Thread(new ClientHandler(clientSocket)).start();
        }

        serverSocket.close();
        System.out.println("Server is closed");
    }

    public static class ClientHandler implements Runnable {
        private Socket socket;
        private PrintWriter out;
        private BufferedReader in;

        public ClientHandler(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try {
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream(), true);

                synchronized (clientOutputs) {
                    clientOutputs.add(out);
                }

                String message;
                while ((message = in.readLine()) != null) {
                    System.out.println("recv message: " + message);
                    broadcast(message);
                }
            } catch (IOException e) {
                System.out.println("Closed to client connection");
            } finally {
                try {
                    socket.close();
                } catch (IOException e) {
                }

                synchronized (clientOutputs) {
                    clientOutputs.remove(out);
                }
            }
        }

        private void broadcast(String message) {
            synchronized (clientOutputs) {
                for (PrintWriter writer : clientOutputs) {
                    writer.println(message);
                }
            }
        }
    }
}
