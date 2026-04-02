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
        System.out.println("5. Upload / Re-upload Resume (PDF)");
        System.out.println("6. View My Resume Status");
        System.out.println("7. Change Password");
        System.out.println("8. Logout");
        System.out.print("Choice: ");
    }

    // Upload or re-upload a PDF resume
    public void uploadResume(Scanner sc) {
        System.out.println("\n========== UPLOAD RESUME ==========");
        System.out.println("Enter the full path to your PDF file.");
        System.out.println("Example: C:\\Users\\Juan\\Documents\\resume.pdf");
        System.out.print("File path: ");
        String inputPath = sc.nextLine().trim();

        if (inputPath.isEmpty()) { System.out.println("No path entered. Cancelled."); return; }

        File source = new File(inputPath);
        if (!source.exists()) {
            System.out.println("File not found: " + inputPath);
            return;
        }
        if (!source.getName().toLowerCase().endsWith(".pdf")) {
            System.out.println("Only PDF files are accepted.");
            return;
        }

        // Create resumes directory if it doesn't exist
        File resumeDir = new File("resumes");
        if (!resumeDir.exists()) resumeDir.mkdir();

        File dest = new File("resumes/" + username + ".pdf");

        // Copy file bytes
        try (FileInputStream fis = new FileInputStream(source);
             FileOutputStream fos = new FileOutputStream(dest)) {
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = fis.read(buffer)) != -1) {
                fos.write(buffer, 0, bytesRead);
            }
        } catch (Exception e) {
            System.out.println("Error copying file: " + e.getMessage());
            return;
        }

        // Update or insert entry in resume_status.txt
        // Format: username|filePath|schoolStatus|schoolNote|uploadDate
        String date = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm").format(new java.util.Date());
        String newEntry = username + "|" + dest.getPath() + "|PENDING|--|" + date;

        try {
            File statusFile = new File("resume_status.txt");
            List<String> lines = new ArrayList<>();
            boolean found = false;

            if (statusFile.exists()) {
                BufferedReader br = new BufferedReader(new FileReader(statusFile));
                String line;
                while ((line = br.readLine()) != null) {
                    String[] d = line.split("\\|");
                    if (d[0].trim().equals(username)) {
                        lines.add(newEntry); // replace old entry
                        found = true;
                    } else {
                        lines.add(line);
                    }
                }
                br.close();
            }
            if (!found) lines.add(newEntry);

            BufferedWriter bw = new BufferedWriter(new FileWriter(statusFile));
            for (String l : lines) { bw.write(l); bw.newLine(); }
            bw.close();

            System.out.println("✔ Resume uploaded successfully to: " + dest.getPath());
            System.out.println("  Status reset to PENDING for school review.");
        } catch (Exception e) {
            System.out.println("Error updating resume status: " + e.getMessage());
        }
    }

    // View own resume submission status
    public void viewResumeStatus() {
        System.out.println("\n========== MY RESUME STATUS ==========");
        try {
            File f = new File("resume_status.txt");
            if (!f.exists()) { System.out.println("You have not uploaded a resume yet."); return; }

            BufferedReader br = new BufferedReader(new FileReader(f));
            String line;
            boolean found = false;
            while ((line = br.readLine()) != null) {
                String[] d = line.split("\\|");
                // username|filePath|schoolStatus|schoolNote|uploadDate
                if (d.length >= 5 && d[0].trim().equals(username)) {
                    System.out.println("File      : " + d[1].trim());
                    System.out.println("Uploaded  : " + d[4].trim());
                    String status = d[2].trim();
                    if (status.equals("APPROVED"))      System.out.println("Status    : APPROVED by school");
                    else if (status.equals("REJECTED")) System.out.println("Status    : REJECTED by school");
                    else                                System.out.println("Status    : PENDING school review");
                    System.out.println("Note      : " + d[3].trim());
                    found = true;
                }
            }
            br.close();
            if (!found) System.out.println("You have not uploaded a resume yet.");
        } catch (Exception e) {
            System.out.println("Error reading resume status: " + e.getMessage());
        }
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

    // Helper: check if this student's resume is APPROVED
    private String getResumeStatus() {
        try {
            File f = new File("resume_status.txt");
            if (!f.exists()) return "NONE";
            BufferedReader br = new BufferedReader(new FileReader(f));
            String line;
            while ((line = br.readLine()) != null) {
                String[] d = line.split("\\|");
                if (d.length >= 3 && d[0].trim().equals(username)) {
                    br.close();
                    return d[2].trim(); // PENDING / APPROVED / REJECTED
                }
            }
            br.close();
        } catch (Exception e) { /* ignore */ }
        return "NONE";
    }

    // Apply for an internship by title
    public void applyInternship(Scanner sc) {
        // Resume gate: must be APPROVED before applying
        String resumeStatus = getResumeStatus();
        if (resumeStatus.equals("NONE")) {
            System.out.println("\n✘ You cannot apply yet.");
            System.out.println("  You have not uploaded a resume.");
            System.out.println("  Please upload your PDF resume first (option 5).");
            return;
        }
        if (resumeStatus.equals("PENDING")) {
            System.out.println("\n✘ You cannot apply yet.");
            System.out.println("  Your resume is still awaiting school approval.");
            System.out.println("  Please wait for the school to review your resume.");
            return;
        }
        if (resumeStatus.equals("REJECTED")) {
            System.out.println("\n✘ You cannot apply yet.");
            System.out.println("  Your resume was REJECTED by the school.");
            System.out.println("  Please re-upload a revised resume (option 5) and wait for re-approval.");
            return;
        }

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
            System.out.println("\n✔ Successfully applied for: " + title + " at " + companyName);
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

            System.out.println("✔ Application withdrawn successfully.");
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