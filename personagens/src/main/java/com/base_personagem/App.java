package com.base_personagem;

import java.util.ArrayList;
import java.util.List;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.QueryParam;

/**
 * Hello world!
 *
 */
@Path("/char")
public class App {
    // ponytail: static list = one shared instance for app lifetime, no
    // CDI/beans.xml needed
    static final List<Personagem> listaP = new ArrayList<Personagem>();

    //
    @GET
    public String getChar() {
        return listaP.toString();
    }

    @POST
    public String createChar(@QueryParam("nome") String nome, @QueryParam("especie") String especie,
            @QueryParam("comida") String comida) {
        Personagem p = new Personagem(nome, especie, comida);
        listaP.add(p);
        return listaP.toString();
    }

}
