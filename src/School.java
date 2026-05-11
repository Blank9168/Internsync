import java.io.*;
import java.util.*;

public class School extends User {
    @Override
    public String getDisplayName() {
        return schoolName + " (School)";
    }

    @Override
    public void viewPersonalInfo() {
        System.out.println("\n=== SCHOOL PROFILE ===");
        System.out.println("Username : " + username);
        System.out.println("School   : " + schoolName);
        System.out.println("Address  : " + address);
        System.out.println("Email    : " + email);
        System.out.println("Role     : " + role);
    }

    @Override
    public List<String[]> viewAllInternships() {
        return new ArrayList<>();
    }

    @Override
    public void logAction(String action) {
        super.logAction(action);
    }

    private String schoolName;
    private String address;

    // Constructor
    public School(int userid, String username, String password, String email, String schoolName, String address) {
        super(userid, username, password, "school", email);
        this.schoolName = schoolName;
        this.address    = address;
    }

    // Getters
    public String getSchoolName() { return schoolName; }
    public String getAddress()    { return address; }

    @Override
    public void displayMenu() {
        System.out.println("\n=============================");
        System.out.println("  School: " + schoolName);
        System.out.println("=============================");
        System.out.println("1. View Registered Students");
        System.out.println("2. View All Student Applications");
        System.out.println("3. View Accepted Students");
        System.out.println("4. Endorse a Student");
        System.out.println("5. View Endorsements");
        System.out.println("6. Review Student Resumes");
        System.out.println("7. Change Password");
        System.out.println("8. Logout");
        System.out.print("Choice: ");
    }

    // Helper: load skills for a student username
    private String loadStudentSkills(String studentUser) {
        try {
            File f = new File("student_skills.txt");
            if (!f.exists()) return "(none)";
            BufferedReader br = new BufferedReader(new FileReader(f));
            String line;
            while ((line = br.readLine()) != null) {
                String[] d = line.split("\\|");
                if (d.length >= 2 && d[0].trim().equalsIgnoreCase(studentUser)) {
                    br.close();
                    return d[1].trim().isEmpty() ? "(none)" : d[1].trim();
                }
            }
            br.close();
        } catch (Exception e) { 
            System.out.println("Error loading skills for " + studentUser + ": " + e.getMessage());
         }
        return "(none)";
    }

    // View all registered students — includes their skills
    public void viewStudents() {
        try {
            File f = new File("users.txt");
            if (!f.exists()) { System.out.println("No users found."); return; }

            BufferedReader br = new BufferedReader(new FileReader(f));
            String line;
            int count = 0;
            System.out.println("\n========== REGISTERED STUDENTS ==========");
            while ((line = br.readLine()) != null) {
                String[] d = line.split(",");
                if (d.length >= 3 && d[2].trim().equalsIgnoreCase("student")) {
                    String uname  = d[0].trim();
                    String skills = loadStudentSkills(uname);
                    System.out.println("Username : " + uname);
                    System.out.println("Email    : " + (d.length > 3 ? d[3].trim() : "N/A"));
                    System.out.println("Name     : " + (d.length > 4 ? d[4].trim() : "N/A"));
                    System.out.println("Course   : " + (d.length > 5 ? d[5].trim() : "N/A"));
                    System.out.println("Skills   : " + skills);
                    System.out.println("-----------------------------------------");
                    count++;
                }
            }
            br.close();
            if (count == 0) System.out.println("No students registered.");
            else System.out.println("Total students: " + count);
        } catch (Exception e) {
            System.out.println("Error reading students: " + e.getMessage());
        }
    }

    // View all student applications
    public List<String[]> viewApplications() {
        try {
            File f = new File("applications.txt");
            if (!f.exists()) { System.out.println("No applications yet.");
                return new ArrayList<>();
            }

            BufferedReader br = new BufferedReader(new FileReader(f));
            String line;
            System.out.println("\n========== ALL STUDENT APPLICATIONS ==========");
            boolean found = false;
            while ((line = br.readLine()) != null) {
                String[] d = line.split("\\|");
                if (d.length >= 8) {
                    System.out.println("App ID   : " + d[0].trim());
                    System.out.println("Student  : " + d[3].trim() + " | Course: " + d[4].trim());
                    System.out.println("Company  : " + d[5].trim());
                    System.out.println("Position : " + d[6].trim());
                    System.out.println("Status   : " + d[7].trim());
                    System.out.println("----------------------------------------------");
                    found = true;
                }
            }
            br.close();
            if (!found) System.out.println("No applications found.");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
        return new ArrayList<>();
    }

    // View only accepted students
    public void viewAcceptedStudents() {
        try {
            File f = new File("applications.txt");
            if (!f.exists()) { System.out.println("No applications yet."); return; }

            BufferedReader br = new BufferedReader(new FileReader(f));
            String line;
            int count = 0;
            System.out.println("\n========== ACCEPTED STUDENTS ==========");
            while ((line = br.readLine()) != null) {
                String[] d = line.split("\\|");
                if (d.length >= 8 && d[7].trim().equalsIgnoreCase("ACCEPTED")) {
                    System.out.println("Student  : " + d[3].trim() + " (" + d[2].trim() + ")");
                    System.out.println("Course   : " + d[4].trim());
                    System.out.println("Company  : " + d[5].trim());
                    System.out.println("Position : " + d[6].trim());
                    System.out.println("---------------------------------------");
                    count++;
                }
            }
            br.close();
            if (count == 0) System.out.println("No accepted students yet.");
            else System.out.println("Total accepted: " + count);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // Endorse a student for their internship application
    public void endorseStudent(Scanner sc) {
        List<String[]> students = new ArrayList<>();
        try {
            File f = new File("users.txt");
            if (!f.exists()) { System.out.println("No students found."); return; }

            BufferedReader br = new BufferedReader(new FileReader(f));
            String line;
            int count = 1;
            System.out.println("\n========== SELECT STUDENT TO ENDORSE ==========");
            while ((line = br.readLine()) != null) {
                String[] d = line.split(",");
                if (d.length >= 3 && d[2].trim().equalsIgnoreCase("student")) {
                    System.out.println("[" + count + "] " + (d.length > 4 ? d[4].trim() : d[0].trim())
                            + " (" + (d.length > 5 ? d[5].trim() : "N/A") + ") — " + d[0].trim());
                    students.add(d);
                    count++;
                }
            }
            br.close();
        } catch (Exception e) {
            System.out.println("Error reading students: " + e.getMessage()); return;
        }

        if (students.isEmpty()) { System.out.println("No students registered."); return; }

        System.out.print("Enter student number to endorse (0 to cancel): ");
        int choice;
        try {
            choice = Integer.parseInt(sc.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("Invalid input."); return;
        }
        if (choice == 0) return;
        if (choice < 1 || choice > students.size()) { System.out.println("Invalid selection."); return; }

        String[] sel       = students.get(choice - 1);
        String studentUser = sel[0].trim();
        String studentName = sel.length > 4 ? sel[4].trim() : studentUser;

        System.out.print("Endorsement note (e.g. 'Recommended for OJT'): ");
        String note = sc.nextLine().trim();
        if (note.isEmpty()) { System.out.println("Endorsement note cannot be empty."); return; }

        String date = new java.text.SimpleDateFormat("yyyy-MM-dd").format(new java.util.Date());
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter("endorsements.txt", true));
            // schoolName|studentUsername|studentName|note|date
            bw.write(schoolName + "|" + studentUser + "|" + studentName + "|" + note + "|" + date);
            bw.newLine();
            bw.close();
            System.out.println(" Endorsement recorded for " + studentName + " on " + date + ".");
        } catch (Exception e) {
            System.out.println("Error saving endorsement: " + e.getMessage());
        }
    }

    // List all uploaded resumes and approve/reject one
    public void reviewResumes(Scanner sc) {
        File statusFile = new File("resume_status.txt");
        if (!statusFile.exists()) { System.out.println("No resumes uploaded yet."); return; }

        List<String[]> resumes = new ArrayList<>();
        try {
            BufferedReader br = new BufferedReader(new FileReader(statusFile));
            String line;
            int count = 1;
            System.out.println("\n========== STUDENT RESUMES ==========");
            while ((line = br.readLine()) != null) {
                String[] d = line.split("\\|");
                // username|filePath|schoolStatus|schoolNote|uploadDate
                if (d.length >= 5) {
                    System.out.println("[" + count + "] Student  : " + d[0].trim());
                    System.out.println("    File     : " + d[1].trim());
                    System.out.println("    Uploaded : " + d[4].trim());
                    System.out.println("    Status   : " + d[2].trim());
                    System.out.println("    Note     : " + d[3].trim());
                    System.out.println("-------------------------------------");
                    resumes.add(d);
                    count++;
                }
            }
            br.close();
        } catch (Exception e) { System.out.println("Error reading resumes: " + e.getMessage()); return; }

        if (resumes.isEmpty()) { System.out.println("No resumes found."); return; }

        System.out.print("\nEnter number to read/review (0 to cancel): ");
        int choice;
        try { choice = Integer.parseInt(sc.nextLine().trim()); }
        catch (NumberFormatException e) { System.out.println("Invalid input."); return; }
        if (choice == 0) return;
        if (choice < 1 || choice > resumes.size()) { System.out.println("Invalid selection."); return; }

        String[] sel = resumes.get(choice - 1);
        String studentUser = sel[0].trim();
        String filePath    = sel[1].trim();

        // Read PDF as text (text-based PDFs) or show file info
        System.out.println("\n--- RESUME FILE INFO ---");
        File pdf = new File(filePath);
        if (!pdf.exists()) {
            System.out.println("File not found on disk: " + filePath);
        } else {
            System.out.println("File Name : " + pdf.getName());
            System.out.println("File Size : " + pdf.length() + " bytes (" + (pdf.length() / 1024) + " KB)");
            System.out.println("Full Path : " + pdf.getAbsolutePath());
            System.out.println("(Open the file path above in a PDF viewer to read the full resume.)");
        }

        System.out.println("\nAction:");
        System.out.println("1. APPROVE");
        System.out.println("2. REJECT");
        System.out.println("0. Cancel");
        System.out.print("Choice: ");
        String action = sc.nextLine().trim();
        if (action.equals("0")) return;
        if (!action.equals("1") && !action.equals("2")) { System.out.println("Invalid action."); return; }

        String newStatus = action.equals("1") ? "APPROVED" : "REJECTED";
        System.out.print("Leave a note for the student: ");
        String note = sc.nextLine().trim();
        if (note.isEmpty()) note = "--";

        // Rewrite resume_status.txt with updated status
        try {
            List<String> lines = new ArrayList<>();
            BufferedReader br = new BufferedReader(new FileReader(statusFile));
            String line;
            while ((line = br.readLine()) != null) {
                String[] d = line.split("\\|");
                if (d[0].trim().equals(studentUser)) {
                    d[2] = newStatus;
                    d[3] = note;
                    line  = String.join("|", d);
                }
                lines.add(line);
            }
            br.close();
            BufferedWriter bw = new BufferedWriter(new FileWriter(statusFile));
            for (String l : lines) { bw.write(l); bw.newLine(); }
            bw.close();
            System.out.println(" Resume of '" + studentUser + "' has been " + newStatus + ".");
        } catch (Exception e) {
            System.out.println("Error updating resume status: " + e.getMessage());
        }
    }

    // View all endorsements issued by this school
    public void viewEndorsements() {
        try {
            File f = new File("endorsements.txt");
            if (!f.exists()) { System.out.println("No endorsements issued yet."); return; }

            BufferedReader br = new BufferedReader(new FileReader(f));
            String line;
            boolean found = false;
            System.out.println("\n========== ENDORSEMENTS ==========");
            while ((line = br.readLine()) != null) {
                String[] d = line.split("\\|");
                if (d.length >= 5 && d[0].trim().equalsIgnoreCase(schoolName)) {
                    System.out.println("Student  : " + d[2].trim() + " (" + d[1].trim() + ")");
                    System.out.println("Note     : " + d[3].trim());
                    System.out.println("Date     : " + d[4].trim());
                    System.out.println("----------------------------------");
                    found = true;
                }
            }
            br.close();
            if (!found) System.out.println("No endorsements issued yet.");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}