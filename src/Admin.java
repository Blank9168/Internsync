import java.io.*;
import java.util.*;

public class Admin extends User {

    public Admin(int userid, String username, String password, String email) {
        super(userid, username, password, "admin", email, email, email);
    }

    @Override
    public void displayMenu() {
        System.out.println("\n--- ADMIN MENU ---");
        System.out.println("1. View Users");
        System.out.println("2. Change User Role");
    }

    public void viewUsers() {
        try {
            BufferedReader br = new BufferedReader(new FileReader("users.txt"));
            String line;
            System.out.println("\n=== USERS ===");
            while ((line = br.readLine()) != null) {
                System.out.println(line);
            }
            br.close();
        } catch (Exception e) {
            System.out.println("Error reading users.");
        }
    }

    public void changeUserRole(String targetUsername, String newRole) {
        try {
            List<String> lines = new ArrayList<>();
            BufferedReader br = new BufferedReader(new FileReader("users.txt"));
            String line;

            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");

                if (data[0].equals(targetUsername)) {
                    line = data[0] + "," + data[1] + "," + newRole + "," + data[3];
                }

                lines.add(line);
            }
            br.close();

            BufferedWriter bw = new BufferedWriter(new FileWriter("users.txt"));
            for (String l : lines) {
                bw.write(l);
                bw.newLine();
            }
            bw.close();

            System.out.println("Role updated!");
        } catch (Exception e) {
            System.out.println("Error updating role.");
        }
    }
}