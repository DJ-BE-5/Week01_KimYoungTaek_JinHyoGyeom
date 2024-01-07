package TicTacToe;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class TicTacToeClient2 {
    public static void main(String[] args) {
        try {
            Socket socket = new Socket("localhost",5555);
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);

            Scanner scanner = new Scanner(System.in);

            // 서버로부터 초기 메시지 수신 및 출력
            String initialMessage = reader.readLine();
            System.out.println(initialMessage);

            while (true) {
                String boardMessage = reader.readLine();
                System.out.println(boardMessage);

                // 현재 차례일 때만 플레이어의 입력을 받음
                if (boardMessage.contains("Your move")) {
                    System.out.print("Enter your move (row column): ");
                    String move = scanner.nextLine();
                    writer.println(move);
                }

                // 게임 종료 메시지 수신 후 종료
                if (boardMessage.contains("wins!")) {
                    break;
                }
            }

            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
