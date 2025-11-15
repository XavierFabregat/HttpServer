import java.io.IOException;

@FunctionalInterface
public interface Handler {
    void run(HttpRequest request, HttpResponse response) throws IOException;
}
