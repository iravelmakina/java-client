# Java Client-Server Application for File Transfer (v1)

## Overview
This project implements a **Java Client** for a **Client-Server application** over **TCP**. The client is designed to communicate with the **v1 server** described in the **[Client-Server C++ Repository](https://github.com/iravelmakina/client-server)**. The Java client performs various file operations on the server, such as **uploading**, **downloading**, **listing**, **deleting**, and **retrieving file information**. Communication follows a **custom protocol** for handling these requests and responses.

The system includes a **Client class** to handle socket operations, manage file transfers, and interact with the server. The **ClientCLI class** provides a simple command-line interface for users to interact with the server.

## Features
- **File Operations**: Implemented commands to handle files: **GET**, **PUT**, **LIST**, **DELETE**, and **INFO**.
- **Client-Server Communication**: TCP-based communication with a custom protocol for sending and receiving data.
- **Message Prefix**: A 4-byte length prefix indicating the size of the data being sent, ensuring reliable data transfer.
- **Error Handling**: Proper error codes for invalid operations (e.g., file not found, permission errors, server failures).
- **ClientCLI**: A command-line interface to interact with the server and perform file operations.
- **Compatibility**: This client is specifically designed to work with **v1** of the server in the [Client-Server C++ Repository](https://github.com/iravelmakina/client-server).

## Installation and Compilation

### **1. Clone the Repository**
```bash
git clone https://github.com/iravelmakina/java-client.git
cd java-client
```

### **2. Compile the Program**
The project is built using **Java**. To compile and run the client, follow these steps:

```
javac -d out/ src/**/*.java
```

Alternatively, you can use **Maven** to handle dependencies and build:

```
mvn clean install
```

### **3. Run the Program**
To run the client application:

```
java -cp out Main
```

Make sure that the **v1 server** (from the [Client-Server C++ Repository](https://github.com/iravelmakina/client-server)) is running before starting the client.

## Usage

### **Commands Supported**:
- **LIST**: List all files in the server's directory.
- **GET <filename>**: Download a file from the server.
- **PUT <filename>**: Upload a file to the server.
- **DELETE <filename>**: Delete a file from the server.
- **INFO <filename>**: Retrieve metadata (size, last modified, etc.) of a file on the server.
- **EXIT**: Disconnect from the server.

These commands are designed to interact with the **v1 server** in the **[Client-Server C++ Repository](https://github.com/iravelmakina/client-server)**. 

### **Example**:
- To **list files**:
  ```
  LIST
  ```
- To **download a file**:
  ```
  GET myfile.txt
  ```
- To **upload a file**:
  ```
  PUT myfile.txt
  ```
- To **delete a file**:
  ```
  DELETE myfile.txt
  ```
- To **get file info**:
  ```
  INFO myfile.txt
  ```

## Code Structure
```
java-client/
│── src/                       # Contains Java source code files
│   ├── client/                # Client-side code
│   │   ├── Client.java        # Implementation of the Client class
│   │   ├── ClientCLI.java     # Implementation of the ClientCLI class (CLI interface)
│   ├── Main.java              # Main entry point for the application
│── files/                     # Folder containing files for transfer or operations
│── .gitignore                 # Git ignore file for excluding unnecessary files
│── .gitattributes             # Git attributes file
│── README.md                  # Documentation (this file)
```

## Protocol Description

For details on the protocol and how to implement it on the server side, refer to the [Client-Server C++ v1](https://github.com/iravelmakina/client-server/tree/version-1). This client is specifically designed for **v1** of the server.

---

## License

This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for details.

---

## Contributor

- [@iravelmakina](https://github.com/iravelmakina)
