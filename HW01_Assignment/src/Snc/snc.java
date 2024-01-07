package Snc;

import java.io.*;
import java.net.*;

public class snc {
    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println("Usage: snc [option] [hostname] [port]");
            System.exit(1);
        }

        if (args[0].equals("-l")) {
            // Server Mode
            int serverPort = Integer.parseInt(args[1]);
            runServer(serverPort);
        } else {
            // Client Mode
            String hostname = args[0];
            int serverPort = Integer.parseInt(args[1]);
            runClient(hostname, serverPort);
        }
    }

    private static void runServer(int serverPort) {
        try {
            ServerSocket serverSocket = new ServerSocket(serverPort);
            System.out.println("Server로 동작");

            // 하나의 연결만 허용
            Socket clientSocket = serverSocket.accept();

            BufferedReader fromClient = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter toClient = new PrintWriter(clientSocket.getOutputStream(), true);

            // Server에서 받은 데이터를 표준 출력으로 내보냄
            // 동시에 클라이언트로 출력
            while (true) {
                String received = fromClient.readLine();
                if (received == null) {
                    break;
                }
                System.out.println("Received from client: " + received);

                // 서버에서 받은 데이터를 클라이언트에 출력
                toClient.println(received);
            }

            serverSocket.close();
        } catch (IOException e) {
            System.err.println("Server에서 에러 발생: " + e.getMessage());
        }
    }


    private static void runClient(String hostname, int serverPort) {
        try {
            Socket clientSocket = new Socket(hostname, serverPort);
            System.out.println("Client로 동작");

            BufferedReader fromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter toServer = new PrintWriter(clientSocket.getOutputStream(), true);
            BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));

            // 표준 입력을 받아 server로 전송
            // 동시에 서버에서 받은 데이터를 출력
            while (true) {
                String input = userInput.readLine();
                if (input == null) {
                    break;
                }
                toServer.println(input);

                // 서버에서 받은 데이터를 출력
                String received = fromServer.readLine();
                if (received != null) {
                    System.out.println("Received from server: " + received);
                }
            }

            clientSocket.close();
        } catch (IOException e) {
            System.err.println("Client에서 에러 발생: " + e.getMessage());
        }
    }

}