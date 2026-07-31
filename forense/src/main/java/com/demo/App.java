package com.demo;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/census")
public class App {

    private String getCensus(int size, int offset, Boolean showAlert) throws Exception {
        HttpClient client = HttpClient.newHttpClient();
        int countAlive = 0;
        int countDead = 0;
        String finalString = "";

        for (int i = offset; i <= size; i++) {

            HttpRequest requestChar = HttpRequest.newBuilder()
                    .uri(URI.create("https://rickandmortyapi.com/api/character/" + i))
                    .GET()
                    .build();

            HttpResponse<String> responseChar = client.send(requestChar, HttpResponse.BodyHandlers.ofString());
            if (responseChar.statusCode() != 200) {
                throw new Exception(
                        "API respondeu com status " + responseChar.statusCode() + " (possível rate limit): "
                                + responseChar.body());
            }

            ObjectMapper mapper = new ObjectMapper();
            JsonNode jsonNode = mapper.readTree(responseChar.body());
            String status = jsonNode.get("status").asText();
            String specie = jsonNode.get("species").asText();

            List<String> episodes = mapper.convertValue(jsonNode.get("episode"), new TypeReference<List<String>>() {
            });

            if (status.contains("Alive")) {
                countAlive++;
            }
            if (status.contains("Dead")) {
                countDead++;
                if (specie.contains("Alien") && showAlert) {
                    HttpRequest requestEpisode = HttpRequest.newBuilder()
                            .uri(URI.create(episodes.getLast()))
                            .GET()
                            .build();
                    HttpResponse<String> responseEpisode = client.send(requestEpisode,
                            HttpResponse.BodyHandlers.ofString());
                    if (responseEpisode.statusCode() != 200) {
                        throw new Exception(
                                "API respondeu com status " + responseEpisode.statusCode()
                                        + " (possível rate limit): " + responseEpisode.body());
                    }
                    finalString += "[PERIGO] Um Alien foi encontrado morto com o ID " + i + "!\n";
                    JsonNode jsonNodeEpisode = mapper.readTree(responseEpisode.body());
                    String episodeName = jsonNodeEpisode.get("name").asText();
                    finalString += "[ALERTA FORENSE] O último registo do alien morto foi no episódio: "
                            + episodeName
                            + ".\n";
                }
            }
        }
        finalString += "CENSO: Detetados " + countAlive + " VIVOS e " + countDead
                + " personagens MORTOS nos primeiros " + size
                + " registos\n";

        return finalString;
    }

    @GET
    public Response doGet(@QueryParam("limit") int limit, @QueryParam("offset") int offset,
            @QueryParam("showAlerts") Boolean showAlert) {
        try {

            if (limit == 0) {
                limit = 20;
            }
            if (offset == 0) {
                offset = 1;
            }
            if (showAlert == null) {
                showAlert = true;
            }

            if (limit < 0 || limit > 50) {
                throw new Exception("O parâmetro 'limit' deve ser um número inteiro entre 1 e 50.");
            }
            if (offset < 0 || offset > 50) {
                throw new Exception("O parâmetro 'offset' deve ser um número inteiro entre 1 e 50.");
            }

            String census = getCensus(limit, offset, showAlert);

            Log log = new Log();

            log.logAudit("limit=" + limit + ";offset=" + offset + ";showAlerts=" + showAlert,
                    census.replace("\n", " "));

            return Response.ok(census, MediaType.TEXT_PLAIN).build();
        } catch (Exception e) {
            System.out.println("error");
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(e.getMessage())
                    .type(MediaType.TEXT_PLAIN)
                    .build();
        }
    }

}
