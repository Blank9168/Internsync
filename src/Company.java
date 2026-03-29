import java.io.*;
import java.util.ArrayList;

public class Company {
    private int companyid;
    private int userId;
    private String companyName;
    private String industryType;
    private String availableSlots;

    private static final String INTERNSHIP_FILE = "data/company_internships.txt";
    private static final String APPLICATION_FILE = "data/company_applications.txt";

    public Company(int companyid, int userId, String companyName, String industryType, String availableSlots) {
        this.companyid = companyid;
        this.userId = userId;
        this.companyName = companyName;
        this.industryType = industryType;
        this.availableSlots = availableSlots;
    }

    public void postInternship(String internshipTitle, String description) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(INTERNSHIP_FILE, true))) {
            bw.write(companyid + "," + internshipTitle + "," + description + "," + availableSlots);
            bw.newLine();
            System.out.println("Internship posted: " + internshipTitle);
        } catch (IOException e) {
            System.out.println("Error posting internship: " + e.getMessage());
        }
    }

    public void viewApplicants() {
        File file = new File(APPLICATION_FILE);
        if (!file.exists()) {
            System.out.println("No applicants yet.");
            return;
        }

        System.out.println("Applicants for company: " + companyName);
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            boolean found = false;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 3 && Integer.parseInt(parts[0]) == companyid) {
                    System.out.println("Student ID: " + parts[1] + " | Internship: " + parts[2] + " | Status: " + parts[3]);
                    found = true;
                }
            }
            if (!found) System.out.println("No applicants found.");
        } catch (IOException e) {
            System.out.println("Error reading applicants: " + e.getMessage());
        }
    }

    public void acceptStudent(Student student, String internshipTitle) {
        updateApplicationStatus(student, internshipTitle, "Accepted");
        System.out.println("Accepted student: " + student.getName());
    }

    public void rejectStudent(Student student, String internshipTitle) {
        updateApplicationStatus(student, internshipTitle, "Rejected");
        System.out.println("Rejected student: " + student.getName());
    }

    public void reviewDocuments(Student student) {
        System.out.println("Reviewing documents for: " + student.getName());
        // Logic for reviewing resume or other documents
    }

    private void updateApplicationStatus(Student student, String internshipTitle, String status) {
        File file = new File(APPLICATION_FILE);
        if (!file.exists()) return;

        ArrayList<String> lines = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 4 &&
                    Integer.parseInt(parts[0]) == companyid &&
                    parts[2].equals(internshipTitle) &&
                    Integer.parseInt(parts[1]) == student.getStudentId()) {

                    parts[3] = status;
                    line = String.join(",", parts);
                }
                lines.add(line);
            }
        } catch (IOException e) {
            System.out.println("Error reading applications: " + e.getMessage());
        }

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(APPLICATION_FILE))) {
            for (String l : lines) {
                bw.write(l);
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error updating application status: " + e.getMessage());
        }
    }

    // Getters
    public int getUserId() { return userId; }
    public int getCompanyId() { return companyid; }
    public String getCompanyName() { return companyName; }
    public String getIndustryType() { return industryType; }
    public String getAvailableSlots() { return availableSlots; }
}