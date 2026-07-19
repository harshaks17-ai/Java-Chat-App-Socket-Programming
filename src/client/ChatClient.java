package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ChatClient {

    private static final String HOST =
            "localhost";

    private static final int PORT =
            5000;

    public static void main(String[] args) {

        System.out.println(
                "================================="
        );

        System.out.println(
                "        JAVA CHAT CLIENT"
        );

        System.out.println(
                "================================="
        );

        try (
                Socket socket =
                        new Socket(HOST, PORT);

                BufferedReader serverReader =
                        new BufferedReader(
                                new InputStreamReader(
                                        socket.getInputStream()
                                )
                        );

                PrintWriter serverWriter =
                        new PrintWriter(
                                socket.getOutputStream(),
                                true
                        );

                BufferedReader consoleReader =
                        new BufferedReader(
                                new InputStreamReader(
                                        System.in
                                )
                        )
        ) {

            System.out.println(
                    "Connected to chat server."
            );

            setupUsername(
                    serverReader,
                    serverWriter,
                    consoleReader
            );

            Thread receiverThread =
                    new Thread(() -> {

                        try {

                            String serverMessage;

                            while ((serverMessage =
                                    serverReader.readLine())
                                    != null) {

                                System.out.println(
                                        serverMessage
                                );
                            }

                        } catch (IOException e) {

                            System.out.println(
                                    "Connection to server closed."
                            );
                        }
                    });

            receiverThread.start();

            String userInput;

            while ((userInput =
                    consoleReader.readLine())
                    != null) {

                serverWriter.println(
                        userInput
                );

                if (userInput
                        .equalsIgnoreCase("/exit")) {

                    System.out.println(
                            "Disconnecting from server..."
                    );

                    break;
                }
            }

        } catch (IOException e) {

            System.err.println(
                    "Could not connect to server."
            );

            System.err.println(
                    "Make sure ChatServer is running."
            );

            System.err.println(
                    "Error: "
                            + e.getMessage()
            );
        }

        System.out.println(
                "Chat client closed."
        );
    }

    private static void setupUsername(
            BufferedReader serverReader,
            PrintWriter serverWriter,
            BufferedReader consoleReader)
            throws IOException {

        while (true) {

            String serverResponse =
                    serverReader.readLine();

            if ("ENTER_USERNAME"
                    .equals(serverResponse)) {

                System.out.print(
                        "Enter your username: "
                );

                String username =
                        consoleReader.readLine();

                serverWriter.println(
                        username
                );

                String result =
                        serverReader.readLine();

                if ("USERNAME_ACCEPTED"
                        .equals(result)) {

                    System.out.println(
                            "Username accepted."
                    );

                    break;

                } else if ("USERNAME_TAKEN"
                        .equals(result)) {

                    System.out.println(
                            "Username already exists. Try another username."
                    );

                } else if ("USERNAME_INVALID"
                        .equals(result)) {

                    System.out.println(
                            "Username cannot be empty."
                    );
                }
            }
        }
    }
}