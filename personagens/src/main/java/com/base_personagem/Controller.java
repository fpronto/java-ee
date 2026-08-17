package com.base_personagem;

import java.util.ArrayList;
import java.util.List;

import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.PATCH;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.QueryParam;

/**
 * Hello world!
 *
 */
@Path("/char")

public class Controller {
    Service s = new Service();

    @GET
    public String getChar() {
        return s.list();
    }

    @GET
    @Path("/{id}")
    public String getIndidualChar(@PathParam("id") int id) {
        return s.get(id);
    }

    @POST
    public String createChar(@QueryParam("nome") String nome, @QueryParam("especie") String especie,
            @QueryParam("comida") String comida) {
        return s.add(nome, especie, comida);
    }

    @DELETE
    public void deleteChar(@QueryParam("id") int id) {
        s.delete(id);
    }

    @PUT
    @Path("/{id}")
    public void putChar(@PathParam("id") int id, @QueryParam("nome") String nome,
            @QueryParam("especie") String especie, @QueryParam("comida") String comida) {
        s.put(id, nome, especie, comida);
    }

    @PATCH
    @Path("/{id}")
    public void updateChar(@PathParam("id") int id, @QueryParam("nome") String nome,
            @QueryParam("especie") String especie, @QueryParam("comida") String comida) {
        s.update(id, nome, especie, comida);
    }
}
