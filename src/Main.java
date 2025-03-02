package src;

public class Main {
    public static void main(String[] args) {
        ClientCLI cli = new ClientCLI("./files/");
        cli.run("127.0.0.1", 9080);
    }
}