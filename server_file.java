import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private ServerSocket serverSocket;

    // Start the server on the specified port
    public void start(int port) {
        try {
            serverSocket = new ServerSocket(port);
            System.out.println("Server started on port: " + port);

            // Continuously listen for new client connections
            while (true) {
                new ClientHandler(serverSocket.accept()).start();
            }
        } catch (IOException e) {
            System.err.println("Could not start the server on port: " + port);
            e.printStackTrace();
        } finally {
            stop();
        }
    }

    // Stop the server and close the server socket
    public void stop() {
        try {
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
                System.out.println("Server stopped.");
            }
        } catch (IOException e) {
            System.err.println("Error while stopping the server.");
            e.printStackTrace();
        }
    }

    // Main method to start the server
    public static void main(String[] args) {
        Server server = new Server();
        server.start(6666);  // Example port number
    }
}

class ClientHandler extends Thread {
    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;

    // Constructor to initialize the client socket
    public ClientHandler(Socket socket) {
        this.clientSocket = socket;
    }

    // Run method to handle client communication
    public void run() {
        try {
            // Initialize input and output streams
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            String inputLine;
            // Read client input line by line
            while ((inputLine = in.readLine()) != null) {
                processCommand(inputLine);
            }
        } catch (IOException e) {
            System.err.println("Error handling client connection.");
            e.printStackTrace();
        } finally {
            // Clean up resources
            closeResources();
        }
    }

    // Method to process commands received from the client
    private void processCommand(String command) {
        // Command processing logic
        System.out.println("Received command: " + command);
        // Respond to the command
        out.println("Command received: " + command);
    }

    // Close the input and output streams and the client socket
    private void closeResources() {
        try {
            if (in != null) {
                in.close();
            }
            if (out != null) {
                out.close();
            }
            if (clientSocket != null && !clientSocket.isClosed()) {
                clientSocket.close();
            }
        } catch (IOException e) {
            System.err.println("Error closing resources.");
            e.printStackTrace();
        }
    }
}
