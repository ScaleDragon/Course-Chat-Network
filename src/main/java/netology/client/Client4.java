package netology.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static netology.serviceServer.NamePort.getNamePort;


public class Client4 {
    //    private final Socket clientSocket;
    private static int PORT;
    private static String name;
    static String messageUser = "";
    private static ClientLogger logger2 = new ClientLogger("client_log.txt");
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_BLUE = "\u001B[34m";
    private static final List<String> clientsList = new ArrayList<>();
    public static final String ANSI_RESET = "\u001B[0m";

    static {
        try {
            PORT = getNamePort("src/main/resources/settings.txt");
        } catch (Exception e) {
            e.fillInStackTrace();
        }
    }

    //    @Override
//    public void run() {
    public static void main(String[] args) {


        System.out.println("Welcome to the chat.\nTo exit the chat use 'exit'");   //Добро пожаловать в чат.Для выхода из чата используйте 'exit'
        logger2.logMessage("Добро пожаловать в чат.\n Для выхода из чата используйте 'exit'");

        try {
            Scanner scanner = new Scanner(System.in);
            Socket clientSocket = new Socket("127.0.0.1", PORT);

            logger2.logMessage("Соединение с сервером установлено.");

            new Thread(new Runnable() {
                @Override
                public void run() {
                    try (PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                         BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {

                        String answerServer;
                        if (name == null) {
                            System.out.print("Create a name for the chat: ");

                            name = scanner.nextLine();
                            out.println(ANSI_BLUE + name);   // отправка имени пользователя

                            answerServer = in.readLine();                // Подключен новый пользователь имя
                            clientsList.add(answerServer);
                            for (String socker : clientsList) {
                                out.println("Подключен новый пользователь [" + name + "]");
                            }

                            answerServer = in.readLine();
                            logger2.logMessage(ANSI_RESET + answerServer);         //Кол-во пользователей в чате:
                            System.out.println(answerServer);
                        }

                        while (!messageUser.equals("exit")) {
                            System.out.println("Write a message: ");
                            messageUser = scanner.nextLine();

                            if (messageUser.equals("exit")) {
                                out.println(ANSI_RED + "Пользователь [" + name + "] покинул чат.");
                                logger2.logMessage(ANSI_RESET + "Пользователь [" + name + "] покинул чат.");
                                clientSocket.close();
                                break;
                            }
                            out.println("Отправлено сообщение: " + messageUser);      // исходящее сообщение
                            logger2.logMessage(ANSI_RESET + messageUser);

                            answerServer = in.readLine();
                            System.out.println(answerServer);                            // входящее сообщение
                            logger2.logMessage(ANSI_RESET + "Отправлено сообщение: " + messageUser);

                        }


                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }

                }


            }).start();

        } catch (IOException e) {
//            logger2.logMessage("Пользователь [" + name + "] покинул чат.");
            throw new RuntimeException(e);
        }

    }


}
