package server;

import common.ChatLogger;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class ChatServer {

    public static final int PORT = 5000;

    private final Set<ClientHandler> clients =
            ConcurrentHashMap.newKeySet();

    private volatile boolean running = true;

    public void start() {

        System.out.println("=================================");
        System.out.println("       JAVA CHAT SERVER");
        System.out.println("=================================");

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {

            System.out.println("Server started successfully.");
            System.out.println("Server is running on port: " + PORT);
            System.out.println("Waiting for clients...");
            System.out.println();

            ChatLogger.log("Server started on port " + PORT);

            while (running) {

                Socket clientSocket = serverSocket.accept();

                System.out.println(
                        "New connection from: "
                                + clientSocket.getInetAddress()
                );

                ClientHandler clientHandler =
                        new ClientHandler(clientSocket, this);

                clients.add(clientHandler);

                Thread clientThread =
                        new Thread(clientHandler);

                clientThread.start();
            }

        } catch (IOException e) {

            if (running) {
                System.err.println(
                        "Server error: " + e.getMessage()
                );
            }
        }
    }

    public void broadcast(
            String message,
            ClientHandler sender) {

        for (ClientHandler client : clients) {

            if (client != sender) {
                client.sendMessage(message);
            }
        }

        System.out.println(message);
        ChatLogger.log(message);
    }

    public boolean sendPrivateMessage(
            String targetUsername,
            String message,
            ClientHandler sender) {

        for (ClientHandler client : clients) {

            if (client.getUsername() != null
                    && client.getUsername()
                    .equalsIgnoreCase(targetUsername)) {

                client.sendMessage(
                        "[Private from "
                                + sender.getUsername()
                                + "] "
                                + message
                );

                sender.sendMessage(
                        "[Private to "
                                + targetUsername
                                + "] "
                                + message
                );

                ChatLogger.log(
                        "[PRIVATE] "
                                + sender.getUsername()
                                + " -> "
                                + targetUsername
                                + ": "
                                + message
                );

                return true;
            }
        }

        return false;
    }

    public boolean isUsernameTaken(String username) {

        for (ClientHandler client : clients) {

            if (client.getUsername() != null
                    && client.getUsername()
                    .equalsIgnoreCase(username)) {

                return true;
            }
        }

        return false;
    }

    public String getActiveUsers() {

        StringBuilder users =
                new StringBuilder("Active users: ");

        boolean first = true;

        for (ClientHandler client : clients) {

            if (client.getUsername() != null) {

                if (!first) {
                    users.append(", ");
                }

                users.append(client.getUsername());

                first = false;
            }
        }

        if (first) {
            return "No active users.";
        }

        return users.toString();
    }

    public void removeClient(
            ClientHandler clientHandler) {

        clients.remove(clientHandler);
    }

    public static void main(String[] args) {

        ChatServer server =
                new ChatServer();

        server.start();
    }
}