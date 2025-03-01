import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.StringTokenizer;

public class ClientCLI {
    public ClientCLI(String directory) {
        client = new Client(directory);
    }


    public void run(String serverIp, int port) {
        if (client.connect(serverIp, port) == -1) {
            return;
        }

        Scanner scanner = new Scanner(System.in);
        printMenu();

        while (client.isConnected()) {
            System.out.print("\nEnter command: ");
            String userInput = scanner.nextLine();
            List<String> commandParts = parseInput(userInput);

            if (commandParts.isEmpty()) {
                System.out.println("Invalid command. Type 'EXIT' to quit.");
                continue;
            }

            String command = commandParts.get(0);

            try {
                if (command.equals("LIST")) {
                    client.listFiles();
                } else if (command.equals("GET") && (commandParts.size() == 2)) {
                    client.getFile(commandParts.get(1));
                } else if (command.equals("PUT") && (commandParts.size() == 2)) {
                    client.putFile(commandParts.get(1));
                } else if (command.equals("INFO") && (commandParts.size() == 2)) {
                    client.getFileInfo(commandParts.get(1));
                } else if (command.equals("DELETE") && (commandParts.size() == 2)) {
                    client.deleteFile(commandParts.get(1));
                } else if (command.equals("EXIT")) {
                    client.disconnect();
                } else {
                    System.out.println("Invalid command. Type 'LIST', 'GET <filename>', 'PUT <filename>', 'INFO <filename>', 'DELETE <filename>', or 'EXIT'.");
                }
            } catch (IOException e) {
                System.err.println("Error: " + e.getMessage());
            }
        }
    }


    private void printMenu() {
        System.out.println("\n===== Available Commands ===============================");
        System.out.println("1. LIST               - List available files on the server");
        System.out.println("2. GET <filename>     - Download a file from the server");
        System.out.println("3. PUT <filename>     - Upload a file to the server");
        System.out.println("4. INFO <filename>    - Get file info from the server");
        System.out.println("5. DELETE <filename>  - Delete a file on the server");
        System.out.println("6. EXIT               - Disconnect and exit");
        System.out.println("==========================================================");
    }


    public List<String> parseInput(String input) {
        StringTokenizer tokenizer = new StringTokenizer(input);
        List<String> tokens = new ArrayList<>();
        while (tokenizer.hasMoreTokens()) {
            tokens.add(tokenizer.nextToken());
        }
        return tokens;
    }


    private final Client client;
}
