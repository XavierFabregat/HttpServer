import java.io.IOException;
public class Main {
    public static void main(String[] args) {
        int port = 8080;
        HttpServer server = new HttpServer(port);
        try {
            server.addConnectionListener((client) -> System.out.println("New connection has been detected: " + client));

            server.registerHandler("/hello", (req, res) -> {
                if (req.getMethod() == Method.GET) {
                    res.status(200, "OK").body("Hello!").send();
                } else if (req.getMethod() == Method.POST) {
                    res.status(200, "OK").body("Hello and " + req.getBody()).send();
                } else {
                    res.status(404, "Not Found").send();
                }
            });
            server.start(() -> System.out.println("Server running on port " + port));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}