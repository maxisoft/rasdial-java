package maxisoft.rasdial;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.requireNonNull;

public class Rasdial implements RasdialInterface {

    private final String vpn;

    public Rasdial(String vpn) {
        this.vpn = requireNonNull(vpn);
    }

    @Override
    public String getCurrentVpn() throws IOException {
        ProcessBuilder pb = new ProcessBuilder(BASE_CMD);
        pb.redirectErrorStream(true);
        Process process = pb.start();
        try(BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))){
            String line;
            List<String> lines = new ArrayList<>(5);
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
            return lines.size() > 2 ? lines.get(1) :null;
        }
    }

    @Override
    public boolean isConnected() throws IOException {
        String currentVpn = getCurrentVpn();
        return currentVpn != null && currentVpn.equals(vpn);
    }

    @Override
    public boolean connect(String user, String password) throws IOException, InterruptedException {
        ProcessBuilder pb = new ProcessBuilder(BASE_CMD, "\"" + vpn + "\"", "\"" + user + "\"", "\"" + password + "\"");
        return pb.start().waitFor() == 0;
    }

    @Override
    public boolean disconnect() throws IOException, InterruptedException {
        ProcessBuilder pb = new ProcessBuilder(BASE_CMD, "\"" + vpn + "\"", "/disconnect");
        return pb.start().waitFor() == 0;
    }

    @Override
    public boolean reconnect(String user, String password) throws IOException, InterruptedException {
        return disconnect() && connect(user, password);
    }

    public String getVpn() {
        return vpn;
    }
}
