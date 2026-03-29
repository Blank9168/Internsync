import java.io.*;

public class School {

    private int schoolId;
    private String schoolName;
    private String address;

    public School(int schoolId, String schoolName, String address) {
        this.schoolId = schoolId;
        this.schoolName = schoolName;
        this.address = address;
    }

    // View all student applications
    public void viewApplications() {
        try {
            BufferedReader br = new BufferedReader(new FileReader("applications.txt"));
            String line;

            System.out.println("\n=== STUDENT APPLICATIONS ===");
            while ((line = br.readLine()) != null) {
                System.out.println(line);
            }

            br.close();
        } catch (Exception e) {
            System.out.println("No applications found.");
        }
    }

    // View all registered students
    public void viewStudents() {
        try {
            BufferedReader br = new BufferedReader(new FileReader("users.txt"));
            String line;

            System.out.println("\n=== STUDENTS LIST ===");
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");

                if (data[2].equals("student")) {
                    System.out.println("Username: " + data[0] + " | Email: " + data[3]);
                }
            }

            br.close();
        } catch (Exception e) {
            System.out.println("Error reading students.");
        }
    }
}