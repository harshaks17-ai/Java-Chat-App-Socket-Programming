package common;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ChatLogger {

    private static final String LOG_DIRECTORY = "logs";
    private static final String LOG_FILE = "logs/chat_history.txt";

    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static synchronized void log(String message) {

        File directory = new File(LOG_DIRECTORY);

        if (!directory.exists()) {
            directory.mkdirs();
        }

        String timestamp = LocalDateTime.now().format(FORMATTER);

        try (PrintWriter writer =
                     new PrintWriter(new FileWriter(LOG_FILE, true))) {

            writer.println("[" + timestamp + "] " + message);

        } catch (IOException e) {
            System.err.println("Could not write to chat log: "
                    + e.getMessage());
        }
    }
}