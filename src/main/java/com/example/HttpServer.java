package com.example;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

public class HttpServer {

    private ServerSocket serverSocket;
    private volatile boolean running = true;

    public void start(int port) {
        try {
            serverSocket = new ServerSocket(port);
            System.out.println("Server started on port %d...".formatted(port));

            while (running) {
                Socket clientSocket = serverSocket.accept();
                handleClient(clientSocket);
            }
        } catch (IOException e) {
            if (running) {
                e.printStackTrace();
            }
        }
    }

    public void stop() {
        running = false;
        try {
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void handleClient(Socket clientSocket) {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream())); BufferedWriter out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()))) {

            List<String> inputLines = in.lines().takeWhile(line -> !line.isEmpty()).toList();

            String response = getResponse(inputLines);
            out.write(response);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String getResponse(List<String> inputLines) {
        StringBuilder outputSb = new StringBuilder();
        String contentType = "text/html";

        if (inputLines.get(0).startsWith("GET /about")) {
            outputSb.append("I am a body!");
        } else if (inputLines.get(0).startsWith("GET /api/v1/posts/1")) {
            outputSb.append("""
                    {
                        "id": 1,
                        "title": "Post 1",
                        "content": "Content 1"
                    }
                    """.stripIndent().trim());
            contentType = "application/json";
        } else {
            outputSb.append("Hello, World!");
        }


        String body = outputSb.toString();

        // Write HTTP response
        String response = """
                HTTP/1.1 200 OK
                Content-Type: %s
                Content-Length: %d
                
                %s
                """.formatted(contentType, body.length(), body).stripIndent().trim();
        return response;
    }
}
