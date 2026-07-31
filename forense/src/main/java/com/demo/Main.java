package com.demo;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/")
public class Main {

    @GET
    @Produces(MediaType.TEXT_HTML)
    protected String doGet() throws ServletException, IOException {

        return "<form method=\"get\" action=\"/census\">\n" + //
                "  <label>Offset: <input type=\"number\" name=\"offset\" value=\"0\"></label>\n" + //
                "  <label>Limit: <input type=\"number\" name=\"limit\" value=\"10\"></label>\n" + //
                "  <label><input type=\"checkbox\" name=\"showAlerts\" value=\"true\"> Show alerts</label>\n" + //
                "  <button type=\"submit\">Buscar</button>\n" + //
                "</form>";
    }
}