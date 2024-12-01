package com.example.httpServer;

import java.util.HashMap;
import java.util.Map;

public class HttpRequest {

    private String method;
    private String requestURI;
    private String protocol;
    private Map<String, String> headers;
    private String body;

    public HttpRequest() {
        this.headers = new HashMap<>();
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getRequestURI() {
        return requestURI;
    }

    public void setRequestURI(String requestURI) {
        this.requestURI = requestURI;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getHeader(String name) {
        return headers.get(name.toLowerCase());
    }

    public void setHeader(String name, String value) {
        headers.put(name.toLowerCase(), value);
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
