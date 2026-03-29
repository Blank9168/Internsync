import java.io.*;
import java.util.ArrayList;

public class Student {
    private int studentid;
    private String name;
    private String email;
    private int userId;
    private String course;
    private String resumeFile;
    private String applicationStatus;

    private static final String APPLICATION_FILE = "data/applications.txt";

    public Student(int studentid, String name, String email, int userId, String course, String resumeFile, String applicationStatus) {
        this.studentid = studentid;
        this.name = name;
        this.email = email;
        this.userId = userId;
        this.course = course;
        this.resumeFile = resumeFile;
        this.applicationStatus = applicationStatus;
    }

    public void browseInternships() {
        System.out.println("\nAvailable Internships:");
        for (Internship i : Internsync.getInternships()) {
            System.out.println("- " + i.getTitle() + ": " + i.getDescription());
        }
    }

    public void applyInternship(String internshipTitle) {
        this.applicationStatus = "Applied";
        // Save application to file
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(APPLICATION_FILE, true))) {
            bw.write(studentid + "," + name + "," + internshipTitle + "," + applicationStatus);
            bw.newLine();
            System.out.println("Applied to internship: " + internshipTitle);
        } catch (IOException e) {
            System.out.println("Error saving application: " + e.getMessage());
        }
    }

    public void viewApplicationStatus() {
        File file = new File(APPLICATION_FILE);
        if (!file.exists()) {
            System.out.println("No applications found.");
            return;
        }

        System.out.println("\nApplication Status:");
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            boolean found = false;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 4 && Integer.parseInt(parts[0]) == this.studentid) {
                    System.out.println("Internship: " + parts[2] + " | Status: " + parts[3]);
                    found = true;
                }
            }
            if (!found) System.out.println("No applications found.");
        } catch (IOException e) {
            System.out.println("Error reading applications: " + e.getMessage());
        }
    }

    public void uploadDocument(String resumeFile) {
        this.resumeFile = resumeFile;
        System.out.println("Resume uploaded: " + resumeFile);
    }

    //Getters
    public int getStudentId() { return studentid; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public int getUserId() { return userId; }
    public String getCourse() { return course; }
    public String getResumeFile() { return resumeFile; }
    public String getApplicationStatus() { return applicationStatus; }
}