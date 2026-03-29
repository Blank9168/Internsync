import java.util.ArrayList;
import java.util.Scanner;

public class MainClass {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        boolean running = true;

        // Sample users for demonstration
        ArrayList<User> users = new ArrayList<>();
        users.add(new User(1, "admin", "admin123", "admin", "admin@example.com"));

        int nextUserId = 2; // For registering new users

        System.out.println("Welcome to InternSync!");

        while (running) {
            System.out.println("\nSelect an option:");
            System.out.println("1. Login");
            System.out.println("2. Register");
            System.out.println("3. Exit");
            System.out.print("Enter choice: ");
            int mainChoice = sc.nextInt();
            sc.nextLine(); // consume newline

            switch (mainChoice) {
                case 1:
                    // Login
                    System.out.println("\nSelect your role to login:");
                    System.out.println("1. Admin");
                    System.out.println("2. Student");
                    System.out.println("3. Company");
                    System.out.print("Enter choice: ");
                    int roleChoice = sc.nextInt();
                    sc.nextLine();

                    System.out.print("Enter username: ");
                    String username = sc.nextLine();
                    System.out.print("Enter password: ");
                    String password = sc.nextLine();

                    User loginUser = null;
                    for (User u : users) {
                        if (u.getUsername().equals(username) && u.getPassword().equals(password)) {
                            loginUser = u;
                            break;
                        }
                    }

                    if (loginUser == null) {
                        System.out.println("Invalid credentials.");
                        break;
                    }

                    switch (roleChoice) {
                        case 1:
                            if (!loginUser.getRole().equals("admin")) {
                                System.out.println("You are not an admin.");
                                break;
                            }
                            System.out.println("Admin login successful.");
                            Admin admin = new Admin(loginUser.getUserid(), loginUser.getUserid());

                            System.out.println("Admin Menu:");
                            System.out.println("1. View System Reports");
                            System.out.println("2. Change User Role");
                            System.out.print("Enter choice: ");
                            int adminChoice = sc.nextInt();
                            sc.nextLine();

                            if (adminChoice == 1) {
                                admin.viewSystemReports();
                            } else if (adminChoice == 2) {
                                System.out.print("Enter User ID to change role: ");
                                int userId = sc.nextInt();
                                sc.nextLine();
                                System.out.print("Enter new role for the user: ");
                                String newRole = sc.nextLine();

                                User targetUser = null;
                                for (User u : users) {
                                    if (u.getUserid() == userId) {
                                        targetUser = u;
                                        break;
                                    }
                                }

                                if (targetUser != null) {
                                    admin.changeUserRole(targetUser, newRole);
                                } else {
                                    System.out.println("User ID not found.");
                                }
                            }
                            break;

                        case 2:
                            if (!loginUser.getRole().equals("student")) {
                                System.out.println("You are not a student.");
                                break;
                            }
                            System.out.println("Student login successful.");
                            Student student = new Student(loginUser.getUserid(), loginUser.getUsername(), loginUser.getEmail(), loginUser.getUserid(), "CS", "", "Not Applied");

                            System.out.println("Student Menu:");
                            System.out.println("1. Browse Internships");
                            System.out.println("2. Apply Internship");
                            System.out.print("Enter choice: ");
                            int studChoice = sc.nextInt();
                            sc.nextLine();

                            if (studChoice == 1) student.browseInternships();
                            else if (studChoice == 2) {
                                System.out.print("Enter internship title to apply: ");
                                String internshipTitle = sc.nextLine();
                                student.applyInternship(internshipTitle);
                            }
                            break;

                        case 3:
                            if (!loginUser.getRole().equals("company")) {
                                System.out.println("You are not a company.");
                                break;
                            }
                            System.out.println("Company login successful.");
                            Company company = new Company(loginUser.getUserid(), loginUser.getUserid(), loginUser.getUsername(), "IT", "5");

                            System.out.println("Company Menu:");
                            System.out.println("1. Post Internship");
                            System.out.print("Enter choice: ");
                            int compChoice = sc.nextInt();
                            sc.nextLine();

                            if (compChoice == 1) {
                                System.out.print("Enter internship title: ");
                                String title = sc.nextLine();
                                System.out.print("Enter internship description: ");
                                String desc = sc.nextLine();
                                company.postInternship(title, desc);
                            }
                            break;

                        default:
                            System.out.println("Invalid role choice.");
                    }
                    break;

                case 2:
                    // Register
                    System.out.print("Enter your username: ");
                    String regUsername = sc.nextLine();
                    System.out.print("Enter your name: ");
                    String regName = sc.nextLine();
                    System.out.print("Enter email: ");
                    String regEmail = sc.nextLine();
                    System.out.print("Enter password: ");
                    String regPassword = sc.nextLine();

                    System.out.println("Select role to register:");
                    System.out.println("1. Student");
                    System.out.println("2. Company");
                    System.out.print("Enter choice: ");
                    int regRoleChoice = sc.nextInt();
                    sc.nextLine();

                    String role = (regRoleChoice == 1) ? "student" : "company";

                    User newUser = new User(nextUserId++, regUsername, regName, regPassword, role, regEmail);
                    users.add(newUser);

                    System.out.println("Registration successful! You can now login.");
                    break;

                case 3:
                    running = false;
                    System.out.println("Exiting InternSync...");
                    break;

                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }

        sc.close();
    }
}