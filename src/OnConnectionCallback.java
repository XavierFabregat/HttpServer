import java.net.Socket;

@FunctionalInterface
public interface OnConnectionCallback {
    void run(Socket client);
}
