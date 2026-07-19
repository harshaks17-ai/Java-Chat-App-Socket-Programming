package server;

import common.ChatLogger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class ClientHandler implements Runnable {

    private final Socket socket;
    private final ChatServer server;

    private BufferedReader reader;
    private PrintWriter writer;

    private String username;

    private static final DateTimeFormatter TIME_FORMAT =
            DateTimeFormatter.ofPattern("HH:mm:ss");

    public ClientHandler(
            Socket socket,
            ChatServer server) {

        this.socket = socket;
        this.server = server;
    }

    @Override
    public void run() {

        try {

            reader = new BufferedReader(
                    new InputStreamReader(
                            socket.getInputStream()
                    )
            );

            writer = new PrintWriter(
                    socket.getOutputStream(),
                    true
            );

            setupUsername();

            sendMessage(
                    "Welcome to the Java Chat App, "
                            + username + "!"
            );

            sendMessage(
                    "Commands:"
            );

            sendMessage(
                    "/users - Show active users"
            );

            sendMessage(
                    "/private <username> <message> - Private message"
            );

            sendMessage(
                    "/exit - Leave the chat"
            );

            server.broadcast(
                    "[SERVER] "
                            + username
                            + " joined the chat.",
                    this
            );

            ChatLogger.log(
                    username + " joined the chat."
            );

            String message;

            while ((message = reader.readLine())
                    != null) {

                message = message.trim();

                if (message.isEmpty()) {
                    continue;
                }

                if (message.equalsIgnoreCase("/exit")) {
                    break;
                }

                if (message.equalsIgnoreCase("/users")) {

                    sendMessage(
                            server.getActiveUsers()
                    );

                    continue;
                }

                if (message.startsWith("/private ")) {

                    handlePrivateMessage(message);

                    continue;
                }

                String timestamp =
                        LocalTime.now()
                                .format(TIME_FORMAT);

                String formattedMessage =
                        "["
                                + timestamp
                                + "] "
                                + username
                                + ": "
                                + message;

                server.broadcast(
                        formattedMessage,
                        this
                );
            }

        } catch (IOException e) {

            if (username != null) {
                System.out.println(
                        username
                                + " disconnected unexpectedly."
                );
            }

        } finally {

            disconnect();
        }
    }

    private void setupUsername()
            throws IOException {

        while (true) {

            writer.println(
                    "ENTER_USERNAME"
            );

            String requestedUsername =
                    reader.readLine();

            if (requestedUsername == null) {
                throw new IOException(
                        "Client disconnected before entering username."
                );
            }

            requestedUsername =
                    requestedUsername.trim();

            if (requestedUsername.isEmpty()) {

                writer.println(
                        "USERNAME_INVALID"
                );

                continue;
            }

            if (server.isUsernameTaken(
                    requestedUsername)) {

                writer.println(
                        "USERNAME_TAKEN"
                );

                continue;
            }

            username =
                    requestedUsername;

            writer.println(
                    "USERNAME_ACCEPTED"
            );

            break;
        }
    }

    private void handlePrivateMessage(
            String message) {

        String[] parts =
                message.split(" ", 3);

        if (parts.length < 3) {

            sendMessage(
                    "Usage: /private <username> <message>"
            );

            return;
        }

        String targetUsername =
                parts[1];

        String privateMessage =
                parts[2];

        boolean sent =
                server.sendPrivateMessage(
                        targetUsername,
                        privateMessage,
                        this
                );

        if (!sent) {

            sendMessage(
                    "User '"
                            + targetUsername
                            + "' was not found."
            );
        }
    }

    public void sendMessage(
            String message) {

        if (writer != null) {
            writer.println(message);
        }
    }

    public String getUsername() {
        return username;
    }

    private void disconnect() {

        server.removeClient(this);

        if (username != null) {

            server.broadcast(
                    "[SERVER] "
                            + username
                            + " left the chat.",
                    this
            );

            ChatLogger.log(
                    username
                            + " left the chat."
            );
        }

        try {

            if (reader != null) {
                reader.close();
            }

            if (writer != null) {
                writer.close();
            }

            if (socket != null
                    && !socket.isClosed()) {

                socket.close();
            }

        } catch (IOException e) {

            System.err.println(
                    "Error closing client connection: "
                            + e.getMessage()
            );
        }
    }
}