import java.io.*;
import java.util.List;
import java.util.Scanner;

public abstract class User implements InternshipActions {
    protected int userid;
    protected String username;
    protected String password;
    protected String role;
    protected String email;

    // Constructor
    public User(int userid, String username, String password, String role, String email) {
        this.userid = userid;
        this.username = username;
        this.password = password;
        this.role = role;
        this.email = email;
    }

    // Getters
    public int getUserid()       { return userid; }
    public String getUsername()  { return username; }
    public String getPassword()  { return password; }
    public String getRole()      { return role; }
    public String getEmail()     { return email; }
    
    // Setters - more encapsulation
    public void setUsername(String username) { this.username = username; }
    public void setEmail(String email)       { this.email = email; }
    
    // Abstract methods (Abstraction)
    public abstract void displayMenu();
    public abstract String getDisplayName();
    public abstract void viewPersonalInfo();
    
    // Concrete method - Change own password (Polymorphism - can be overridden)
    @Override
    public void changePassword(Scanner sc) {
        logAction("attempting password change");
        
        System.out.print("Enter current password: ");
        String current = sc.nextLine().trim();
        if (!this.password.equals(current)) {
            System.out.println("Incorrect current password.");
            return;
        }
        System.out.print("Enter new password    : ");
        String newPass = sc.nextLine().trim();
        if (newPass.isEmpty()) { System.out.println("Password cannot be empty."); return; }
        System.out.print("Confirm new password  : ");
        String confirm = sc.nextLine().trim();
        if (!newPass.equals(confirm)) { System.out.println("Passwords do not match."); return; }

        try {
            List<String> lines = new java.util.ArrayList<>();
            BufferedReader br = new BufferedReader(new FileReader("users.txt"));
            String line;
            while ((line = br.readLine()) != null) {
                String[] d = line.split(",");
                if (d[0].trim().equals(this.username)) { d[1] = newPass; line = String.join(",", d); }
                lines.add(line);
            }
            br.close();
            BufferedWriter bw = new BufferedWriter(new FileWriter("users.txt"));
            for (String l : lines) { bw.write(l); bw.newLine(); }
            bw.close();
            this.password = newPass;
            System.out.println(" Password changed successfully.");
            logAction("password changed successfully");
        } catch (Exception e) {
            System.out.println("Error changing password: " + e.getMessage());
        }
    }
    
    // Default implementations from interface (can be overridden)
    @Override
    public void browseInternships() {
        System.out.println(getDisplayName() + " doesn't have direct internship browsing.");
    }
    
    @Override
    public void viewApplications() {
        System.out.println(getDisplayName() + " is viewing applications.");
    }
}