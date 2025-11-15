import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.logging.Level;
import java.util.logging.Logger;

public class HttpServer {
    private static final Logger logger = Logger.getLogger(HttpServer.class.getName());
    private ServerSocket serverSocket;
    private boolean running = false;
    private final int port;
    public final ThreadPoolExecutor threadPool;

    private final Map<String, Handler> routeHandlers;

    private OnConnectionCallback onConnectionListener;

    public HttpServer(int port) {
        this.port = port;
        this.routeHandlers = new HashMap<>();
        this.threadPool = (ThreadPoolExecutor) Executors.newCachedThreadPool();
    }

    public void start(Runnable onStart) throws IOException {
        serverSocket = new ServerSocket(port);
        running = true;

        // call the callback after server is ready
        if (onStart != null) onStart.run();

        while (running) {
            Socket clientSocket = serverSocket.accept();
            if (onConnectionListener != null) onConnectionListener.run(clientSocket);

            // submit to thread pool for processing instead of calling it directly
            threadPool.submit(() -> handleClient(clientSocket));
        }
    }

    public void stop() throws IOException {
        running = false;
        if (serverSocket != null) {
            serverSocket.close();
        }
        threadPool.shutdown();
    }


    private void handleClient(Socket clientSocket) {
        try {
            // Read request
            HttpRequest request = HttpRequest.parse(clientSocket.getInputStream());

            Handler handler = routeHandlers.get(request.getPath());

            if (handler != null) {
                handler.run(request, new HttpResponse(clientSocket.getOutputStream()));
            } else {
                HttpResponse response = new HttpResponse(clientSocket.getOutputStream());
                response.status(404, "Not Found").send();
            }

            clientSocket.close();
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error handling client", e);
        }
    }

    public void addConnectionListener(OnConnectionCallback onConnectionCB) {
        onConnectionListener = onConnectionCB;
    }


    public void registerHandler(String route, Handler handler) {
        this.routeHandlers.put(route, handler);
    }
}
