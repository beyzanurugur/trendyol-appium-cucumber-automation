package utils;

import io.cucumber.core.internal.com.fasterxml.jackson.databind.JsonNode;
import io.cucumber.core.internal.com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;

public class JSONReader {

    public static String getValuesFromJson(String key) {
        try {
            ObjectMapper mapper = new ObjectMapper(); // JSON dosyasını işlemek için ObjectMapper kullanıyoruz
            File jsonFile = new File("src/test/java/pages/values.json"); // JSON dosyasının yolu

            JsonNode rootNode = mapper.readTree(jsonFile); // JSON dosyasını okuyoruz
            JsonNode valueNode = rootNode.path(key); // JSON dosyasındaki "key" değerini çekiyoruz

            return valueNode.asText();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}