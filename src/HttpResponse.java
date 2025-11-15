import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

public class HttpResponse {
    private int statusCode;
    private String statusMessage;
    private final Map<String, String> headers;
    private String body;
    private Boolean sent;
    private OutputStream out;

    public HttpResponse(OutputStream outputStream) {
        this.sent = false;
        this.headers = new HashMap<>();
        this.body = "";
        this.statusCode = 200;
        this.statusMessage = "OK";
        this.out = outputStream;
    }

    public HttpResponse status(int code, String message) throws IllegalStateException {
        if (sent) {
            throw new IllegalStateException("Can't change status if request already sent.");
        }
        this.statusCode = code;
        this.statusMessage = message;
        return this;
    }

    public HttpResponse header(String name, String value) throws IllegalStateException {
        if (sent) {
            throw new IllegalStateException("Can't change headers if request already sent.");
        }
        this.headers.put(name, value);
        return this;
    }

    public HttpResponse body(String body) throws IllegalStateException {
        if (sent) {
            throw new IllegalStateException("Can't change body if request already sent.");
        }
        this.body = body;
        return this;
    }

    public void send() throws IOException, IllegalStateException {
        if (sent) {
            throw new IllegalStateException("Response already sent.");
        }

        validateResponse();

        if (!headers.containsKey("Content-Length")) {
            headers.put("Content-Length", String.valueOf(body.length()));
        }

        this.sent = true;
        String EOL = "\r\n";

        PrintWriter writer = new PrintWriter(out, true);

        // write status line
        writer.print("HTTP/1.1 " + statusCode + " " + statusMessage + EOL);

        // write headers
        for (Map.Entry<String, String> header: headers.entrySet()) {
            writer.print(header.getKey() + ": " + header.getValue() + EOL);
        }
        // Write empty line
        writer.print(EOL);

        // write body
        writer.print(body);
        writer.flush();
    }

    private void validateResponse() throws IllegalStateException {
        // Don't pass 'response' - validate THIS object

        if (statusCode == 0) {
            throw new IllegalStateException("Status code not set");
        }

        if (statusCode < 100 || statusCode > 599) {
            throw new IllegalStateException("Invalid status code: " + statusCode);
        }

        if (statusMessage == null || statusMessage.isEmpty()) {
            throw new IllegalStateException("Status message not set");
        }
    }
 }
