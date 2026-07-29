import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class Ex1VarredorPortais {
  public static void main(String[] args) throws Exception {
    HttpClient client = HttpClient.newHttpClient();

    for (int i = 1; i <= 20; i++) {
      HttpRequest request = HttpRequest.newBuilder()
          .uri(URI.create("https://rickandmortyapi.com/api/character/" + i))
          .GET()
          .build();
      HttpResponse<String> res = client.send(request, HttpResponse.BodyHandlers.ofString());
      System.out.println(res.body() + "\n");
    }
  }
}
