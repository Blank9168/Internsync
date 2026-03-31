import java.io.*;

public class Company extends User {

    private String companyName;

    public Company(int userid, String username, String password, String email, String companyName) {
        super(userid, username, password, "company", email);
        this.companyName = companyName;
    }

    @Override
    public void displayMenu() {
        System.out.println("\n--- COMPANY MENU ---");
        System.out.println("1. Post Internship");
    }

    public void postInternship(String title, String desc) {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter("internships.txt", true));
            bw.write(companyName + " | " + title + " | " + desc);
            bw.newLine();
            bw.close();
            System.out.println("Internship posted!");
        } catch (Exception e) {
            System.out.println("Error posting internship.");
        }
    }
}