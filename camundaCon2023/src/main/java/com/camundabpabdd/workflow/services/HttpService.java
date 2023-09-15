package com.camundabpabdd.workflow.services;



import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Service
public class HttpService {

    public HttpResponse<String> httpCall(String url, String body) throws Exception {
        // create a client
        HttpClient client = HttpClient.newHttpClient();

        // create a request
        HttpRequest.Builder builder = HttpRequest.newBuilder();
        builder.uri(URI.create(url));
        builder.method("POST", HttpRequest.BodyPublishers.ofString(body));
        HttpRequest request = builder.build();

        try {
            return client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new Exception(e);
        }
    }
}
