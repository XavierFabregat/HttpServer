import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class HttpRequest {
    private Method method;
    private String path;
    private String version;
    private final Map<String, String> headers;
    private String body; // optional, for POST

    private HttpRequest () {
        this.headers = new HashMap<>();
    }

    public static HttpRequest parse(InputStream inputStream) throws IOException {
        HttpRequest request = new HttpRequest();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

        // First line of the request is the method, path and the protocol version: METHOD PATH PROTOCOL
        String[] parsedReqLine = request.parseRequestLine(reader.readLine());

        try {
            request.method = Method.valueOf(parsedReqLine[0]);
        } catch (IllegalArgumentException e) {
            throw new IOException("Unsupported HTTP method: " + parsedReqLine[0]);
        }
        request.path = parsedReqLine[1];
        request.version = parsedReqLine[2];

        // after the method and protocol come the headers, the end of the headers is delimited by an empty line:
        String header;

        while (!(header = reader.readLine()).isEmpty()) {
            String[] parsedHeader = Arrays.stream(header.split(":", 2))
                            .map(String::trim).toArray(String[]::new);
            request.headers.put(parsedHeader[0], parsedHeader[1]);
        }

        if (request.method != Method.GET && request.method != Method.HEAD) {
            // if the method is not GET, parse until EOF to get the body:
            String contentLengthString = request.headers.get("Content-Length");
            if (contentLengthString != null) {
                int contentLength;
                try {
                    contentLength = Integer.parseInt(contentLengthString);
                } catch (NumberFormatException e) {
                    throw new IOException("Invalid Content-Length header: " + contentLengthString);
                }
                char[] bodyChars = new char[contentLength];
                int bytesRead = reader.read(bodyChars, 0, contentLength);
                if (bytesRead != contentLength) {
                    throw new IOException("Expected " + contentLength + " bytes, but read " + bytesRead);
                }
                request.body = new String(bodyChars);
            }
        }

        return request;
    }

    private String[] parseRequestLine(String requestLine) throws IOException {
        if (requestLine == null) throw new IOException("Invalid HTTP request. Reason: No request line.");
        // we can split it by the spaces
        String[] parsedReqLine = Arrays.stream(requestLine.split(" ", 3))
                .map(String::trim)
                .toArray(String[]::new);

        if (parsedReqLine.length != 3) throw new IOException("Invalid HTTP request. Reason: Invalid request line.");

        return parsedReqLine;
    }

    public Method getMethod() {
        return method;
    }

    public Map<String, String> getHeader() {
        return Collections.unmodifiableMap(headers);
    }

    public String getHeader(String name) {
        return headers.get(name);
    }

    public String getPath() {
        return path;
    }

    public String getBody() {
        return body;
    }

    public String getVersion() {
        return version;
    }

    @Override
    public String toString() {
        return method + " " + path + " " + version;
    }
}
