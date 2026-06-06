package services;

import config.JsonConfig;
import exceptions.AppException;
import exceptions.NotFoundException;
import exceptions.ValidationException;
import models.Address;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class CepService {
    private final HttpClient httpClient;

    public CepService() {
        this.httpClient = HttpClient.newHttpClient();
    }

    public Address lookup(String zipCode) {
        String normalized = normalize(zipCode);

        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://viacep.com.br/ws/" + normalized + "/json/"))
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() < 200 || response.statusCode() >= 300) {
                throw new AppException("ViaCEP returned HTTP " + response.statusCode() + ".", 502);
            }

            Address address = JsonConfig.mapper().readValue(response.body(), Address.class);
            if (address.isError()) {
                throw new NotFoundException("Zip code not found.");
            }
            return address;
        } catch (IOException exception) {
            throw new AppException("Could not parse ViaCEP response.", 502);
        } catch (InterruptedException exception) {
            Thread.currentThread().interrupt();
            throw new AppException("ViaCEP request was interrupted.", 502);
        }
    }

    private String normalize(String zipCode) {
        if (zipCode == null) {
            throw new ValidationException("zipCode is required.");
        }
        String digits = zipCode.replaceAll("\\D", "");
        if (digits.length() != 8) {
            throw new ValidationException("zipCode must have 8 digits.");
        }
        return digits;
    }
}
