package logger;

/**
 * Logging class to time execution and report info and error level information
 */
public class Logger {

    static boolean displayLogging = true;

    static long startTime = 0;

    public static void startTiming() {
        startTime = System.currentTimeMillis();
        print("===== Started timing execution...");
    }

    public static void endTiming() {
        print("===== Finished timing. Total time: " + (System.currentTimeMillis() - startTime) + " ms");
    }

    public static void info(String message) {
        print(message);
    }

    public static void error(String message) {
        throw new RuntimeException(message);
    }

    private static void print(String message) {
        if (displayLogging) {
            System.out.println("[LOGGER] " + message);
        }
    }
}
