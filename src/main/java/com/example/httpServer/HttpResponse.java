package com.example.httpServer;

import java.util.HashMap;
import java.util.Map;

public class HttpResponse {

    private int status = 200;
    private String statusMessage = "OK";
    private Map<String, String> headers;
    private StringBuilder body;

    public HttpResponse() {
        this.headers = new HashMap<>();
        this.body = new StringBuilder();
        setHeader("Content-Type", "text/html");
    }

    public void setStatus(int status) {
        this.status = status;
        switch (status) {
            case 200 ->
                statusMessage = "OK";
            case 404 ->
                statusMessage = "Not Found";
            case 500 ->
                statusMessage = "Internal Server Error";
            // 필요한 상태 코드 추가 가능
        }
    }

    public int getStatus() {
        return status;
    }

    public void setHeader(String name, String value) {
        headers.put(name, value);
    }

    public String getHeader(String name) {
        return headers.get(name);
    }

    public void setBody(String body) {
        this.body = new StringBuilder(body);
        setHeader("Content-Length", String.valueOf(body.length()));
    }

    public void write(String content) {
        body.append(content);
        setHeader("Content-Length", String.valueOf(body.length()));
    }

    public String toString() {
        StringBuilder responseBuilder = new StringBuilder();
        responseBuilder.append(String.format("HTTP/1.1 %d %s\r\n", status, statusMessage));

        // 헤더 추가
        headers.forEach((name, value)
                -> responseBuilder.append(String.format("%s: %s\r\n", name, value))
        );

        // 빈 줄로 헤더와 본문 구분
        responseBuilder.append("\r\n");

        // 본문 추가
        responseBuilder.append(body);

        return responseBuilder.toString();
    }
}
