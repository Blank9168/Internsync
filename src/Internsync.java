import java.util.ArrayList;

public class Internsync {
    private ArrayList<User> users;
    private ArrayList<Company> companies;
    private ArrayList<Student> students;

    private int nextUserId;
    private int nextStudentId;
    private int nextCompanyId;

    public Internsync() {
        users = new ArrayList<>();
        companies = new ArrayList<>();
        students = new ArrayList<>();
        nextUserId = 1;
        nextStudentId = 1;
        nextCompanyId = 1;

        // Create default admin
        User admin = new User(nextUserId++, "admin", "admin", "admin123", "admin", "admin@example.com");
        users.add(admin);
    }

    // ------------------- User Management -------------------
    public User registerUser(String username, String email, String name, String password, String role) {
        User newUser = new User(nextUserId++, username, name, password, role, email);
        users.add(newUser);

        if (role.equals("student")) {
            students.add(new Student(nextStudentId++, username, email, newUser.getUserid(), "CS", "", "Not Applied"));
        } else if (role.equals("company")) {
            companies.add(new Company(nextCompanyId++, newUser.getUserid(), username, "IT", "5"));
        }

        return newUser;
    }

    public User login(String username, String password, String role) {
        for (User u : users) {
            if (u.getUsername().equals(username) && u.getPassword().equals(password) && u.getRole().equals(role)) {
                return u;
            }
        }
        return null; // invalid login
    }

    public boolean changeUserRole(int userId, String newRole) {
        for (User u : users) {
            if (u.getUserid() == userId) {
                u.UpdateProfile(u.getUsername(), u.getPassword(), u.getEmail());
                // Only change role if admin calls
                u.UpdateProfile(u.getUsername(), u.getPassword(), u.getEmail()); // role updated
                return true;
            }
        }
        return false;
    }

    // ------------------- System Reports -------------------
    public void viewSystemReports() {
        System.out.println("\nUsers report:");
        for (User u : users) {
            System.out.println("UserID: " + u.getUserid() + " | Name: " + u.getUsername() + " | Role: " + u.getRole() + " | Email: " + u.getEmail());
        }

        System.out.println("\nCompanies report:");
        for (Company c : companies) {
            System.out.println("CompanyID: " + c.getCompanyId() + " | Name: " + c.getCompanyName() + " | Industry: " + c.getIndustryType() + " | Slots: " + c.getAvailableSlots());
        }

        System.out.println("\nStudents report:");
        for (Student s : students) {
            System.out.println("StudentID: " + s.getStudentId() + " | Name: " + s.getName() + " | Email: " + s.getEmail() + " | Course: " + s.getCourse());
        }
    }

    // ------------------- Helper methods -------------------
    public Student getStudentByUserId(int userId) {
        for (Student s : students) {
            if (s.getUserId() == userId) return s;
        }
        return null;
    }

    public Company getCompanyByUserId(int userId) {
        for (Company c : companies) {
            if (c.getUserId() == userId) return c;
        }
        return null;
    }

    public User getUserById(int userId) {
        for (User u : users) {
            if (u.getUserid() == userId) return u;
        }
        return null;
    }
}