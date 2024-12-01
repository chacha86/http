package com.example.httpServer;

import com.fasterxml.jackson.core.JsonProcessingException;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class HttpServer {

    private ServerSocket serverSocket;
    private volatile boolean running = true;

    public void start(int port) {
        try {
            serverSocket = new ServerSocket(port);
            System.out.println("Server started on port %d...".formatted(port));

            while (running) {
                Socket clientSocket = serverSocket.accept();
                Thread.startVirtualThread(() -> handleClient(clientSocket));
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
        try (clientSocket;
             BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
             BufferedWriter out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()))) {

            HttpRequest request = parseRequest(in);
            HttpResponse response = new HttpResponse();

            handleRequest(request, response);

            out.write(response.toString());
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static HttpRequest parseRequest(BufferedReader in) throws IOException {
        HttpRequest request = new HttpRequest();

        // 첫 번째 줄 파싱 (예: "GET /about HTTP/1.1")
        String requestLine = in.readLine();
        if (requestLine != null) {
            String[] parts = requestLine.split(" ");
            request.setMethod(parts[0]);
            request.setRequestURI(parts[1]);
            request.setProtocol(parts[2]);
        }

        // 헤더 파싱
        String headerLine;
        while ((headerLine = in.readLine()) != null && !headerLine.isEmpty()) {
            String[] headerParts = headerLine.split(": ", 2);
            if (headerParts.length == 2) {
                request.setHeader(headerParts[0], headerParts[1]);
            }
        }

        return request;
    }

    private static void handleRequest(HttpRequest request, HttpResponse response) {
        try {
            if (request.getRequestURI().equals("/about")) {
                response.setBody("I am a body!");
            } else if (request.getRequestURI().equals("/api/v1/posts/1")) {
                Post post = new Post(1L, "Post 1", "Content 1");
                response.setJsonBody(post);
            } else {
                response.setBody("Hello, World!");
            }
        } catch (JsonProcessingException e) {
            response.setStatus(500);
            response.setBody("Error processing JSON");
        }
    }
}
