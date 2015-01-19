package maxisoft.rasdial;

import java.io.IOException;

public interface RasdialInterface {
    static String BASE_CMD = "rasdial";

    String getCurrentVpn() throws IOException;
    boolean isConnected() throws IOException;
    boolean connect(String user, String password) throws IOException, InterruptedException;
    boolean disconnect() throws IOException, InterruptedException;
    boolean reconnect(String user, String password) throws IOException, InterruptedException;
}
