import java.util.Scanner;

public class MainClass {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        boolean running = true;

        System.out.println("Welcome to InternSync!");

        while (running) {
            System.out.println("\nSelect your role to login:");
            System.out.println("1. Admin");
            System.out.println("2. Student");
            System.out.println("3. Company");
            System.out.println("4. Exit");
            System.out.print("Enter choice: ");
            int choice = sc.nextInt();
            sc.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    // Admin actions
                    System.out.print("Enter Admin ID: ");
                    int adminId = sc.nextInt();
                    sc.nextLine();
                    Admin admin = new Admin(adminId, 1); // UserId can be fixed for demo

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

                        // Example: Fetching user by userId (replace with real file-based logic if needed)
                        User user = new User(userId, "johndoe", "1234", "student", "email@example.com"); 
                        admin.changeUserRole(user, newRole);
                    } else {
                        System.out.println("Invalid choice.");
                    }
                    break;
                    
                case 2:
                    // Student actions
                    System.out.print("Enter your name: ");
                    String studentName = sc.nextLine();
                    System.out.print("Enter your email: ");
                    String email = sc.nextLine();
                    Student student = new Student(1, studentName, email, 1, "CS", "", "Not Applied");

                    System.out.println("Student Menu:");
                    System.out.println("1. Browse Internships");
                    System.out.println("2. Apply Internship");
                    System.out.print("Enter choice: ");
                    int studChoice = sc.nextInt();
                    sc.nextLine();

                    if (studChoice == 1) {
                        student.browseInternships();
                    } else if (studChoice == 2) {
                        System.out.print("Enter internship title to apply: ");
                        String internshipTitle = sc.nextLine();
                        student.applyInternship(internshipTitle);
                    } else {
                        System.out.println("Invalid choice.");
                    }
                    break;

                case 3:
                    // Company actions
                    System.out.print("Enter company name: ");
                    String companyName = sc.nextLine();
                    Company company = new Company(1, 1, companyName, "IT", "5");

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
                    } else {
                        System.out.println("Invalid choice.");
                    }
                    break;

                case 4:
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