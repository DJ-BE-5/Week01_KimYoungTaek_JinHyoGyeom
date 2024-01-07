package TicTacToe;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class TicTacToeServer {
    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(5555);
            InetAddress serverAddress = InetAddress.getLocalHost();
            System.out.println("내 서버 IP 주소: " + serverAddress.getHostAddress());
            System.out.println("Tic Tac Toe Server is running. Waiting for players...");

            // 두 명의 플레이어가 참가할 때까지 대기
            Socket player1Socket = serverSocket.accept();
            System.out.println("Player 1 connected.");

            Socket player2Socket = serverSocket.accept();
            System.out.println("Player 2 connected.");

            // 게임 시작
            TicTacToeGame game = new TicTacToeGame(player1Socket, player2Socket);
            game.startGame();

            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
