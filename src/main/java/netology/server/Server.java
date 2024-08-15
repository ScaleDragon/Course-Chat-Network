package netology.server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static netology.serviceServer.NamePort.getNamePort;


public class Server {
    private static int PORT;

    private static final ServerLogger logger2 = new ServerLogger("server_log.txt");
    private static final CopyOnWriteArrayList<Socket> clientsList = new CopyOnWriteArrayList<>();
    private static String nameUser = null;

    static {
        try {
            PORT = getNamePort("src/main/resources/settings.txt");
        } catch (Exception e) {
            e.fillInStackTrace();
        }
    }

    public static void main(String[] args) {

        while (true) {
            try (ServerSocket serverSocket = new ServerSocket(PORT)) {
                System.out.println("Starting the server");                           //Запуск сервера
                logger2.logMessage("The server is running on the port: " + PORT);   //Сервер запущен на порту

                {
                    Socket clientSocket = serverSocket.accept();
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try (PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                                 BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {

                                String message = " ";
                                while (true) {

                                    message = in.readLine();

                                    out.println(String.format(message));  //Сообщение от

                                    logger2.logMessage(String.format(message));
                                }

                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }

                        }
                    }).start();

                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }


        }

    }


}

