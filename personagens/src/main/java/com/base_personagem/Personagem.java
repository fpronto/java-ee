package com.base_personagem;

public class Personagem extends Object {
  private String nome;
  private String especie;
  private String comidaFavorita;

  public String getNome() {
    return nome;
  }

  public void setNome(String nome) {
    this.nome = nome;
  }

  public String getEspecie() {
    return especie;
  }

  public void setEspecie(String especie) {
    this.especie = especie;
  }

  public String getComidaFavorita() {
    return comidaFavorita;
  }

  public void setComidaFavorita(String comidaFavorita) {
    this.comidaFavorita = comidaFavorita;
  }

  Personagem() {
    super();
  }

  Personagem(String nome, String especie, String comidaFavorita) {
    super();
    this.nome = nome;
    this.especie = especie;
    this.comidaFavorita = comidaFavorita;
  }

  @Override
  public boolean equals(Object p) {
    if (this == p) {
      return true;
    }
    if (!(p instanceof Personagem)) {
      return false;
    }
    Personagem innerP = (Personagem) p;
    return this.nome != null
        && this.nome.equalsIgnoreCase(innerP.nome)
        && this.especie != null
        && this.especie.equalsIgnoreCase(innerP.especie)
        && this.comidaFavorita != null
        && this.comidaFavorita.equalsIgnoreCase(innerP.comidaFavorita);
  }

  @Override
  public String toString() {
    return "Personagem [nome=" + nome + ", especie=" + especie + ", comidaFavorita=" + comidaFavorita + "]";
  }

}
