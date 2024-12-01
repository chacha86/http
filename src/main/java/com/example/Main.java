package com.example;

import com.example.httpServer.HttpServer;

public class Main {
    public static void main(String[] args) {
        new HttpServer().start(8070);
    }
}