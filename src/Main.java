import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {
    public static void main(String[] args) {
        final Logger logger = Logger.getLogger(Main.class.getName());
        int port = 8080;
        HttpServer server = new HttpServer(port);
        try {
            server.addConnectionListener((client) -> System.out.println(server.threadPool.getActiveCount()));

            server.registerHandler("/hello", (req, res) -> {
                if (req.getMethod() == Method.GET) {
                    for (int i=0; i <= 100_000; i++) {
                        // do nothing
                        if (i==100_000) System.out.println("Done.");
                    }
                    res.status(200, "OK").body("Hello!\n").send();
                } else if (req.getMethod() == Method.POST) {
                    res.status(200, "OK").body("Hello and " + req.getBody()).send();
                } else {
                    res.status(404, "Not Found").send();
                }
            });
            server.start(() -> System.out.println("Server running on port " + port));
        } catch (IOException e) {
            try {
                server.stop();
            } catch (IOException ex) {
                logger.log(Level.SEVERE, "Error stopping server: ", e);
            }
            logger.log(Level.SEVERE, "Unknown error: ", e);
        }
    }
}