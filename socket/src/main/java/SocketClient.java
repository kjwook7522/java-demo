import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class SocketClient {
    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("localhost", 8080);
        System.out.println("Successfuly connect to server on 8080 port");

        // 메세지 받기 쓰레드 시작
        new Thread(new MessageHandler(socket)).start();

        // 사용자가 입력한 메시지를 서버로 보내기
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));
        String input;
        while ((input = userInput.readLine()) != null) {
            out.println(input);
        }

        socket.close();
    }

    public static class MessageHandler implements Runnable {
        private Socket socket;
        private BufferedReader in;

        public MessageHandler(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try {
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                String message;
                while ((message = in.readLine()) != null) {
                    System.out.println(message);
                }
            } catch (IOException e) {
                System.out.println("Connection closed");
            }
        }
    }
}
