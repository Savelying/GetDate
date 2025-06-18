package ru.savelying.getdate;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class GetDateClient {

    public static void main(String[] args) throws IOException, InterruptedException {

        try (HttpClient httpClient = HttpClient.newBuilder().version(HttpClient.Version.HTTP_2).build();) {

            HttpRequest accept = HttpRequest.newBuilder(URI.create("https://www.google.com"))
                    .setHeader("Accept", "application/json")
                    .setHeader("Content-Type", "application/json")
                    .GET()
                    .build();

            HttpResponse<String> send = httpClient.send(accept, HttpResponse.BodyHandlers.ofString());

            Map<String, List<String>> headers = send.headers().map();

            System.out.println(send.statusCode());
            System.out.println();
            System.out.println(headers);
            System.out.println(send.body());
        }


//        try (Socket socket = new Socket("localhost", 8080);
//             DataOutputStream outStream = new DataOutputStream(socket.getOutputStream());
//             DataInputStream inStream = new DataInputStream(socket.getInputStream());
//             Scanner scanner = new Scanner(System.in)) {
//
//            while (scanner.hasNextLine()) {
//                String input = scanner.nextLine();
//                outStream.writeUTF(input);
//                String output = inStream.readUTF();
//                System.out.println(output);
//            }
//        }
    }
}