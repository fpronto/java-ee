import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class Ex3Ameca {
  public static void main(String[] args) throws Exception {
    HttpClient client = HttpClient.newHttpClient();
    int size = 20;
    int countAlive = 0;
    int countDead = 0;

    for (int i = 1; i <= size; i++) {
      HttpRequest request = HttpRequest.newBuilder()
          .uri(URI.create("https://rickandmortyapi.com/api/character/" + i))
          .GET()
          .build();
      HttpResponse<String> res = client.send(request, HttpResponse.BodyHandlers.ofString());
      if (res.body().contains("Alive")) {
        countAlive++;
      } else if (res.body().contains("Dead")) {
        countDead++;
        if (res.body().contains("Alien")) {
          System.out.println("[PERIGO] Um Alien foi encontrado morto com o ID " + i + "!");
        }
      } else {
        // System.out.println(res.body());
      }
    }
    System.out.println(
        "CENSO: Detetados " + countAlive + " VIVOS e " + countDead + " personagens MORTOS nos primeiros " + size
            + " registos");
  }
}
