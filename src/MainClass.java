public class MainClass {
    public static void main(String[] args) {
        // Default admin instance object
        Admin admin = new Admin(1, "admin", "admin123", "admin@internsync.com");

        // Initialize the system
        Internsync system = new Internsync("InternSync", "v3.0");
        system.startSystem();
    }
}