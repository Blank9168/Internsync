import java.io.*;

public class Student extends User {

    private String course;

    public Student(int userid, String username, String password, String email, String course) {
        super(userid, username, password, "student", email);
        this.course = course;
    }

    @Override
    public void displayMenu() {
        System.out.println("\n--- STUDENT MENU ---");
        System.out.println("1. Browse Internships");
        System.out.println("2. Apply Internship");
    }

    public void browseInternships() {
        try {
            BufferedReader br = new BufferedReader(new FileReader("internships.txt"));
            String line;
            System.out.println("\nAvailable Internships:");
            while ((line = br.readLine()) != null) {
                System.out.println(line);
            }
            br.close();
        } catch (Exception e) {
            System.out.println("No internships found.");
        }
    }

    public void applyInternship(String title) {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter("applications.txt", true));
            bw.write(username + " applied to " + title);
            bw.newLine();
            bw.close();
            System.out.println("Application submitted!");
        } catch (Exception e) {
            System.out.println("Error applying.");
        }
    }
}