import java.io.*;
import java.util.ArrayList;

public class School {
    private int schoolid;
    private int userId;
    private String schoolName;
    private String coordinatorName;
    private String contactEmail;

    private static final String STUDENT_FILE = "data/school_students.txt";

    public School(int schoolid, int userId, String schoolName, String coordinatorName, String contactEmail) {
        this.schoolid = schoolid;
        this.userId = userId;
        this.schoolName = schoolName;
        this.coordinatorName = coordinatorName;
        this.contactEmail = contactEmail;
    }

    public void addStudent(Student student) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(STUDENT_FILE, true))) {
            bw.write(schoolid + "," + student.getStudentId() + "," + student.getName());
            bw.newLine();
            System.out.println("Student added to school: " + student.getName());
        } catch (IOException e) {
            System.out.println("Error adding student: " + e.getMessage());
        }
    }

    public void removeStudent(Student student) {
        File file = new File(STUDENT_FILE);
        if (!file.exists()) return;

        ArrayList<String> lines = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (!line.contains("," + student.getStudentId() + ",")) {
                    lines.add(line);
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading students: " + e.getMessage());
        }

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(STUDENT_FILE))) {
            for (String l : lines) {
                bw.write(l);
                bw.newLine();
            }
            System.out.println("Student removed: " + student.getName());
        } catch (IOException e) {
            System.out.println("Error removing student: " + e.getMessage());
        }
    }

    public void vertifyDocuments(Student student) {
        System.out.println("Verifying documents for: " + student.getName());
        // Logic to verify resume or other uploaded documents can go here
    }

    public void partnerCompany(Company company) {
        System.out.println("Partnering with company: " + company.getCompanyName());
        // Logic to link company to school
    }

    public void monitorInternship() {
        System.out.println("Monitoring internships for school: " + schoolName);
        // Logic to track student internships
    }

    // Getters
    public int getSchoolId() { return schoolid; }
    public String getSchoolName() { return schoolName; }
    public String getCoordinatorName() { return coordinatorName; }
    public String getContactEmail() { return contactEmail; }
}