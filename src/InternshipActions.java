// InternshipActions.java
public interface InternshipActions {
    
    // Common actions for all users
    void displayMenu();
    void changePassword(java.util.Scanner sc);
    
    // Actions related to internships
    void browseInternships();  // implemented differently by Student, Company, Admin
    void viewApplications();   // viewing applications from different perspectives
    
    // Default method (Java 8+ feature)
    default void logAction(String action) {
        System.out.println("[LOG] " + getClass().getSimpleName() + " performed: " + action);
    }
    
    // Static method
    static void showSystemInfo() {
        System.out.println("InternSync - Connecting Students, Companies & Schools");
        System.out.println("Version: 2.0 with OOP Features");
    }
}