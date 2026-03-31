import java.io.*;
import java.util.*;

public class Admin extends User {

    public Admin(int userid, String username, String password, String email) {
        super(userid, username, password, "admin", email);
    }

    @Override
    public void displayMenu() {
        System.out.println("\n=============================");
        System.out.println("       ADMIN PANEL");
        System.out.println("=============================");
        System.out.println("1. View All Users");
        System.out.println("2. Change User Role");
        System.out.println("3. Delete User");
        System.out.println("4. View All Internships");
        System.out.println("5. View All Applications");
        System.out.println("6. Reset User Password");
        System.out.println("7. Change My Password");
        System.out.println("8. Logout");
        System.out.print("Choice: ");
    }

    // View all users
    public void viewUsers() {
        try {
            File f = new File("users.txt");
            if (!f.exists()) { System.out.println("No users found."); return; }

            BufferedReader br = new BufferedReader(new FileReader(f));
            String line;
            System.out.println("\n========== ALL USERS ==========");
            int count = 1;
            while ((line = br.readLine()) != null) {
                String[] d = line.split(",");
                if (d.length >= 4) {
                    System.out.println("[" + count + "] Username : " + d[0].trim());
                    System.out.println("     Role     : " + d[2].trim());
                    System.out.println("     Email    : " + d[3].trim());
                    if (d.length > 4 && !d[4].trim().isEmpty())
                        System.out.println("     Name     : " + d[4].trim());
                    System.out.println("--------------------------------");
                    count++;
                }
            }
            br.close();
        } catch (Exception e) {
            System.out.println("Error reading users: " + e.getMessage());
        }
    }

    // Change a user's role
    public void changeUserRole(Scanner sc) {
        viewUsers();
        System.out.print("Enter username to change role: ");
        String targetUsername = sc.nextLine().trim();
        System.out.println("Select new role:");
        System.out.println("1. student  2. company  3. admin  4. school");
        System.out.print("Choice: ");
        String roleChoice = sc.nextLine().trim();
        String newRole;
        switch (roleChoice) {
            case "1": newRole = "student";  break;
            case "2": newRole = "company";  break;
            case "3": newRole = "admin";    break;
            case "4": newRole = "school";   break;
            default:  System.out.println("Invalid role."); return;
        }

        try {
            List<String> lines = new ArrayList<>();
            BufferedReader br = new BufferedReader(new FileReader("users.txt"));
            String line;
            boolean found = false;
            while ((line = br.readLine()) != null) {
                String[] d = line.split(",");
                if (d[0].trim().equals(targetUsername)) {
                    d[2] = newRole;
                    line = String.join(",", d);
                    found = true;
                }
                lines.add(line);
            }
            br.close();

            if (!found) { System.out.println("User not found."); return; }

            BufferedWriter bw = new BufferedWriter(new FileWriter("users.txt"));
            for (String l : lines) { bw.write(l); bw.newLine(); }
            bw.close();
            System.out.println("✔ Role updated to '" + newRole + "' for user: " + targetUsername);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // Delete a user
    public void deleteUser(Scanner sc) {
        viewUsers();
        System.out.print("Enter username to delete (0 to cancel): ");
        String target = sc.nextLine().trim();
        if (target.equals("0")) return;

        try {
            List<String> lines = new ArrayList<>();
            BufferedReader br = new BufferedReader(new FileReader("users.txt"));
            String line;
            boolean found = false;
            while ((line = br.readLine()) != null) {
                String[] d = line.split(",");
                if (d[0].trim().equals(target)) { found = true; continue; }
                lines.add(line);
            }
            br.close();

            if (!found) { System.out.println("User not found."); return; }

            BufferedWriter bw = new BufferedWriter(new FileWriter("users.txt"));
            for (String l : lines) { bw.write(l); bw.newLine(); }
            bw.close();
            System.out.println("✔ User '" + target + "' deleted.");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // Reset any user's password
    public void resetUserPassword(Scanner sc) {
        viewUsers();
        System.out.print("Enter username to reset password (0 to cancel): ");
        String target = sc.nextLine().trim();
        if (target.equals("0")) return;

        System.out.print("Enter new password for '" + target + "': ");
        String newPass = sc.nextLine().trim();
        if (newPass.isEmpty()) { System.out.println("Password cannot be empty."); return; }

        System.out.print("Confirm new password: ");
        String confirm = sc.nextLine().trim();
        if (!newPass.equals(confirm)) { System.out.println("Passwords do not match."); return; }

        try {
            List<String> lines = new ArrayList<>();
            BufferedReader br = new BufferedReader(new FileReader("users.txt"));
            String line;
            boolean found = false;
            while ((line = br.readLine()) != null) {
                String[] d = line.split(",");
                if (d[0].trim().equals(target)) {
                    d[1] = newPass;
                    line = String.join(",", d);
                    found = true;
                }
                lines.add(line);
            }
            br.close();

            if (!found) { System.out.println("User not found."); return; }

            BufferedWriter bw = new BufferedWriter(new FileWriter("users.txt"));
            for (String l : lines) { bw.write(l); bw.newLine(); }
            bw.close();
            System.out.println("✔ Password reset successfully for user: " + target);
        } catch (Exception e) {
            System.out.println("Error resetting password: " + e.getMessage());
        }
    }

    // View all internships
    public void viewAllInternships() {
        try {
            File f = new File("internships.txt");
            if (!f.exists()) { System.out.println("No internships found."); return; }

            BufferedReader br = new BufferedReader(new FileReader(f));
            String line;
            System.out.println("\n========== ALL INTERNSHIPS ==========");
            while ((line = br.readLine()) != null) {
                String[] d = line.split("\\|");
                if (d.length >= 6) {
                    System.out.println("ID      : " + d[0].trim());
                    System.out.println("Company : " + d[1].trim());
                    System.out.println("Title   : " + d[2].trim());
                    System.out.println("Details : " + d[3].trim());
                    System.out.println("Slots   : " + d[4].trim());
                    System.out.println("Status  : " + d[5].trim());
                    System.out.println("--------------------------------------");
                }
            }
            br.close();
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // View all applications
    public void viewAllApplications() {
        try {
            File f = new File("applications.txt");
            if (!f.exists()) { System.out.println("No applications found."); return; }

            BufferedReader br = new BufferedReader(new FileReader(f));
            String line;
            System.out.println("\n========== ALL APPLICATIONS ==========");
            while ((line = br.readLine()) != null) {
                String[] d = line.split("\\|");
                if (d.length >= 8) {
                    System.out.println("App ID   : " + d[0].trim());
                    System.out.println("Student  : " + d[3].trim() + " (" + d[2].trim() + ")");
                    System.out.println("Company  : " + d[5].trim());
                    System.out.println("Position : " + d[6].trim());
                    System.out.println("Status   : " + d[7].trim());
                    System.out.println("--------------------------------------");
                }
            }
            br.close();
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}