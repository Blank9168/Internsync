import java.io.*;

public class School extends User {
    private String schoolName;
    private String address;

    public School(int userid, String username, String password, String email, String schoolName, String address) {
        super(userid, username, password, "school", email);
        this.schoolName = schoolName;
        this.address    = address;
    }

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
        System.out.println("4. Logout");
        System.out.print("Choice: ");
    }

    // View all registered students
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
                    System.out.println("Username : " + d[0].trim());
                    System.out.println("Email    : " + (d.length > 3 ? d[3].trim() : "N/A"));
                    System.out.println("Name     : " + (d.length > 4 ? d[4].trim() : "N/A"));
                    System.out.println("Course   : " + (d.length > 5 ? d[5].trim() : "N/A"));
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
    public void viewApplications() {
        try {
            File f = new File("applications.txt");
            if (!f.exists()) { System.out.println("No applications yet."); return; }

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
}