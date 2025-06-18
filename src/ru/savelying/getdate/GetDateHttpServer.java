package ru.savelying.getdate;

import ru.savelying.getdate.controller.LikeControl;
import ru.savelying.getdate.controller.ProfileControl;
import ru.savelying.getdate.model.Profile;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class GetDateHttpServer {
    private final int port;
    private final ExecutorService executor;
    private final ProfileControl profileControl;
    private final LikeControl likeControl;

    public GetDateHttpServer(int port, int poolSize, ProfileControl profileControl, LikeControl likeControl) {
        this.port = port;
        this.executor = Executors.newFixedThreadPool(poolSize);
        this.profileControl = profileControl;
        this.likeControl = likeControl;
    }

    public void start() throws IOException {
        try (ServerSocket serverSocket = new ServerSocket(8080)) {
            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("-----Accepted connection from " + socket.getInetAddress().getHostAddress() + "-----");
                executor.submit(() -> processConnection(socket));
            }
        }
    }

    private void processConnection(Socket socket) {
        try (socket; BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             DataOutputStream output = new DataOutputStream(socket.getOutputStream())) {

            while (!input.ready());
            String[] firstParam = null;
            while (input.ready()) {
                String line = input.readLine();
                if (firstParam == null) {
                    firstParam = line.split(" ");
                }
                System.out.println(line);
            }

            String statusStr = "404 Not Found";
            byte[] body = new byte[0];

            if (firstParam != null && firstParam.length == 3) {
                Map<String,String> queryParams = getQueryParams(firstParam[1]);
                String bodyStr = null;
                if (firstParam[1].startsWith("/profile")) {
                    if (firstParam[0].equals("GET")) {
                        if (queryParams.get("id") != null) {
                            Optional<Profile> profileOptional = profileControl.search(Long.parseLong(queryParams.get("id")));
                            if (profileOptional.isPresent()) bodyStr = profileOptional.get().toString();
                        } else bodyStr = profileControl.all().toString();
                    }
                } else if (firstParam[1].startsWith("/like")) {
                    if (firstParam[0].equals("GET")) {
                        bodyStr = likeControl.count() + "";
                    }
                }
                if (bodyStr != null) {
                    statusStr = "200 OK";
                    body = "<p>%s</p>".formatted(bodyStr).getBytes();
                }
            }

            byte[] startLine = "HTTP/1.1 %s\r\n".formatted(statusStr).getBytes();
            byte[] headers = "Content-Type: text/html; charset=utf-8\nContent-length: %s\r\n".formatted(body.length).getBytes();
            byte[] emptyLine = "\r\n".getBytes();

            output.write(startLine);
            output.write(headers);
            output.write(emptyLine);
            output.write(body);
            System.out.println("-----Connection closed-----");

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Map<String, String> getQueryParams(String url) {
        Map<String, String> queryParams = new HashMap<>();
        if (!url.contains("?")) return queryParams;
        String[] params = url.split("\\?")[1].split("&");
        for (String param : params) {
            String[] keyValue = param.split("=");
            queryParams.put(keyValue[0], keyValue[1]);
        }
        return queryParams;
    }
}
