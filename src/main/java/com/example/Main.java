package com.example;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
public class Main {
    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(8080)) {
            System.out.println("Server started on port 8080...");

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

            // Read HTTP request
            String line;
            while (!(line = in.readLine()).isEmpty()) {
                System.out.println(line);
            }

            // Write HTTP response
            String response = """
                    HTTP/1.1 200 OK
                    Content-Type: text/html
                    Content-Length: 13
                    
                    Hello, World!
                    """;
            out.write(response);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}