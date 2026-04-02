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
        System.out.println("7. Manage My Skills");
        System.out.println("8. Change Password");
        System.out.println("9. Logout");
        System.out.print("Choice: ");
    }

    // ── SKILL HELPERS ────────────────────────────────────────────

    // Load this student's skills from student_skills.txt
    private List<String> loadMySkills() {
        List<String> skills = new ArrayList<>();
        try {
            File f = new File("student_skills.txt");
            if (!f.exists()) return skills;
            BufferedReader br = new BufferedReader(new FileReader(f));
            String line;
            while ((line = br.readLine()) != null) {
                String[] d = line.split("\\|");
                if (d.length >= 2 && d[0].trim().equalsIgnoreCase(username)) {
                    for (String s : d[1].split(",")) {
                        String sk = s.trim();
                        if (!sk.isEmpty()) skills.add(sk);
                    }
                    break;
                }
            }
            br.close();
        } catch (Exception e) { /* ignore */ }
        return skills;
    }

    // Save skills list back to student_skills.txt
    private void saveMySkills(List<String> skills) {
        try {
            File f = new File("student_skills.txt");
            List<String> lines = new ArrayList<>();
            boolean found = false;
            if (f.exists()) {
                BufferedReader br = new BufferedReader(new FileReader(f));
                String line;
                while ((line = br.readLine()) != null) {
                    String[] d = line.split("\\|");
                    if (d[0].trim().equalsIgnoreCase(username)) {
                        found = true;
                        if (!skills.isEmpty())
                            lines.add(username + "|" + String.join(",", skills));
                        // if empty, just skip — removes entry
                    } else {
                        lines.add(line);
                    }
                }
                br.close();
            }
            if (!found && !skills.isEmpty())
                lines.add(username + "|" + String.join(",", skills));

            BufferedWriter bw = new BufferedWriter(new FileWriter(f));
            for (String l : lines) { bw.write(l); bw.newLine(); }
            bw.close();
        } catch (Exception e) {
            System.out.println("Error saving skills: " + e.getMessage());
        }
    }

    // Manage skills: view, add, remove
    public void manageSkills(Scanner sc) {
        boolean managing = true;
        while (managing) {
            List<String> skills = loadMySkills();
            System.out.println("\n========== MY SKILLS ==========");
            if (skills.isEmpty()) {
                System.out.println("  (No skills added yet)");
            } else {
                for (int i = 0; i < skills.size(); i++)
                    System.out.println("  [" + (i + 1) + "] " + skills.get(i));
            }
            System.out.println("--------------------------------");
            System.out.println("1. Add a skill");
            System.out.println("2. Remove a skill");
            System.out.println("3. Back");
            System.out.print("Choice: ");
            String opt = sc.nextLine().trim();

            if (opt.equals("1")) {
                System.out.print("Enter skill to add (e.g. Java, Data Analysis, MS Office): ");
                String newSkill = sc.nextLine().trim();
                if (newSkill.isEmpty()) { System.out.println("Skill cannot be empty."); continue; }
                // Prevent comma-injection
                newSkill = newSkill.replace(",", "").replace("|", "").trim();
                // Check duplicate (case-insensitive)
                boolean exists = false;
                for (String s : skills) {
                    if (s.equalsIgnoreCase(newSkill)) { exists = true; break; }
                }
                if (exists) { System.out.println("You already have that skill listed."); continue; }
                skills.add(newSkill);
                saveMySkills(skills);
                System.out.println(" Skill added: " + newSkill);

            } else if (opt.equals("2")) {
                if (skills.isEmpty()) { System.out.println("No skills to remove."); continue; }
                System.out.print("Enter number of skill to remove: ");
                int idx;
                try { idx = Integer.parseInt(sc.nextLine().trim()); }
                catch (NumberFormatException e) { System.out.println("Invalid input."); continue; }
                if (idx < 1 || idx > skills.size()) { System.out.println("Invalid selection."); continue; }
                String removed = skills.remove(idx - 1);
                saveMySkills(skills);
                System.out.println(" Skill removed: " + removed);

            } else if (opt.equals("3")) {
                managing = false;
            } else {
                System.out.println("Invalid option.");
            }
        }
    }

    // ── BROWSE with skill-match scoring ──────────────────────────

    // Browse internships — matched ones float to top with a match badge
    public List<String[]> browseInternships() {
        List<String[]> list = new ArrayList<>();
        try {
            File f = new File("internships.txt");
            if (!f.exists()) { System.out.println("No internships posted yet."); return list; }

            List<String> mySkills = loadMySkills();

            // Load all open internships
            List<String[]> raw = new ArrayList<>();
            BufferedReader br = new BufferedReader(new FileReader(f));
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split("\\|");
                // id|company|title|desc|slots|status[|skills]
                if (data.length >= 6 && data[5].trim().equalsIgnoreCase("open"))
                    raw.add(data);
            }
            br.close();

            if (raw.isEmpty()) { System.out.println("No open internships at the moment."); return list; }

            // Score each internship by how many required skills the student has
            List<int[]> scores = new ArrayList<>(); // index → matched count
            for (int i = 0; i < raw.size(); i++) {
                String[] data = raw.get(i);
                int matched = 0;
                if (data.length >= 7 && !data[6].trim().isEmpty() && !mySkills.isEmpty()) {
                    for (String req : data[6].split(",")) {
                        String r = req.trim().toLowerCase();
                        for (String mine : mySkills) {
                            if (mine.toLowerCase().contains(r) || r.contains(mine.toLowerCase())) {
                                matched++;
                                break;
                            }
                        }
                    }
                }
                scores.add(new int[]{i, matched});
            }

            // Sort: higher match count first, then original order
            scores.sort((a, b) -> b[1] - a[1]);

            System.out.println("\n========== AVAILABLE INTERNSHIPS ==========");
            if (!mySkills.isEmpty())
                System.out.println("  Your skills: " + String.join(", ", mySkills));
            System.out.println("-------------------------------------------");

            int count = 1;
            for (int[] entry : scores) {
                String[] data = raw.get(entry[0]);
                int matchCount = entry[1];
                String reqSkills = (data.length >= 7 && !data[6].trim().isEmpty()) ? data[6].trim() : "None specified";

                // Match badge
                if (!mySkills.isEmpty() && matchCount > 0) {
                    int total = (data.length >= 7) ? data[6].split(",").length : 0;
                    System.out.println("[" + count + "] *** MATCH: " + matchCount + "/" + total + " skills matched ***");
                } else {
                    System.out.println("[" + count + "]");
                }
                System.out.println("    Company  : " + data[1].trim());
                System.out.println("    Title    : " + data[2].trim());
                System.out.println("    Details  : " + data[3].trim());
                System.out.println("    Slots    : " + data[4].trim());
                System.out.println("    Req.Skills: " + reqSkills);
                System.out.println("-------------------------------------------");
                list.add(data);
                count++;
            }
        } catch (Exception e) {
            System.out.println("Error reading internships: " + e.getMessage());
        }
        return list;
    }
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

            System.out.println(" Resume uploaded successfully to: " + dest.getPath());
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

    // ── RESUME GATE ──────────────────────────────────────────────

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
            System.out.println("\n You cannot apply yet.");
            System.out.println("  You have not uploaded a resume.");
            System.out.println("  Please upload your PDF resume first (option 5).");
            return;
        }
        if (resumeStatus.equals("PENDING")) {
            System.out.println("\n You cannot apply yet.");
            System.out.println("  Your resume is still awaiting school approval.");
            System.out.println("  Please wait for the school to review your resume.");
            return;
        }
        if (resumeStatus.equals("REJECTED")) {
            System.out.println("\n You cannot apply yet.");
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