package ru.savelying.getdate;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class GetDateServer {
    public static void main(String[] args) throws IOException {
        try (ServerSocket server = new ServerSocket(8080);
             Socket socket = server.accept();
             DataInputStream inStream = new DataInputStream(socket.getInputStream());
             DataOutputStream outStream = new DataOutputStream(socket.getOutputStream());
             Scanner scanner = new Scanner(System.in)) {

            String input = inStream.readUTF();

            while (!input.equals("exit")) {
                System.out.println("Client send: " + input);
                String output = scanner.nextLine();
                outStream.writeUTF(output);
                input = inStream.readUTF();
            }
        }

    }
}
