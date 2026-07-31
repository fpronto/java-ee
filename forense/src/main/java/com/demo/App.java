package com.demo;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.QueryParam;

@jakarta.ws.rs.Path("/census")
public class App {
    private static final Path LOG_FILE = Path.of("citadela_audit.log");
    private static final DateTimeFormatter TIMESTAMP_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS");

    private void logAudit(String args, String stdout) throws IOException {
        String timestamp = LocalDateTime.now().format(TIMESTAMP_FORMAT);
        String line = "[" + timestamp + "] "
                + "<command-name>/census</command-name>"
                + "<command-message>census successful</command-message>"
                + "<command-args>" + args + "</command-args>"
                + "<local-command-stdout>" + stdout + "</local-command-stdout>"
                + System.lineSeparator();
        Files.writeString(LOG_FILE, line, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
    }

    private String getCensus(int size, int offset, boolean showAlert) throws Exception {
        HttpClient client = HttpClient.newHttpClient();
        int countAlive = 0;
        int countDead = 0;
        String finalString = "";

        for (int i = offset; i <= offset + size; i++) {
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
        System.out.println(showAlert);
        finalString += "CENSO: Detetados " + countAlive + " VIVOS e " + countDead
                + " personagens MORTOS nos primeiros " + size
                + " registos\n";

        return finalString;
    }

    @GET
    protected String doGet(@QueryParam("limit") int limit, @QueryParam("offset") int offset,
            @QueryParam("showAlerts") boolean showAlert) {
        try {

            // String limitRaw = req.getParameter("limit");
            // String offsetRaw = req.getParameter("offset");

            // int limit = limitRaw == null ? 20 : Integer.parseInt(limitRaw);
            // int offset = offsetRaw == null ? 1 : Integer.parseInt(offsetRaw);

            // String showAlertRaw = req.getParameter("showAlerts");

            // boolean showAlert = false;

            if (limit < 0 || limit > 50) {
                throw new Exception("O parâmetro 'limit' deve ser um número inteiro entre 0 e 50.");
            }
            if (offset < 0 || offset > 50) {
                throw new Exception("O parâmetro 'offset' deve ser um número inteiro entre 0 e 50.");
            }
            // if (showAlertRaw == null) {
            // showAlert = true;
            // } else if (showAlertRaw.equalsIgnoreCase("true")) {
            // showAlert = true;
            // } else if (showAlertRaw.equalsIgnoreCase("false")) {
            // showAlert = false;
            // } else {
            // throw new Exception("showAlerts must be true or false");
            // }

            String census = getCensus(limit, offset, showAlert);
            logAudit("limit=" + limit + ";offset=" + offset + ";showAlerts=" + showAlert, census.replace("\n", " "));
            return census;
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.setContentType("application/json");
            resp.getWriter()
                    .write("{\"status\":400,\"error\":\"Bad Request\",\"message\":\"%s\"}".formatted(e.getMessage()));
        }
    }

}
