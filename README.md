# Java Chat App with Socket Programming

A real-time multi-client chat application built using Java Socket Programming, TCP communication, Multithreading, and Java Swing. The application allows multiple users to connect to a central server and communicate in real time through both console-based and GUI-based clients.

---

## Project Overview

The **Java Chat App with Socket Programming** demonstrates how real-time client-server communication works using Java TCP sockets.

A central chat server accepts connections from multiple clients and creates a separate thread for each connected user. This allows multiple users to communicate simultaneously without blocking other client connections.

Users can send public messages, communicate privately, view active users, receive join and leave notifications, and safely disconnect from the server.

Chat activity is also stored in a history log for future reference.

This project was developed as a practical implementation of:

* Java Networking
* Socket Programming
* TCP/IP Communication
* Multithreading
* Client-Server Architecture
* Java Swing GUI
* File Handling
* Concurrent Programming

---

## Features

The application currently supports:

* Real-time client-server communication
* TCP socket programming
* Multiple client connections
* Multithreading
* Unique username support
* Duplicate username prevention
* Public message broadcasting
* Private messaging
* Active user listing
* User join notifications
* User leave notifications
* Message timestamps
* Chat history logging
* Graceful client disconnection
* Basic exception handling
* Console-based chat client
* User-friendly Java Swing GUI
* Localhost multi-client simulation

---

## Technologies Used

* Java
* Java Socket API
* TCP/IP
* ServerSocket
* Socket
* Java Multithreading
* Java Swing
* Java I/O
* Concurrent Collections
* VS Code
* Git
* GitHub

---

## Project Architecture

The application follows a centralized client-server architecture.

```text
                   +----------------------+
                   |     Chat Server      |
                   |    localhost:5000    |
                   +----------+-----------+
                              |
              +---------------+---------------+
              |               |               |
              v               v               v
       ClientHandler    ClientHandler    ClientHandler
          Thread 1         Thread 2         Thread 3
              |               |               |
              v               v               v
          Client 1        Client 2        Client 3
```

Each connected client is handled by a separate `ClientHandler` thread.

This allows multiple users to communicate with the server at the same time.

When a client sends a public message, the message follows this flow:

```text
Client
   |
   v
TCP Socket
   |
   v
Chat Server
   |
   v
Client Handler Thread
   |
   v
Message Broadcasting
   |
   v
Connected Clients
```

The server receives the message from one client and broadcasts it to the other connected clients.

---

## Project Structure

```text
Java-Chat-App-Socket-Programming/
|
+-- src/
|   |
|   +-- server/
|   |   +-- ChatServer.java
|   |   +-- ClientHandler.java
|   |
|   +-- client/
|   |   +-- ChatClient.java
|   |
|   +-- common/
|   |   +-- ChatLogger.java
|   |
|   +-- gui/
|       +-- ChatClientGUI.java
|
+-- logs/
|   +-- chat_history.txt
|
+-- outputs/
|
+-- screenshots/
|
+-- docs/
|
+-- README.md
|
+-- .gitignore
```

---

## Main Components

### ChatServer.java

`ChatServer.java` acts as the central server of the application.

Its main responsibilities include:

* Starting the server using `ServerSocket`
* Listening for incoming connections on port `5000`
* Accepting multiple client connections
* Creating a separate thread for each connected client
* Maintaining connected clients
* Broadcasting public messages
* Processing private messages
* Providing the active user list
* Removing disconnected clients

---

### ClientHandler.java

`ClientHandler.java` handles communication with an individual connected client.

Each connected user receives a separate `ClientHandler` running on its own thread.

Its main responsibilities include:

* Handling one client connection
* Running each client in a separate thread
* Reading the client's username
* Preventing duplicate usernames
* Receiving client messages
* Broadcasting public messages
* Processing private-message commands
* Handling active-user requests
* Handling client disconnection
* Sending join and leave notifications

---

### ChatClient.java

`ChatClient.java` is the console-based version of the chat client.

Its responsibilities include:

* Connecting to the chat server
* Accepting a username
* Sending messages to the server
* Receiving messages in real time
* Running a separate thread for incoming messages
* Supporting public messages
* Supporting private-message commands
* Supporting active-user commands
* Disconnecting safely from the server

---

### ChatClientGUI.java

`ChatClientGUI.java` provides a graphical user interface for the chat application using Java Swing.

The GUI includes:

* Username input
* Chat message display area
* Message input field
* Send button
* Online users panel
* Refresh Users button
* Clear Chat button
* Disconnect button
* Connection status indicator
* Scrollable chat interface

The GUI communicates with the same Java socket server used by the console client.

---

### ChatLogger.java

`ChatLogger.java` handles chat history logging.

Chat activity is stored inside:

```text
logs/chat_history.txt
```

The log can contain:

* Server startup events
* User join events
* Public chat messages
* Private-message activity
* User leave events

This provides a basic persistent record of chat activity.

---

## Java Concepts Used

### Socket

The `Socket` class is used by clients to establish a TCP connection with the server.

### ServerSocket

The `ServerSocket` class allows the server to listen for and accept incoming client connections.

### Multithreading

Each connected client is handled by a separate thread. This allows multiple clients to communicate simultaneously.

### BufferedReader

`BufferedReader` is used to efficiently read incoming text messages from socket connections.

### PrintWriter

`PrintWriter` is used to send text messages through socket connections.

### Concurrent Collections

Thread-safe collections are used to manage multiple connected clients safely.

### Exception Handling

Exception handling is used to manage connection errors, unexpected disconnections, and I/O problems.

### Java Swing

Java Swing is used to create the graphical user interface for the chat client.

---

## How to Compile

Make sure Java JDK is installed.

Check your Java installation using:

```bash
java -version
```

Check the Java compiler using:

```bash
javac -version
```

Open a terminal inside the project root directory.

Compile the complete project using:

```bash
javac -encoding UTF-8 -d out src/common/ChatLogger.java src/server/ChatServer.java src/server/ClientHandler.java src/client/ChatClient.java src/gui/ChatClientGUI.java
```

The compiled `.class` files will be generated inside the `out` directory.

---

## How to Run

### Step 1: Start the Server

Open a terminal in the project directory and run:

```bash
java -cp out server.ChatServer
```

The server runs using:

```text
Host: localhost
Port: 5000
```

Expected server output:

```text
=================================
       JAVA CHAT SERVER
=================================
Server started successfully.
Server is running on port: 5000
Waiting for clients...
```

Keep the server terminal running.

---

### Step 2: Start the GUI Client

Open another terminal and run:

```bash
java -cp out gui.ChatClientGUI
```

The application will ask you to enter a username.

After entering a valid username, the GUI client connects to the server.

---

### Step 3: Start Multiple Clients

Open additional terminals and run:

```bash
java -cp out gui.ChatClientGUI
```

Enter a different username for each client.

For example:

```text
Harsha
Rahul
Kiran
```

You can now simulate multiple users communicating with each other through the same chat server.

---

## Running the Console Client

The console-based client can be started using:

```bash
java -cp out client.ChatClient
```

Enter your username when prompted.

You can then send and receive messages directly from the terminal.

---

## Chat Commands

### View Active Users

Use:

```text
/users
```

This displays the users currently connected to the chat server.

---

### Send a Private Message

Use:

```text
/private <username> <message>
```

Example:

```text
/private Rahul Hello Rahul
```

Only the selected user will receive the private message.

---

### Disconnect from the Server

Use:

```text
/exit
```

The user will be safely disconnected from the chat server.

Other connected clients will receive a notification that the user has left the chat.

---

## Multi-Client Simulation

The application can simulate a real-time multi-user chat environment locally.

For example:

```text
Terminal 1 -> Chat Server

Terminal 2 -> Client 1 (Harsha)

Terminal 3 -> Client 2 (Rahul)

Terminal 4 -> Client 3 (Kiran)
```

Each client communicates with the central server using TCP sockets.

The server uses separate threads to handle each connected client.

---

## Testing

The application can be tested locally using multiple client instances.

Testing scenarios include:

* Single client connection
* Multiple simultaneous client connections
* Public message broadcasting
* Private messaging
* Duplicate username prevention
* Active user listing
* Empty message handling
* Client disconnection
* Sudden client disconnection
* Join notifications
* Leave notifications
* Invalid private-message recipient
* Chat history logging
* Server connection errors

---


## Industry Relevance

Socket programming and client-server communication are fundamental concepts used in many modern software systems.

Similar concepts and architectures are used in:

* Messaging applications
* Live customer support systems
* Multiplayer games
* Team collaboration platforms
* Customer service applications
* Real-time notification systems
* Backend services
* Distributed systems

This project demonstrates practical knowledge of networking, concurrent programming, backend communication, and real-time application development.

---

## Why Java?

Java remains widely used for enterprise and backend application development.

Java provides strong support for:

* Network programming
* Multithreading
* Concurrent applications
* Backend development
* Enterprise applications
* Cloud-based systems
* API development

Even in AI-driven systems, reliable backend infrastructure is required to manage APIs, databases, authentication, networking, and communication between different services.

The networking and multithreading concepts demonstrated in this project are therefore relevant to modern software engineering.

---

## Learning Outcomes

Through this project, I gained practical experience with:

* Java Socket Programming
* TCP/IP communication
* Client-server architecture
* ServerSocket and Socket
* Java Multithreading
* Thread-per-client architecture
* Concurrent client management
* Java I/O streams
* Real-time communication
* Public message broadcasting
* Private messaging
* Active user management
* Java Swing GUI development
* Exception handling
* File-based chat logging
* Local multi-client simulation
* Git version control
* GitHub repository management

---

## Limitations

The current version has the following limitations:

* The application primarily runs on localhost.
* Messages are transmitted as plain text.
* User authentication is not implemented.
* Password-based login is not available.
* Chat history is stored in a text file instead of a database.
* Multiple chat rooms are not supported.
* Group creation is not implemented.
* File sharing is not implemented.
* Image sharing is not implemented.
* Message encryption is not currently implemented.

---

## Future Improvements

Future versions of the project can include:

* User registration
* Secure login and authentication
* Password hashing
* Database integration
* Multiple chat rooms
* Group chats
* File sharing
* Image sharing
* Message encryption
* End-to-end encryption
* Online and offline user status
* Message delivery status
* Read receipts
* Improved chat message bubbles
* User profile pictures
* Cloud server deployment
* Internet-based communication
* Mobile application integration

---

## Author

**Harsha K S**

B.E. Computer Science and Engineering

Dayananda Sagar Academy of Technology and Management (DSATM)

---

## License

This project was developed for educational and learning purposes.
