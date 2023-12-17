package sejahterainformationsystem;

public class SessionManager {
    private static SessionManager instance;
    private String loggedInUsername;

    private SessionManager() {
        
    }

    public static synchronized SessionManager getInstance() {
        if (instance == null) {
            instance = new SessionManager();
        }
        return instance;
    }

    public String getLoggedInUsername() {
        return loggedInUsername;
    }

    public void setLoggedInUsername(String username) {
        loggedInUsername = username;
        System.out.println("User " + username + " has logged in.");
    }
    
    public void logout() {
        loggedInUsername = null;
    }
}
