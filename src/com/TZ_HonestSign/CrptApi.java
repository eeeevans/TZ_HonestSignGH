package com.TZ_HonestSign;

//
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.concurrent.*;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.swing.text.Document;

//
public class CrptApi {
    private final int requestLimit;
    private final TimeUnit timeUnit;
    private final Semaphore semaphore; // Управляет
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private final HttpClient client;
    private final ObjectMapper objectMapper;

    public CrptApi(TimeUnit timeUnit, int requestLimit){
        this.timeUnit = timeUnit;
        this.requestLimit = requestLimit;
        this.semaphore = new Semaphore(requestLimit);
        this.client = new HttpClient.newHttpClient();
        this.objectMapper = new ObjectMapper();

        long delay = timeUnit.toMillis(1);
        scheduler.scheduleAtFixedRate(() -> semaphore.release(requestLimit - semaphore.availablePermits()),
                delay, delay, TimeUnit.MILLISECONDS);

    }

    public void createDocument(Document document, String signature) throws Exception {
        semaphore.acquire();
        try {
            String json = objectMapper.writeValueAsString(document);
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(""))
                    .header("", "")
                    .POST(HttpRequest.BodyPublisher.ofString(json))
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println("Response = " + response.body());
        } finally {
            semaphore.release();
        }
    }

    public static class Document {
        public String description;
        public String participantInn;
        public String doc_id;
        public String doc_status;
        public String doc_type;
        public boolean importRequest;
        public String owner_inn;
        public String participant_inn;
        public String producer_inn;
        public String production_date;
        public String production_type
        public Product[] products;
        public String reg_date;
        public String reg_number;

    }

    public static class Product {
        public String certificate_document;
        public String certificate_document_date;
        public String certificate_document_number;
        public String owner_inn;
        public String producer_inn;
        public String production_date;
        public String tnved_code;
        public String uit_code;
        public String uitu_code;

    }

}
