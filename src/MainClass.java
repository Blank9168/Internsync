public class MainClass {
    public static void main(String[] args) {
        // Show system information using the interface's static method
        InternshipActions.showSystemInfo();
        
        // Initialize the system
        Internsync system = new Internsync("InternSync", "v2.0");
        system.startSystem();
    }
}