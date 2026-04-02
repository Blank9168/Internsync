import java.io.*;
import java.util.*;

public class Company extends User {
    private String companyName;

    // Constructor
    public Company(int userid, String username, String password, String email, String companyName) {
        super(userid, username, password, "company", email);
        this.companyName = companyName;
    }

    // Getter for company name
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
        System.out.println("6. Edit an Internship");
        System.out.println("7. View Applicant Resume");
        System.out.println("8. Filter Applicants by Skills");
        System.out.println("9. Change Password");
        System.out.println("10. Logout");
        System.out.print("Choice: ");
    }

    // Post a new internship
    public void postInternship(Scanner sc) {
        System.out.print("Internship Title  : ");
        String title = sc.nextLine().trim();
        System.out.print("Description       : ");
        String desc  = sc.nextLine().trim();
        System.out.print("Available Slots   : ");
        String slots = sc.nextLine().trim();
        System.out.println("Required Skills (comma-separated, e.g. Java,SQL,Excel)");
        System.out.println("  Leave blank if none required.");
        System.out.print("Required Skills   : ");
        String skills = sc.nextLine().trim().replace("|", "");

        String id = "INT" + System.currentTimeMillis();

        try {
            // Append the new internship to internships.txt
            BufferedWriter bw = new BufferedWriter(new FileWriter("internships.txt", true));
            // Format: id|company|title|description|slots|status|skills
            bw.write(id + "|" + companyName + "|" + title + "|" + desc + "|" + slots + "|open|" + skills);
            bw.newLine();
            bw.close();
            System.out.println("\n Internship posted successfully! (ID: " + id + ")");
        } catch (Exception e) {
            System.out.println("Error posting internship: " + e.getMessage());
        }
    }

    // View all internships posted by this company
    public void viewMyInternships() {
        try {

            // Check if internships file exists
            File f = new File("internships.txt");
            if (!f.exists()) { System.out.println("No internships posted yet."); return; }

            BufferedReader br = new BufferedReader(new FileReader(f));
            String line;
            boolean found = false;
            System.out.println("\n========== MY INTERNSHIPS ==========");
            while ((line = br.readLine()) != null) {
                String[] d = line.split("\\|");
                if (d.length >= 6 && d[1].trim().equalsIgnoreCase(companyName)) {
                    System.out.println("ID        : " + d[0].trim());
                    System.out.println("Title     : " + d[2].trim());
                    System.out.println("Details   : " + d[3].trim());
                    System.out.println("Slots     : " + d[4].trim());
                    System.out.println("Status    : " + d[5].trim());
                    System.out.println("Req.Skills: " + (d.length >= 7 && !d[6].trim().isEmpty() ? d[6].trim() : "None"));
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

    // Helper: load skills for a given student username from student_skills.txt
    private String loadStudentSkills(String studentUser) {
        try { 
            // Check if the skills file exists
            File f = new File("student_skills.txt");
            if (!f.exists()) return "(no skills listed)";
            BufferedReader br = new BufferedReader(new FileReader(f));
            String line;
            while ((line = br.readLine()) != null) {
                String[] d = line.split("\\|");
                if (d.length >= 2 && d[0].trim().equalsIgnoreCase(studentUser))  {
                    br.close();
                    return d[1].trim().isEmpty() ? "(no skills listed)" : d[1].trim();
                }
            }
            br.close();
        } catch (Exception e) { /* ignore */ }
        return "(no skills listed)";
    }

    // View all applicants for this company's internships — shows skills inline
    public List<String[]> viewApplicants() {
        List<String[]> applicants = new ArrayList<>();
        try {
            // Check if applications file exists
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
                    String studentSkills = loadStudentSkills(d[2].trim());
                    System.out.println("[" + count + "] App ID   : " + d[0].trim());
                    System.out.println("    Student  : " + d[3].trim() + " (" + d[4].trim() + ")");
                    System.out.println("    Position : " + d[6].trim());
                    System.out.println("    Skills   : " + studentSkills);
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

    // Filter applicants by skill match against a specific internship's required skills
    public void filterApplicantsBySkills(Scanner sc) {
        // First show this company's internships to pick one
        List<String[]> myInternships = new ArrayList<>();
        try {
            // Check if internships file exists
            File f = new File("internships.txt");
            if (!f.exists()) { System.out.println("No internships posted."); return; }
            BufferedReader br = new BufferedReader(new FileReader(f));
            String line;
            int count = 1;
            System.out.println("\n========== SELECT INTERNSHIP TO FILTER BY ==========");
            while ((line = br.readLine()) != null) {
                String[] d = line.split("\\|");
                if (d.length >= 6 && d[1].trim().equalsIgnoreCase(companyName)) {
                    String reqSkills = (d.length >= 7 && !d[6].trim().isEmpty()) ? d[6].trim() : "None";
                    System.out.println("[" + count + "] " + d[2].trim() + " | Req.Skills: " + reqSkills);
                    myInternships.add(d);
                    count++;
                }
            }
            br.close();
        } catch (Exception e) { System.out.println("Error: " + e.getMessage()); return; }

        if (myInternships.isEmpty()) { System.out.println("No internships found."); return; }

        System.out.print("Enter internship number (0 to cancel): ");
        int pick;
        try { pick = Integer.parseInt(sc.nextLine().trim()); }
        catch (NumberFormatException e) { System.out.println("Invalid input."); return; }
        if (pick == 0) return;
        if (pick < 1 || pick > myInternships.size()) { System.out.println("Invalid selection."); return; }

        // Extract required skills for the chosen internship
        String[] chosen = myInternships.get(pick - 1);
        String internshipId = chosen[0].trim();
        String internshipTitle = chosen[2].trim();
        String reqSkillsRaw = (chosen.length >= 7 && !chosen[6].trim().isEmpty()) ? chosen[6].trim() : "";

        if (reqSkillsRaw.isEmpty()) {
            System.out.println("This internship has no required skills set. Cannot filter.");
            return;
        }

        List<String> required = new ArrayList<>();
        for (String s : reqSkillsRaw.split(",")) required.add(s.trim().toLowerCase());

        // Load all applicants for this internship
        try {
            File f = new File("applications.txt");
            if (!f.exists()) { System.out.println("No applications yet."); return; }

            // Build two lists: matched and unmatched
            List<String[]> matched   = new ArrayList<>();
            List<String[]> unmatched = new ArrayList<>();

            // Read through applications and categorize them based on skill match
            BufferedReader br = new BufferedReader(new FileReader(f));
            String line;
            while ((line = br.readLine()) != null) {
                String[] d = line.split("\\|");
                if (d.length >= 8 && d[1].trim().equals(internshipId) && d[5].trim().equalsIgnoreCase(companyName)) {
                    String studentUser = d[2].trim();
                    String skillsRaw   = loadStudentSkills(studentUser);
                    List<String> studentSkills = new ArrayList<>();
                    if (!skillsRaw.equals("(no skills listed)"))
                        for (String s : skillsRaw.split(",")) studentSkills.add(s.trim().toLowerCase());

                    int matchCount = 0;
                    for (String req : required) {
                        for (String mine : studentSkills) {
                            if (mine.contains(req) || req.contains(mine)) { matchCount++; break; }
                        }
                    }
                    // Store match count as extra element for display
                    String[] extended = Arrays.copyOf(d, d.length + 2);
                    extended[d.length]     = skillsRaw;
                    extended[d.length + 1] = String.valueOf(matchCount);
                    if (matchCount > 0) matched.add(extended);
                    else                unmatched.add(extended);
                }
            }
            br.close();

            System.out.println("\n========== SKILL-MATCHED APPLICANTS: " + internshipTitle + " ==========");
            System.out.println("Required skills: " + reqSkillsRaw);
            System.out.println("--------------------------------------------------------------");

            if (matched.isEmpty() && unmatched.isEmpty()) {
                System.out.println("No applicants for this internship.");
                return;
            }

            // Sort matched applicants by match count descending
            int count = 1;
            if (!matched.isEmpty()) {
                System.out.println("--- SKILL MATCHES (sorted by match count) ---");
                matched.sort((a, b) -> Integer.parseInt(b[b.length - 1]) - Integer.parseInt(a[a.length - 1]));
                for (String[] d : matched) {
                    int mc = Integer.parseInt(d[d.length - 1]);
                    System.out.println("[" + count + "] *** MATCH: " + mc + "/" + required.size() + " skills ***");
                    System.out.println("    Student : " + d[3].trim() + " (" + d[4].trim() + ")");
                    System.out.println("    Skills  : " + d[d.length - 2]);
                    System.out.println("    Status  : " + d[7].trim());
                    System.out.println("--------------------------------------------------------------");
                    count++;
                }
            }

            if (!unmatched.isEmpty()) {
                System.out.println("--- NO SKILL MATCH ---");
                for (String[] d : unmatched) {
                    System.out.println("[" + count + "] Student : " + d[3].trim() + " (" + d[4].trim() + ")");
                    System.out.println("    Skills  : " + d[d.length - 2]);
                    System.out.println("    Status  : " + d[7].trim());
                    System.out.println("--------------------------------------------------------------");
                    count++;
                }
            }

        } catch (Exception e) {
            System.out.println("Error filtering applicants: " + e.getMessage());
        }
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

        // applicants list already filtered to this company by viewApplicants()
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
            // Check if the applications file exists
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

            // Write back the updated applications to the file
            BufferedWriter bw = new BufferedWriter(new FileWriter("applications.txt"));
            for (String l : lines) { bw.write(l); bw.newLine(); }
            bw.close();

            System.out.println("\n Application " + appId + " has been " + newStatus + ".");
        } catch (Exception e) {
            System.out.println("Error updating application: " + e.getMessage());
        }
    }

    // Edit an existing internship's title, description, or slots
    public void editInternship(Scanner sc) {
        viewMyInternships();
        System.out.print("\nEnter Internship ID to edit (0 to cancel): ");
        String id = sc.nextLine().trim();
        if (id.equals("0")) return;

        try {
            // First verify the internship exists and belongs to this company
            boolean found = false;
            BufferedReader br = new BufferedReader(new FileReader("internships.txt"));
            String line;
            String[] target = null;
            while ((line = br.readLine()) != null) {
                String[] d = line.split("\\|");
                if (d.length >= 6 && d[0].trim().equals(id) && d[1].trim().equalsIgnoreCase(companyName)) {
                    target = d;
                    found = true;
                    break;
                }
            }
            br.close();

            if (!found) { System.out.println("Internship not found or does not belong to you."); return; }

            System.out.println("\nWhat would you like to edit?");
            System.out.println("1. Title       (current: " + target[2].trim() + ")");
            System.out.println("2. Description (current: " + target[3].trim() + ")");
            System.out.println("3. Slots       (current: " + target[4].trim() + ")");
            System.out.println("4. Req. Skills (current: " + (target.length >= 7 && !target[6].trim().isEmpty() ? target[6].trim() : "None") + ")");
            System.out.print("Choice (0 to cancel): ");
            String choice = sc.nextLine().trim();

            int field;
            switch (choice) {
                case "1": field = 2; break;
                case "2": field = 3; break;
                case "3": field = 4; break;
                case "4": field = 6; break;
                default: System.out.println("Cancelled."); return;
            }

            // For skills, allow comma-separated input. For others, just a single value.
            System.out.print("New value" + (field == 6 ? " (comma-separated skills)" : "") + ": ");
            String newValue = sc.nextLine().trim().replace("|", "");
            if (field != 6 && newValue.isEmpty()) { System.out.println("Value cannot be empty."); return; }

            // Rewrite internships.txt with updated field
            // Ensure row has 7 fields (pad with empty skills if older entry)
            List<String> lines = new ArrayList<>();
            br = new BufferedReader(new FileReader("internships.txt"));
            while ((line = br.readLine()) != null) {
                String[] d = line.split("\\|");
                if (d.length >= 6 && d[0].trim().equals(id) && d[1].trim().equalsIgnoreCase(companyName)) {
                    // Extend array to 7 if needed
                    if (d.length < 7) d = Arrays.copyOf(d, 7);
                    if (d[6] == null) d[6] = "";
                    d[field] = newValue;
                    line = String.join("|", d);
                }
                lines.add(line);
            }
            br.close();

            // Write back the updated internships to the file
            BufferedWriter bw = new BufferedWriter(new FileWriter("internships.txt"));
            for (String l : lines) { bw.write(l); bw.newLine(); }
            bw.close();
            System.out.println(" Internship updated successfully.");
        } catch (Exception e) {
            System.out.println("Error editing internship: " + e.getMessage());
        }
    }

    // Close an internship (set status to closed)
    public void closeInternship(Scanner sc) {
        viewMyInternships();
        System.out.print("\nEnter Internship ID to close (or 0 to cancel): ");
        String id = sc.nextLine().trim();
        if (id.equals("0")) return;

        try {
            // Check if the internships file exists
            File internshipsFile = new File("internships.txt");
            if (!internshipsFile.exists()) {
                System.out.println("No internships found.");
                return;
            }
            // Read through internships and update the status of the selected one to "closed"
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

            // Write back the updated internships to the file
            BufferedWriter bw = new BufferedWriter(new FileWriter("internships.txt"));
            for (String l : lines) { bw.write(l); bw.newLine(); }
            bw.close();
            System.out.println(" Internship closed.");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // View resume of a specific applicant — only students who applied to THIS company
    public void viewApplicantResume(Scanner sc) {
        List<String[]> applicants = viewApplicants();
        if (applicants.isEmpty()) return;

        System.out.print("\nEnter applicant number to view resume (0 to cancel): ");
        int choice;
        // Validate that the input is an integer and within the valid range
        try { choice = Integer.parseInt(sc.nextLine().trim()); }
        catch (NumberFormatException e) { System.out.println("Invalid input."); return; }
        if (choice == 0) return;
        if (choice < 1 || choice > applicants.size()) { System.out.println("Invalid selection."); return; }

        // applicants list already filtered to this company by viewApplicants()
        // d[2] = studentUsername, d[5] = company
        String studentUser    = applicants.get(choice - 1)[2].trim();
        String studentName    = applicants.get(choice - 1)[3].trim();
        String appliedCompany = applicants.get(choice - 1)[5].trim();

        // Safety: double-check the applicant belongs to this company
        if (!appliedCompany.equalsIgnoreCase(companyName)) {
            System.out.println("Access denied. This applicant did not apply to your company.");
            return;
        }

        try {
            // Check if the resume status file exists
            File statusFile = new File("resume_status.txt");
            if (!statusFile.exists()) { System.out.println("No resumes have been uploaded yet."); return; }

            BufferedReader br = new BufferedReader(new FileReader(statusFile));
            String line;
            boolean found = false;
            while ((line = br.readLine()) != null) {
                String[] d = line.split("\\|");
                // username|filePath|schoolStatus|schoolNote|uploadDate
                if (d.length >= 5 && d[0].trim().equals(studentUser)) {
                    System.out.println("\n--- RESUME: " + studentName + " (" + studentUser + ") ---");
                    System.out.println("File           : " + d[1].trim());
                    System.out.println("Uploaded       : " + d[4].trim());
                    System.out.println("School approval: " + d[2].trim());
                    System.out.println("School note    : " + d[3].trim());
                    File pdf = new File(d[1].trim());
                    if (pdf.exists()) {
                        System.out.println("File Size      : " + pdf.length() + " bytes (" + (pdf.length() / 1024) + " KB)");
                        System.out.println("Full Path      : " + pdf.getAbsolutePath());
                        System.out.println("(Open the path above in a PDF viewer to read the full resume.)");
                    } else {
                        System.out.println("File not found on disk.");
                    }
                    found = true;
                    break;
                }
            }
            br.close();
            if (!found) System.out.println(studentName + " has not uploaded a resume yet.");
        } catch (Exception e) {
            System.out.println("Error reading resume: " + e.getMessage());
        }
    }
}