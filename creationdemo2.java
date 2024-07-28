// Singleton Logger class
class Logger {
    // Private static instance of Logger
    private static Logger instance;
    
    // Private constructor to prevent instantiation
    private Logger() {
        // Initialize resources, if necessary
    }
    
    // Public static method to provide access to the instance
    public static Logger getInstance() {
        if (instance == null) {
            instance = new Logger();
        }
        return instance;
    }
    
    // Method to log messages
    public void log(String message) {
        System.out.println("Log: " + message);
    }
}

// Main class to demonstrate Singleton pattern
public class creationdemo2 {
    public static void main(String[] args) {
        // Get the single instance of Logger
        Logger logger1 = Logger.getInstance();
        Logger logger2 = Logger.getInstance();
        
        // Log messages
        logger1.log("This is the first log message.");
        logger2.log("This is the second log message.");
        
        // Verify that both logger1 and logger2 refer to the same instance
        if (logger1 == logger2) {
            System.out.println("Both logger1 and logger2 refer to the same instance.");
        } else {
            System.out.println("logger1 and logger2 refer to different instances.");
        }
    }
}
