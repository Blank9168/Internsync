import java.io.*;
import java.util.*;

public class Student extends User {
    private String name;
    private String course;

    public Student(int id, String username, String password, String email, String name, String course) {
        super(id, username, password, "student", email);
        this.name = name;
        this.course = course;
    }

    public String getName()   { return name; }
    public String getCourse() { return course; }

    @Override
    public void displayMenu() {
        System.out.println("\n=============================");
        System.out.println("  Welcome, " + name + " (" + course + ")");
        System.out.println("=============================");
        System.out.println("1. Browse Available Internships");
        System.out.println("2. Apply for an Internship");
        System.out.println("3. View My Applications");
        System.out.println("4. Withdraw an Application");
        System.out.println("5. Change Password");
        System.out.println("6. Logout");
        System.out.print("Choice: ");
    }

    // Browse all internships from internships.txt
    public List<String[]> browseInternships() {
        List<String[]> list = new ArrayList<>();
        try {
            File f = new File("internships.txt");
            if (!f.exists()) {
                System.out.println("No internships posted yet.");
                return list;
            }
            BufferedReader br = new BufferedReader(new FileReader(f));
            String line;
            int count = 1;
            System.out.println("\n========== AVAILABLE INTERNSHIPS ==========");
            while ((line = br.readLine()) != null) {
                // Format: id,company,title,description,slots,status
                String[] data = line.split("\\|");
                if (data.length >= 4 && data[4].trim().equalsIgnoreCase("open")) {
                    System.out.println("[" + count + "] Company : " + data[1].trim());
                    System.out.println("    Title   : " + data[2].trim());
                    System.out.println("    Details : " + data[3].trim());
                    System.out.println("    Slots   : " + data[4].trim());
                    System.out.println("    Status  : " + data[5].trim());
                    System.out.println("-------------------------------------------");
                    list.add(data);
                    count++;
                }
            }
            br.close();
            if (list.isEmpty()) System.out.println("No open internships at the moment.");
        } catch (Exception e) {
            System.out.println("Error reading internships: " + e.getMessage());
        }
        return list;
    }

    // Apply for an internship by title
    public void applyInternship(Scanner sc) {
        List<String[]> internships = browseInternships();
        if (internships.isEmpty()) return;

        System.out.print("\nEnter the number of the internship to apply for (0 to cancel): ");
        int choice;
        try {
            choice = Integer.parseInt(sc.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("Invalid input.");
            return;
        }

        if (choice == 0) return;
        if (choice < 1 || choice > internships.size()) {
            System.out.println("Invalid selection.");
            return;
        }

        String[] selected = internships.get(choice - 1);
        String internshipId = selected[0].trim();
        String companyName  = selected[1].trim();
        String title        = selected[2].trim();

        // Check if already applied
        try {
            File f = new File("applications.txt");
            if (f.exists()) {
                BufferedReader br = new BufferedReader(new FileReader(f));
                String line;
                while ((line = br.readLine()) != null) {
                    String[] d = line.split("\\|");
                    // Format: appId|internshipId|studentUsername|studentName|course|company|title|status
                    if (d.length >= 7 && d[1].trim().equals(internshipId) && d[2].trim().equals(username)) {
                        System.out.println("You have already applied for this internship!");
                        br.close();
                        return;
                    }
                }
                br.close();
            }
        } catch (Exception e) { /* ignore */ }

        // Generate app ID
        String appId = "APP" + System.currentTimeMillis();

        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter("applications.txt", true));
            // appId|internshipId|studentUsername|studentName|course|company|title|status
            bw.write(appId + "|" + internshipId + "|" + username + "|" + name + "|" + course + "|" + companyName + "|" + title + "|PENDING");
            bw.newLine();
            bw.close();
            System.out.println("\n Successfully applied for: " + title + " at " + companyName);
        } catch (Exception e) {
            System.out.println("Error saving application: " + e.getMessage());
        }
    }

    // Withdraw a PENDING application
    public void withdrawApplication(Scanner sc) {
        try {
            File f = new File("applications.txt");
            if (!f.exists()) { System.out.println("You have no applications to withdraw."); return; }

            List<String[]> pending = new ArrayList<>();
            BufferedReader br = new BufferedReader(new FileReader(f));
            String line;
            while ((line = br.readLine()) != null) {
                String[] d = line.split("\\|");
                if (d.length >= 8 && d[2].trim().equals(username) && d[7].trim().equals("PENDING")) {
                    pending.add(d);
                }
            }
            br.close();

            if (pending.isEmpty()) {
                System.out.println("You have no pending applications to withdraw.");
                return;
            }

            System.out.println("\n========== PENDING APPLICATIONS ==========");
            for (int i = 0; i < pending.size(); i++) {
                String[] d = pending.get(i);
                System.out.println("[" + (i + 1) + "] Company  : " + d[5].trim());
                System.out.println("    Position : " + d[6].trim());
                System.out.println("    App ID   : " + d[0].trim());
                System.out.println("------------------------------------------");
            }

            System.out.print("Enter number to withdraw (0 to cancel): ");
            int choice;
            try {
                choice = Integer.parseInt(sc.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input."); return;
            }
            if (choice == 0) return;
            if (choice < 1 || choice > pending.size()) { System.out.println("Invalid selection."); return; }

            String targetAppId = pending.get(choice - 1)[0].trim();

            List<String> lines = new ArrayList<>();
            br = new BufferedReader(new FileReader(f));
            while ((line = br.readLine()) != null) {
                String[] d = line.split("\\|");
                if (d[0].trim().equals(targetAppId)) continue; // remove this line
                lines.add(line);
            }
            br.close();

            BufferedWriter bw = new BufferedWriter(new FileWriter(f));
            for (String l : lines) { bw.write(l); bw.newLine(); }
            bw.close();

            System.out.println(" Application withdrawn successfully.");
        } catch (Exception e) {
            System.out.println("Error withdrawing application: " + e.getMessage());
        }
    }

    // View own applications and their status
    public void viewMyApplications() {
        try {
            File f = new File("applications.txt");
            if (!f.exists()) {
                System.out.println("You have no applications yet.");
                return;
            }
            BufferedReader br = new BufferedReader(new FileReader(f));
            String line;
            boolean found = false;
            System.out.println("\n========== MY APPLICATIONS ==========");
            while ((line = br.readLine()) != null) {
                String[] d = line.split("\\|");
                if (d.length >= 8 && d[2].trim().equals(username)) {
                    System.out.println("App ID   : " + d[0].trim());
                    System.out.println("Company  : " + d[5].trim());
                    System.out.println("Position : " + d[6].trim());
                    System.out.println("Status   : " + d[7].trim());
                    System.out.println("--------------------------------------");
                    found = true;
                }
            }
            br.close();
            if (!found) System.out.println("You have no applications yet.");
        } catch (Exception e) {
            System.out.println("Error reading applications: " + e.getMessage());
        }
    }
}