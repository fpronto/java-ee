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

public class App {

    static final List<Personagem> listaP = new ArrayList<Personagem>();

    static {
        listaP.add(new Personagem("Goku", "Saiyajin", "Arroz"));
        listaP.add(new Personagem("Bulma", "Humana", "Sushi"));
        listaP.add(new Personagem("Vegeta", "Saiyajin", "Sushi"));
    }

    @GET
    public String getChar() {
        return listaP.toString();
    }

    @GET
    @Path("/{id}")
    public String getIndidualChar(@PathParam("id") int id) {
        if (id < 0 || id >= listaP.size())
            throw new NotFoundException();
        return listaP.get(id).toString();
    }

    @POST
    public String createChar(@QueryParam("nome") String nome, @QueryParam("especie") String especie,
            @QueryParam("comida") String comida) {
        Personagem p = new Personagem(nome, especie, comida);
        listaP.add(p);
        return listaP.toString();
    }

    @DELETE
    public void deleteChar(@QueryParam("id") int id) {
        if (id < 0 || id >= listaP.size())
            throw new NotFoundException("Not");
        listaP.remove(id);
    }

    @PUT
    @Path("/{id}")
    public void putChar(@PathParam("id") int id, @QueryParam("nome") String nome,
            @QueryParam("especie") String especie, @QueryParam("comida") String comida) {
        if (id < 0 || id >= listaP.size())
            throw new NotFoundException("Not");
        listaP.set(id, new Personagem(nome, especie, comida));
    }

    @PATCH
    @Path("/{id}")
    public void updateChar(@PathParam("id") int id, @QueryParam("nome") String nome,
            @QueryParam("especie") String especie, @QueryParam("comida") String comida) {
        if (id < 0 || id >= listaP.size())
            throw new NotFoundException("Not");
        Personagem newP = listaP.get(id);
        if (nome != null && !nome.isEmpty()) {
            newP.setNome(nome);
        }
        if (especie != null && !especie.isEmpty()) {
            newP.setEspecie(especie);
        }
        if (comida != null && !comida.isEmpty()) {
            newP.setComidaFavorita(comida);
        }
        listaP.set(id, newP);
    }
}
