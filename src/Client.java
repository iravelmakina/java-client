package src;

import java.io.*;
import java.net.Socket;


public class Client {
    public Client(String directory) {
        this.directory = directory;
    }

    public int connect(String serverIp, int port) {
        try {
            socket = new Socket(serverIp, port);
            dataInputStream = new DataInputStream(socket.getInputStream());
            dataOutputStream = new DataOutputStream(socket.getOutputStream());

            String connectionResponse = receiveMessage();
            if (!connectionResponse.equals(RESPONSE_OK)) {
                System.out.println(connectionResponse);
                return -1;
            }

            sendMessage("1.0");
            String versionResponse = receiveMessage();
            if (!versionResponse.equals(RESPONSE_OK)) {
                System.out.println(versionResponse);
                return -1;
            }

            System.out.println("\nConnected to server at " + serverIp + ":" + port);
            return 0;
        } catch (IOException e) {
            System.err.println("Connect failed: " + e.getMessage());
            return -1;
        }
    }


    public void disconnect() throws IOException {
        sendMessage("EXIT");
        socket.close();
        System.out.println("\nDisconnected from server.");
    }


    public boolean isConnected() {
        return socket != null && socket.isConnected() && !socket.isClosed();
    }


    private void sendMessage(String message) throws IOException {
        byte[] messageBytes = message.getBytes();
        dataOutputStream.writeInt(messageBytes.length);
        dataOutputStream.write(messageBytes);
        dataOutputStream.flush();
    }


    private String receiveMessage() throws IOException {
        try {
            int messageSize = dataInputStream.readInt();
            byte[] messageBytes = new byte[messageSize];
            dataInputStream.readFully(messageBytes);
            return new String(messageBytes);
        } catch (IOException e) {
            System.out.println("\033[31mError: No response from server. Closing socket.\033[0m");
            socket.close();
            return "";
        }
    }


    private void downloadFile(String filename) throws IOException {
        String response = receiveMessage();
        if (!response.equals(RESPONSE_OK)) {
            System.out.println(response);
            return;
        }

        sendMessage(RESPONSE_ACK);

        File file = new File(directory + filename);
        try (FileOutputStream fileOutputStream = new FileOutputStream(file)) {
            byte[] buffer = new byte[BUFFER_SIZE];
            int bytesRead;
            while ((bytesRead = dataInputStream.readInt()) != 0) {
                dataInputStream.readFully(buffer, 0, bytesRead);
                fileOutputStream.write(buffer, 0, bytesRead);
            }

        } catch (IOException e) {
            System.out.println("\033[31mError: Unable to create file.\033[0m");
        }

        System.out.println("Download complete: " + filename);
    }


    private void uploadFile(File file) throws IOException {
        String response = receiveMessage();
        if (!response.equals(RESPONSE_OK)) {
            System.out.println(response);
            return;
        }

        FileInputStream fileInputStream = new FileInputStream(file);
        byte[] buffer = new byte[BUFFER_SIZE];
        int bytesRead;
        while ((bytesRead = fileInputStream.read(buffer)) != -1) {
            dataOutputStream.writeInt(bytesRead);
            dataOutputStream.write(buffer, 0, bytesRead);
        }
        dataOutputStream.flush();
        dataOutputStream.writeInt(0);
        fileInputStream.close();
        dataOutputStream.flush();

        if (receiveMessage().equals(RESPONSE_OK)) {
            System.out.println("Upload complete: " + file.getName());
        } else {
            System.out.println("\033[31mError: Upload failed.\033[0m");
        }
    }


    public void listFiles() throws IOException {
        sendMessage("LIST");
        System.out.println(receiveMessage());
    }


    public void getFile(String filename) throws IOException {
        sendMessage("GET " + filename);
        downloadFile(filename);
    }


    public void putFile(String filename) throws IOException {
        File file = new File(directory + filename);
        if (!file.exists() && !file.isFile()) {
            System.out.println("File not found on client.");
            return;
        }

        sendMessage("PUT " + filename);
        uploadFile(file);
    }


    public void deleteFile(String filename) throws IOException {
        sendMessage("DELETE " + filename);

        String response = receiveMessage();
        if (response.equals(RESPONSE_OK)) {
            System.out.println("Delete complete.");
        } else {
            System.out.println(response);
        }
    }


    public void getFileInfo(String filename) throws IOException {
        sendMessage("INFO " + filename);
        System.out.println(receiveMessage());
    }


    private static final String RESPONSE_OK = "200 OK";
    private static final String RESPONSE_ACK = "ACK";
    private static final int BUFFER_SIZE = 1024;

    private Socket socket;
    private final String directory;
    private DataInputStream dataInputStream;
    private DataOutputStream dataOutputStream;
}
