package com.example;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

public class HttpServer {
    public void start(int port) {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server started on port %d...".formatted(port));

            while (true) {
                Socket clientSocket = serverSocket.accept();
                handleClient(clientSocket);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void handleClient(Socket clientSocket) {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
             BufferedWriter out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()))) {

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

        if ( inputLines.get(0).startsWith("GET /about") ) {
            outputSb.append("I am a body!");
        } else {
            outputSb.append("Hello, World!");
        }

        String body = outputSb.toString();

        // Write HTTP response
        String response = """
                HTTP/1.1 200 OK
                Content-Type: text/html
                Content-Length: %d
                
                %s
                """.formatted(body.length(), body).stripIndent().trim();
        return response;
    }
}