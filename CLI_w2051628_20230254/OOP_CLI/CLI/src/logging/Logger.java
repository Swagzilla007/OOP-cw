package logging;

/**
 * Singleton logger implementation for system-wide logging.
 * Provides centralized logging functionality with enable/disable capability.
 */
public class Logger {
    // Singleton instance
    private static Logger instance;
    // Flag to control logging state
    private boolean isEnabled;

    /**
     * Private constructor to enforce singleton pattern
     */
    private Logger() {
        this.isEnabled = true;
    }

    /**
     * Returns the singleton instance of the logger
     * @return Logger instance
     */
    public static Logger getInstance() {
        if (instance == null) {
            instance = new Logger();
        }
        return instance;
    }

    /**
     * Logs a message if logging is enabled
     * @param message The message to be logged
     */
    public void log(String message) {
        if (isEnabled) {
            System.out.println("[LOG] " + message);
        }
    }

    /**
     * Enables or disables logging
     * @param enabled true to enable logging, false to disable
     */
    public void setEnabled(boolean enabled) {
        this.isEnabled = enabled;
    }
}