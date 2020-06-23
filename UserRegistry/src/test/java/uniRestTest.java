import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

public class uniRestTest {
    public static void main(String[] args) throws UnirestException {
        Unirest.setTimeouts(0, 0);
        HttpResponse<String> response = Unirest.post("http://localhost:8080/addUser/")
                .header("Content-Type", "application/json")
                .body("{\n    \"k110\": \"v15\"\n}")
                .asString();

        System.out.println(response.getBody());

    }
}
