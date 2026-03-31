import java.io.*;
import java.util.*;

public class Internsync {

    private String systemName;
    private String version;

    public Internsync(String systemName, String version) {
        this.systemName = systemName;
        this.version = version;
    }

    public void startSystem() {
        Scanner sc = new Scanner(System.in);
        boolean running = true;

        System.out.println("=== " + systemName + " (" + version + ") ===");

        while (running) {
            System.out.println("\n1. Register");
            System.out.println("2. Login");
            System.out.println("3. Exit");
            System.out.print("Choice: ");
            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1:
                    registerUser(sc);
                    break;
                case 2:
                    loginUser(sc);
                    break;
                case 3:
                    running = false;
                    System.out.println("System closed.");
                    break;
            }
        }
    }

    // 🔐 REGISTER
    public void registerUser(Scanner sc) {
    try {
        BufferedWriter bw = new BufferedWriter(new FileWriter("users.txt", true));

        System.out.print("Username: ");
        String username = sc.nextLine();

        System.out.print("Email: ");
        String email = sc.nextLine();

        System.out.print("Password: ");
        String password = sc.nextLine();

        System.out.println("Role: 1.Student 2.Company");
        int r = sc.nextInt();
        sc.nextLine();

        String role = (r == 1) ? "student" : "company";
        String name = "";
        String course = "";

        if (role.equals("student")) {
            System.out.print("Full Name: ");
            name = sc.nextLine();

            System.out.print("Course: ");
            course = sc.nextLine();
        }

        // Save format: username,password,role,email,name,course
        bw.write(username + "," + password + "," + role + "," + email + "," + name + "," + course);
        bw.newLine();
        bw.close();

        System.out.println("Registered successfully!");
    } catch (Exception e) {
        System.out.println("Error saving user: " + e.getMessage());
    }
}

    // 🔑 LOGIN (uses polymorphism)
    public void loginUser(Scanner sc) {
        System.out.print("Username: ");
        String user = sc.nextLine();

        System.out.print("Password: ");
        String pass = sc.nextLine();

        boolean found = false;

        try {
            BufferedReader br = new BufferedReader(new FileReader("users.txt"));
            String line;

            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");

                if (data[0].equals(user) && data[1].equals(pass)) {
                    found = true;

                    String role = data[2];
                    User currentUser;

                    // 🔥 POLYMORPHISM
                    if (role.equals("student")) {
                    String name = data.length > 4 ? data[4] : "";
                    String course = data.length > 5 ? data[5] : "CS"; // default fallback
                    currentUser = new Student(1, user, pass, data[3], name, course);
                    } else if (role.equals("company")) {
                        currentUser = new Company(1, user, pass, data[3], user);
                    } else {
                        currentUser = new Admin(1, user, pass, data[3]);
                    }

                    currentUser.displayMenu();

                    int option = sc.nextInt();
                    sc.nextLine();

                    handleUserAction(currentUser, option, sc);
                }
            }
            br.close();

            if (!found) {
                System.out.println("Invalid login.");
            }

        } catch (Exception e) {
            System.out.println("Error reading file.");
        }
    }

    // 🎯 HANDLE ACTIONS (clean separation)
    private void handleUserAction(User currentUser, int option, Scanner sc) {

        if (currentUser instanceof Student) {
            Student s = (Student) currentUser;

            if (option == 1) s.browseInternships();
            else if (option == 2) {
                System.out.print("Enter title: ");
                String t = sc.nextLine();
                s.applyInternship(t);
            }
        }

        else if (currentUser instanceof Company) {
            Company c = (Company) currentUser;

            if (option == 1) {
                System.out.print("Title: ");
                String t = sc.nextLine();
                System.out.print("Desc: ");
                String d = sc.nextLine();
                c.postInternship(t, d);
            }
        }

        else if (currentUser instanceof Admin) {
            Admin a = (Admin) currentUser;

            if (option == 1) a.viewUsers();
            else if (option == 2) {
                System.out.print("Username: ");
                String target = sc.nextLine();
                System.out.print("New Role: ");
                String newRole = sc.nextLine();
                a.changeUserRole(target, newRole);
            }
        }
    }
}