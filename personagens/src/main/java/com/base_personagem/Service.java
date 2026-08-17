package com.base_personagem;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.NotFoundException;

@ApplicationScoped()
public class Service {
  @Inject
  Repository r;

  public String list() {
    return r.list();
  }

  public String get(int id) {
    if (id < 0 || id >= r.size()) {
      throw new NotFoundException("Not");
    }
    return r.get(id);
  }

  public String add(String nome, String especie, String comida) {
    if (comida == null || comida.isBlank()) {
      comida = "Donuts";
    }
    return r.add(nome, especie, comida);
  }

  public void delete(int id) {
    if (id < 0 || id >= r.size()) {
      throw new NotFoundException("Not");
    }
    r.delete(id);

  }

  public void update(int id, String nome, String especie, String comida) {
    if (id < 0 || id >= r.size()) {
      throw new NotFoundException("Not");
    }
    if (comida == null || comida.isBlank()) {
      comida = "Donuts";
    }
    r.update(id, nome, especie, comida);

  }

  public void put(int id, String nome, String especie, String comida) {
    if (id < 0 || id >= r.size()) {
      throw new NotFoundException("Not");
    }
    if (comida == null || comida.isBlank()) {
      comida = "Donuts";
    }
    r.put(id, nome, especie, comida);
  }

}
