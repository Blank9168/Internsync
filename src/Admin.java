import java.io.*;
import java.util.*;

public class Admin extends User {

    // Constructor
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
        System.out.println("7. Manage Student Resumes");
        System.out.println("8. View All Student Skills");
        System.out.println("9. Change My Password");
        System.out.println("10. Logout");
        System.out.print("Choice: ");
    }

    // View all users
    public void viewUsers() {
        try {
            // Check if users file exists
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
            // Check if users file exists
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

            // Write back the updated users to the file
            BufferedWriter bw = new BufferedWriter(new FileWriter("users.txt"));
            for (String l : lines) { bw.write(l); bw.newLine(); }
            bw.close();
            System.out.println(" Role updated to '" + newRole + "' for user: " + targetUsername);
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
            // Check if users file exists
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

            // Write back the updated users to the file
            BufferedWriter bw = new BufferedWriter(new FileWriter("users.txt"));
            for (String l : lines) { bw.write(l); bw.newLine(); }
            bw.close();
            System.out.println(" User '" + target + "' deleted.");
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
            // Check if users file exists
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

            // Write back the updated users to the file
            BufferedWriter bw = new BufferedWriter(new FileWriter("users.txt"));
            for (String l : lines) { bw.write(l); bw.newLine(); }
            bw.close();
            System.out.println(" Password reset successfully for user: " + target);
        } catch (Exception e) {
            System.out.println("Error resetting password: " + e.getMessage());
        }
    }

    // View all internships
    public void viewAllInternships() {
        try {
            // Check if internships file exists
            File f = new File("internships.txt");
            if (!f.exists()) { System.out.println("No internships found."); return; }

            BufferedReader br = new BufferedReader(new FileReader(f));
            String line;
            System.out.println("\n========== ALL INTERNSHIPS ==========");
            while ((line = br.readLine()) != null) {
                String[] d = line.split("\\|");
                if (d.length >= 6) {
                    System.out.println("ID        : " + d[0].trim());
                    System.out.println("Company   : " + d[1].trim());
                    System.out.println("Title     : " + d[2].trim());
                    System.out.println("Details   : " + d[3].trim());
                    System.out.println("Slots     : " + d[4].trim());
                    System.out.println("Status    : " + d[5].trim());
                    System.out.println("Req.Skills: " + (d.length >= 7 && !d[6].trim().isEmpty() ? d[6].trim() : "None"));
                    System.out.println("--------------------------------------");
                }
            }
            br.close();
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // View all student skills
    public void viewAllStudentSkills() {
        try {
            // Check if student skills file exists
            File f = new File("student_skills.txt");
            if (!f.exists()) { System.out.println("No student skills on record yet."); return; }

            BufferedReader br = new BufferedReader(new FileReader(f));
            String line;
            boolean found = false;
            System.out.println("\n========== ALL STUDENT SKILLS ==========");
            while ((line = br.readLine()) != null) {
                String[] d = line.split("\\|");
                if (d.length >= 2 && !d[1].trim().isEmpty()) {
                    System.out.println("Student : " + d[0].trim());
                    System.out.println("Skills  : " + d[1].trim());
                    System.out.println("----------------------------------------");
                    found = true;
                } else if (d.length >= 1) {
                    System.out.println("Student : " + d[0].trim());
                    System.out.println("Skills  : (none listed)");
                    System.out.println("----------------------------------------");
                    found = true;
                }
            }
            br.close();
            if (!found) System.out.println("No student skills on record yet.");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // View all applications
    public void viewAllApplications() {
        try {
            // Check if applications file exists
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

    // Admin: list all resumes, read info, or delete a file
    public void manageResumes(Scanner sc) {
        // Check if resume status file exists
        File statusFile = new File("resume_status.txt");
        if (!statusFile.exists()) { System.out.println("No resumes uploaded yet."); return; }

        List<String[]> resumes = new ArrayList<>();
        try {
            // Read through resume_status.txt and list all resumes with their status
            BufferedReader br = new BufferedReader(new FileReader(statusFile));
            String line;
            int count = 1;
            System.out.println("\n========== ALL STUDENT RESUMES ==========");
            while ((line = br.readLine()) != null) {
                String[] d = line.split("\\|");
                // username|filePath|schoolStatus|schoolNote|uploadDate
                if (d.length >= 5) {
                    System.out.println("[" + count + "] Student  : " + d[0].trim());
                    System.out.println("    File     : " + d[1].trim());
                    System.out.println("    Uploaded : " + d[4].trim());
                    System.out.println("    Status   : " + d[2].trim());
                    System.out.println("    Note     : " + d[3].trim());
                    System.out.println("-----------------------------------------");
                    resumes.add(d);
                    count++;
                }
            }
            br.close();
        } catch (Exception e) { System.out.println("Error reading resumes: " + e.getMessage()); return; }

        if (resumes.isEmpty()) { System.out.println("No resumes found."); return; }

        System.out.print("\nEnter number to manage (0 to cancel): ");
        int choice;
        // Validate that the input is an integer and within the valid range
        try { choice = Integer.parseInt(sc.nextLine().trim()); }
        catch (NumberFormatException e) { System.out.println("Invalid input."); return; }
        if (choice == 0) return;
        if (choice < 1 || choice > resumes.size()) { System.out.println("Invalid selection."); return; }

        String[] sel       = resumes.get(choice - 1);
        String studentUser = sel[0].trim();
        String filePath    = sel[1].trim();
        File pdf           = new File(filePath);

        System.out.println("\nAction:");
        System.out.println("1. Read (view file info)");
        System.out.println("2. Delete resume file");
        System.out.println("0. Cancel");
        System.out.print("Choice: ");
        String action = sc.nextLine().trim();

        if (action.equals("0")) return;

        if (action.equals("1")) {
            System.out.println("\n--- RESUME: " + studentUser + " ---");
            System.out.println("File     : " + pdf.getName());
            System.out.println("Status   : " + sel[2].trim());
            System.out.println("Note     : " + sel[3].trim());
            System.out.println("Uploaded : " + sel[4].trim());
            if (pdf.exists()) {
                System.out.println("Size     : " + pdf.length() + " bytes (" + (pdf.length() / 1024) + " KB)");
                System.out.println("Path     : " + pdf.getAbsolutePath());
                System.out.println("(Open the path above in a PDF viewer to read the full resume.)");
            } else {
                System.out.println("File not found on disk.");
            }

        } else if (action.equals("2")) {
            System.out.print("Confirm delete resume of '" + studentUser + "'? (yes/no): ");
            String confirm = sc.nextLine().trim();
            if (!confirm.equalsIgnoreCase("yes")) { System.out.println("Cancelled."); return; }

            // Delete the physical PDF file
            boolean fileDeleted = false;
            if (pdf.exists()) {
                fileDeleted = pdf.delete();
            } else {
                System.out.println("Note: File not found on disk, removing record only.");
                fileDeleted = true;
            }

            if (fileDeleted) {
                // Remove entry from resume_status.txt
                try {
                    // Check if resume status file exists
                    List<String> lines = new ArrayList<>();
                    BufferedReader br = new BufferedReader(new FileReader(statusFile));
                    String line;
                    while ((line = br.readLine()) != null) {
                        String[] d = line.split("\\|");
                        if (d[0].trim().equals(studentUser)) continue; // skip — delete
                        lines.add(line);
                    }
                    br.close();
                    // Write back the updated records to the file
                    BufferedWriter bw = new BufferedWriter(new FileWriter(statusFile));
                    for (String l : lines) { bw.write(l); bw.newLine(); }
                    bw.close();
                    System.out.println(" Resume of '" + studentUser + "' deleted successfully.");
                } catch (Exception e) {
                    System.out.println("Error updating records: " + e.getMessage());
                }
            } else {
                System.out.println("Failed to delete file. Check file permissions.");
            }
        } else {
            System.out.println("Invalid action.");
        }
    }
}