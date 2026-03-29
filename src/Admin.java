import java.io.*;

public class Admin {
    private int adminid;
    private int userId;

    private static final String USER_FILE = "data/users.txt";
    private static final String COMPANY_FILE = "data/companies.txt";
    private static final String INTERNSHIP_FILE = "data/internships.txt";

    public Admin(int adminid, int userId) {
        this.adminid = adminid;
        this.userId = userId;
    }

    public void manageUsers() {
        System.out.println("Managing users...");
        try (BufferedReader br = new BufferedReader(new FileReader(USER_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException e) {
            System.out.println("Error reading users: " + e.getMessage());
        }
    }

    public void approveCompanies() {
        System.out.println("Approving companies...");
        try (BufferedReader br = new BufferedReader(new FileReader(COMPANY_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException e) {
            System.out.println("Error reading companies: " + e.getMessage());
        }
    }

    public void manageInternships() {
        System.out.println("Managing internships...");
        try (BufferedReader br = new BufferedReader(new FileReader(INTERNSHIP_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException e) {
            System.out.println("Error reading internships: " + e.getMessage());
        }
    }

    public void changeUserRole(User user, String newRole) {
        // Keep UpdateProfile unchanged for username/password/email
        user.UpdateProfile(user.getUsername(), user.getPassword(), user.getEmail());

        // Update role via reflection (role is private)
        try {
            java.lang.reflect.Field roleField = User.class.getDeclaredField("role");
            roleField.setAccessible(true);
            roleField.set(user, newRole);
            System.out.println("Role updated for user: " + user.getUsername() + " -> " + newRole);
        } catch (Exception e) {
            System.out.println("Error changing user role: " + e.getMessage());
        }
    }

    public void viewSystemReports() {
        System.out.println("System Reports:");
        System.out.println("Users report:");
        manageUsers();
        System.out.println("Companies report:");
        approveCompanies();
        System.out.println("Internships report:");
        manageInternships();
    }

    // Getters
    public int getAdminid() { return adminid; }
    public int getUserId() { return userId; }
}