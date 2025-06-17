package ru.savelying.getdate;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class GetDateClient {

    public static void main(String[] args) throws IOException {

        try (Socket socket = new Socket("localhost", 8080);
             DataOutputStream outStream = new DataOutputStream(socket.getOutputStream());
             DataInputStream inStream = new DataInputStream(socket.getInputStream());
             Scanner scanner = new Scanner(System.in)) {

            while (scanner.hasNextLine()) {
                String input = scanner.nextLine();
                outStream.writeUTF(input);
                String output = inStream.readUTF();
                System.out.println("Server send: " + output);
            }
        }
    }
}