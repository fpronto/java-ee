package com.demo;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/census")
public class App extends HttpServlet {
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

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {

            String limitRaw = req.getParameter("limit");
            String offsetRaw = req.getParameter("offset");

            int limit = limitRaw == null ? 20 : Integer.parseInt(limitRaw);
            int offset = offsetRaw == null ? 1 : Integer.parseInt(offsetRaw);

            String showAlertRaw = req.getParameter("showAlerts");

            boolean showAlert = false;

            if (limit < 0 || limit > 50) {
                throw new Exception("O parâmetro 'limit' deve ser um número inteiro entre 0 e 50.");
            }
            if (offset < 0 || offset > 50) {
                throw new Exception("O parâmetro 'offset' deve ser um número inteiro entre 0 e 50.");
            }
            if (showAlertRaw == null) {
                showAlert = true;
            } else if (showAlertRaw.equalsIgnoreCase("true")) {
                showAlert = true;
            } else if (showAlertRaw.equalsIgnoreCase("false")) {
                showAlert = false;
            } else {
                throw new Exception("showAlerts must be true or false");
            }

            String census = getCensus(limit, offset, showAlert);
            resp.getWriter().write(census);
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.setContentType("application/json");
            resp.getWriter()
                    .write("{\"status\":400,\"error\":\"Bad Request\",\"message\":\"%s\"}".formatted(e.getMessage()));
        }
    }

}