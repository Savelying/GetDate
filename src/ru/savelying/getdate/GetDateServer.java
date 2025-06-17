package ru.savelying.getdate;

import ru.savelying.getdate.controller.ProfileControl;
import ru.savelying.getdate.dao.ProfileDAO;
import ru.savelying.getdate.service.ProfileService;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class GetDateServer {
    public static void main(String[] args) throws IOException {

        ProfileControl profileControl = new ProfileControl(new ProfileService(new ProfileDAO()));

        try (ServerSocket server = new ServerSocket(8080);
             Socket socket = server.accept();
             DataInputStream inStream = new DataInputStream(socket.getInputStream());
             DataOutputStream outStream = new DataOutputStream(socket.getOutputStream())) {

            String input = inStream.readUTF();
            String output = "";

            while (!input.equals("exit")) {
                if (input.startsWith("create ")) {
                    output = profileControl.create(input.split("create ")[1]);
                } else if (input.startsWith("update ")) {
                    output = profileControl.update(input.split("update ")[1]);
                } else if (input.startsWith("delete ")) {
                    output = profileControl.delete(input.split("delete ")[1]);
                } else if (input.startsWith("find ")) {
                    output = profileControl.search(input.split("find ")[1]);
                } else if (input.startsWith("all")) {
                    output = profileControl.all();
                } else output = "Unknown command";

                System.out.println("Client request: " + input);
                outStream.writeUTF(output);
                input = inStream.readUTF();
            }
        }

    }
}
