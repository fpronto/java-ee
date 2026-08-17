package com.base_personagem;

import java.util.ArrayList;
import java.util.List;

public class Repository {
  static final List<Personagem> listaP = new ArrayList<Personagem>();

  static {
    listaP.add(new Personagem("Goku", "Saiyajin", "Arroz"));
    listaP.add(new Personagem("Bulma", "Humana", "Sushi"));
    listaP.add(new Personagem("Vegeta", "Saiyajin", "Sushi"));
  }

  public String list() {
    return listaP.toString();
  }

  public String get(int id) {
    return listaP.get(id).toString();
  }

  public void delete(int id) {
    listaP.remove(id);
  }

  public void update(int id, String nome, String especie, String comida) {
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

  public void put(int id, String nome, String especie, String comida) {
    Personagem newP = new Personagem(nome, especie, comida);
    listaP.set(id, newP);
  }

  public String add(String nome, String especie, String comida) {
    Personagem newP = new Personagem(nome, especie, comida);
    listaP.add(newP);
    return listaP.toString();
  }

}
