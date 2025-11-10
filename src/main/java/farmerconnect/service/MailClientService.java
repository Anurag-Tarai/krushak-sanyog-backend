package farmerconnect.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class MailClientService {

    private final RestTemplate restTemplate;

    @Value("${mail.service.base.url:http://ec2-43-205-243-164.ap-south-1.compute.amazonaws.com:8080}")
    private String mailServiceBaseUrl;

    public MailClientService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String sendEmail(String to, String subject, String body) {
        String url = mailServiceBaseUrl + "/api/mail/text";

        // Create JSON body as a Map
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("to", to);
        requestBody.put("subject", subject);
        requestBody.put("body", body);

        // Set headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(MediaType.parseMediaTypes("*/*"));

        HttpEntity<Map<String, String>> request = new HttpEntity<>(requestBody, headers);

        try {
            ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);
            return "Email sent successfully. Response: " + response.getBody();
        } catch (Exception e) {
            System.err.println("Error sending email: " + e.getMessage());
            return "Failed to send email: " + e.getMessage();
        }
    }
}
