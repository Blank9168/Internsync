public class Student extends User {
    private String name;
    private String course;

    public Student(int id, String username, String password, String email, String name, String course) {
        super(id, username, password, "student", email);
        this.name = name;
        this.course = course;
    }

    @Override
    public void displayMenu() {
        System.out.println("\nWelcome, " + name + " (" + course + ")");
        System.out.println("1. Browse Internships");
        System.out.println("2. Apply for Internship");
        System.out.print("Choice: ");
    }

    // Example methods
    public void browseInternships() {
        System.out.println("Displaying available internships...");
    }

    public void applyInternship(String title) {
        System.out.println("Applied for internship: " + title);
    }
}