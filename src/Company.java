import java.io.*;
import java.util.*;

public class Company extends User {
    private String companyName;

    public Company(int userid, String username, String password, String email, String companyName) {
        super(userid, username, password, "company", email);
        this.companyName = companyName;
    }

    public String getCompanyName() { return companyName; }

    @Override
    public void displayMenu() {
        System.out.println("\n=============================");
        System.out.println("  Company: " + companyName);
        System.out.println("=============================");
        System.out.println("1. Post Internship");
        System.out.println("2. View My Posted Internships");
        System.out.println("3. View Applicants");
        System.out.println("4. Accept / Reject Applicant");
        System.out.println("5. Close an Internship");
        System.out.println("6. Logout");
        System.out.print("Choice: ");
    }

    // Post a new internship
    public void postInternship(Scanner sc) {
        System.out.print("Internship Title: ");
        String title = sc.nextLine().trim();
        System.out.print("Description     : ");
        String desc  = sc.nextLine().trim();
        System.out.print("Available Slots : ");
        String slots = sc.nextLine().trim();

        String id = "INT" + System.currentTimeMillis();

        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter("internships.txt", true));
            // id|company|title|description|slots|status
            bw.write(id + "|" + companyName + "|" + title + "|" + desc + "|" + slots + "|open");
            bw.newLine();
            bw.close();
            System.out.println("\n✔ Internship posted successfully! (ID: " + id + ")");
        } catch (Exception e) {
            System.out.println("Error posting internship: " + e.getMessage());
        }
    }

    // View all internships posted by this company
    public void viewMyInternships() {
        try {
            File f = new File("internships.txt");
            if (!f.exists()) { System.out.println("No internships posted yet."); return; }

            BufferedReader br = new BufferedReader(new FileReader(f));
            String line;
            boolean found = false;
            System.out.println("\n========== MY INTERNSHIPS ==========");
            while ((line = br.readLine()) != null) {
                String[] d = line.split("\\|");
                if (d.length >= 6 && d[1].trim().equalsIgnoreCase(companyName)) {
                    System.out.println("ID      : " + d[0].trim());
                    System.out.println("Title   : " + d[2].trim());
                    System.out.println("Details : " + d[3].trim());
                    System.out.println("Slots   : " + d[4].trim());
                    System.out.println("Status  : " + d[5].trim());
                    System.out.println("------------------------------------");
                    found = true;
                }
            }
            br.close();
            if (!found) System.out.println("You have not posted any internships.");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // View all applicants for this company's internships
    public List<String[]> viewApplicants() {
        List<String[]> applicants = new ArrayList<>();
        try {
            File f = new File("applications.txt");
            if (!f.exists()) { System.out.println("No applications yet."); return applicants; }

            BufferedReader br = new BufferedReader(new FileReader(f));
            String line;
            int count = 1;
            System.out.println("\n========== APPLICANTS ==========");
            while ((line = br.readLine()) != null) {
                // appId|internshipId|studentUsername|studentName|course|company|title|status
                String[] d = line.split("\\|");
                if (d.length >= 8 && d[5].trim().equalsIgnoreCase(companyName)) {
                    System.out.println("[" + count + "] App ID   : " + d[0].trim());
                    System.out.println("    Student  : " + d[3].trim() + " (" + d[4].trim() + ")");
                    System.out.println("    Position : " + d[6].trim());
                    System.out.println("    Status   : " + d[7].trim());
                    System.out.println("--------------------------------");
                    applicants.add(d);
                    count++;
                }
            }
            br.close();
            if (applicants.isEmpty()) System.out.println("No applicants for your internships.");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
        return applicants;
    }

    // Accept or reject an applicant
    public void updateApplicationStatus(Scanner sc) {
        List<String[]> applicants = viewApplicants();
        if (applicants.isEmpty()) return;

        System.out.print("\nEnter applicant number to update (0 to cancel): ");
        int choice;
        try {
            choice = Integer.parseInt(sc.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("Invalid input."); return;
        }
        if (choice == 0) return;
        if (choice < 1 || choice > applicants.size()) { System.out.println("Invalid selection."); return; }

        String[] selected = applicants.get(choice - 1);
        String appId = selected[0].trim();

        System.out.println("1. ACCEPT");
        System.out.println("2. REJECT");
        System.out.print("Action: ");
        String action = sc.nextLine().trim();
        String newStatus = action.equals("1") ? "ACCEPTED" : action.equals("2") ? "REJECTED" : null;

        if (newStatus == null) { System.out.println("Invalid action."); return; }

        // Rewrite applications.txt with updated status
        try {
            List<String> lines = new ArrayList<>();
            BufferedReader br = new BufferedReader(new FileReader("applications.txt"));
            String line;
            while ((line = br.readLine()) != null) {
                String[] d = line.split("\\|");
                if (d[0].trim().equals(appId)) {
                    d[7] = newStatus;
                    line = String.join("|", d);
                }
                lines.add(line);
            }
            br.close();

            BufferedWriter bw = new BufferedWriter(new FileWriter("applications.txt"));
            for (String l : lines) { bw.write(l); bw.newLine(); }
            bw.close();

            System.out.println("\n✔ Application " + appId + " has been " + newStatus + ".");
        } catch (Exception e) {
            System.out.println("Error updating application: " + e.getMessage());
        }
    }

    // Close an internship (set status to closed)
    public void closeInternship(Scanner sc) {
        viewMyInternships();
        System.out.print("\nEnter Internship ID to close (or 0 to cancel): ");
        String id = sc.nextLine().trim();
        if (id.equals("0")) return;

        try {
            List<String> lines = new ArrayList<>();
            BufferedReader br = new BufferedReader(new FileReader("internships.txt"));
            String line;
            boolean found = false;
            while ((line = br.readLine()) != null) {
                String[] d = line.split("\\|");
                if (d[0].trim().equals(id) && d[1].trim().equalsIgnoreCase(companyName)) {
                    d[5] = "closed";
                    line = String.join("|", d);
                    found = true;
                }
                lines.add(line);
            }
            br.close();

            if (!found) { System.out.println("Internship not found or not yours."); return; }

            BufferedWriter bw = new BufferedWriter(new FileWriter("internships.txt"));
            for (String l : lines) { bw.write(l); bw.newLine(); }
            bw.close();
            System.out.println("✔ Internship closed.");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}