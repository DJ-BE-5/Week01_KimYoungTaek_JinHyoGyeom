package TicTacToe;

import java.io.*;
import java.net.Socket;

public class TicTacToeGame {
    private char[][] board = {
            {' ', ' ', ' '},
            {' ', ' ', ' '},
            {' ', ' ', ' '}
    };

    private char currentPlayer = 'O';
    private Socket player1Socket;
    private Socket player2Socket;
    private PrintWriter player1Writer;
    private PrintWriter player2Writer;

    public TicTacToeGame(Socket player1Socket, Socket player2Socket) {
        this.player1Socket = player1Socket;
        this.player2Socket = player2Socket;

        try {
            player1Writer = new PrintWriter(player1Socket.getOutputStream(), true);
            player2Writer = new PrintWriter(player2Socket.getOutputStream(), true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void startGame() {
        try {
            // 두 플레이어에게 게임 시작 메시지 전송
            PrintWriter player1Writer = new PrintWriter(player1Socket.getOutputStream(), true);
            PrintWriter player2Writer = new PrintWriter(player2Socket.getOutputStream(), true);

            player1Writer.println("Game is starting. You are Player 1 (O).");
            player2Writer.println("Game is starting. You are Player 2 (X).");

            // 게임 진행
            // TicTacToeGame 클래스의 startGame() 메서드 내 수정

            while (true) {
                showCurrentBoardToPlayers(); // 게임판 상태를 플레이어에게 보여줌

                if (checkWinner()) {
                    showCurrentBoardToPlayers(); // 게임이 끝났을 때 최종 상태를 보여줌
                    player1Writer.println("Player " + currentPlayer + " wins!");
                    player2Writer.println("Player " + currentPlayer + " wins!");
                    break;
                }

                switchPlayer();

                // 플레이어에게 현재 차례를 알림
                if (currentPlayer == 'O') {
                    player1Writer.println("Your move (row column): ");
                    player2Writer.println("Waiting for opponent's move...");
                    int[] move = getPlayerMove(player1Socket);
                    updateBoard(move[0], move[1]); // 보드 업데이트
                } else {
                    player1Writer.println("Waiting for opponent's move...");
                    player2Writer.println("Your move (row column): ");
                    int[] move = getPlayerMove(player2Socket);
                    updateBoard(move[0], move[1]); // 보드 업데이트
                }
            }


            // 게임 종료 후 소켓 닫기
            player1Socket.close();
            player2Socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendBoardToPlayers(PrintWriter player1Writer, PrintWriter player2Writer) {
        player1Writer.println("Current Board:");
        player1Writer.println(getFormattedBoard());

        player2Writer.println("Current Board:");
        player2Writer.println(getFormattedBoard());
    }

    public String getFormattedBoard() {
        StringBuilder formattedBoard = new StringBuilder();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                formattedBoard.append("[")
                        .append((board[i][j] == ' ') ? ' ' : board[i][j])
                        .append("] ");
            }
            formattedBoard.append("\n");
        }
        return formattedBoard.toString();
    }
    private int[] getPlayerMove(Socket playerSocket) {
        int[] move = new int[2];
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(playerSocket.getInputStream()));
            String moveInput = reader.readLine();
            String[] coordinates = moveInput.split(" ");
            move[0] = Integer.parseInt(coordinates[0]) - 1;
            move[1] = Integer.parseInt(coordinates[1]) - 1;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return move;
    }

    public void showCurrentBoardToPlayers() {
        sendBoardToPlayers(player1Writer, player2Writer);
    }
    private void updateBoard(int row, int col) {
        if (row >= 0 && row < 3 && col >= 0 && col < 3 && board[row][col] == ' ') {
            board[row][col] = currentPlayer;
        }
    }

    private void switchPlayer() {
        currentPlayer = (currentPlayer == 'O') ? 'X' : 'O';
    }

    private boolean checkWinner() {
        for (int i = 0; i < 3; i++) {
            if (checkRow(i) || checkColumn(i)) {
                return true;
            }
        }

        return checkDiagonals();
    }

    private boolean checkRow(int row) {
        return (board[row][0] == currentPlayer &&
                board[row][1] == currentPlayer &&
                board[row][2] == currentPlayer);
    }

    private boolean checkColumn(int col) {
        return (board[0][col] == currentPlayer &&
                board[1][col] == currentPlayer &&
                board[2][col] == currentPlayer);
    }

    private boolean checkDiagonals() {
        return (board[0][0] == currentPlayer && board[1][1] == currentPlayer && board[2][2] == currentPlayer) ||
                (board[0][2] == currentPlayer && board[1][1] == currentPlayer && board[2][0] == currentPlayer);
    }
}
