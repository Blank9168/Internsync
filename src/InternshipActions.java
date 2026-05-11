// InternshipActions.java
import java.util.*;

public interface InternshipActions {
    
    // Common actions for all users
    void displayMenu();
    void changePassword(java.util.Scanner sc);
    
    // Actions related to internships
    List<String[]> browseInternships();  // implemented differently by Student, Company, Admin
    List<String[]>  viewApplications();   // viewing applications from different perspectives
    List<String[]>  viewAllInternships(); // for Admin to see all internships in the system
    
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