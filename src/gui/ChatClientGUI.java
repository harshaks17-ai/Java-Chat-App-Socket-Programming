package gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ChatClientGUI extends JFrame {

    private static final String HOST = "localhost";
    private static final int PORT = 5000;

    private JTextArea chatArea;
    private JTextArea usersArea;
    private JTextField messageField;

    private JButton sendButton;
    private JButton usersButton;
    private JButton clearButton;
    private JButton disconnectButton;

    private JLabel statusLabel;
    private JLabel usernameLabel;

    private Socket socket;
    private BufferedReader reader;
    private PrintWriter writer;

    private String username;
    private volatile boolean connected = false;

    public ChatClientGUI() {

        username = JOptionPane.showInputDialog(
                null,
                "Enter your username:",
                "Welcome to Java Chat",
                JOptionPane.PLAIN_MESSAGE
        );

        if (username == null || username.trim().isEmpty()) {

            JOptionPane.showMessageDialog(
                    null,
                    "Please enter a valid username.",
                    "Invalid Username",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        username = username.trim();

        initializeGUI();
        connectToServer();
    }

    // =========================================================
    // INITIALIZE GUI
    // =========================================================

    private void initializeGUI() {

        setTitle("Java Chat - " + username);

        setSize(900, 600);

        setMinimumSize(
                new Dimension(700, 450)
        );

        setLocationRelativeTo(null);

        setDefaultCloseOperation(
                JFrame.DO_NOTHING_ON_CLOSE
        );

        setLayout(
                new BorderLayout()
        );

        createHeader();
        createMainArea();
        createBottomPanel();

        addWindowListener(
                new WindowAdapter() {

                    @Override
                    public void windowClosing(
                            WindowEvent e) {

                        disconnect();
                    }
                }
        );

        setVisible(true);

        messageField.requestFocusInWindow();
    }

    // =========================================================
    // HEADER
    // =========================================================

    private void createHeader() {

        JPanel headerPanel =
                new JPanel(
                        new BorderLayout()
                );

        headerPanel.setBackground(
                new Color(
                        45,
                        52,
                        70
                )
        );

        headerPanel.setBorder(
                new EmptyBorder(
                        15,
                        20,
                        15,
                        20
                )
        );

        JLabel appTitle =
                new JLabel(
                        "Java Chat"
                );

        appTitle.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        24
                )
        );

        appTitle.setForeground(
                Color.WHITE
        );

        JPanel userInfoPanel =
                new JPanel();

        userInfoPanel.setOpaque(
                false
        );

        userInfoPanel.setLayout(
                new BoxLayout(
                        userInfoPanel,
                        BoxLayout.Y_AXIS
                )
        );

        usernameLabel =
                new JLabel(
                        "Logged in as: "
                                + username
                );

        usernameLabel.setForeground(
                Color.WHITE
        );

        usernameLabel.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        14
                )
        );

        statusLabel =
                new JLabel(
                        "Status: Connecting..."
                );

        statusLabel.setForeground(
                new Color(
                        255,
                        193,
                        7
                )
        );

        statusLabel.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        12
                )
        );

        userInfoPanel.add(
                usernameLabel
        );

        userInfoPanel.add(
                statusLabel
        );

        headerPanel.add(
                appTitle,
                BorderLayout.WEST
        );

        headerPanel.add(
                userInfoPanel,
                BorderLayout.EAST
        );

        add(
                headerPanel,
                BorderLayout.NORTH
        );
    }

    // =========================================================
    // MAIN CHAT AREA
    // =========================================================

    private void createMainArea() {

        chatArea =
                new JTextArea();

        chatArea.setEditable(
                false
        );

        chatArea.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        15
                )
        );

        chatArea.setLineWrap(
                true
        );

        chatArea.setWrapStyleWord(
                true
        );

        chatArea.setBackground(
                new Color(
                        248,
                        249,
                        250
                )
        );

        chatArea.setBorder(
                new EmptyBorder(
                        15,
                        15,
                        15,
                        15
                )
        );

        JScrollPane chatScrollPane =
                new JScrollPane(
                        chatArea
                );

        chatScrollPane.setBorder(
                BorderFactory
                        .createEmptyBorder()
        );

        JPanel usersPanel =
                createUsersPanel();

        JPanel centerPanel =
                new JPanel(
                        new BorderLayout()
                );

        centerPanel.add(
                chatScrollPane,
                BorderLayout.CENTER
        );

        centerPanel.add(
                usersPanel,
                BorderLayout.EAST
        );

        add(
                centerPanel,
                BorderLayout.CENTER
        );
    }

    // =========================================================
    // USERS PANEL
    // =========================================================

    private JPanel createUsersPanel() {

        JPanel usersPanel =
                new JPanel(
                        new BorderLayout()
                );

        usersPanel.setPreferredSize(
                new Dimension(
                        210,
                        0
                )
        );

        usersPanel.setBackground(
                new Color(
                        245,
                        245,
                        245
                )
        );

        usersPanel.setBorder(
                BorderFactory
                        .createMatteBorder(
                                0,
                                1,
                                0,
                                0,
                                new Color(
                                        220,
                                        220,
                                        220
                                )
                        )
        );

        JLabel usersTitle =
                new JLabel(
                        "Online Users"
                );

        usersTitle.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        16
                )
        );

        usersTitle.setBorder(
                new EmptyBorder(
                        15,
                        15,
                        10,
                        10
                )
        );

        usersArea =
                new JTextArea();

        usersArea.setEditable(
                false
        );

        usersArea.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        14
                )
        );

        usersArea.setBackground(
                new Color(
                        245,
                        245,
                        245
                )
        );

        usersArea.setBorder(
                new EmptyBorder(
                        10,
                        15,
                        10,
                        10
                )
        );

        usersArea.setText(
                "Click Refresh Users\n"
                        + "to see who is online."
        );

        JScrollPane usersScrollPane =
                new JScrollPane(
                        usersArea
                );

        usersScrollPane.setBorder(
                BorderFactory
                        .createEmptyBorder()
        );

        usersButton =
                new JButton(
                        "Refresh Users"
                );

        usersButton.setFocusPainted(
                false
        );

        usersButton.setEnabled(
                false
        );

        usersButton.addActionListener(
                e -> refreshUsers()
        );

        usersPanel.add(
                usersTitle,
                BorderLayout.NORTH
        );

        usersPanel.add(
                usersScrollPane,
                BorderLayout.CENTER
        );

        usersPanel.add(
                usersButton,
                BorderLayout.SOUTH
        );

        return usersPanel;
    }

    // =========================================================
    // BOTTOM MESSAGE PANEL
    // =========================================================

    private void createBottomPanel() {

        JPanel bottomPanel =
                new JPanel(
                        new BorderLayout(
                                10,
                                10
                        )
                );

        bottomPanel.setBorder(
                new EmptyBorder(
                        12,
                        15,
                        12,
                        15
                )
        );

        messageField =
                new JTextField();

        messageField.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        15
                )
        );

        messageField.setToolTipText(
                "Type your message here"
        );

        messageField.setBorder(
                BorderFactory
                        .createCompoundBorder(

                                BorderFactory
                                        .createLineBorder(
                                                new Color(
                                                        200,
                                                        200,
                                                        200
                                                )
                                        ),

                                new EmptyBorder(
                                        10,
                                        10,
                                        10,
                                        10
                                )
                        )
        );

        sendButton =
                new JButton(
                        "Send"
                );

        sendButton.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        14
                )
        );

        sendButton.setFocusPainted(
                false
        );

        sendButton.setEnabled(
                false
        );

        clearButton =
                new JButton(
                        "Clear Chat"
                );

        clearButton.setFocusPainted(
                false
        );

        disconnectButton =
                new JButton(
                        "Disconnect"
                );

        disconnectButton.setFocusPainted(
                false
        );

        JPanel buttonPanel =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.RIGHT,
                                8,
                                0
                        )
                );

        buttonPanel.add(
                clearButton
        );

        buttonPanel.add(
                disconnectButton
        );

        buttonPanel.add(
                sendButton
        );

        bottomPanel.add(
                messageField,
                BorderLayout.CENTER
        );

        bottomPanel.add(
                buttonPanel,
                BorderLayout.EAST
        );

        add(
                bottomPanel,
                BorderLayout.SOUTH
        );

        sendButton.addActionListener(
                e -> sendMessage()
        );

        messageField.addActionListener(
                e -> sendMessage()
        );

        clearButton.addActionListener(
                e -> chatArea.setText("")
        );

        disconnectButton.addActionListener(
                e -> disconnect()
        );
    }

    // =========================================================
    // CONNECT TO SERVER
    // =========================================================

    private void connectToServer() {

        Thread connectionThread =
                new Thread(() -> {

                    try {

                        socket =
                                new Socket(
                                        HOST,
                                        PORT
                                );

                        reader =
                                new BufferedReader(
                                        new InputStreamReader(
                                                socket
                                                        .getInputStream()
                                        )
                                );

                        writer =
                                new PrintWriter(
                                        socket
                                                .getOutputStream(),
                                        true
                                );

                        String request =
                                reader.readLine();

                        if (!"ENTER_USERNAME"
                                .equals(request)) {

                            showError(
                                    "Invalid response from server."
                            );

                            closeSocket();

                            return;
                        }

                        writer.println(
                                username
                        );

                        String response =
                                reader.readLine();

                        if ("USERNAME_TAKEN"
                                .equals(response)) {

                            showError(
                                    "Username '"
                                            + username
                                            + "' is already being used."
                            );

                            closeSocket();

                            SwingUtilities
                                    .invokeLater(
                                            this::dispose
                                    );

                            return;
                        }

                        if ("USERNAME_INVALID"
                                .equals(response)) {

                            showError(
                                    "The username is invalid."
                            );

                            closeSocket();

                            SwingUtilities
                                    .invokeLater(
                                            this::dispose
                                    );

                            return;
                        }

                        if (!"USERNAME_ACCEPTED"
                                .equals(response)) {

                            showError(
                                    "Unable to register username."
                            );

                            closeSocket();

                            SwingUtilities
                                    .invokeLater(
                                            this::dispose
                                    );

                            return;
                        }

                        connected = true;

                        SwingUtilities
                                .invokeLater(() -> {

                                    statusLabel.setText(
                                            "Status: Online"
                                    );

                                    statusLabel
                                            .setForeground(
                                                    new Color(
                                                            76,
                                                            175,
                                                            80
                                                    )
                                            );

                                    sendButton
                                            .setEnabled(
                                                    true
                                            );

                                    usersButton
                                            .setEnabled(
                                                    true
                                            );

                                    messageField
                                            .setEnabled(
                                                    true
                                            );

                                    appendSystemMessage(
                                            "Connected successfully."
                                    );

                                    messageField
                                            .requestFocusInWindow();
                                });

                        receiveMessages();

                    } catch (IOException e) {

                        connected = false;

                        SwingUtilities
                                .invokeLater(() -> {

                                    statusLabel.setText(
                                            "Status: Offline"
                                    );

                                    statusLabel
                                            .setForeground(
                                                    Color.RED
                                            );

                                    sendButton
                                            .setEnabled(
                                                    false
                                            );

                                    usersButton
                                            .setEnabled(
                                                    false
                                            );

                                    showError(
                                            "Could not connect to the server.\n\n"
                                                    + "Make sure ChatServer is running."
                                    );
                                });
                    }
                });

        connectionThread.start();
    }

    // =========================================================
    // RECEIVE MESSAGES
    // =========================================================

    private void receiveMessages() {

        try {

            String message;

            while (
                    connected
                            && (message =
                            reader.readLine())
                            != null
            ) {

                final String receivedMessage =
                        message;

                SwingUtilities
                        .invokeLater(() -> {

                            if (receivedMessage
                                    .startsWith(
                                            "Active users:"
                                    )) {

                                updateUsersList(
                                        receivedMessage
                                );

                            } else if (
                                    receivedMessage
                                            .equals(
                                                    "No active users."
                                            )
                            ) {

                                usersArea.setText(
                                        "No users online."
                                );

                            } else {

                                appendMessage(
                                        receivedMessage
                                );
                            }
                        });
            }

        } catch (IOException e) {

            if (connected) {

                SwingUtilities
                        .invokeLater(() ->
                                appendSystemMessage(
                                        "Connection to server lost."
                                )
                        );
            }

        } finally {

            connected = false;

            SwingUtilities
                    .invokeLater(() -> {

                        statusLabel.setText(
                                "Status: Offline"
                        );

                        statusLabel
                                .setForeground(
                                        Color.RED
                                );

                        sendButton
                                .setEnabled(
                                        false
                                );

                        usersButton
                                .setEnabled(
                                        false
                                );

                        messageField
                                .setEnabled(
                                        false
                                );
                    });
        }
    }

    // =========================================================
    // SEND MESSAGE
    // =========================================================

    private void sendMessage() {

        String message =
                messageField
                        .getText()
                        .trim();

        if (message.isEmpty()) {
            return;
        }

        if (!connected
                || writer == null) {

            JOptionPane
                    .showMessageDialog(
                            this,
                            "You are not connected to the server.",
                            "Not Connected",
                            JOptionPane.WARNING_MESSAGE
                    );

            return;
        }

        writer.println(
                message
        );

        /*
         * The server broadcasts messages only
         * to the other clients.
         *
         * Therefore, we display the sender's
         * own public message locally.
         */
        if (!message.startsWith("/")) {

            appendMessage(
                    "You: "
                            + message
            );
        }

        messageField.setText(
                ""
        );

        messageField
                .requestFocusInWindow();

        if (message
                .equalsIgnoreCase(
                        "/exit"
                )) {

            disconnect();
        }
    }

    // =========================================================
    // REFRESH USERS
    // =========================================================

    private void refreshUsers() {

        if (connected
                && writer != null) {

            writer.println(
                    "/users"
            );
        }
    }

    // =========================================================
    // UPDATE USERS LIST
    // =========================================================

    private void updateUsersList(
            String message) {

        String users =
                message
                        .replace(
                                "Active users:",
                                ""
                        )
                        .trim();

        if (users.isEmpty()) {

            usersArea.setText(
                    "No users online."
            );

            return;
        }

        String[] userArray =
                users.split(",");

        StringBuilder display =
                new StringBuilder();

        for (String user
                : userArray) {

            String cleanUser =
                    user.trim();

            display.append(
                    "- "
            );

            display.append(
                    cleanUser
            );

            if (cleanUser
                    .equalsIgnoreCase(
                            username
                    )) {

                display.append(
                        " (You)"
                );
            }

            display.append(
                    "\n"
            );
        }

        usersArea.setText(
                display.toString()
        );
    }

    // =========================================================
    // APPEND NORMAL MESSAGE
    // =========================================================

    private void appendMessage(
            String message) {

        chatArea.append(
                message
                        + System.lineSeparator()
        );

        chatArea.setCaretPosition(
                chatArea
                        .getDocument()
                        .getLength()
        );
    }

    // =========================================================
    // APPEND SYSTEM MESSAGE
    // =========================================================

    private void appendSystemMessage(
            String message) {

        chatArea.append(
                "[SYSTEM] "
                        + message
                        + System.lineSeparator()
        );

        chatArea.setCaretPosition(
                chatArea
                        .getDocument()
                        .getLength()
        );
    }

    // =========================================================
    // DISCONNECT
    // =========================================================

    private void disconnect() {

        if (connected
                && writer != null) {

            writer.println(
                    "/exit"
            );
        }

        connected = false;

        closeSocket();

        dispose();
    }

    // =========================================================
    // CLOSE SOCKET
    // =========================================================

    private void closeSocket() {

        try {

            if (reader != null) {
                reader.close();
            }

        } catch (IOException ignored) {
        }

        if (writer != null) {
            writer.close();
        }

        try {

            if (socket != null
                    && !socket.isClosed()) {

                socket.close();
            }

        } catch (IOException e) {

            System.err.println(
                    "Error closing connection: "
                            + e.getMessage()
            );
        }
    }

    // =========================================================
    // SHOW ERROR
    // =========================================================

    private void showError(
            String message) {

        SwingUtilities
                .invokeLater(() ->

                        JOptionPane
                                .showMessageDialog(
                                        this,
                                        message,
                                        "Java Chat",
                                        JOptionPane.ERROR_MESSAGE
                                )
                );
    }

    // =========================================================
    // MAIN METHOD
    // =========================================================

    public static void main(
            String[] args) {

        SwingUtilities
                .invokeLater(
                        ChatClientGUI::new
                );
    }
}