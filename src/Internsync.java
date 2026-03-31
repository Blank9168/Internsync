import java.io.*;
import java.util.*;

public class Internsync {

    private String systemName;
    private String version;

    public Internsync(String systemName, String version) {
        this.systemName = systemName;
        this.version    = version;
    }

    // ─────────────────────────────────────────
    //  SYSTEM ENTRY POINT
    // ─────────────────────────────────────────
    public void startSystem() {
        seedDefaultAdmin();
        Scanner sc = new Scanner(System.in);
        boolean running = true;

        System.out.println("╔════════════════════════════════╗");
        System.out.println("║        " + systemName + " " + version + "         ║");
        System.out.println("║  Internship Management System  ║");
        System.out.println("╚════════════════════════════════╝");

        while (running) {
            System.out.println("\n========== MAIN MENU ==========");
            System.out.println("1. Register");
            System.out.println("2. Login");
            System.out.println("3. Exit");
            System.out.print("Choice: ");

            String input = sc.nextLine().trim();
            switch (input) {
                case "1": registerUser(sc);  break;
                case "2": loginUser(sc);     break;
                case "3": running = false;   System.out.println("\nGoodbye! System closed."); break;
                default:  System.out.println("Invalid choice. Please enter 1, 2, or 3.");
            }
        }
    }

    // ─────────────────────────────────────────
    //  REGISTER
    // ─────────────────────────────────────────
    public void registerUser(Scanner sc) {
        System.out.println("\n========== REGISTER ==========");

        System.out.print("Username : ");
        String username = sc.nextLine().trim();

        // Check for duplicate username
        if (userExists(username)) {
            System.out.println("Username already taken. Please choose another.");
            return;
        }

        System.out.print("Email    : ");
        String email = sc.nextLine().trim();

        System.out.print("Password : ");
        String password = sc.nextLine().trim();

        System.out.println("Role:");
        System.out.println("  1. Student");
        System.out.println("  2. Company");
        System.out.println("  3. School");
        System.out.print("Choice: ");
        String roleChoice = sc.nextLine().trim();

        String role;
        String extraFields = "";

        switch (roleChoice) {
            case "1":
                role = "student";
                System.out.print("Full Name : ");
                String name = sc.nextLine().trim();
                System.out.print("Course    : ");
                String course = sc.nextLine().trim();
                extraFields = "," + name + "," + course;
                break;
            case "2":
                role = "company";
                System.out.print("Company Name : ");
                String companyName = sc.nextLine().trim();
                extraFields = "," + companyName + ",";
                break;
            case "3":
                role = "school";
                System.out.print("School Name : ");
                String schoolName = sc.nextLine().trim();
                System.out.print("Address     : ");
                String address = sc.nextLine().trim();
                extraFields = "," + schoolName + "," + address;
                break;
            default:
                System.out.println("Invalid role selection.");
                return;
        }

        // Format: username,password,role,email,field1,field2
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter("users.txt", true));
            bw.write(username + "," + password + "," + role + "," + email + extraFields);
            bw.newLine();
            bw.close();
            System.out.println("\n✔ Registered successfully! You can now login.");
        } catch (Exception e) {
            System.out.println("Error saving user: " + e.getMessage());
        }
    }

    // ─────────────────────────────────────────
    //  LOGIN
    // ─────────────────────────────────────────
    public void loginUser(Scanner sc) {
        System.out.println("\n========== LOGIN ==========");
        System.out.print("Username : ");
        String user = sc.nextLine().trim();
        System.out.print("Password : ");
        String pass = sc.nextLine().trim();

        try {
            File f = new File("users.txt");
            if (!f.exists()) { System.out.println("No users registered yet."); return; }

            BufferedReader br = new BufferedReader(new FileReader(f));
            String line;
            User currentUser = null;

            while ((line = br.readLine()) != null) {
                String[] d = line.split(",");
                if (d.length < 4) continue;

                if (d[0].trim().equals(user) && d[1].trim().equals(pass)) {
                    String role  = d[2].trim();
                    String email = d[3].trim();

                    switch (role) {
                        case "student":
                            String sName   = d.length > 4 ? d[4].trim() : "";
                            String sCourse = d.length > 5 ? d[5].trim() : "";
                            currentUser = new Student(1, user, pass, email, sName, sCourse);
                            break;
                        case "company":
                            String cName = d.length > 4 ? d[4].trim() : user;
                            currentUser = new Company(1, user, pass, email, cName);
                            break;
                        case "admin":
                            currentUser = new Admin(1, user, pass, email);
                            break;
                        case "school":
                            String schName = d.length > 4 ? d[4].trim() : "";
                            String schAddr = d.length > 5 ? d[5].trim() : "";
                            currentUser = new School(1, user, pass, email, schName, schAddr);
                            break;
                        default:
                            System.out.println("Unknown role: " + role);
                    }
                    break;
                }
            }
            br.close();

            if (currentUser == null) {
                System.out.println("Invalid username or password.");
                return;
            }

            System.out.println("\n✔ Login successful!");
            runUserSession(currentUser, sc);

        } catch (Exception e) {
            System.out.println("Error during login: " + e.getMessage());
        }
    }

    // ─────────────────────────────────────────
    //  SESSION LOOP (stays in menu until logout)
    // ─────────────────────────────────────────
    private void runUserSession(User currentUser, Scanner sc) {
        boolean loggedIn = true;

        while (loggedIn) {
            currentUser.displayMenu();
            String option = sc.nextLine().trim();

            if (currentUser instanceof Student) {
                Student s = (Student) currentUser;
                switch (option) {
                    case "1": s.browseInternships();      break;
                    case "2": s.applyInternship(sc);      break;
                    case "3": s.viewMyApplications();     break;
                    case "4": s.withdrawApplication(sc);  break;
                    case "5": s.changePassword(sc);       break;
                    case "6": loggedIn = false; System.out.println("Logged out."); break;
                    default:  System.out.println("Invalid option.");
                }

            } else if (currentUser instanceof Company) {
                Company c = (Company) currentUser;
                switch (option) {
                    case "1": c.postInternship(sc);           break;
                    case "2": c.viewMyInternships();          break;
                    case "3": c.viewApplicants();             break;
                    case "4": c.updateApplicationStatus(sc);  break;
                    case "5": c.closeInternship(sc);          break;
                    case "6": c.editInternship(sc);           break;
                    case "7": c.changePassword(sc);           break;
                    case "8": loggedIn = false; System.out.println("Logged out."); break;
                    default:  System.out.println("Invalid option.");
                }

            } else if (currentUser instanceof Admin) {
                Admin a = (Admin) currentUser;
                switch (option) {
                    case "1": a.viewUsers();              break;
                    case "2": a.changeUserRole(sc);       break;
                    case "3": a.deleteUser(sc);           break;
                    case "4": a.viewAllInternships();     break;
                    case "5": a.viewAllApplications();    break;
                    case "6": a.resetUserPassword(sc);    break;
                    case "7": a.changePassword(sc);       break;
                    case "8": loggedIn = false; System.out.println("Logged out."); break;
                    default:  System.out.println("Invalid option.");
                }

            } else if (currentUser instanceof School) {
                School sch = (School) currentUser;
                switch (option) {
                    case "1": sch.viewStudents();         break;
                    case "2": sch.viewApplications();     break;
                    case "3": sch.viewAcceptedStudents(); break;
                    case "4": sch.endorseStudent(sc);     break;
                    case "5": sch.viewEndorsements();     break;
                    case "6": sch.changePassword(sc);     break;
                    case "7": loggedIn = false; System.out.println("Logged out."); break;
                    default:  System.out.println("Invalid option.");
                }
            }
        }
    }

    // ─────────────────────────────────────────
    //  HELPER
    // ─────────────────────────────────────────
    private boolean userExists(String username) {
        try {
            File f = new File("users.txt");
            if (!f.exists()) return false;
            BufferedReader br = new BufferedReader(new FileReader(f));
            String line;
            while ((line = br.readLine()) != null) {
                String[] d = line.split(",");
                if (d[0].trim().equalsIgnoreCase(username)) { br.close(); return true; }
            }
            br.close();
        } catch (Exception e) { /* ignore */ }
        return false;
    }
    // Seed a default admin account on first run
    private void seedDefaultAdmin() {
        if (!userExists("admin")) {
            try {
                BufferedWriter bw = new BufferedWriter(new FileWriter("users.txt", true));
                // username,password,role,email,field1,field2
                bw.write("admin,admin123,admin,admin@internsync.com,,");
                bw.newLine();
                bw.close();
                System.out.println("[System] Default admin account created.");
                System.out.println("[System] Username: admin | Password: admin123");
            } catch (Exception e) {
                System.out.println("Error seeding admin: " + e.getMessage());
            }
        }
    }
}