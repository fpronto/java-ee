package com.demo;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/")
public class Main extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html");
        resp.getWriter().write("<form method=\"get\" action=\"/census\">\n" + //
                "  <label>Offset: <input type=\"number\" name=\"offset\" value=\"0\"></label>\n" + //
                "  <label>Limit: <input type=\"number\" name=\"limit\" value=\"10\"></label>\n" + //
                "  <label><input type=\"checkbox\" name=\"showAlerts\" value=\"true\"> Show alerts</label>\n" + //
                "  <button type=\"submit\">Buscar</button>\n" + //
                "</form>");
    }
}