package com.demo;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Hello world!
 *
 */
public class App {
    public static void main(String[] args) throws Exception {
        HttpClient client = HttpClient.newHttpClient();
        int size = 20;
        int countAlive = 0;
        int countDead = 0;

        for (int i = 1; i <= size; i++) {
            HttpRequest requestChar = HttpRequest.newBuilder()
                    .uri(URI.create("https://rickandmortyapi.com/api/character/" + i))
                    .GET()
                    .build();
            HttpResponse<String> responseChar = client.send(requestChar, HttpResponse.BodyHandlers.ofString());
            ObjectMapper mapper = new ObjectMapper();
            JsonNode jsonNode = mapper.readTree(responseChar.body());
            String status = jsonNode.get("status").asText();
            String specie = jsonNode.get("species").asText();

            // JsonNode episodesNode = jsonNode.get("episode");
            List<String> episodes = mapper.convertValue(jsonNode.get("episode"), new TypeReference<List<String>>() {
            });

            if (status.contains("Alive")) {
                countAlive++;
            }
            if (status.contains("Dead")) {
                countDead++;
                if (specie.contains("Alien")) {
                    HttpRequest requestEpisode = HttpRequest.newBuilder()
                            .uri(URI.create(episodes.getLast()))
                            .GET()
                            .build();
                    HttpResponse<String> responseEpisode = client.send(requestEpisode,
                            HttpResponse.BodyHandlers.ofString());

                    System.out.println("[PERIGO] Um Alien foi encontrado morto com o ID " + i + "!");
                    JsonNode jsonNodeEpisode = mapper.readTree(responseEpisode.body());
                    String episodeName = jsonNodeEpisode.get("name").asText();
                    System.err.println(
                            "[ALERTA FORENSE] O último registo do alien morto foi no episódio: " + episodeName + ".");
                }
            }
        }
        System.out.println(
                "CENSO: Detetados " + countAlive + " VIVOS e " + countDead + " personagens MORTOS nos primeiros " + size
                        + " registos");
    }
}