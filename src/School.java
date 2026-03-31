import java.io.*;

public class School extends User {

    private String schoolName;
    private String address;

    public School(int userid, String username, String password, String email, String schoolName, String address) {
        super(userid, username, password, "school", email);
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
                String username = data[0];
                String email = data[3];
                String name = data.length > 4 ? data[4] : "";
                String course = data.length > 5 ? data[5] : "";

                System.out.println("Username: " + username +
                                   " | Name: " + name +
                                   " | Course: " + course +
                                   " | Email: " + email);
            }
        }

        br.close();
    } catch (Exception e) {
        System.out.println("Error reading students.");
    }
}
}